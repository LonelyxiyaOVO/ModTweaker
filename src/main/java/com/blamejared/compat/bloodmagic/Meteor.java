package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.meteor.MeteorComponent;
import WayofTime.bloodmagic.meteor.MeteorRegistry;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@ZenClass("mods.bloodmagic.Meteor")
@ModOnly("bloodmagic")
public class Meteor {
    @ZenMethod
    public static void addRecipe(IItemStack catalyst, String[] components, int[] weights,
                                 float explosionStrength, int radius, int cost) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(catalyst), components, weights,
                explosionStrength, radius, cost));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack catalyst) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(catalyst)));
    }

    @ZenMethod
    public static void removeRecipeByInput(IItemStack catalyst) {
        removeRecipe(catalyst);
    }

    @ZenMethod
    public static void removeRecipeByCatalyst(IItemStack catalyst) {
        removeRecipe(catalyst);
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Meteor", () -> MeteorRegistry.meteorMap.clear());
    }

    private static class Add extends BaseAction {
        private final ItemStack catalyst;
        private final String[] components;
        private final int[] weights;
        private final float explosionStrength;
        private final int radius, cost;

        Add(ItemStack catalyst, String[] components, int[] weights, float explosionStrength, int radius, int cost) {
            super("Meteor");
            this.catalyst = catalyst;
            this.components = components == null ? new String[0] : components;
            this.weights = weights == null ? new int[0] : weights;
            this.explosionStrength = explosionStrength;
            this.radius = radius;
            this.cost = cost;
        }

        @Override
        public void apply() {
            if (components.length != weights.length) {
                LogHelper.logError("Meteor component and weight arrays must have the same length");
                return;
            }
            List<MeteorComponent> list = new ArrayList<>();
            for (int i = 0; i < components.length; i++) {
                list.add(new MeteorComponent(weights[i], components[i]));
            }
            WayofTime.bloodmagic.meteor.Meteor meteor =
                    new WayofTime.bloodmagic.meteor.Meteor(catalyst, list, explosionStrength, radius);
            setCost(meteor, cost);
            MeteorRegistry.registerMeteor(catalyst, meteor);
        }

        @Override
        protected String getRecipeInfo() { return LogHelper.getStackDescription(catalyst); }
    }

    private static class Remove extends BaseAction {
        private final ItemStack catalyst;
        Remove(ItemStack catalyst) { super("Meteor"); this.catalyst = catalyst; }

        @Override
        public void apply() {
            if (MeteorRegistry.getMeteorForItem(catalyst) == null) {
                LogHelper.logWarning("No Blood Magic meteor recipe found for " + catalyst);
                return;
            }
            WayofTime.bloodmagic.meteor.Meteor meteor = MeteorRegistry.getMeteorForItem(catalyst);
            MeteorRegistry.meteorMap.remove(meteor.getCatalystStack());
        }

        @Override
        protected String getRecipeInfo() { return LogHelper.getStackDescription(catalyst); }
    }

    private static void setCost(WayofTime.bloodmagic.meteor.Meteor meteor, int cost) {
        try {
            Method method = meteor.getClass().getMethod("setCost", int.class);
            method.invoke(meteor, cost);
            return;
        } catch (ReflectiveOperationException ignored) {
            // Older Blood Magic releases do not expose meteor cost.
        }
        try {
            Field field = meteor.getClass().getDeclaredField("cost");
            field.setAccessible(true);
            field.setInt(meteor, cost);
        } catch (ReflectiveOperationException ignored) {
            LogHelper.logWarning("This Blood Magic version does not support meteor cost");
        }
    }
}
