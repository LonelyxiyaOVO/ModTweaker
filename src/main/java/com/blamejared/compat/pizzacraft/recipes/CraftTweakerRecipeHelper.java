package com.blamejared.compat.pizzacraft.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

/** Small shared adapter helper for CraftTweaker ingredient transformers. */
final class CraftTweakerRecipeHelper {

    private CraftTweakerRecipeHelper() {
    }

    static boolean matches(IIngredient ingredient, ItemStack stack) {
        return CraftTweakerMC.getIngredient(ingredient).apply(stack);
    }

    static ItemStack transformed(IIngredient ingredient, ItemStack stack) {
        return CraftTweakerMC.getItemStack(
                ingredient.applyNewTransform(CraftTweakerMC.getIItemStack(stack)));
    }

    static void validateMortar(IItemStack output, int duration, IIngredient[] input) {
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Mortar output cannot be empty");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Mortar duration must be greater than zero");
        }
        if (input.length == 0 || input.length > 4) {
            throw new IllegalArgumentException("Mortar recipes must have 1 to 4 inputs");
        }
    }
}
