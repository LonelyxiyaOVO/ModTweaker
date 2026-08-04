package com.blamejared.compat.thermalexpansion;

import cofh.core.util.BlockWrapper;
import cofh.core.util.ItemWrapper;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.util.Map;

@ZenClass("mods.thermalexpansion.Tapper")
@ModOnly("thermalexpansion")
public class Tapper {

    @ZenMethod
    public static void addItem(IItemStack input, ILiquidStack output) {
        ModTweaker.LATE_ADDITIONS.add(new AddItem(
                InputHelper.toStack(input), InputHelper.toFluid(output)));
    }

    @ZenMethod
    public static void addBlock(IItemStack input, ILiquidStack output) {
        ModTweaker.LATE_ADDITIONS.add(new AddBlock(
                InputHelper.toStack(input), InputHelper.toFluid(output)));
    }

    @ZenMethod
    public static void removeItemByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input, false));
    }

    @ZenMethod
    public static void removeBlockByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input, true));
    }

    @ZenMethod
    public static void removeAllItems() {
        RecipeActions.removeAll("TapperItems", () -> itemMap().clear());
    }

    @ZenMethod
    public static void removeAllBlocks() {
        RecipeActions.removeAll("TapperBlocks", () -> blockMap().clear());
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Tapper", () -> {
            itemMap().clear();
            blockMap().clear();
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, FluidStack> itemMap() {
        return (Map<Object, FluidStack>) field("itemMap");
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, FluidStack> blockMap() {
        return (Map<Object, FluidStack>) field("blockMap");
    }

    private static Object field(String name) {
        try {
            Field field = Class.forName(
                    "cofh.thermalexpansion.util.managers.device.TapperManager")
                    .getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Thermal Expansion Tapper " + name, e);
        }
    }

    private static BlockWrapper blockWrapper(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) stack.getItem();
            return new BlockWrapper(itemBlock.getBlock(), stack.getMetadata());
        }
        return null;
    }

    private static class AddItem extends BaseAction {
        private final ItemStack input;
        private final FluidStack output;

        AddItem(ItemStack input, FluidStack output) {
            super("Tapper");
            this.input = input;
            this.output = output;
        }

        @Override
        public void apply() {
            itemMap().put(new ItemWrapper(input), output);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    private static class AddBlock extends BaseAction {
        private final ItemStack input;
        private final FluidStack output;

        AddBlock(ItemStack input, FluidStack output) {
            super("Tapper");
            this.input = input;
            this.output = output;
        }

        @Override
        public void apply() {
            BlockWrapper key = blockWrapper(input);
            if (key != null) {
                blockMap().put(key, output);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient target;
        private final boolean block;

        Remove(IIngredient target, boolean block) {
            super("Tapper");
            this.target = target;
            this.block = block;
        }

        @Override
        public void apply() {
            Map<Object, FluidStack> map = block ? blockMap() : itemMap();
            map.entrySet().removeIf(entry -> {
                if (target.matches(InputHelper.toILiquidStack(entry.getValue()))) {
                    return true;
                }
                if (!block && entry.getKey() instanceof ItemWrapper) {
                    ItemWrapper wrapper = (ItemWrapper) entry.getKey();
                    ItemStack item = new ItemStack(wrapper.item, wrapper.metadata);
                    return target.matches(InputHelper.toIItemStack(item));
                }
                return false;
            });
        }

        @Override
        protected String getRecipeInfo() {
            return target.toString();
        }
    }
}
