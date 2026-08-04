package com.blamejared.compat.thermalexpansion.dynamos;

import cofh.thermalexpansion.util.managers.dynamo.ReactantManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.compat.thermalexpansion.ThermalExpansionReflection;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import cofh.core.inventory.ComparableItemStack;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.ReactantDynamo")
@ModOnly("thermalexpansion")
@ZenRegister
public class ReactantDynamo {

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("ReactantDynamo", () -> {
            ThermalExpansionReflection.clear(ReactantManager.class, "reactionMap");
            ThermalExpansionReflection.clear(ReactantManager.class, "validReactants");
            ThermalExpansionReflection.clear(ReactantManager.class, "validFluids");
            ThermalExpansionReflection.clear(ReactantManager.class, "validReactantsElemental");
            ThermalExpansionReflection.clear(ReactantManager.class, "validFluidsElemental");
        });
    }

    @ZenMethod
    public static void addReaction(IIngredient stack, ILiquidStack fluid, int energy) {
        for (IItemStack item : stack.getItems()) addReaction(item, fluid, energy);
    }

    @ZenMethod
    public static void addReactionElemental(IIngredient stack, ILiquidStack fluid, int energy) {
        for (IItemStack item : stack.getItems()) addReactionElemental(item, fluid, energy);
    }

    @ZenMethod
    public static void removeReaction(IIngredient stack, ILiquidStack fluid) {
        for (IItemStack item : stack.getItems()) removeReaction(item, fluid);
    }

    @ZenMethod
    public static void removeReactionElemental(IIngredient stack, ILiquidStack fluid) {
        for (IItemStack item : stack.getItems()) removeReactionElemental(item, fluid);
    }

    @ZenMethod
    public static void addElementalReactant(IItemStack stack) {
        ModTweaker.LATE_ADDITIONS.add(new Elemental(stack, true, null));
    }

    @ZenMethod
    public static void removeElementalReactant(IItemStack stack) {
        ModTweaker.LATE_REMOVALS.add(new Elemental(stack, false, null));
    }

    @ZenMethod
    public static void addElementalFluid(String fluid) {
        ModTweaker.LATE_ADDITIONS.add(new Elemental(null, true, fluid));
    }

    @ZenMethod
    public static void removeElementalFluid(String fluid) {
        ModTweaker.LATE_REMOVALS.add(new Elemental(null, false, fluid));
    }

    @ZenMethod
    public static void addElementalFluid(ILiquidStack fluid) {
        addElementalFluid(InputHelper.toFluid(fluid).getFluid().getName());
    }

    @ZenMethod
    public static void removeElementalFluid(ILiquidStack fluid) {
        removeElementalFluid(InputHelper.toFluid(fluid).getFluid().getName());
    }
    
    @ZenMethod
    public static void addReaction(IItemStack stack, ILiquidStack fluid, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid(), energy));
    }
    
    @ZenMethod
    public static void addReactionElemental(IItemStack stack, ILiquidStack fluid, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new AddElemental(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid(), energy));
    }
    
    @ZenMethod
    public static void removeReaction(IItemStack stack, ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid()));
    }
    
    @ZenMethod
    public static void removeReactionElemental(IItemStack stack, ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new RemoveElemental(InputHelper.toStack(stack), InputHelper.toFluid(fluid).getFluid()));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        private int energy;
        
        protected Add(ItemStack stack, Fluid fluid, int energy) {
            super("ReactantDynamo");
            this.stack = stack;
            this.fluid = fluid;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            ReactantManager.addReaction(stack, fluid, energy);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        
        protected Remove(ItemStack stack, Fluid fluid) {
            super("ReactantDynamo");
            this.stack = stack;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            ReactantManager.removeReaction(stack, fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class AddElemental extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        private int energy;
        
        protected AddElemental(ItemStack stack, Fluid fluid, int energy) {
            super("ReactantDynamoElemental");
            this.stack = stack;
            this.fluid = fluid;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            ReactantManager.addElementalReaction(stack, fluid, energy);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class RemoveElemental extends BaseAction {
        
        private ItemStack stack;
        private Fluid fluid;
        
        protected RemoveElemental(ItemStack stack, Fluid fluid) {
            super("ReactantDynamo");
            this.stack = stack;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            ReactantManager.removeElementalReaction(stack, fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(stack) + " " + LogHelper.getStackDescription(fluid);
        }
    }

    @SuppressWarnings("unchecked")
    private static class Elemental extends BaseAction {
        private final ItemStack stack;
        private final boolean add;
        private final String fluid;

        Elemental(IItemStack stack, boolean add, String fluid) {
            super("ReactantDynamo Elemental");
            this.stack = stack == null ? null : InputHelper.toStack(stack);
            this.add = add;
            this.fluid = fluid;
        }

        @Override
        public void apply() {
            if (stack != null) {
                java.util.Set<ComparableItemStack> set = (java.util.Set<ComparableItemStack>) ThermalExpansionReflection.get(ReactantManager.class, "validReactantsElemental");
                ComparableItemStack comparable = new ComparableItemStack(stack);
                if (add) set.add(comparable); else set.remove(comparable);
            } else {
                java.util.Set<String> set = (java.util.Set<String>) ThermalExpansionReflection.get(ReactantManager.class, "validFluidsElemental");
                if (add) set.add(fluid); else set.remove(fluid);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return stack == null ? fluid : LogHelper.getStackDescription(stack);
        }
    }
    
}
