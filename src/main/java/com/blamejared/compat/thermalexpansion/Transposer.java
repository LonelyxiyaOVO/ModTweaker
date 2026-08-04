package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.TransposerManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.compat.thermalexpansion.ThermalExpansionReflection;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Transposer")
@ModOnly("thermalexpansion")
@ZenRegister
public class Transposer {

    @ZenMethod
    public static void addExtractRecipe(ILiquidStack output, IIngredient input, int energy, @Optional WeightedItemStack itemOut) {
        for (IItemStack stack : input.getItems()) addExtractRecipe(output, stack, energy, itemOut);
    }

    @ZenMethod
    public static void removeAllExtractRecipes() {
        RecipeActions.removeAll("Transposer Extract", () -> {
            ThermalExpansionReflection.clear(TransposerManager.class, "recipeMapExtract");
            ThermalExpansionReflection.clear(TransposerManager.class, "validationSet");
        });
    }

    @ZenMethod
    public static void removeAllFillRecipes() {
        RecipeActions.removeAll("Transposer Fill", () -> {
            ThermalExpansionReflection.clear(TransposerManager.class, "recipeMapFill");
            ThermalExpansionReflection.clear(TransposerManager.class, "validationSet");
        });
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Transposer", () -> {
            ThermalExpansionReflection.clear(TransposerManager.class, "recipeMapExtract");
            ThermalExpansionReflection.clear(TransposerManager.class, "recipeMapFill");
            ThermalExpansionReflection.clear(TransposerManager.class, "containerOverrides");
            ThermalExpansionReflection.clear(TransposerManager.class, "validationSet");
        });
    }
    
    @ZenMethod
    public static void addExtractRecipe(ILiquidStack output, IItemStack input, int energy, @Optional WeightedItemStack itemOut) {
        ItemStack stack = ItemStack.EMPTY;
        int percent = 0;
        if(itemOut != null) {
            stack = InputHelper.toStack(itemOut.getStack());
            percent = (int) itemOut.getPercent();
        }
        
        ModTweaker.LATE_ADDITIONS.add(new AddExtract(InputHelper.toFluid(output), InputHelper.toStack(input), energy, stack, percent));
    }
    
    @ZenMethod
    public static void removeExtractRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new RemoveExtract(InputHelper.toStack(input)));
    }

    @ZenMethod
    public static void removeExtractRecipe(IIngredient input) {
        for (IItemStack stack : input.getItems()) removeExtractRecipe(stack);
    }

    @ZenMethod
    public static void removeExtractRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new crafttweaker.IAction() {
            @Override
            public void apply() {
                java.util.Map<?, ?> recipes = (java.util.Map<?, ?>) ThermalExpansionReflection.get(TransposerManager.class, "recipeMapExtract");
                for (Object recipe : new java.util.ArrayList<>(recipes.values())) {
                    Object fluid = invoke(recipe, "getOutputFluid");
                    Object item = invoke(recipe, "getOutputItem");
                    if ((fluid instanceof FluidStack && output.matches(InputHelper.toILiquidStack((FluidStack) fluid)))
                            || (item instanceof ItemStack && output.matches(InputHelper.toIItemStack((ItemStack) item)))) {
                        TransposerManager.removeExtractRecipe((ItemStack) invoke(recipe, "getInput"));
                    }
                }
            }

            @Override
            public String describe() { return "Removing Transposer extract recipes by output"; }
        });
    }
    
    @ZenMethod
    public static void addFillRecipe(IItemStack output, IItemStack input, ILiquidStack fluid, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new AddFill(InputHelper.toFluid(fluid), InputHelper.toStack(input), energy, InputHelper.toStack(output)));
    }
    
    @ZenMethod
    public static void removeFillRecipe(IItemStack input, ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new RemoveFill(InputHelper.toStack(input), InputHelper.toFluid(fluid)));
    }

    @ZenMethod
    public static void addFillRecipe(IItemStack output, IIngredient input, ILiquidStack fluid, int energy) {
        for (IItemStack stack : input.getItems()) addFillRecipe(output, stack, fluid, energy);
    }

    @ZenMethod
    public static void removeFillRecipe(IIngredient input, ILiquidStack fluid) {
        for (IItemStack stack : input.getItems()) removeFillRecipe(stack, fluid);
    }

    @ZenMethod
    public static void removeFillRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new crafttweaker.IAction() {
            @Override
            public void apply() {
                java.util.Map<?, ?> recipes = (java.util.Map<?, ?>) ThermalExpansionReflection.get(TransposerManager.class, "recipeMapFill");
                for (Object recipe : new java.util.ArrayList<>(recipes.values())) {
                    Object item = invoke(recipe, "getOutput");
                    if (item instanceof ItemStack && output.matches(InputHelper.toIItemStack((ItemStack) item))) {
                        TransposerManager.removeFillRecipe((ItemStack) invoke(recipe, "getInput"), (FluidStack) invoke(recipe, "getFluidInput"));
                    }
                }
            }

            @Override
            public String describe() { return "Removing Transposer fill recipes by output"; }
        });
    }

    private static Object invoke(Object recipe, String name) {
        try {
            java.lang.reflect.Method method = recipe.getClass().getMethod(name);
            method.setAccessible(true);
            return method.invoke(recipe);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
    
    private static class AddExtract extends BaseAction {
        
        private FluidStack output;
        private ItemStack input;
        private int energy;
        
        private ItemStack itemOut;
        private int chance;
        
        public AddExtract(FluidStack output, ItemStack input, int energy, ItemStack itemOut, int chance) {
            super("Transposer Extract");
            this.output = output;
            this.input = input;
            this.energy = energy;
            this.itemOut = itemOut;
            this.chance = chance;
        }
        
        @Override
        public void apply() {
            TransposerManager.addExtractRecipe(energy, input, itemOut, output, chance, false);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class RemoveExtract extends BaseAction {
        
        private ItemStack input;
        
        public RemoveExtract(ItemStack input) {
            super("Transposer Extract");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!TransposerManager.extractRecipeExists(input)) {
                CraftTweakerAPI.logError("No Transposer Extraction recipe exists for: " + input);
                return;
            }
            TransposerManager.removeExtractRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
    private static class AddFill extends BaseAction {
        
        private FluidStack fluid;
        private ItemStack input;
        private int energy;
        
        private ItemStack output;
        
        public AddFill(FluidStack output, ItemStack input, int energy, ItemStack itemOut) {
            super("Transposer Fill");
            this.fluid = output;
            this.input = input;
            this.energy = energy;
            this.output = itemOut;
        }
        
        @Override
        public void apply() {
            TransposerManager.addFillRecipe(energy, input, output, fluid, false);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(fluid);
        }
    }
    
    private static class RemoveFill extends BaseAction {
        
        private ItemStack input;
        private FluidStack fluid;
        
        public RemoveFill(ItemStack input, FluidStack fluid) {
            super("Transposer Fill");
            this.input = input;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            if(!TransposerManager.fillRecipeExists(input, fluid)) {
                CraftTweakerAPI.logError("No Transposer Fill recipe exists for: " + input);
                return;
            }
            TransposerManager.removeFillRecipe(input, fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
}
