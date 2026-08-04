package com.blamejared.compat.thermalexpansion;

import cofh.core.inventory.ComparableItemStackValidated;
import cofh.thermalexpansion.util.managers.machine.ChargerManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.util.Map;

@ZenClass("mods.thermalexpansion.Charger")
@ModOnly("thermalexpansion")
public class Charger {
    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, int energy) {
        for (IItemStack stack : input.getItems()) {
            ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(stack), InputHelper.toStack(output), energy));
        }
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
        RecipeActions.removeAll("Charger", () -> recipeMap().clear());
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, ChargerManager.ChargerRecipe> recipeMap() {
        try {
            Field field = ChargerManager.class.getDeclaredField("recipeMap");
            field.setAccessible(true);
            return (Map<Object, ChargerManager.ChargerRecipe>) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Charger registry", e);
        }
    }

    private static class Add extends BaseAction {
        private final ItemStack input, output;
        private final int energy;
        Add(ItemStack input, ItemStack output, int energy) {
            super("Charger"); this.input = input; this.output = output; this.energy = energy;
        }
        @Override public void apply() {
            ChargerManager.ChargerRecipe recipe = new ChargerManager.ChargerRecipe(input, output, energy);
            recipeMap().put(new ComparableItemStackValidated(input), recipe);
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(output); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target;
        private final boolean input;
        Remove(IIngredient target, boolean input) { super("Charger"); this.target = target; this.input = input; }
        @Override public void apply() {
            recipeMap().values().removeIf(recipe -> input
                    ? target.matches(InputHelper.toIItemStack(recipe.getInput()))
                    : target.matches(InputHelper.toIItemStack(recipe.getOutput())));
        }
        @Override protected String getRecipeInfo() { return target.toString(); }
    }
}
