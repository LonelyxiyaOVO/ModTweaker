package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.InsolatorManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.Insolator")
@ModOnly("thermalexpansion")
@ZenRegister
public class Insolator {

    @ZenMethod
    public static void addFertilizer(IItemStack input) {
        ModTweaker.LATE_ADDITIONS.add(new Fertilizer(InputHelper.toStack(input), true));
    }

    @ZenMethod
    public static void removeFertilizer(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Fertilizer(InputHelper.toStack(input), false));
    }

    @ZenMethod
    public static void addRecipe(IItemStack primaryOutput, IIngredient primaryInput, IIngredient secondaryInput,
            int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance,
            @Optional(valueLong = -1L) int water) {
        for (IItemStack first : primaryInput.getItems()) for (IItemStack second : secondaryInput.getItems())
            addRecipe(primaryOutput, first, second, energy, secondaryOutput, secondaryChance, water);
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(ThermalExpansionReflection.removeBy("Insolator", InsolatorManager.class,
                "getRecipeList", input, false, "getPrimaryInput", "getSecondaryInput"));
    }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(ThermalExpansionReflection.removeBy("Insolator", InsolatorManager.class,
                "getRecipeList", output, true, "getPrimaryOutput", "getSecondaryOutput"));
    }

    @SuppressWarnings("unchecked")
    private static class Fertilizer extends BaseAction {
        private final ItemStack input;
        private final boolean add;

        Fertilizer(ItemStack input, boolean add) {
            super("Insolator Fertilizer");
            this.input = input;
            this.add = add;
        }

        @Override
        public void apply() {
            java.util.Set<Object> lockSet = (java.util.Set<Object>) ThermalExpansionReflection.get(InsolatorManager.class, "lockSet");
            Object comparable = InsolatorManager.convertInput(input);
            if (add) lockSet.add(comparable); else lockSet.remove(comparable);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Insolator", () -> {
            for(InsolatorManager.InsolatorRecipe recipe : InsolatorManager.getRecipeList()) {
                InsolatorManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
            }
        });
    }
    
    @ZenMethod
    public static void addRecipe(IItemStack primaryOutput, IItemStack primaryInput, IItemStack secondaryInput, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance, @Optional(valueLong = -1L) int water) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(primaryOutput), InputHelper.toStack(primaryInput), InputHelper.toStack(secondaryInput), energy, InputHelper.toStack(secondaryOutput), secondaryChance, water, false));
    }
    
    @ZenMethod
    public static void addRecipeSaplingInfuser(IItemStack primaryOutput, IItemStack primaryInput, IItemStack secondaryInput, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance, @Optional(valueLong = -1L) int water) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(primaryOutput), InputHelper.toStack(primaryInput), InputHelper.toStack(secondaryInput), energy, InputHelper.toStack(secondaryOutput), secondaryChance, water, true));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack primaryInput, IItemStack secondaryInput) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(primaryInput), InputHelper.toStack(secondaryInput)));
    }
    
    private static class Add extends BaseAction {
        
        private final ItemStack primaryOutput, primaryInput, secondaryInput, secondaryOutput;
        private final int energy, secondaryChance, water;
        private final boolean isSapling;
        
        public Add(ItemStack primaryOutput, ItemStack primaryInput, ItemStack secondaryInput, int energy, ItemStack secondaryOutput, int secondaryChance, int water, boolean isSapling) {
            super("PhytogenicInsolator");
            this.primaryOutput = primaryOutput;
            this.primaryInput = primaryInput;
            this.secondaryInput = secondaryInput;
            this.secondaryOutput = secondaryOutput;
            this.energy = energy;
            this.water = water;
            this.isSapling = isSapling;
            this.secondaryChance = !secondaryOutput.isEmpty() && secondaryChance <= 0 ? 100 : secondaryChance;
        }
        
        @Override
        public void apply() {
            final InsolatorManager.Type type = isSapling ? InsolatorManager.Type.TREE : InsolatorManager.Type.STANDARD;
            if(water == -1)
                InsolatorManager.addRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, secondaryChance, type);
            else
                InsolatorManager.addRecipe(energy, water, primaryInput, secondaryInput, primaryOutput, secondaryOutput, secondaryChance, type);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(primaryOutput);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack primaryInput, secondaryInput;
        
        public Remove(ItemStack primaryInput, ItemStack secondaryInput) {
            super("PhytogenicInsolator");
            this.primaryInput = primaryInput;
            this.secondaryInput = secondaryInput;
        }
        
        @Override
        public void apply() {
            if(!InsolatorManager.recipeExists(primaryInput, secondaryInput)) {
                CraftTweakerAPI.logError("No Insolator recipe exists for: " + primaryInput + " and " + secondaryInput);
                return;
            }
            InsolatorManager.removeRecipe(primaryInput, secondaryInput);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(primaryInput) + " and " + LogHelper.getStackDescription(secondaryInput);
        }
    }
}
