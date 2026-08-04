package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.util.Map;

@ZenClass("mods.bloodmagic.Sacrificial")
@ModOnly("bloodmagic")
public class Sacrificial {
    @ZenMethod
    public static void add(String entity, int value) {
        ModTweaker.LATE_ADDITIONS.add(new Add(new ResourceLocation(entity), value));
    }

    @ZenMethod
    public static void remove(String entity) {
        ModTweaker.LATE_REMOVALS.add(new Remove(new ResourceLocation(entity)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Sacrificial", () -> sacrificial().clear());
    }

    @SuppressWarnings("unchecked")
    private static Map<ResourceLocation, Integer> sacrificial() {
        try {
            Object manager = BloodMagicAPI.INSTANCE.getValueManager();
            Field field = manager.getClass().getDeclaredField("sacrificial");
            field.setAccessible(true);
            return (Map<ResourceLocation, Integer>) field.get(manager);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Blood Magic sacrificial registry", e);
        }
    }

    private static class Add extends BaseAction {
        private final ResourceLocation entity;
        private final int value;
        Add(ResourceLocation entity, int value) { super("Sacrificial"); this.entity = entity; this.value = value; }

        @Override
        public void apply() {
            if (EntityList.getClass(entity) == null) {
                LogHelper.logError("Unknown entity for Blood Magic sacrificial value: " + entity);
                return;
            }
            sacrificial().put(entity, value);
        }

        @Override
        protected String getRecipeInfo() { return entity.toString(); }
    }

    private static class Remove extends BaseAction {
        private final ResourceLocation entity;
        Remove(ResourceLocation entity) { super("Sacrificial"); this.entity = entity; }

        @Override
        public void apply() {
            if (sacrificial().remove(entity) == null) {
                LogHelper.logWarning("No Blood Magic sacrificial value found for: " + entity);
            }
        }

        @Override
        protected String getRecipeInfo() { return entity.toString(); }
    }
}
