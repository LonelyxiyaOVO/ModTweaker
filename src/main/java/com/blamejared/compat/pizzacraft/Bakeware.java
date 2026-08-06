package com.blamejared.compat.pizzacraft;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.compat.pizzacraft.recipes.ShapedBakewareRecipeCrT;
import com.blamejared.compat.pizzacraft.recipes.ShapelessBakewareRecipeCrT;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.PizzaCraftingManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

/** CraftTweaker integration for PizzaCraft's Bakeware. */
@ZenClass("mods.pizzacraft.Bakeware")
@ModOnly("pizzacraft")
@ZenRegister
public final class Bakeware {

    private static final String NAME = "PizzaCraft Bakeware";

    private Bakeware() {
    }

    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] inputs) {
        if (inputs == null || inputs.length == 0) {
            throw new IllegalArgumentException("Bakeware shaped input matrix cannot be empty");
        }
        int height = inputs.length;
        int width = inputs[0] == null ? 0 : inputs[0].length;
        if (width == 0) {
            throw new IllegalArgumentException("Bakeware shaped input rows cannot be empty");
        }
        Object[] flattened = new Object[height * width];
        for (int row = 0; row < height; row++) {
            if (inputs[row] == null || inputs[row].length != width) {
                throw new IllegalArgumentException("Bakeware shaped input rows must have the same width");
            }
            System.arraycopy(inputs[row], 0, flattened, row * width, width);
        }
        add(ShapedBakewareRecipeCrT.create(output, height, width, flattened));
    }

    @ZenMethod
    public static void addShaped(IItemStack output, Object... args) {
        int rowCount = 0;
        while (rowCount < 3 && rowCount < args.length
                && args[rowCount] instanceof String
                && ((String) args[rowCount]).length() > 1) {
            rowCount++;
        }
        if (rowCount == 0) {
            throw new IllegalArgumentException("Bakeware shaped recipes need at least one pattern row");
        }
        Object[] keyInputs = Arrays.copyOfRange(args, rowCount, args.length);
        add(ShapedBakewareRecipeCrT.create(
                output, Arrays.copyOf(args, rowCount, String[].class), keyInputs));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] inputs) {
        add(ShapelessBakewareRecipeCrT.create(output, inputs));
    }

    /** Kept as an alias for the original CRT API. */
    @Deprecated
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] inputs) {
        addShapeless(output, inputs);
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(output)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, () -> manager().getRecipeList().clear());
    }

    private static void add(IBakewareRecipe recipe) {
        ModTweaker.LATE_ADDITIONS.add(new Add(recipe));
    }

    private static PizzaCraftingManager manager() {
        return PizzaCraftingManager.getPizzaCraftingInstance();
    }

    private static final class Add extends BaseAction {

        private final IBakewareRecipe recipe;

        private Add(IBakewareRecipe recipe) {
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
