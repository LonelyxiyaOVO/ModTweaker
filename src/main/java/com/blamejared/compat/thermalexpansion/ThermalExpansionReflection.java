package com.blamejared.compat.thermalexpansion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import com.blamejared.mtlib.helpers.InputHelper;

/** Small reflection bridge for Thermal Expansion's private static registries. */
public final class ThermalExpansionReflection {

    private ThermalExpansionReflection() {
    }

    public static Object get(Class<?> owner, String name) {
        try {
            Field field = owner.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access " + owner.getName() + "." + name, e);
        }
    }

    public static void clear(Class<?> owner, String name) {
        Object value = get(owner, name);
        if (!(value instanceof java.util.Map) && !(value instanceof java.util.Collection)) {
            throw new IllegalStateException(owner.getName() + "." + name + " is not a clearable registry");
        }
        if (value instanceof java.util.Map) {
            ((java.util.Map<?, ?>) value).clear();
        } else {
            ((java.util.Collection<?>) value).clear();
        }
    }

    public static IAction removeBy(String description, Class<?> owner, String listMethod,
            IIngredient target, boolean output, String... valueMethods) {
        return new IAction() {
            @Override
            public void apply() {
                removeBy(owner, listMethod, target, output, valueMethods);
            }

            @Override
            public String describe() {
                return "Removing " + description + " recipes by " + (output ? "output" : "input");
            }
        };
    }

    public static void removeBy(Class<?> owner, String listMethod, IIngredient target,
            boolean output, String... valueMethods) {
        Object recipes = invokeStatic(owner, listMethod);
        if (!(recipes instanceof Collection)) return;
        for (Object recipe : new ArrayList<>((Collection<?>) recipes)) {
            boolean matches = false;
            for (String valueMethod : valueMethods) {
                Object value = invoke(recipe, valueMethod);
                if (value instanceof ItemStack) matches |= target.matches(InputHelper.toIItemStack((ItemStack) value));
                if (value instanceof FluidStack) matches |= target.matches(InputHelper.toILiquidStack((FluidStack) value));
            }
            if (matches) {
                Object input = invoke(recipe, "getInput");
                if (input == null) input = invoke(recipe, "getPrimaryInput");
                invokeStatic(owner, "removeRecipe", input);
            }
        }
    }

    private static Object invokeStatic(Class<?> owner, String name, Object... args) {
        for (Method method : owner.getDeclaredMethods()) {
            if (!method.getName().equals(name) || method.getParameterTypes().length != args.length) continue;
            try {
                method.setAccessible(true);
                return method.invoke(null, args);
            } catch (ReflectiveOperationException ignored) {
            }
        }
        throw new IllegalStateException("Unable to invoke " + owner.getName() + "." + name);
    }

    private static Object invoke(Object owner, String name) {
        try {
            Method method = owner.getClass().getMethod(name);
            method.setAccessible(true);
            return method.invoke(owner);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
