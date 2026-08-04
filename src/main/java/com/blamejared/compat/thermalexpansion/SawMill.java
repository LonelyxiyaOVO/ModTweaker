package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.SawmillManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Sawmill")
@ModOnly("thermalexpansion")
@ZenRegister
public class SawMill {

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("SawMill", () -> {
            for(SawmillManager.SawmillRecipe recipe : SawmillManager.getRecipeList()) {
                SawmillManager.removeRecipe(recipe.getInput());
            }
        });
    }
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy, InputHelper.toStack(secondaryOutput), secondaryChance));
    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance) {
        for (IItemStack stack : input.getItems()) addRecipe(output, stack, energy, secondaryOutput, secondaryChance);
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(ThermalExpansionReflection.removeBy("SawMill", SawmillManager.class,
                "getRecipeList", input, false, "getInput"));
    }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(ThermalExpansionReflection.removeBy("SawMill", SawmillManager.class,
                "getRecipeList", output, true, "getOutput", "getSecondaryOutput"));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack output, input, secondaryOutput;
        private int energy, secondaryChance;
        
        public Add(ItemStack output, ItemStack input, int energy, ItemStack secondaryOutput, int secondaryChance) {
            super("Sawmill");
            this.output = output;
            this.input = input;
            this.secondaryOutput = secondaryOutput;
            this.energy = energy;
            this.secondaryChance = secondaryChance;
            if(!secondaryOutput.isEmpty() && secondaryChance <= 0) {
                this.secondaryChance = 100;
            }
        }
        
        @Override
        public void apply() {
            SawmillManager.addRecipe(energy, input, output, secondaryOutput, secondaryChance);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Sawmill");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!SawmillManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Sawmill recipe exists for: " + input);
                return;
            }
            SawmillManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
