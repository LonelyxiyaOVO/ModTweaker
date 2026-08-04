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

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.thermalexpansion.FurnacePyrolysis")
@ModOnly("thermalexpansion")
public class FurnacePyrolysis {
    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, int energy, int creosote) {
        for (IItemStack stack : input.getItems()) {
            ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(stack), InputHelper.toStack(output), energy, creosote));
        }
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) { ModTweaker.LATE_REMOVALS.add(new Remove(input, true)); }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) { ModTweaker.LATE_REMOVALS.add(new Remove(output, false)); }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("FurnacePyrolysis", () -> {
            for (FurnaceManager.FurnaceRecipe recipe : FurnaceManager.getRecipeList(true)) FurnaceManager.removeRecipePyrolysis(recipe.getInput());
        });
    }

    private static class Add extends BaseAction {
        private final ItemStack input, output; private final int energy, creosote;
        Add(ItemStack input, ItemStack output, int energy, int creosote) { super("FurnacePyrolysis"); this.input = input; this.output = output; this.energy = energy; this.creosote = creosote; }
        @Override public void apply() { FurnaceManager.addRecipePyrolysis(energy, input, output, creosote); }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(output); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target; private final boolean input;
        Remove(IIngredient target, boolean input) { super("FurnacePyrolysis"); this.target = target; this.input = input; }
        @Override public void apply() {
            List<FurnaceManager.FurnaceRecipe> recipes = new ArrayList<>();
            for (FurnaceManager.FurnaceRecipe recipe : FurnaceManager.getRecipeList(true)) {
                if (input ? target.matches(InputHelper.toIItemStack(recipe.getInput())) : target.matches(InputHelper.toIItemStack(recipe.getOutput()))) recipes.add(recipe);
            }
            for (FurnaceManager.FurnaceRecipe recipe : recipes) FurnaceManager.removeRecipePyrolysis(recipe.getInput());
        }
        @Override protected String getRecipeInfo() { return target.toString(); }
    }
}
