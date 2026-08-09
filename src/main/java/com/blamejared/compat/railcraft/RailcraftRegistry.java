package com.blamejared.compat.railcraft;

import mods.railcraft.api.fuel.FluidFuelManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/** Access to Railcraft's private fluid-fuel registry for CRT removals. */
final class RailcraftRegistry {

    private RailcraftRegistry() {
    }

    @SuppressWarnings("unchecked")
    private static Map<FluidStack, Integer> fuelRegistry() {
        try {
            Field field = FluidFuelManager.class.getDeclaredField("boilerFuel");
            field.setAccessible(true);
            return (Map<FluidStack, Integer>) field.get(null);
        } catch (ReflectiveOperationException e) {
            return Collections.emptyMap();
        }
    }

    static void removeFuel(Fluid fluid) {
        Iterator<Map.Entry<FluidStack, Integer>> iterator = fuelRegistry().entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey().getFluid() == fluid) {
                iterator.remove();
                return;
            }
        }
    }

    static void removeAllFuel() {
        fuelRegistry().clear();
    }
}
