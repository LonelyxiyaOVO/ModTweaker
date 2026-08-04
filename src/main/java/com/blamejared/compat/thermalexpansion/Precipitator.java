package com.blamejared.compat.thermalexpansion;

import cofh.core.util.ItemWrapper;
import cofh.thermalexpansion.util.managers.machine.PrecipitatorManager;
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
import java.util.List;
import java.util.Map;

@ZenClass("mods.thermalexpansion.Precipitator")
@ModOnly("thermalexpansion")
public class Precipitator {
    @ZenMethod
    public static void addRecipe(IItemStack output, int water, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), water, energy));
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input, true));
    }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, false));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Precipitator", () -> {
            recipeMap().clear();
            outputList().clear();
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, PrecipitatorManager.PrecipitatorRecipe> recipeMap() { return (Map<Object, PrecipitatorManager.PrecipitatorRecipe>) field("recipeMap"); }
    @SuppressWarnings("unchecked")
    private static List<ItemStack> outputList() { return (List<ItemStack>) field("outputList"); }
    private static Object field(String name) {
        try {
            Field field = PrecipitatorManager.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Precipitator " + name, e);
        }
    }

    private static class Add extends BaseAction {
        private final ItemStack output; private final int water, energy;
        Add(ItemStack output, int water, int energy) { super("Precipitator"); this.output = output; this.water = water; this.energy = energy; }
        @Override public void apply() {
            try {
                Constructor<PrecipitatorManager.PrecipitatorRecipe> constructor = PrecipitatorManager.PrecipitatorRecipe.class.getDeclaredConstructor(ItemStack.class, FluidStack.class, int.class);
                constructor.setAccessible(true);
                PrecipitatorManager.PrecipitatorRecipe recipe = constructor.newInstance(output, new FluidStack(FluidRegistry.WATER, water), energy);
                recipeMap().put(new ItemWrapper(output), recipe);
                refreshOutputs();
            } catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to create Precipitator recipe", e); }
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(output); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target; private final boolean input;
        Remove(IIngredient target, boolean input) { super("Precipitator"); this.target = target; this.input = input; }
        @Override public void apply() {
            recipeMap().values().removeIf(recipe -> input
                    ? target.matches(InputHelper.toILiquidStack(recipe.getInput()))
                    : target.matches(InputHelper.toIItemStack(recipe.getOutput())));
            refreshOutputs();
        }
        @Override protected String getRecipeInfo() { return target.toString(); }
    }

    private static void refreshOutputs() {
        outputList().clear();
        for (PrecipitatorManager.PrecipitatorRecipe recipe : recipeMap().values()) outputList().add(recipe.getOutput());
    }
}
