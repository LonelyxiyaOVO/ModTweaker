package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ShapelessBakewareRecipeCrT extends ShapelessBakewareRecipe {

    protected ShapelessBakewareRecipeCrT(ItemStack output, Object[] input) {
        super(output, input);
    }

    public static ShapelessBakewareRecipeCrT create(IItemStack output, IIngredient[] input) {
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Bakeware output cannot be empty");
        }
        if (input.length == 0 || input.length > 9) {
            throw new IllegalArgumentException("Bakeware shapeless recipes must have 1 to 9 inputs");
        }
        return new ShapelessBakewareRecipeCrT(CraftTweakerMC.getItemStack(output), input.clone());
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        if (input instanceof IIngredient) {
            return CraftTweakerRecipeHelper.matches((IIngredient) input, stack);
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCraftingImproved inventory) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(inventory);
        boolean[] matched = new boolean[input.length];
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            for (int ingredientIndex = 0; ingredientIndex < input.length; ingredientIndex++) {
                IIngredient ingredient = (IIngredient) input[ingredientIndex];
                if (!matched[ingredientIndex] && ingredient.hasNewTransformers() && isSame(stack, ingredient)) {
                    matched[ingredientIndex] = true;
                    remaining.set(slot, CraftTweakerRecipeHelper.transformed(ingredient, stack));
                    break;
                }
            }
        }
        return remaining;
    }
}
