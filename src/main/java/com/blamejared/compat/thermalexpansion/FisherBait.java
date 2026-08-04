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
import java.util.List;

@ZenClass("mods.thermalexpansion.FisherBait")
@ModOnly("thermalexpansion")
public class FisherBait {
    @ZenMethod
    public static void addRecipe(IItemStack bait, int multiplier) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(bait), multiplier));
    }

    @ZenMethod
    public static void removeRecipe(IIngredient bait) {
        ModTweaker.LATE_REMOVALS.add(new Remove(bait));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("FisherBait", () -> invoke(baitMap(), "clear"));
    }

    private static Object baitMap() {
        try {
            Class<?> manager = Class.forName("cofh.thermalexpansion.util.managers.device.FisherManager");
            Field field = manager.getDeclaredField("baitMap");
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Fisher bait registry", e);
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
            throw new IllegalStateException("Unable to call Fisher bait registry method " + name, e);
        }
    }

    private static ComparableItemStack key(ItemStack stack) { return new ComparableItemStack(stack); }

    private static class Add extends BaseAction {
        private final ItemStack bait;
        private final int multiplier;
        Add(ItemStack bait, int multiplier) { super("FisherBait"); this.bait = bait; this.multiplier = multiplier; }
        @Override public void apply() { invoke(baitMap(), "put", key(bait), multiplier); }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(bait); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient bait;
        Remove(IIngredient bait) { super("FisherBait"); this.bait = bait; }
        @Override public void apply() {
            List<Object> keys = new ArrayList<>((java.util.Collection<?>) invoke(baitMap(), "keySet"));
            for (Object key : keys) {
                ItemStack stack = (ItemStack) invoke(key, "toItemStack");
                if (bait.matches(InputHelper.toIItemStack(stack))) invoke(baitMap(), "remove", key);
            }
        }
        @Override protected String getRecipeInfo() { return bait.toString(); }
    }
}
