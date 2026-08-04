package com.blamejared.compat.thermalexpansion;

import cofh.core.util.ItemWrapper;
import cofh.thermalexpansion.util.managers.machine.ExtruderManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ZenClass("mods.thermalexpansion.Extruder")
@ModOnly("thermalexpansion")
public class Extruder {

    @ZenMethod
    public static void addRecipe(IItemStack output, int fluidHot, int fluidCold,
                                 int energy, boolean sedimentary) {
        ModTweaker.LATE_ADDITIONS.add(new Add(
                InputHelper.toStack(output), fluidHot, fluidCold, energy, sedimentary));
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input, true, null));
    }

    @ZenMethod
    public static void removeRecipeByInput(boolean sedimentary, IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input, true, sedimentary));
    }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, false, null));
    }

    @ZenMethod
    public static void removeRecipeByOutput(boolean sedimentary, IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, false, sedimentary));
    }

    @ZenMethod
    public static void removeByType(boolean sedimentary) {
        RecipeActions.removeAll("Extruder", () -> map(sedimentary).clear());
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Extruder", () -> {
            map(true).clear();
            map(false).clear();
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, ExtruderManager.ExtruderRecipe> map(boolean sedimentary) {
        String fieldName = sedimentary ? "recipeMapSedimentary" : "recipeMapIgneous";
        return (Map<Object, ExtruderManager.ExtruderRecipe>) field(fieldName);
    }

    private static Object field(String name) {
        try {
            Field field = ExtruderManager.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Extruder " + name, e);
        }
    }

    private static class Add extends BaseAction {
        private final ItemStack output;
        private final int fluidHot;
        private final int fluidCold;
        private final int energy;
        private final boolean sedimentary;

        Add(ItemStack output, int fluidHot, int fluidCold, int energy, boolean sedimentary) {
            super("Extruder");
            this.output = output;
            this.fluidHot = fluidHot;
            this.fluidCold = fluidCold;
            this.energy = energy;
            this.sedimentary = sedimentary;
        }

        @Override
        public void apply() {
            try {
                Constructor<ExtruderManager.ExtruderRecipe> constructor =
                        ExtruderManager.ExtruderRecipe.class.getDeclaredConstructor(
                                ItemStack.class, FluidStack.class, FluidStack.class, int.class);
                constructor.setAccessible(true);
                ExtruderManager.ExtruderRecipe recipe = constructor.newInstance(
                        output,
                        new FluidStack(FluidRegistry.LAVA, fluidHot),
                        new FluidStack(FluidRegistry.WATER, fluidCold),
                        energy);
                map(sedimentary).put(new ItemWrapper(output), recipe);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to create Extruder recipe", e);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target;
        private final boolean input;
        private final Boolean sedimentary;

        Remove(IIngredient target, boolean input, Boolean sedimentary) {
            super("Extruder");
            this.target = target;
            this.input = input;
            this.sedimentary = sedimentary;
        }

        @Override
        public void apply() {
            List<Boolean> types = new ArrayList<>();
            if (sedimentary == null) {
                types.add(false);
                types.add(true);
            } else {
                types.add(sedimentary);
            }

            for (boolean type : types) {
                for (ExtruderManager.ExtruderRecipe recipe : new ArrayList<>(map(type).values())) {
                    boolean matches = input
                            ? target.matches(InputHelper.toILiquidStack(recipe.getInputHot()))
                                    || target.matches(InputHelper.toILiquidStack(recipe.getInputCold()))
                            : target.matches(InputHelper.toIItemStack(recipe.getOutput()));
                    if (matches) {
                        map(type).remove(new ItemWrapper(recipe.getOutput()));
                    }
                }
            }
        }

        @Override
        protected String getRecipeInfo() {
            return target.toString();
        }
    }
}
