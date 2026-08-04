package com.blamejared.compat.tcomplement;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thermalexpansion.ThermalExpansionReflection;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import knightminer.tcomplement.library.TCompRegistry;

@ZenClass("mods.tcomplement.Melter")
@ModOnly("tcomplement")
@ZenRegister
public class Melter {

    @ZenMethod
    public static void removeByInput(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Match(input, null));
    }

    @ZenMethod
    public static void removeByOutput(ILiquidStack output) {
        ModTweaker.LATE_REMOVALS.add(new Match(null, com.blamejared.mtlib.helpers.InputHelper.toFluid(output)));
    }

    @ZenMethod
    public static void removeByInputAndOutput(IIngredient input, ILiquidStack output) {
        ModTweaker.LATE_REMOVALS.add(new Match(input, com.blamejared.mtlib.helpers.InputHelper.toFluid(output)));
    }

    private static class Match extends BaseAction {
        private final IIngredient input;
        private final FluidStack output;

        Match(IIngredient input, FluidStack output) {
            super("Tinkers Complement Melter");
            this.input = input;
            this.output = output;
        }

        @Override
        public void apply() {
            java.util.List<?> recipes = (java.util.List<?>) ThermalExpansionReflection.get(TCompRegistry.class, "meltingOverrides");
            java.util.List<Object> remove = new java.util.ArrayList<>();
            for (Object recipe : recipes) {
                Object recipeOutput = field(recipe, "output");
                boolean outputMatch = output == null || recipeOutput instanceof FluidStack && output.isFluidEqual((FluidStack) recipeOutput);
                boolean inputMatch = input == null || matchesInput(field(recipe, "input"));
                if (outputMatch && inputMatch) remove.add(recipe);
            }
            recipes.removeAll(remove);
        }

        private boolean matchesInput(Object recipeInput) {
            if (recipeInput == null) return false;
            for (IItemStack stack : input.getItems()) {
                try {
                    java.lang.reflect.Method method = recipeInput.getClass().getMethod("matches", ItemStack.class);
                    if (Boolean.TRUE.equals(method.invoke(recipeInput, com.blamejared.mtlib.helpers.InputHelper.toStack(stack)))) return true;
                } catch (ReflectiveOperationException ignored) {
                }
            }
            return false;
        }

        private Object field(Object object, String name) {
            try {
                java.lang.reflect.Field field = object.getClass().getDeclaredField(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (ReflectiveOperationException e) {
                return null;
            }
        }

        @Override
        protected String getRecipeInfo() {
            return output == null ? input.toString() : output.toString();
        }
    }
}
