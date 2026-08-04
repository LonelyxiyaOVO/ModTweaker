package com.blamejared.compat.thermalexpansion;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cofh.core.inventory.ComparableItemStackValidatedNBT;
import cofh.core.util.helpers.FluidHelper;
import cofh.thermalexpansion.util.managers.machine.BrewerManager;

@ZenClass("mods.thermalexpansion.Brewer")
@ModOnly("thermalexpansion")
public class Brewer {
    @ZenMethod
    public static void addRecipe(IItemStack input, ILiquidStack fluidInput, ILiquidStack fluidOutput, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(input), InputHelper.toFluid(fluidInput), InputHelper.toFluid(fluidOutput), energy));
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, ILiquidStack fluidInput, ILiquidStack fluidOutput, int energy) {
        for (IItemStack stack : input.getItems()) addRecipe(stack, fluidInput, fluidOutput, energy);
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) { ModTweaker.LATE_REMOVALS.add(new Remove(input, false)); }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) { ModTweaker.LATE_REMOVALS.add(new Remove(output, true)); }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Brewer", () -> {
            recipeMap().clear();
            validationSet().clear();
            validationFluids().clear();
        });
    }

    private static Map<List<Integer>, BrewerManager.BrewerRecipe> recipeMap() { return staticField("recipeMap"); }
    private static Set<ComparableItemStackValidatedNBT> validationSet() { return staticField("validationSet"); }
    private static Set<String> validationFluids() { return staticField("validationFluids"); }

    @SuppressWarnings("unchecked")
    private static <T> T staticField(String name) {
        try {
            Field field = BrewerManager.class.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Brewer " + name, e);
        }
    }

    private static List<Integer> key(ItemStack item, FluidStack fluid) {
        return Arrays.asList(BrewerManager.convertInput(item).hashCode(), FluidHelper.getFluidHash(fluid));
    }

    private static class Add extends BaseAction {
        private final ItemStack input;
        private final FluidStack fluidInput, fluidOutput;
        private final int energy;
        Add(ItemStack input, FluidStack fluidInput, FluidStack fluidOutput, int energy) {
            super("Brewer"); this.input = input; this.fluidInput = fluidInput; this.fluidOutput = fluidOutput; this.energy = energy;
        }
        @Override public void apply() {
            try {
                Constructor<BrewerManager.BrewerRecipe> c = BrewerManager.BrewerRecipe.class.getDeclaredConstructor(ItemStack.class, FluidStack.class, FluidStack.class, int.class);
                c.setAccessible(true);
                BrewerManager.BrewerRecipe recipe = c.newInstance(input, fluidInput, fluidOutput, energy);
                recipeMap().put(key(input, fluidInput), recipe);
                refreshValidation();
            } catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to create Thermal Expansion Brewer recipe", e); }
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(input); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target; private final boolean output;
        Remove(IIngredient target, boolean output) { super("Brewer"); this.target = target; this.output = output; }
        @Override public void apply() {
            recipeMap().values().removeIf(recipe -> {
                Object value = invoke(recipe, output ? "getOutputFluid" : "getInput");
                return value instanceof FluidStack ? target.matches(InputHelper.toILiquidStack((FluidStack) value)) : target.matches(InputHelper.toIItemStack((ItemStack) value));
            });
            refreshValidation();
        }
        @Override protected String getRecipeInfo() { return target.toString(); }
    }

    private static Object invoke(Object object, String method) {
        try { return object.getClass().getMethod(method).invoke(object); }
        catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to read Brewer recipe " + method, e); }
    }

    private static void refreshValidation() {
        validationSet().clear(); validationFluids().clear();
        for (BrewerManager.BrewerRecipe recipe : recipeMap().values()) {
            validationSet().add(BrewerManager.convertInput((ItemStack) invoke(recipe, "getInput")));
            validationFluids().add(((FluidStack) invoke(recipe, "getInputFluid")).getFluid().getName());
        }
    }
}
