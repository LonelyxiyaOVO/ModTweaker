package com.blamejared.compat.thermalexpansion;

import cofh.core.util.helpers.FluidHelper;
import cofh.thermalexpansion.util.managers.machine.RefineryManager;
import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

@ZenClass("mods.thermalexpansion.RefineryPotion")
@ModOnly("thermalexpansion")
public class RefineryPotion {
    @ZenMethod
    public static void addRecipe(ILiquidStack input, ILiquidStack output, @Optional WeightedItemStack outputItem, int chance, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(input), InputHelper.toFluid(output), outputItem, chance, energy));
    }
    @ZenMethod
    public static void removeRecipeByInput(IIngredient input) { ModTweaker.LATE_REMOVALS.add(new Remove(input, true)); }
    @ZenMethod
    public static void removeRecipeByOutput(IIngredient output) { ModTweaker.LATE_REMOVALS.add(new Remove(output, false)); }
    @ZenMethod
    public static void removeAll() { RecipeActions.removeAll("RefineryPotion", () -> recipeMap().clear()); }

    @SuppressWarnings("unchecked") private static Map<Integer, RefineryManager.RefineryRecipe> recipeMap() { return (Map<Integer, RefineryManager.RefineryRecipe>) field("recipeMapPotion"); }
    private static Object field(String name) {
        try { Field f=RefineryManager.class.getDeclaredField(name); f.setAccessible(true); return f.get(null); }
        catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to access Refinery potion registry", e); }
    }
    private static class Add extends BaseAction {
        private final FluidStack input, output; private final WeightedItemStack outputItem; private final int chance, energy;
        Add(FluidStack input, FluidStack output, WeightedItemStack outputItem, int chance, int energy) { super("RefineryPotion"); this.input=input; this.output=output; this.outputItem=outputItem; this.chance=chance; this.energy=energy; }
        @Override public void apply() {
            try {
                ItemStack item = outputItem == null ? null : InputHelper.toStack(outputItem.getStack());
                Constructor<RefineryManager.RefineryRecipe> c=RefineryManager.RefineryRecipe.class.getDeclaredConstructor(FluidStack.class, FluidStack.class, ItemStack.class, int.class, int.class);
                c.setAccessible(true);
                RefineryManager.RefineryRecipe recipe=c.newInstance(input, output, item, energy, outputItem == null ? 0 : chance);
                recipeMap().put(FluidHelper.getFluidHash(input), recipe);
            } catch (ReflectiveOperationException e) { throw new IllegalStateException("Unable to create Refinery potion recipe", e); }
        }
        @Override protected String getRecipeInfo() { return LogHelper.getStackDescription(output); }
    }
    private static class Remove extends BaseAction {
        private final IIngredient target; private final boolean input;
        Remove(IIngredient target, boolean input) { super("RefineryPotion"); this.target=target; this.input=input; }
        @Override public void apply() { recipeMap().values().removeIf(r -> input ? target.matches(InputHelper.toILiquidStack(r.getInput())) : target.matches(InputHelper.toILiquidStack(r.getOutputFluid())) || (r.getOutputItem() != null && target.matches(InputHelper.toIItemStack(r.getOutputItem())))); }
        @Override protected String getRecipeInfo() { return target.toString(); }
    }
}
