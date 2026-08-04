package com.blamejared.compat.thermalexpansion;

import com.blamejared.ModTweaker;
import com.blamejared.compat.RecipeActions;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@ZenClass("mods.thermalexpansion.Coolant")
@ModOnly("thermalexpansion")
public class Coolant {

    @ZenMethod
    public static void addRecipe(ILiquidStack fluid, int rf, int factor) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(fluid), rf, factor));
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(fluid)));
    }

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("Coolant", () -> {
            invoke(coolantMap(), "clear");
            invoke(factorMap(), "clear");
        });
    }

    private static Object registryField(String name) {
        try {
            Class<?> manager = Class.forName(
                    "cofh.thermalexpansion.util.managers.device.CoolantManager");
            Field field = manager.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to access Coolant " + name, e);
        }
    }

    private static Object coolantMap() {
        return registryField("coolantMap");
    }

    private static Object factorMap() {
        return registryField("coolantFactorMap");
    }

    private static Object invoke(Object target, String name, Object... args) {
        try {
            for (Method method : target.getClass().getMethods()) {
                if (method.getName().equals(name)
                        && method.getParameterTypes().length == args.length) {
                    return method.invoke(target, args);
                }
            }
            throw new NoSuchMethodException(name);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to call Coolant registry method " + name, e);
        }
    }

    private static class Add extends BaseAction {
        private final FluidStack fluid;
        private final int rf;
        private final int factor;

        Add(FluidStack fluid, int rf, int factor) {
            super("Coolant");
            this.fluid = fluid;
            this.rf = rf;
            this.factor = factor;
        }

        @Override
        public void apply() {
            String name = fluid.getFluid().getName();
            invoke(coolantMap(), "put", name, rf);
            invoke(factorMap(), "put", name, factor);
        }

        @Override
        protected String getRecipeInfo() {
            return fluid.getFluid().getName();
        }
    }

    private static class Remove extends BaseAction {
        private final FluidStack fluid;

        Remove(FluidStack fluid) {
            super("Coolant");
            this.fluid = fluid;
        }

        @Override
        public void apply() {
            String name = fluid.getFluid().getName();
            invoke(coolantMap(), "remove", name);
            invoke(factorMap(), "remove", name);
        }

        @Override
        protected String getRecipeInfo() {
            return fluid.getFluid().getName();
        }
    }
}
