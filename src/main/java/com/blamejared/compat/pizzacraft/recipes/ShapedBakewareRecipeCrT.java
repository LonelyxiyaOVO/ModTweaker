package com.blamejared.compat.pizzacraft.recipes;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ShapedBakewareRecipeCrT extends ShapedBakewareRecipe {

    protected ShapedBakewareRecipeCrT(ItemStack output, int height, int width, Object[] input) {
        super(output, height, width, input);
    }

    public static ShapedBakewareRecipeCrT create(IItemStack output, int height, int width, Object[] input) {
        validateShape(output, height, width, input);
        return new ShapedBakewareRecipeCrT(CraftTweakerMC.getItemStack(output), height, width, input);
    }

    public static ShapedBakewareRecipeCrT create(IItemStack output, String[] recipeMap, Object... keyInputs) {
        if (recipeMap == null || recipeMap.length == 0 || recipeMap.length > 3) {
            throw new IllegalArgumentException("Bakeware recipe must have between 1 and 3 rows");
        }
        if (keyInputs.length == 0 || (keyInputs.length & 1) != 0) {
            throw new IllegalArgumentException("Bakeware shaped inputs must be character/ingredient pairs");
        }

        java.util.Map<Character, Object> inputMap = new java.util.HashMap<>();
        for (int i = 0; i < keyInputs.length; i += 2) {
            char key = toCharacter(keyInputs[i]);
            if (key == ' ') {
                throw new IllegalArgumentException("The space character is reserved for an empty slot");
            }
            inputMap.put(key, keyInputs[i + 1]);
        }

        int height = recipeMap.length;
        int width = recipeMap[0].length();
        Object[] input = new Object[height * width];
        for (int row = 0; row < height; row++) {
            if (recipeMap[row].length() != width) {
                throw new IllegalArgumentException("Bakeware recipe rows must have the same width");
            }
            for (int column = 0; column < width; column++) {
                char key = recipeMap[row].charAt(column);
                input[row * width + column] = key == ' ' ? null : inputMap.get(key);
                if (key != ' ' && input[row * width + column] == null) {
                    throw new IllegalArgumentException("Bakeware recipe key '" + key + "' has no ingredient");
                }
            }
        }
        return create(output, height, width, input);
    }

    private static void validateShape(IItemStack output, int height, int width, Object[] input) {
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Bakeware output cannot be empty");
        }
        if (height < 1 || height > 3 || width < 1 || width > 3 || input.length != height * width) {
            throw new IllegalArgumentException("Bakeware shaped recipes must be a 1x1 to 3x3 matrix");
        }
        for (Object ingredient : input) {
            if (ingredient != null && !(ingredient instanceof IIngredient)) {
                throw new IllegalArgumentException("Bakeware inputs must be CraftTweaker ingredients");
            }
        }
    }

    private static char toCharacter(Object key) {
        if (key instanceof Character) {
            return (Character) key;
        }
        if (key instanceof String && ((String) key).length() == 1) {
            return ((String) key).charAt(0);
        }
        throw new IllegalArgumentException("Bakeware keys must be single characters");
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        if (input == null) {
            return stack.isEmpty();
        }
        if (input instanceof IIngredient) {
            return CraftTweakerRecipeHelper.matches((IIngredient) input, stack);
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCraftingImproved inventory) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(inventory);
        int start = -1;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            if (isSame(inventory.getStackInSlot(slot), input[0])) {
                start = slot;
                break;
            }
        }
        if (start < 0) {
            return remaining;
        }
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                int recipeSlot = row * width + column;
                int inventorySlot = start + recipeSlot;
                if (inventorySlot >= inventory.getSizeInventory()) {
                    continue;
                }
                Object ingredientObject = input[recipeSlot];
                ItemStack stack = inventory.getStackInSlot(inventorySlot);
                if (!stack.isEmpty() && ingredientObject instanceof IIngredient) {
                    IIngredient ingredient = (IIngredient) ingredientObject;
                    if (ingredient.hasNewTransformers() && isSame(stack, ingredient)) {
                        remaining.set(inventorySlot, CraftTweakerRecipeHelper.transformed(ingredient, stack));
                    }
                }
            }
        }
        return remaining;
    }
}
