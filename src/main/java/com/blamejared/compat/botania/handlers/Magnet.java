package com.blamejared.compat.botania.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Method;

@ZenClass("mods.botania.Magnet")
@ModOnly("botania")
@ZenRegister
public class Magnet {

    @ZenMethod
    public static boolean isInBlacklist(IIngredient item) {
        return !item.getItems().isEmpty() && BotaniaAPI.isItemBlacklistedFromMagnet(InputHelper.toStack(item.getItems().get(0)));
    }

    @ZenMethod
    public static boolean isInBlacklist(IBlock block, @Optional int meta) {
        return BotaniaAPI.isBlockBlacklistedFromMagnet(CraftTweakerMC.getBlock(block), meta);
    }

    @ZenMethod
    public static boolean isInBlacklist(crafttweaker.api.block.IBlockState state) {
        return BotaniaAPI.isBlockBlacklistedFromMagnet(CraftTweakerMC.getBlockState(state));
    }

    @ZenMethod
    public static void addToBlacklist(IIngredient item) {
        queueAdd("Magnet", () -> {
            for(crafttweaker.api.item.IItemStack stack : item.getItems()) {
                BotaniaAPI.blacklistItemFromMagnet(InputHelper.toStack(stack));
            }
        });
    }

    @ZenMethod
    public static void addToBlacklist(IBlock block, @Optional int meta) {
        queueAdd("Magnet", () -> BotaniaAPI.blacklistBlockFromMagnet(CraftTweakerMC.getBlock(block), meta));
    }

    @ZenMethod
    public static void addToBlacklist(crafttweaker.api.block.IBlockState state) {
        queueAdd("Magnet", () -> {
            IBlockState internal = CraftTweakerMC.getBlockState(state);
            BotaniaAPI.blacklistBlockFromMagnet(internal.getBlock(), internal.getBlock().getMetaFromState(internal));
        });
    }

    @ZenMethod
    public static void removeFromBlacklist(IIngredient item) {
        queueRemove("Magnet", () -> {
            for(crafttweaker.api.item.IItemStack stack : item.getItems()) {
                BotaniaAPI.magnetBlacklist.remove(itemKey(InputHelper.toStack(stack)));
            }
        });
    }

    @ZenMethod
    public static void removeFromBlacklist(IBlock block, @Optional int meta) {
        queueRemove("Magnet", () -> BotaniaAPI.magnetBlacklist.remove(blockKey(CraftTweakerMC.getBlock(block), meta)));
    }

    @ZenMethod
    public static void removeFromBlacklist(crafttweaker.api.block.IBlockState state) {
        queueRemove("Magnet", () -> {
            IBlockState internal = CraftTweakerMC.getBlockState(state);
            BotaniaAPI.magnetBlacklist.remove(blockKey(internal.getBlock(), internal.getBlock().getMetaFromState(internal)));
        });
    }

    @ZenMethod
    public static void removeAll() {
        queueRemove("Magnet", BotaniaAPI.magnetBlacklist::clear);
    }

    private static void queueAdd(String name, Runnable action) {
        ModTweaker.LATE_ADDITIONS.add(new Action(name, action));
    }

    private static void queueRemove(String name, Runnable action) {
        ModTweaker.LATE_REMOVALS.add(new Action(name, action));
    }

    private static class Action extends BaseAction {
        private final Runnable action;

        private Action(String name, Runnable action) {
            super(name);
            this.action = action;
        }

        @Override
        public void apply() {
            action.run();
        }

        @Override
        protected String getRecipeInfo() {
            return "magnet blacklist";
        }
    }

    private static String itemKey(ItemStack stack) {
        return invokeKey("getMagnetKey", new Class[]{ItemStack.class}, stack);
    }

    private static String blockKey(Block block, int meta) {
        return invokeKey("getMagnetKey", new Class[]{Block.class, int.class}, block, meta);
    }

    private static String invokeKey(String name, Class<?>[] types, Object... args) {
        try {
            Method method = BotaniaAPI.class.getDeclaredMethod(name, types);
            method.setAccessible(true);
            return (String) method.invoke(null, args);
        } catch(ReflectiveOperationException e) {
            LogHelper.logError("Unable to access Botania magnet key method", e);
            throw new IllegalStateException(e);
        }
    }
}
