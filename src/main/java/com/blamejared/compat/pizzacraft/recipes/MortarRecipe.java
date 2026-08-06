package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.crafting.mortar.IMortarRecipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

/** Base implementation used by the CraftTweaker Mortar recipes. */
public abstract class MortarRecipe implements IMortarRecipe {

    protected final ItemStack output;
    protected final Object[] input;
    protected final int duration;

    protected MortarRecipe(ItemStack output, int duration, Object[] input) {
        this.output = output;
        this.duration = duration;
        this.input = input;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ItemStack getResult() {
        return output.copy();
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    public Object[] getInput() {
        return input;
    }

    protected boolean isSame(ItemStack stack, Object input) {
        if (stack.isEmpty()) {
            return false;
        }
        if (input instanceof Item) {
            return stack.getItem().equals(input);
        }
        if (input instanceof Block) {
            Item item = stack.getItem();
            Block block = Blocks.AIR;
            if (item instanceof ItemBlock) {
                block = ((ItemBlock) item).getBlock();
            } else if (item instanceof ItemBlockSpecial) {
                block = ((ItemBlockSpecial) item).getBlock();
            }
            return block != Blocks.AIR && block.equals(input);
        }
        if (input instanceof ItemStack) {
            return ItemStack.areItemStacksEqual(stack, (ItemStack) input);
        }
        if (input instanceof String) {
            int oreId = OreDictionary.getOreID((String) input);
            for (int stackId : OreDictionary.getOreIDs(stack)) {
                if (stackId == oreId) {
                    return true;
                }
            }
            return false;
        }
        if (input instanceof Ingredient) {
            return ((Ingredient) input).apply(stack);
        }
        return false;
    }
}
