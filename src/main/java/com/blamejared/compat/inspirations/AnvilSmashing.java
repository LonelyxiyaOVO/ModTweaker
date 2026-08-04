package com.blamejared.compat.inspirations;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import knightminer.inspirations.library.InspirationsRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ZenClass("mods.inspirations.AnvilSmashing")
@ModOnly("inspirations")
@ZenRegister
public class AnvilSmashing {

    @ZenMethod
    public static void add(crafttweaker.api.block.IBlockState input, crafttweaker.api.block.IBlockState output) {
        queueAdd("Anvil Smashing", () -> stateMap().put(CraftTweakerMC.getBlockState(input), CraftTweakerMC.getBlockState(output)));
    }

    @ZenMethod
    public static void add(IBlock input, crafttweaker.api.block.IBlockState output) {
        queueAdd("Anvil Smashing", () -> blockMap().put(CraftTweakerMC.getBlock(input), CraftTweakerMC.getBlockState(output)));
    }

    @ZenMethod
    public static void addBreaking(crafttweaker.api.block.IBlockState input) {
        queueAdd("Anvil Breaking", () -> breakingSet().add(CraftTweakerMC.getBlockState(input).getMaterial()));
    }

    @ZenMethod
    public static void remove(crafttweaker.api.block.IBlockState input, crafttweaker.api.block.IBlockState output) {
        queueRemove("Anvil Smashing", () -> stateMap().remove(CraftTweakerMC.getBlockState(input), CraftTweakerMC.getBlockState(output)));
    }

    @ZenMethod
    public static void remove(IBlock input, crafttweaker.api.block.IBlockState output) {
        queueRemove("Anvil Smashing", () -> blockMap().remove(CraftTweakerMC.getBlock(input), CraftTweakerMC.getBlockState(output)));
    }

    @ZenMethod
    public static void removeByInput(crafttweaker.api.block.IBlockState input) {
        queueRemove("Anvil Smashing", () -> stateMap().remove(CraftTweakerMC.getBlockState(input)));
    }

    @ZenMethod
    public static void removeByInput(IBlock input) {
        queueRemove("Anvil Smashing", () -> blockMap().remove(CraftTweakerMC.getBlock(input)));
    }

    @ZenMethod
    public static void removeByOutput(crafttweaker.api.block.IBlockState output) {
        queueRemove("Anvil Smashing", () -> {
            IBlockState state = CraftTweakerMC.getBlockState(output);
            stateMap().entrySet().removeIf(entry -> entry.getValue().equals(state));
            blockMap().entrySet().removeIf(entry -> entry.getValue().equals(state));
        });
    }

    @ZenMethod
    public static void removeAll() {
        queueRemove("Anvil Smashing", () -> {
            stateMap().clear();
            blockMap().clear();
            breakingSet().clear();
        });
    }

    private static void queueAdd(String name, Runnable action) {
        ModTweaker.LATE_ADDITIONS.add(new BaseAction(name) {
            @Override
            public void apply() {
                action.run();
            }

            @Override
            protected String getRecipeInfo() {
                return "anvil smashing registry";
            }
        });
    }

    private static void queueRemove(String name, Runnable action) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction(name) {
            @Override
            public void apply() {
                action.run();
            }

            @Override
            protected String getRecipeInfo() {
                return "anvil smashing registry";
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<IBlockState, IBlockState> stateMap() {
        return (Map<IBlockState, IBlockState>) getField("anvilSmashing");
    }

    @SuppressWarnings("unchecked")
    private static Map<Block, IBlockState> blockMap() {
        return (Map<Block, IBlockState>) getField("anvilSmashingBlocks");
    }

    @SuppressWarnings("unchecked")
    private static Set<Material> breakingSet() {
        return (Set<Material>) getField("anvilBreaking");
    }

    private static Object getField(String name) {
        try {
            Field field = InspirationsRegistry.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch(ReflectiveOperationException e) {
            LogHelper.logError("Unable to access Inspirations registry field: " + name, e);
            throw new IllegalStateException(e);
        }
    }
}
