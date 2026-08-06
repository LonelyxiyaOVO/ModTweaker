package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ShapedBakewareRecipe extends BakewareRecipe {

    protected final int height;
    protected final int width;

    protected ShapedBakewareRecipe(@Nonnull ItemStack output, int height, int width, Object[] input) {
        super(output, input);
        this.height = height;
        this.width = width;
    }

    @Override
    public boolean matches(InventoryCraftingImproved inventory, World world) {
        int start = -1;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (isSame(stack, input[0])) {
                start = slot;
                break;
            }
            if (!stack.isEmpty()) {
                return false;
            }
        }
        if (start == -1 || start % 3 + width > 3 || start / 3 + height > 3) {
            return false;
        }

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                int recipeSlot = row * width + column;
                int inventorySlot = start + recipeSlot;
                if (inventorySlot >= inventory.getSizeInventory()
                        || !isSame(inventory.getStackInSlot(inventorySlot), input[recipeSlot])) {
                    return false;
                }
            }
        }

        for (int slot = start + input.length; slot < inventory.getSizeInventory(); slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCraftingImproved inventory) {
        return output.copy();
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    public int getRecipeHeight() {
        return height;
    }

    public int getRecipeWidth() {
        return width;
    }
}
