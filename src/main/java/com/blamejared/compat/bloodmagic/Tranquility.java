package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.incense.EnumTranquilityType;
import WayofTime.bloodmagic.incense.TranquilityStack;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.block.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.util.Map;

@ZenClass("mods.bloodmagic.Tranquility")
@ModOnly("bloodmagic")
public class Tranquility {
    @ZenMethod
    public static void add(IBlock block, String type, double value) {
        Block internal = block(block);
        if (internal == null) return;
        for (IBlockState state : internal.getBlockState().getValidStates()) addInternal(state, type, value);
    }

    @ZenMethod
    public static void add(crafttweaker.api.block.IBlockState state, String type, double value) {
        EnumTranquilityType tranquility = type(type);
        if (tranquility == null) return;
        addInternal(state(state), tranquility, value);
    }

    @ZenMethod
    public static void remove(IBlock block, String type) {
        Block internal = block(block);
        if (internal == null) return;
        for (IBlockState state : internal.getBlockState().getValidStates()) removeInternal(state, type);
    }

    @ZenMethod
    public static void remove(crafttweaker.api.block.IBlockState state, String type) {
        EnumTranquilityType tranquility = type(type);
        if (tranquility == null) return;
        removeInternal(state(state), tranquility);
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Tranquility", () -> tranquility().clear());
    }

    private static EnumTranquilityType type(String name) {
        for (EnumTranquilityType type : EnumTranquilityType.values()) {
            if (type.name().equalsIgnoreCase(name)) return type;
        }
        return null;
    }

    private static void addInternal(IBlockState state, String type, double value) {
        EnumTranquilityType tranquility = type(type);
        if (tranquility != null) addInternal(state, tranquility, value);
    }

    private static void addInternal(IBlockState state, EnumTranquilityType type, double value) {
        ModTweaker.LATE_ADDITIONS.add(new Add(state, type, value));
    }

    private static void removeInternal(IBlockState state, String type) {
        EnumTranquilityType tranquility = type(type);
        if (tranquility != null) removeInternal(state, tranquility);
    }

    private static void removeInternal(IBlockState state, EnumTranquilityType type) {
        ModTweaker.LATE_REMOVALS.add(new Remove(state, type));
    }

    private static Block block(IBlock block) {
        Object internal = block.getDefinition().getInternal();
        return internal instanceof Block ? (Block) internal : null;
    }

    private static IBlockState state(crafttweaker.api.block.IBlockState state) {
        Object internal = state.getInternal();
        return internal instanceof IBlockState ? (IBlockState) internal : null;
    }

    @SuppressWarnings("unchecked")
    private static Map<IBlockState, TranquilityStack> tranquility() {
        try {
            Object manager = BloodMagicAPI.INSTANCE.getValueManager();
            Field field = manager.getClass().getDeclaredField("tranquility");
            field.setAccessible(true);
            return (Map<IBlockState, TranquilityStack>) field.get(manager);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Blood Magic tranquility registry", e);
        }
    }

    private static class Add extends BaseAction {
        private final IBlockState state;
        private final EnumTranquilityType type;
        private final double value;
        Add(IBlockState state, EnumTranquilityType type, double value) {
            super("Tranquility"); this.state = state; this.type = type; this.value = value;
        }
        @Override public void apply() {
            if (state != null) tranquility().put(state, new TranquilityStack(type, value));
        }
        @Override protected String getRecipeInfo() { return String.valueOf(state); }
    }

    private static class Remove extends BaseAction {
        private final IBlockState state;
        private final EnumTranquilityType type;
        Remove(IBlockState state, EnumTranquilityType type) { super("Tranquility"); this.state = state; this.type = type; }
        @Override public void apply() {
            TranquilityStack entry = tranquility().get(state);
            if (entry != null && entry.type == type) tranquility().remove(state);
        }
        @Override protected String getRecipeInfo() { return String.valueOf(state); }
    }
}
