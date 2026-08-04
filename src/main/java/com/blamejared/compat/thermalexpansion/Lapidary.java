package com.blamejared.compat.thermalexpansion;

import cofh.core.inventory.ComparableItemStack;
import cofh.thermalexpansion.util.managers.dynamo.NumismaticManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

@ZenClass("mods.thermalexpansion.Lapidary")
@ModOnly("thermalexpansion")
public class Lapidary {

    @ZenMethod
    public static void addFuel(IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(input), energy));
    }

    @ZenMethod
    public static void removeFuel(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Lapidary", () -> invoke(gemFuelMap(), "clear"));
    }

    private static Object gemFuelMap() {
        try {
            Field field = NumismaticManager.class.getDeclaredField("gemFuelMap");
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Numismatic gem fuel registry", e);
        }
    }

    private static Object invoke(Object target, String name, Object... args) {
        try {
            for (Method method : target.getClass().getMethods()) {
                if (method.getName().equals(name)
                        && method.getParameterTypes().length == args.length) {
                    return method.invoke(target, args);
                }
            }
            throw new NoSuchMethodException(name);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to call Lapidary registry method " + name, e);
        }
    }

    private static class Add extends BaseAction {
        private final ItemStack input;
        private final int energy;

        Add(ItemStack input, int energy) {
            super("Lapidary");
            this.input = input;
            this.energy = energy;
        }

        @Override
        public void apply() {
            NumismaticManager.addGemFuel(input, energy);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient input;

        Remove(IIngredient input) {
            super("Lapidary");
            this.input = input;
        }

        @Override
        public void apply() {
            Collection<?> keys = (Collection<?>) invoke(gemFuelMap(), "keySet");
            for (Object key : new ArrayList<>(keys)) {
                ItemStack stack = (ItemStack) invoke(key, "toItemStack");
                if (input.matches(InputHelper.toIItemStack(stack))) {
                    invoke(gemFuelMap(), "remove", key);
                }
            }
        }

        @Override
        protected String getRecipeInfo() {
            return input.toString();
        }
    }
}
