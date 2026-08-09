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
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.api.crafting.ICokeOvenCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.CokeOven")
@ModOnly("railcraft")
@ZenRegister
public final class CokeOven {

    private static final String NAME = "Railcraft Coke Oven";

    private CokeOven() {
    }

    @ZenMethod
    public static void addRecipe(String name, IItemStack output, IIngredient input,
                                 @Optional(valueLong = ICokeOvenCrafter.DEFAULT_COOK_TIME) int time,
                                 @Optional ILiquidStack outputFluid) {
        ModTweaker.LATE_ADDITIONS.add(new Add(
                name, InputHelper.toStack(output), CraftTweakerMC.getIngredient(input), time,
                CraftTweakerMC.getLiquidStack(outputFluid)));
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
        RecipeActions.removeAll(NAME, () -> Crafters.cokeOven().getRecipes().clear());
    }

    private static final class Add extends BaseAction {

        private final String name;
        private final ItemStack output;
        private final Ingredient input;
        private final int time;
        private final FluidStack outputFluid;

        private Add(String name, ItemStack output, Ingredient input, int time, FluidStack outputFluid) {
            super(NAME);
            this.name = name;
            this.output = output;
            this.input = input;
            this.time = time;
            this.outputFluid = outputFluid;
        }

        @Override
        public void apply() {
            Crafters.cokeOven().newRecipe(input)
                    .name(name)
                    .output(output)
                    .time(time)
                    .fluid(outputFluid)
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
            Crafters.cokeOven().getRecipes().removeIf(recipe ->
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
            Crafters.cokeOven().getRecipes().removeIf(recipe ->
                    recipe.getOutput().isItemEqual(output)
                            && (input == Ingredient.EMPTY || recipe.getInput().equals(input)));
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
}
