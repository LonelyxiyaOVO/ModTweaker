package com.blamejared.compat.railcraft;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidDefinition;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.railcraft.api.fuel.FluidFuelManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.railcraft.FluidFuel")
@ModOnly("railcraft")
@ZenRegister
public final class FluidFuel {

    private static final String NAME = "Railcraft Fluid Fuel";

    private FluidFuel() {
    }

    @ZenMethod
    public static void addFuel(ILiquidStack liquid, int heatValuePerBucket) {
        ModTweaker.LATE_ADDITIONS.add(new AddStack(
                CraftTweakerMC.getLiquidStack(liquid), heatValuePerBucket));
    }

    @ZenMethod
    public static void addFuel(ILiquidDefinition liquidType, int heatValuePerBucket) {
        ModTweaker.LATE_ADDITIONS.add(new AddFluid(
                CraftTweakerMC.getFluid(liquidType), heatValuePerBucket));
    }

    @ZenMethod
    public static void removeFuel(ILiquidStack liquid) {
        removeFuel(liquid.getDefinition());
    }

    @ZenMethod
    public static void removeFuel(ILiquidDefinition liquidType) {
        ModTweaker.LATE_REMOVALS.add(new Remove(CraftTweakerMC.getFluid(liquidType)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll(NAME, RailcraftRegistry::removeAllFuel);
    }

    private static final class AddStack extends BaseAction {

        private final FluidStack liquid;
        private final int value;

        private AddStack(FluidStack liquid, int value) {
            super(NAME);
            this.liquid = liquid;
            this.value = value;
        }

        @Override
        public void apply() {
            FluidFuelManager.addFuel(liquid, value);
        }

        @Override
        protected String getRecipeInfo() {
            return liquid == null ? "null" : liquid.getFluid().getName();
        }
    }

    private static final class AddFluid extends BaseAction {

        private final Fluid liquid;
        private final int value;

        private AddFluid(Fluid liquid, int value) {
            super(NAME);
            this.liquid = liquid;
            this.value = value;
        }

        @Override
        public void apply() {
            FluidFuelManager.addFuel(liquid, value);
        }

        @Override
        protected String getRecipeInfo() {
            return liquid == null ? "null" : liquid.getName();
        }
    }

    private static final class Remove extends BaseAction {

        private final Fluid liquid;

        private Remove(Fluid liquid) {
            super(NAME);
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            RailcraftRegistry.removeFuel(liquid);
        }

        @Override
        protected String getRecipeInfo() {
            return String.format(Locale.ENGLISH, "fluid '%s'", liquid.getName());
        }
    }
}
