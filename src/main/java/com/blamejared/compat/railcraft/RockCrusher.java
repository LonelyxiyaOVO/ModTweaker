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
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.api.crafting.IRockCrusherCrafter;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.railcraft.RockCrusher")
@ModOnly("railcraft")
@ZenRegister
public final class RockCrusher {

    private static final String NAME = "Railcraft Rock Crusher";

    private RockCrusher() {
    }

    @ZenMethod
    public static void addRecipe(String name, WeightedItemStack[] outputs, IIngredient input) {
        ModTweaker.LATE_ADDITIONS.add(new Add(
                name, outputs, CraftTweakerMC.getIngredient(input)));
    }

    @ZenMethod
    public static void removeRecipe(String name) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByName(name));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        removeRecipeByInput(input);
    }

    @ZenMethod
    public static void removeRecipeByInput(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByInput(InputHelper.toStack(input)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, () -> Crafters.rockCrusher().getRecipes().clear());
    }

    private static final class Add extends BaseAction {

        private final String name;
        private final WeightedItemStack[] outputs;
        private final net.minecraft.item.crafting.Ingredient input;

        private Add(String name, WeightedItemStack[] outputs,
                    net.minecraft.item.crafting.Ingredient input) {
            super(NAME);
            this.name = name;
            this.outputs = outputs.clone();
            this.input = input;
        }

        @Override
        public void apply() {
            IRockCrusherCrafter.IRockCrusherRecipeBuilder recipe = Crafters.rockCrusher()
                    .makeRecipe(input)
                    .name(name);
            for (int i = 0; i < Math.min(outputs.length, 9); i++) {
                WeightedItemStack output = outputs[i];
                if (output != null) {
                    recipe = recipe.addOutput(
                            CraftTweakerMC.getItemStack(output.getStack()), output.getChance());
                }
            }
            recipe.register();
        }

        @Override
        protected String getRecipeInfo() {
            return name;
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
            Crafters.rockCrusher().getRecipes().removeIf(recipe ->
                    name.equals(recipe.getName().toString()));
        }

        @Override
        protected String getRecipeInfo() {
            return String.format(Locale.ENGLISH, "recipe '%s'", name);
        }
    }

    private static final class RemoveByInput extends BaseAction {

        private final ItemStack input;

        private RemoveByInput(ItemStack input) {
            super(NAME);
            this.input = input;
        }

        @Override
        public void apply() {
            Crafters.rockCrusher().getRecipes().removeIf(recipe -> recipe.getInput().apply(input));
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
