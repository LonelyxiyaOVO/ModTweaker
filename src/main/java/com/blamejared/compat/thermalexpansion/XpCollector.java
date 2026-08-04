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
import java.util.Map;

@ZenClass("mods.thermalexpansion.XpCollector")
@ModOnly("thermalexpansion")
public class XpCollector {
    @ZenMethod
    public static void add(IItemStack catalyst, int xp, int factor) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(catalyst), xp, factor));
    }

    @ZenMethod
    public static void addRecipe(IIngredient catalyst, int xp, int factor) {
        for (IItemStack stack : catalyst.getItems()) add(stack, xp, factor);
    }

    @ZenMethod
    public static void remove(IIngredient catalyst) {
        ModTweaker.LATE_REMOVALS.add(new Remove(catalyst));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("XpCollector", () -> {
            invoke(catalystMap(), "clear");
            invoke(catalystFactorMap(), "clear");
        });
    }

    private static Object catalystMap() { return staticField("catalystMap"); }
    private static Object catalystFactorMap() { return staticField("catalystFactorMap"); }

    private static Object staticField(String name) {
        try {
            Class<?> manager = Class.forName("cofh.thermalexpansion.util.managers.device.XpCollectorManager");
            Field field = manager.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion XpCollector " + name, e);
        }
    }

    private static Object invoke(Object target, String name, Object... args) {
        try {
            for (Method method : target.getClass().getMethods()) {
                if (method.getName().equals(name) && method.getParameterTypes().length == args.length) {
                    method.setAccessible(true);
                    return method.invoke(target, args);
                }
            }
            throw new NoSuchMethodException(name);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to call XpCollector registry method " + name, e);
        }
    }

    private static ComparableItemStack key(ItemStack stack) { return new ComparableItemStack(stack); }

    private static class Add extends BaseAction {
        private final ItemStack catalyst;
        private final int xp, factor;
        Add(ItemStack catalyst, int xp, int factor) {
            super("XpCollector"); this.catalyst = catalyst; this.xp = xp; this.factor = factor;
        }
        @Override public void apply() {
            Object key = key(catalyst);
            invoke(catalystMap(), "put", key, xp);
            invoke(catalystFactorMap(), "put", key, factor);
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(catalyst); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient catalyst;
        Remove(IIngredient catalyst) { super("XpCollector"); this.catalyst = catalyst; }
        @Override public void apply() {
            for (IItemStack stack : catalyst.getItems()) {
                Object key = key(InputHelper.toStack(stack));
                invoke(catalystMap(), "remove", key);
                invoke(catalystFactorMap(), "remove", key);
            }
        }
        @Override protected String getRecipeInfo() { return catalyst.toString(); }
    }
}
