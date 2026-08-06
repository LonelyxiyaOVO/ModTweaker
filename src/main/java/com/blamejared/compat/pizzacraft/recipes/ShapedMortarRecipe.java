package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ShapedMortarRecipe extends MortarRecipe {

    protected ShapedMortarRecipe(ItemStack output, int duration, Object[] input) {
        super(output, duration, input);
    }

    @Override
    public boolean matches(TileEntityMortarAndPestle tile, World world) {
        ItemStackHandler inventory = tile.getInventory();
        for (int slot = 0; slot < input.length; slot++) {
            if (!isSame(inventory.getStackInSlot(slot), input[slot])) {
                return false;
            }
        }
        return true;
    }
}
