package com.blamejared.compat.pizzacraft;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.compat.pizzacraft.recipes.ShapedMortarRecipeCrT;
import com.blamejared.compat.pizzacraft.recipes.ShapelessMortarRecipeCrT;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.tiviacz.pizzacraft.crafting.mortar.IMortarRecipe;
import com.tiviacz.pizzacraft.crafting.mortar.MortarRecipeManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/** CraftTweaker integration for PizzaCraft's Mortar and Pestle. */
@ZenClass("mods.pizzacraft.Mortar")
@ModOnly("pizzacraft")
@ZenRegister
public final class Mortar {

    private static final String NAME = "PizzaCraft Mortar";

    private Mortar() {
    }

    @ZenMethod
    public static void addShaped(IItemStack output, int duration, IIngredient[] inputs) {
        add(ShapedMortarRecipeCrT.create(output, duration, inputs));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, int duration, IIngredient[] inputs) {
        add(ShapelessMortarRecipeCrT.create(output, duration, inputs));
    }

    /** Kept as an alias for the original CRT API. */
    @Deprecated
    @ZenMethod
    public static void addRecipe(IItemStack output, int duration, IIngredient[] inputs) {
        addShapeless(output, duration, inputs);
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(output)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, () -> manager().getRecipeList().clear());
    }

    private static void add(IMortarRecipe recipe) {
        ModTweaker.LATE_ADDITIONS.add(new Add(recipe));
    }

    private static MortarRecipeManager manager() {
        return MortarRecipeManager.getMortarManagerInstance();
    }

    private static final class Add extends BaseAction {

        private final IMortarRecipe recipe;

        private Add(IMortarRecipe recipe) {
            super(NAME);
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            manager().addRecipe(recipe);
        }

        @Override
        protected String getRecipeInfo() {
            return recipe.getRecipeOutput().toString();
        }
    }

    private static final class Remove extends BaseAction {

        private final ItemStack output;

        private Remove(ItemStack output) {
            super(NAME);
            this.output = output;
        }

        @Override
        public void apply() {
            manager().getRecipeList().removeIf(recipe ->
                    ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output));
        }

        @Override
        protected String getRecipeInfo() {
            return output.toString();
        }
    }
}
