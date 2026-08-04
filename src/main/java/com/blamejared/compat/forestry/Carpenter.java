package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAddForestry;
import com.blamejared.mtlib.utils.BaseRemoveForestry;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.RecipeManagers;
import forestry.core.recipes.ShapedRecipeCustom;
import forestry.factory.recipes.CarpenterRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.forestry.Carpenter")
@ModOnly("forestry")
@ZenRegister
public class Carpenter {
    @ZenMethod
    public static void removeAll() {
        RegistryHelper.removeAll(name, RecipeManagers.carpenterManager.recipes());
    }
    
    public static final String name = "Forestry Carpenter";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds shaped recipe to Carpenter
     *
     * @param output        recipe product
     * @param ingredients   required ingredients
     * @param packagingTime amount of ticks per crafting operation
     *                      * @param fluidInput      required mB of fluid (optional)
     *                      * @param box             required box in top slot (optional)
     */
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[][] ingredients, int packagingTime, @Optional ILiquidStack fluidInput, @Optional IItemStack box) {
        ModTweaker.LATE_ADDITIONS.add(new Add(new CarpenterRecipe(packagingTime, toFluid(fluidInput), toStack(box), new ShapedRecipeCustom(toStack(output), toShapedObjects(ingredients)))));
    }
    
    @SuppressWarnings("unused")
    private static IItemStack[][] transform(IItemStack[] arr, int N) {
        int M = (arr.length + N - 1) / N;
        IItemStack[][] mat = new IItemStack[M][];
        int start = 0;
        for(int r = 0; r < M; r++) {
            int L = Math.min(N, arr.length - start);
            mat[r] = Arrays.copyOfRange(arr, start, start + L);
            start += L;
        }
        return mat;
    }
    
    
    private static class Add extends BaseAddForestry<ICarpenterRecipe> {
        
        protected Add(ICarpenterRecipe recipe) {
            super(Carpenter.name, RecipeManagers.carpenterManager, recipe);
        }
        
        @Override
        protected String getRecipeInfo() {
            return recipe.getCraftingGridRecipe().getOutput().getDisplayName();
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Removes recipe from Carpenter
     *
     * @param output = recipe result
     *               * @param fluidInput = required type of fluid (optional)
     */
    @ZenMethod
    public static void removeRecipe(IItemStack output, @Optional ILiquidStack fluidInput) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, fluidInput));
    }

    @ZenMethod
    public static void removeByFluidInput(ILiquidStack fluidInput) {
        RecipeRemoval.add(name, RecipeManagers.carpenterManager.recipes(), recipe ->
                recipe.getFluidResource() != null && matches(fluidInput, toILiquidStack(recipe.getFluidResource())), fluidInput.toString());
    }

    @ZenMethod
    public static void removeByBox(IIngredient box) {
        RecipeRemoval.add(name, RecipeManagers.carpenterManager.recipes(), recipe ->
                matches(box, toIItemStack(recipe.getBox())), box.toString());
    }

    @ZenMethod
    public static void removeByInput(IIngredient[] inputs) {
        RecipeRemoval.add(name, RecipeManagers.carpenterManager.recipes(), recipe -> {
            ItemStack[][] raw = recipe.getCraftingGridRecipe().getRawIngredients().stream()
                    .map(list -> list.toArray(new ItemStack[0])).toArray(ItemStack[][]::new);
            for (IIngredient input : inputs) {
                boolean found = false;
                for (ItemStack[] row : raw) for (ItemStack stack : row)
                    if (!stack.isEmpty() && matches(input, toIItemStack(stack))) found = true;
                if (!found) return false;
            }
            return true;
        }, java.util.Arrays.toString(inputs));
    }
    
    private static class Remove extends BaseRemoveForestry<ICarpenterRecipe> {
        
        private ItemStack output;
        private FluidStack fluidInput;
        
        public Remove(IItemStack output, ILiquidStack fluidInput) {
            super(Carpenter.name, RecipeManagers.carpenterManager);
            this.output = InputHelper.toStack(output);
            this.fluidInput = toFluid(fluidInput);
        }
        
        @Override
        public boolean checkIsRecipe(ICarpenterRecipe recipe) {
            if(recipe.getCraftingGridRecipe().getOutput().isItemEqual(output)) {
                if(fluidInput == null) {
                    return true;
                } else {
                    return fluidInput.isFluidEqual(recipe.getFluidResource());
                }
            }
            
            return false;
        }
        
        @Override
        protected String getRecipeInfo() {
            return output.getDisplayName();
        }
    }
}
