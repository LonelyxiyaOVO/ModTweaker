package com.blamejared.compat.thermalexpansion;

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
import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.thermalexpansion.Fisher")
@ModOnly("thermalexpansion")
public class Fisher {
    @ZenMethod
    public static void addRecipe(IItemStack fish, int weight) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(fish), weight));
    }

    @ZenMethod
    public static void removeRecipe(IIngredient fish) {
        ModTweaker.LATE_REMOVALS.add(new Remove(fish));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Fisher", () -> {
            fishList().clear();
            weightList().clear();
            setTotalWeight(0);
        });
    }

    @SuppressWarnings("unchecked")
    private static List<ItemStack> fishList() { return (List<ItemStack>) staticField("fishList"); }
    @SuppressWarnings("unchecked")
    private static List<Integer> weightList() { return (List<Integer>) staticField("weightList"); }

    private static Object staticField(String name) {
        try {
            Class<?> manager = Class.forName("cofh.thermalexpansion.util.managers.device.FisherManager");
            Field field = manager.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Fisher " + name, e);
        }
    }

    private static int totalWeight() {
        try {
            Class<?> manager = Class.forName("cofh.thermalexpansion.util.managers.device.FisherManager");
            Field field = manager.getDeclaredField("totalWeight");
            field.setAccessible(true);
            return field.getInt(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Fisher total weight", e);
        }
    }

    private static void setTotalWeight(int value) {
        try {
            Class<?> manager = Class.forName("cofh.thermalexpansion.util.managers.device.FisherManager");
            Field field = manager.getDeclaredField("totalWeight");
            field.setAccessible(true);
            field.setInt(null, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to update Thermal Expansion Fisher total weight", e);
        }
    }

    private static class Add extends BaseAction {
        private final ItemStack fish;
        private final int weight;
        Add(ItemStack fish, int weight) { super("Fisher"); this.fish = fish; this.weight = weight; }
        @Override public void apply() {
            fishList().add(fish); weightList().add(weight); setTotalWeight(totalWeight() + weight);
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(fish); }
    }

    private static class Remove extends BaseAction {
        private final IIngredient fish;
        Remove(IIngredient fish) { super("Fisher"); this.fish = fish; }
        @Override public void apply() {
            List<ItemStack> snapshot = new ArrayList<>(fishList());
            int removedWeight = 0;
            for (ItemStack stack : snapshot) {
                if (fish.matches(InputHelper.toIItemStack(stack))) {
                    int index = fishList().indexOf(stack);
                    if (index >= 0) { removedWeight += weightList().remove(index); fishList().remove(index); }
                }
            }
            setTotalWeight(Math.max(0, totalWeight() - removedWeight));
        }
        @Override protected String getRecipeInfo() { return fish.toString(); }
    }
}
