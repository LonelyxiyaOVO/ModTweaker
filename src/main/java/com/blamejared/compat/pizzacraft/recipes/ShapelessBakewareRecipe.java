package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ShapelessBakewareRecipe extends BakewareRecipe {

    protected ShapelessBakewareRecipe(@Nonnull ItemStack output, Object[] input) {
        super(output, input);
    }

    @Override
    public boolean matches(InventoryCraftingImproved inventory, World world) {
        boolean[] matched = new boolean[input.length];
        int itemCount = 0;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            boolean found = false;
            for (int ingredient = 0; ingredient < input.length; ingredient++) {
                if (!matched[ingredient] && isSame(stack, input[ingredient])) {
                    matched[ingredient] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
            itemCount++;
        }
        if (itemCount != input.length) {
            return false;
        }
        for (boolean ingredientMatched : matched) {
            if (!ingredientMatched) {
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
}
