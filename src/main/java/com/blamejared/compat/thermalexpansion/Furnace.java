package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.FurnaceManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ZenClass("mods.thermalexpansion.Furnace")
@ModOnly("thermalexpansion")
public class Furnace {
    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, int energy) {
        for (IItemStack stack : input.getItems()) {
            ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(stack), InputHelper.toStack(output), energy));
        }
    }

    @ZenMethod
    public static void addFood(IItemStack input) {
        ModTweaker.LATE_ADDITIONS.add(new Food(InputHelper.toStack(input), true));
    }

    @ZenMethod
    public static void removeFood(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Food(InputHelper.toStack(input), false));
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) { ModTweaker.LATE_REMOVALS.add(new Remove(input, true)); }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) { ModTweaker.LATE_REMOVALS.add(new Remove(output, false)); }

    @ZenMethod
    public static void removeAllFood() { RecipeActions.removeAll("FurnaceFood", () -> foodSet().clear()); }

    @ZenMethod
    public static void removeAll() { RecipeActions.removeAll("Furnace", () -> { for (FurnaceManager.FurnaceRecipe r : FurnaceManager.getRecipeList(false)) FurnaceManager.removeRecipe(r.getInput()); }); }

    @SuppressWarnings("unchecked")
    private static Set<Object> foodSet() {
        try {
            Field field = FurnaceManager.class.getDeclaredField("foodSet");
            field.setAccessible(true);
            return (Set<Object>) field.get(null);
        } catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to access Furnace food registry", e); }
    }

    private static class Add extends BaseAction {
        private final ItemStack input, output; private final int energy;
        Add(ItemStack input, ItemStack output, int energy) { super("Furnace"); this.input = input; this.output = output; this.energy = energy; }
        @Override public void apply() { FurnaceManager.addRecipe(energy, input, output); }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(output); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target; private final boolean input;
        Remove(IIngredient target, boolean input) { super("Furnace"); this.target = target; this.input = input; }
        @Override public void apply() {
            List<FurnaceManager.FurnaceRecipe> recipes = new ArrayList<>();
            for (FurnaceManager.FurnaceRecipe recipe : FurnaceManager.getRecipeList(false)) {
                if (input ? target.matches(InputHelper.toIItemStack(recipe.getInput())) : target.matches(InputHelper.toIItemStack(recipe.getOutput()))) recipes.add(recipe);
            }
            for (FurnaceManager.FurnaceRecipe recipe : recipes) FurnaceManager.removeRecipe(recipe.getInput());
        }
        @Override protected String getRecipeInfo() { return target.toString(); }
    }

    private static class Food extends BaseAction {
        private final ItemStack input; private final boolean add;
        Food(ItemStack input, boolean add) { super("FurnaceFood"); this.input = input; this.add = add; }
        @Override public void apply() {
            Object key = FurnaceManager.convertInput(input);
            if (add) foodSet().add(key); else foodSet().remove(key);
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(input); }
    }
}
