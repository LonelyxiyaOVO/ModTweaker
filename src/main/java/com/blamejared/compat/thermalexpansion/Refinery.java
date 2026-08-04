package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.RefineryManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.compat.thermalexpansion.ThermalExpansionReflection;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Refinery")
@ModOnly("thermalexpansion")
@ZenRegister
public class Refinery {

    @ZenMethod
    public static void addFossilFuel(String name) {
        ModTweaker.LATE_ADDITIONS.add(new Fuel(name, true, true));
    }

    @ZenMethod
    public static void removeFossilFuel(String name) {
        ModTweaker.LATE_REMOVALS.add(new Fuel(name, true, false));
    }

    @ZenMethod
    public static void addBioFuel(String name) {
        ModTweaker.LATE_ADDITIONS.add(new Fuel(name, false, true));
    }

    @ZenMethod
    public static void removeBioFuel(String name) {
        ModTweaker.LATE_REMOVALS.add(new Fuel(name, false, false));
    }

    @ZenMethod
    public static void addFossilFuel(ILiquidStack fluid) {
        addFossilFuel(InputHelper.toFluid(fluid).getFluid().getName());
    }

    @ZenMethod
    public static void removeFossilFuel(ILiquidStack fluid) {
        removeFossilFuel(InputHelper.toFluid(fluid).getFluid().getName());
    }

    @ZenMethod
    public static void addBioFuel(ILiquidStack fluid) {
        addBioFuel(InputHelper.toFluid(fluid).getFluid().getName());
    }

    @ZenMethod
    public static void removeBioFuel(ILiquidStack fluid) {
        removeBioFuel(InputHelper.toFluid(fluid).getFluid().getName());
    }

    @ZenMethod
    public static void removeAllFossilFuels() {
        RecipeActions.removeAll("Refinery Fossil Fuels", () -> ThermalExpansionReflection.clear(RefineryManager.class, "fossilFluids"));
    }

    @ZenMethod
    public static void removeAllBioFuels() {
        RecipeActions.removeAll("Refinery Bio Fuels", () -> ThermalExpansionReflection.clear(RefineryManager.class, "bioFluids"));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Refinery", () -> {
            ThermalExpansionReflection.clear(RefineryManager.class, "recipeMap");
            ThermalExpansionReflection.clear(RefineryManager.class, "recipeMapPotion");
            ThermalExpansionReflection.clear(RefineryManager.class, "fossilFluids");
            ThermalExpansionReflection.clear(RefineryManager.class, "bioFluids");
        });
    }
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, WeightedItemStack outputItem, ILiquidStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(output), InputHelper.toFluid(input), outputItem, energy));
    }

    @ZenMethod
    public static void addRecipePotion(ILiquidStack output, ILiquidStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new AddPotion(InputHelper.toFluid(output), InputHelper.toFluid(input), energy));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(input), false));
    }

    @ZenMethod
    public static void removeRecipeByInput(crafttweaker.api.item.IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new RecipeMatch(input, true));
    }

    @ZenMethod
    public static void removeRecipeByOutput(crafttweaker.api.item.IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new RecipeMatch(output, false));
    }

    @ZenMethod
    public static void removeRecipePotion(ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(input), true));
    }

    private static class RecipeMatch extends BaseAction {
        private final crafttweaker.api.item.IIngredient target;
        private final boolean input;

        RecipeMatch(crafttweaker.api.item.IIngredient target, boolean input) {
            super("Refinery");
            this.target = target;
            this.input = input;
        }

        @Override
        public void apply() {
            java.util.Map<?, ?> recipes = (java.util.Map<?, ?>) ThermalExpansionReflection.get(RefineryManager.class, "recipeMap");
            for (Object recipe : new java.util.ArrayList<>(recipes.values())) {
                Object value = invoke(recipe, input ? "getInput" : "getOutputFluid");
                Object item = invoke(recipe, "getOutputItem");
                boolean match = value instanceof FluidStack
                        && target.matches(InputHelper.toILiquidStack((FluidStack) value));
                if (!input && item instanceof net.minecraft.item.ItemStack) {
                    match |= target.matches(InputHelper.toIItemStack((net.minecraft.item.ItemStack) item));
                }
                if (match) RefineryManager.removeRecipe((FluidStack) invoke(recipe, "getInput"));
            }
        }

        private Object invoke(Object recipe, String name) {
            try {
                java.lang.reflect.Method method = recipe.getClass().getMethod(name);
                method.setAccessible(true);
                return method.invoke(recipe);
            } catch (ReflectiveOperationException e) {
                return null;
            }
        }

        @Override
        protected String getRecipeInfo() {
            return target.toString();
        }
    }

    @SuppressWarnings("unchecked")
    private static class Fuel extends BaseAction {
        private final String name;
        private final boolean fossil;
        private final boolean add;

        Fuel(String name, boolean fossil, boolean add) {
            super("Refinery");
            this.name = name;
            this.fossil = fossil;
            this.add = add;
        }

        @Override
        public void apply() {
            Object registry = ThermalExpansionReflection.get(RefineryManager.class, fossil ? "fossilFluids" : "bioFluids");
            if (add) {
                ((java.util.Set<String>) registry).add(name);
            } else {
                ((java.util.Set<String>) registry).remove(name);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return name;
        }
    }
    
    private static class Add extends BaseAction {
        
        private FluidStack output, input;
        private WeightedItemStack outputItem;
        private int energy;
        
        public Add(FluidStack output, FluidStack input, WeightedItemStack outputItem, int energy) {
            super("Refinery");
            this.output = output;
            this.input = input;
            this.outputItem = outputItem;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            if(outputItem == null) {
                RefineryManager.addRecipe(energy, input, output);
            } else {
                RefineryManager.addRecipe(energy, input, output, InputHelper.toStack(outputItem.getStack()), (int) outputItem.getPercent());
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }

    private static class AddPotion extends BaseAction {

        private FluidStack output, input;
        private int energy;

        public AddPotion(FluidStack output, FluidStack input, int energy) {
            super("Refinery");
            this.output = output;
            this.input = input;
            this.energy = energy;
        }

        @Override
        public void apply() {
            RefineryManager.addRecipePotion(energy, input, output);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
        
    private static class Remove extends BaseAction {
        
        private FluidStack input;
        private boolean potion;
        
        public Remove(FluidStack input, boolean potion) {
            super("Refinery");
            this.potion = potion;
            this.input = input;
        }

        @Override
        public void apply() {
            if (potion) {
                if(!RefineryManager.recipeExistsPotion(input)) {
                    CraftTweakerAPI.logError("No Refinery potion recipe exists for: " + input);
                    return;
                }
                RefineryManager.removeRecipePotion(input);
            } else {
                if(!RefineryManager.recipeExists(input)) {
                    CraftTweakerAPI.logError("No Refinery recipe exists for: " + input);
                    return;
                }
                RefineryManager.removeRecipe(input);
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
