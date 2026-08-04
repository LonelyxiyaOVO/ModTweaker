package com.blamejared.compat.thermalexpansion;

import cofh.core.util.BlockWrapper;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@ZenClass("mods.thermalexpansion.TapperTree")
@ModOnly("thermalexpansion")
public class TapperTree {

    @ZenMethod
    public static void addRecipe(crafttweaker.api.block.IBlockState log,
                                 crafttweaker.api.block.IBlockState leaf) {
        ModTweaker.LATE_ADDITIONS.add(new Add(wrapper(log), wrapper(leaf)));
    }

    @ZenMethod
    public static void removeRecipeByLog(crafttweaker.api.block.IBlockState log) {
        ModTweaker.LATE_REMOVALS.add(new Remove(wrapper(log), true));
    }

    @ZenMethod
    public static void removeRecipeByLeaf(crafttweaker.api.block.IBlockState leaf) {
        ModTweaker.LATE_REMOVALS.add(new Remove(wrapper(leaf), false));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("TapperTree", () -> invoke(leafMap(), "clear"));
    }

    private static BlockWrapper wrapper(crafttweaker.api.block.IBlockState state) {
        Object internal = state.getInternal();
        return internal instanceof IBlockState ? new BlockWrapper((IBlockState) internal) : null;
    }

    private static Object leafMap() {
        try {
            Field field = Class.forName(
                    "cofh.thermalexpansion.util.managers.device.TapperManager")
                    .getDeclaredField("leafMap");
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Tapper tree registry", e);
        }
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
            throw new IllegalStateException("Unable to call Tapper tree registry method " + name, e);
        }
    }

    private static Collection<?> entries() {
        return (Collection<?>) invoke(leafMap(), "entries");
    }

    private static class Add extends BaseAction {
        private final BlockWrapper log;
        private final BlockWrapper leaf;

        Add(BlockWrapper log, BlockWrapper leaf) {
            super("TapperTree");
            this.log = log;
            this.leaf = leaf;
        }

        @Override
        public void apply() {
            invoke(leafMap(), "put", log, leaf);
        }

        @Override
        protected String getRecipeInfo() {
            return String.valueOf(log);
        }
    }

    private static class Remove extends BaseAction {
        private final BlockWrapper target;
        private final boolean log;

        Remove(BlockWrapper target, boolean log) {
            super("TapperTree");
            this.target = target;
            this.log = log;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void apply() {
            Collection<Object> matchingEntries = new ArrayList<>();
            for (Object object : entries()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
                if ((log && target.equals(entry.getKey()))
                        || (!log && target.equals(entry.getValue()))) {
                    matchingEntries.add(object);
                }
            }
            ((Collection<Object>) entries()).removeAll(matchingEntries);
        }

        @Override
        protected String getRecipeInfo() {
            return String.valueOf(target);
        }
    }
}
