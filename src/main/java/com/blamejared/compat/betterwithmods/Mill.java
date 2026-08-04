package com.blamejared.compat.betterwithmods;


import betterwithmods.common.BWRegistry;
import com.blamejared.compat.betterwithmods.base.bulkrecipes.MillBuilder;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.betterwithmods.Mill")
@ModOnly("betterwithmods")
@ZenRegister
public class Mill {

    public static MillBuilder INSTANCE = new MillBuilder(() -> BWRegistry.MILLSTONE, "Mill");

    @ZenMethod
    public static MillBuilder builder() {
        return new MillBuilder(() -> BWRegistry.MILLSTONE, "Mill");
    }

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack[] outputs) {
        INSTANCE.buildRecipe(inputs, outputs).build();
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, IItemStack secondaryOutput, @NotNull IIngredient[] inputs) {
        addRecipe(inputs, new IItemStack[]{output, secondaryOutput});
    }

    @Deprecated
    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] inputs) {
        addRecipe(inputs, new IItemStack[]{output});
    }

    @ZenMethod
    public static void remove(IItemStack[] output) {
        INSTANCE.removeRecipe(output);
    }

    @ZenMethod
    public static void removeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Better With Mods Mill") {
            @Override
            public void apply() {
                List<betterwithmods.common.registry.bulk.recipes.MillRecipe> remove = new ArrayList<>();
                for (betterwithmods.common.registry.bulk.recipes.MillRecipe recipe : BWRegistry.MILLSTONE.getRecipes()) {
                    for (Ingredient recipeInput : recipe.getInputs()) {
                        for (IItemStack stack : input.getItems()) {
                            if (recipeInput.apply(com.blamejared.mtlib.helpers.InputHelper.toStack(stack))) {
                                remove.add(recipe);
                                break;
                            }
                        }
                        if (remove.contains(recipe)) break;
                    }
                }
                remove.forEach(BWRegistry.MILLSTONE::remove);
            }

            @Override
            protected String getRecipeInfo() {
                return input.toString();
            }
        });
    }

    @ZenMethod
    public static void removeAll() {
        builder().removeAll();
    }

}
