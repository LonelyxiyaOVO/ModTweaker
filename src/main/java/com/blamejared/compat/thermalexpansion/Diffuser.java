package com.blamejared.compat.thermalexpansion;

import cofh.core.inventory.ComparableItemStack;
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
import java.util.List;

@ZenClass("mods.thermalexpansion.Diffuser")
@ModOnly("thermalexpansion")
public class Diffuser {

    @ZenMethod
    public static void addRecipe(IItemStack input, int amplifier, int duration) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(input), amplifier, duration));
    }

    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Diffuser", () -> {
            invoke(amplifierMap(), "clear");
            invoke(durationMap(), "clear");
        });
    }

    private static Object map(String name) {
        try {
            Field field = Class.forName("cofh.thermalexpansion.util.managers.device.DiffuserManager")
                    .getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Diffuser " + name, e);
        }
    }

    private static Object amplifierMap() {
        return map("reagentAmpMap");
    }

    private static Object durationMap() {
        return map("reagentDurMap");
    }

    private static Object invoke(Object target, String name, Object... args) {
        try {
            for (Method method : target.getClass().getMethods()) {
                if (method.getName().equals(name) && method.getParameterTypes().length == args.length) {
                    return method.invoke(target, args);
                }
            }
            throw new NoSuchMethodException(name);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to call Diffuser registry method " + name, e);
        }
    }

    private static ComparableItemStack key(ItemStack stack) {
        return new ComparableItemStack(stack);
    }

    private static class Add extends BaseAction {
        private final ItemStack input;
        private final int amplifier;
        private final int duration;

        Add(ItemStack input, int amplifier, int duration) {
            super("Diffuser");
            this.input = input;
            this.amplifier = amplifier;
            this.duration = duration;
        }

        @Override
        public void apply() {
            Object key = key(input);
            invoke(amplifierMap(), "put", key, amplifier);
            invoke(durationMap(), "put", key, duration);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient input;

        Remove(IIngredient input) {
            super("Diffuser");
            this.input = input;
        }

        @Override
        public void apply() {
            Collection<?> keys = (Collection<?>) invoke(amplifierMap(), "keySet");
            List<Object> matchingKeys = new ArrayList<>();
            for (Object key : keys) {
                ItemStack stack = (ItemStack) invoke(key, "toItemStack");
                if (input.matches(InputHelper.toIItemStack(stack))) {
                    matchingKeys.add(key);
                }
            }
            for (Object key : matchingKeys) {
                invoke(amplifierMap(), "remove", key);
                invoke(durationMap(), "remove", key);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return input.toString();
        }
    }
}
