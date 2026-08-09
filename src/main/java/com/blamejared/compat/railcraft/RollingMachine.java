package com.blamejared.compat.railcraft;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.api.crafting.IRollingMachineCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.railcraft.RollingMachine")
@ModOnly("railcraft")
@ZenRegister
public final class RollingMachine {

    private static final String NAME = "Railcraft Rolling Machine";

    private RollingMachine() {
    }

    @ZenMethod
    public static void addShaped(String name, IItemStack output, IIngredient[][] inputs,
                                 @Optional(valueLong = IRollingMachineCrafter.DEFAULT_PROCESS_TIME) int time) {
        ModTweaker.LATE_ADDITIONS.add(new AddShaped(name, InputHelper.toStack(output), inputs, time));
    }

    @ZenMethod
    public static void addShapeless(String name, IItemStack output, IIngredient[] inputs,
                                    @Optional(valueLong = IRollingMachineCrafter.DEFAULT_PROCESS_TIME) int time) {
        ModTweaker.LATE_ADDITIONS.add(new AddShapeless(name, InputHelper.toStack(output), inputs, time));
    }

    @ZenMethod
    public static void remove(String name) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByName(name));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        removeByOutput(output);
    }

    @ZenMethod
    public static void removeByOutput(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByOutput(InputHelper.toStack(output)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, () -> Crafters.rollingMachine().getRecipes().clear());
    }

    private static final class AddShaped extends BaseAction {

        private final String name;
        private final ItemStack output;
        private final IIngredient[][] inputs;
        private final int time;

        private AddShaped(String name, ItemStack output, IIngredient[][] inputs, int time) {
            super(NAME);
            this.name = name;
            this.output = output;
            this.inputs = inputs;
            this.time = time;
        }

        @Override
        public void apply() {
            int height = inputs.length;
            int width = 0;
            for (IIngredient[] row : inputs) {
                width = Math.max(width, row == null ? 0 : row.length);
            }
            NonNullList<Ingredient> ingredients = NonNullList.withSize(
                    width * height, Ingredient.EMPTY);
            for (int row = 0; row < height; row++) {
                IIngredient[] inputRow = inputs[row];
                if (inputRow == null) {
                    continue;
                }
                for (int column = 0; column < inputRow.length; column++) {
                    ingredients.set(row * width + column,
                            CraftTweakerMC.getIngredient(inputRow[column]));
                }
            }
            Crafters.rollingMachine().newRecipe(output)
                    .name("modtweaker", name)
                    .time(time)
                    .recipe(new ShapedRecipes("modtweaker", width, height, ingredients, output));
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }

    private static final class AddShapeless extends BaseAction {

        private final String name;
        private final ItemStack output;
        private final IIngredient[] inputs;
        private final int time;

        private AddShapeless(String name, ItemStack output, IIngredient[] inputs, int time) {
            super(NAME);
            this.name = name;
            this.output = output;
            this.inputs = inputs;
            this.time = time;
        }

        @Override
        public void apply() {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (IIngredient input : inputs) {
                ingredients.add(CraftTweakerMC.getIngredient(input));
            }
            Crafters.rollingMachine().newRecipe(output)
                    .name("modtweaker", name)
                    .time(time)
                    .recipe(new ShapelessRecipes("modtweaker", output, ingredients));
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }

    private static final class RemoveByName extends BaseAction {

        private final String name;

        private RemoveByName(String name) {
            super(NAME);
            this.name = name;
        }

        @Override
        public void apply() {
            Crafters.rollingMachine().getRecipes().removeIf(recipe ->
                    Objects.toString(recipe.getRegistryName()).equals(name));
        }

        @Override
        protected String getRecipeInfo() {
            return name;
        }
    }

    private static final class RemoveByOutput extends BaseAction {

        private final ItemStack output;

        private RemoveByOutput(ItemStack output) {
            super(NAME);
            this.output = output;
        }

        @Override
        public void apply() {
            Crafters.rollingMachine().getRecipes().removeIf(recipe ->
                    recipe.getRecipeOutput().isItemEqual(output));
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
}
