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
import mods.railcraft.api.crafting.IBlastFurnaceCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.BlastFurnace")
@ModOnly("railcraft")
@ZenRegister
public final class BlastFurnace {

    private static final String NAME = "Railcraft Blast Furnace";

    private BlastFurnace() {
    }

    @ZenMethod
    public static void addRecipe(String name, IItemStack output, IIngredient input,
                                 @Optional(valueLong = IBlastFurnaceCrafter.SMELT_TIME) int time,
                                 @Optional int slag) {
        ModTweaker.LATE_ADDITIONS.add(new Add(
                name, InputHelper.toStack(output), CraftTweakerMC.getIngredient(input), time, slag));
    }

    @ZenMethod
    public static void removeRecipe(String name) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByName(name));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output, @Optional IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByOutput(
                InputHelper.toStack(output), CraftTweakerMC.getIngredient(input)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, () -> Crafters.blastFurnace().getRecipes().clear());
    }

    private static final class Add extends BaseAction {

        private final String name;
        private final ItemStack output;
        private final Ingredient input;
        private final int time;
        private final int slag;

        private Add(String name, ItemStack output, Ingredient input, int time, int slag) {
            super(NAME);
            this.name = name;
            this.output = output;
            this.input = input;
            this.time = time;
            this.slag = slag;
        }

        @Override
        public void apply() {
            Crafters.blastFurnace().newRecipe(input)
                    .name(name)
                    .time(time)
                    .output(output)
                    .slagOutput(slag)
                    .register();
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
            Crafters.blastFurnace().getRecipes().removeIf(recipe ->
                    name.equals(recipe.getName().toString()));
        }

        @Override
        protected String getRecipeInfo() {
            return name;
        }
    }

    private static final class RemoveByOutput extends BaseAction {

        private final ItemStack output;
        private final Ingredient input;

        private RemoveByOutput(ItemStack output, Ingredient input) {
            super(NAME);
            this.output = output;
            this.input = input;
        }

        @Override
        public void apply() {
            Crafters.blastFurnace().getRecipes().removeIf(recipe ->
                    recipe.getOutput().isItemEqual(output)
                            && (input == Ingredient.EMPTY || recipe.getInput().equals(input)));
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
}
