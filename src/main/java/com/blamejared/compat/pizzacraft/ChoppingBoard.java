package com.blamejared.compat.pizzacraft;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/** CraftTweaker integration for PizzaCraft's Chopping Board. */
@ZenClass("mods.pizzacraft.ChoppingBoard")
@ModOnly("pizzacraft")
@ZenRegister
public final class ChoppingBoard {

    private static final String NAME = "PizzaCraft Chopping Board";

    private ChoppingBoard() {
    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input) {
        ModTweaker.LATE_ADDITIONS.add(new Add(
                InputHelper.toStack(output), CraftTweakerMC.getIngredient(input)));
    }

    @ZenMethod
    public static void removeByOutput(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByOutput(InputHelper.toStack(output)));
    }

    @ZenMethod
    public static void removeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByInput(input));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, () -> manager().getRecipes().clear());
    }

    private static ChoppingBoardRecipes manager() {
        return ChoppingBoardRecipes.instance();
    }

    private static final class Add extends BaseAction {

        private final ItemStack output;
        private final Ingredient input;

        private Add(ItemStack output, Ingredient input) {
            super(NAME);
            this.output = output;
            this.input = input;
        }

        @Override
        public void apply() {
            for (ItemStack stack : input.getMatchingStacks()) {
                manager().addChoppingRecipe(stack, output);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return output.toString();
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
            manager().getRecipes().entrySet().removeIf(entry ->
                    ItemStack.areItemStacksEqual(entry.getValue(), output));
        }

        @Override
        protected String getRecipeInfo() {
            return output.toString();
        }
    }

    private static final class RemoveByInput extends BaseAction {

        private final IIngredient input;

        private RemoveByInput(IIngredient input) {
            super(NAME);
            this.input = input;
        }

        @Override
        public void apply() {
            manager().getRecipes().entrySet().removeIf(entry ->
                    StackHelper.matches(input, InputHelper.toIItemStack(entry.getKey())));
        }

        @Override
        protected String getRecipeInfo() {
            return input.toString();
        }
    }
}
