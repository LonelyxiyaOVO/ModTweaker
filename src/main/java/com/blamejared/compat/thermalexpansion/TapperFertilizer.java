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

@ZenClass("mods.thermalexpansion.TapperFertilizer")
@ModOnly("thermalexpansion")
public class TapperFertilizer {
    @ZenMethod
    public static void addRecipe(IItemStack bait, int multiplier) { ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(bait), multiplier)); }
    @ZenMethod
    public static void removeRecipe(IIngredient bait) { ModTweaker.LATE_REMOVALS.add(new Remove(bait)); }
    @ZenMethod
    public static void removeAll() { RecipeActions.removeAll("TapperFertilizer", () -> invoke(fertilizerMap(), "clear")); }

    private static Object fertilizerMap() {
        try { Field f = Class.forName("cofh.thermalexpansion.util.managers.device.TapperManager").getDeclaredField("fertilizerMap"); f.setAccessible(true); return f.get(null); }
        catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to access Thermal Expansion Tapper fertilizer registry", e); }
    }
    private static Object invoke(Object target, String name, Object... args) {
        try {
            for (Method method : target.getClass().getMethods()) if (method.getName().equals(name) && method.getParameterTypes().length == args.length) return method.invoke(target, args);
            throw new NoSuchMethodException(name);
        } catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to call Tapper fertilizer registry method " + name, e); }
    }
    private static ComparableItemStack key(ItemStack stack) { return new ComparableItemStack(stack); }

    private static class Add extends BaseAction {
        private final ItemStack bait; private final int multiplier;
        Add(ItemStack bait, int multiplier) { super("TapperFertilizer"); this.bait=bait; this.multiplier=multiplier; }
        @Override public void apply() { invoke(fertilizerMap(), "put", key(bait), multiplier); }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(bait); }
    }
    private static class Remove extends BaseAction {
        private final IIngredient bait;
        Remove(IIngredient bait) { super("TapperFertilizer"); this.bait=bait; }
        @Override public void apply() {
            List<Object> keys = new ArrayList<>((Collection<?>) invoke(fertilizerMap(), "keySet"));
            for (Object key : keys) if (bait.matches(InputHelper.toIItemStack((ItemStack) invoke(key, "toItemStack")))) invoke(fertilizerMap(), "remove", key);
        }
        @Override protected String getRecipeInfo() { return bait.toString(); }
    }
}
