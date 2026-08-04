package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ZenClass("mods.thermalexpansion.Smelter")
@ModOnly("thermalexpansion")
public class Smelter {

    @ZenMethod
    public static void addRecipe(IIngredient primaryInput, IIngredient secondaryInput,
                                 IItemStack primaryOutput, @Optional IItemStack secondaryOutput,
                                 int chance, int energy) {
        for (IItemStack primary : primaryInput.getItems()) {
            for (IItemStack secondary : secondaryInput.getItems()) {
                ModTweaker.LATE_ADDITIONS.add(new Add(
                        InputHelper.toStack(primary), InputHelper.toStack(secondary),
                        InputHelper.toStack(primaryOutput),
                        secondaryOutput == null ? null : InputHelper.toStack(secondaryOutput),
                        chance, energy));
            }
        }
    }

    @ZenMethod
    public static void addFlux(IItemStack input) {
        ModTweaker.LATE_ADDITIONS.add(new Flux(InputHelper.toStack(input), true));
    }

    @ZenMethod
    public static void removeFlux(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Flux(InputHelper.toStack(input), false));
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input, true));
    }

    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, false));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Smelter", () -> {
            for (SmelterManager.SmelterRecipe recipe : recipeList()) {
                SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
            }
        });
    }

    private static List<SmelterManager.SmelterRecipe> recipeList() {
        return new ArrayList<>(Arrays.asList(SmelterManager.getRecipeList()));
    }

    @SuppressWarnings("unchecked")
    private static Set<Object> lockSet() {
        try {
            Field field = SmelterManager.class.getDeclaredField("lockSet");
            field.setAccessible(true);
            return (Set<Object>) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Smelter flux registry", e);
        }
    }

    private static class Add extends BaseAction {
        private final ItemStack primaryInput;
        private final ItemStack secondaryInput;
        private final ItemStack primaryOutput;
        private final ItemStack secondaryOutput;
        private final int chance;
        private final int energy;

        Add(ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput,
            ItemStack secondaryOutput, int chance, int energy) {
            super("Smelter");
            this.primaryInput = primaryInput;
            this.secondaryInput = secondaryInput;
            this.primaryOutput = primaryOutput;
            this.secondaryOutput = secondaryOutput;
            this.chance = chance;
            this.energy = energy;
        }

        @Override
        public void apply() {
            SmelterManager.addRecipe(energy, primaryInput, secondaryInput,
                    primaryOutput, secondaryOutput, chance);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(primaryOutput);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target;
        private final boolean input;

        Remove(IIngredient target, boolean input) {
            super("Smelter");
            this.target = target;
            this.input = input;
        }

        @Override
        public void apply() {
            for (SmelterManager.SmelterRecipe recipe : recipeList()) {
                boolean matches = input
                        ? target.matches(InputHelper.toIItemStack(recipe.getPrimaryInput()))
                                || target.matches(InputHelper.toIItemStack(recipe.getSecondaryInput()))
                        : target.matches(InputHelper.toIItemStack(recipe.getPrimaryOutput()))
                                || recipe.getSecondaryOutput() != null
                                && target.matches(InputHelper.toIItemStack(recipe.getSecondaryOutput()));
                if (matches) {
                    SmelterManager.removeRecipe(recipe.getPrimaryInput(), recipe.getSecondaryInput());
                }
            }
        }

        @Override
        protected String getRecipeInfo() {
            return target.toString();
        }
    }

    private static class Flux extends BaseAction {
        private final ItemStack input;
        private final boolean add;

        Flux(ItemStack input, boolean add) {
            super("SmelterFlux");
            this.input = input;
            this.add = add;
        }

        @Override
        public void apply() {
            Object key = SmelterManager.convertInput(input);
            if (add) {
                lockSet().add(key);
            } else {
                lockSet().remove(key);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}
