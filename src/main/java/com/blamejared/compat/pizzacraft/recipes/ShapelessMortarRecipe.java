package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ShapelessMortarRecipe extends MortarRecipe {

    protected ShapelessMortarRecipe(ItemStack output, int duration, Object[] input) {
        super(output, duration, input);
    }

    @Override
    public boolean matches(TileEntityMortarAndPestle tile, World world) {
        ItemStackHandler inventory = tile.getInventory();
        boolean[] matched = new boolean[input.length];
        int itemCount = 0;
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
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
}
