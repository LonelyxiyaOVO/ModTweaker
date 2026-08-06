package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ShapedMortarRecipeCrT extends ShapedMortarRecipe {

    protected ShapedMortarRecipeCrT(ItemStack output, int duration, Object[] input) {
        super(output, duration, input);
    }

    public static ShapedMortarRecipeCrT create(IItemStack output, int duration, IIngredient[] input) {
        CraftTweakerRecipeHelper.validateMortar(output, duration, input);
        return new ShapedMortarRecipeCrT(CraftTweakerMC.getItemStack(output), duration, input.clone());
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
        for (int slot = 0; slot < input.length; slot++) {
            IIngredient ingredient = (IIngredient) input[slot];
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && ingredient.hasNewTransformers() && isSame(stack, ingredient)) {
                remaining.set(slot, CraftTweakerRecipeHelper.transformed(ingredient, stack));
            }
        }
        return remaining;
    }
}
