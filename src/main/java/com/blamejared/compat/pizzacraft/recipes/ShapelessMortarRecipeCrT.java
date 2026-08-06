package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ShapelessMortarRecipeCrT extends ShapelessMortarRecipe {

    protected ShapelessMortarRecipeCrT(ItemStack output, int duration, Object[] input) {
        super(output, duration, input);
    }

    public static ShapelessMortarRecipeCrT create(IItemStack output, int duration, IIngredient[] input) {
        CraftTweakerRecipeHelper.validateMortar(output, duration, input);
        return new ShapelessMortarRecipeCrT(CraftTweakerMC.getItemStack(output), duration, input.clone());
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        if (input instanceof IIngredient) {
            return CraftTweakerRecipeHelper.matches((IIngredient) input, stack);
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(TileEntityMortarAndPestle tile) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(tile);
        ItemStackHandler inventory = tile.getInventory();
        boolean[] matched = new boolean[input.length];
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
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
