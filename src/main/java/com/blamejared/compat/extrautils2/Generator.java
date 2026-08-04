package com.blamejared.compat.extrautils2;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.rwtema.extrautils2.api.machine.IMachineRecipe;
import com.rwtema.extrautils2.api.machine.Machine;
import com.rwtema.extrautils2.api.machine.MachineRegistry;
import com.rwtema.extrautils2.api.machine.XUMachineGenerators;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.extrautils2.Generator")
@ModOnly("extrautils2")
@ZenRegister
public class Generator {
    private static final String[] GENERATOR_NAMES = {
            "extrautils2:generator", "extrautils2:generator_survival", "extrautils2:generator_culinary",
            "extrautils2:generator_potion", "extrautils2:generator_tnt", "extrautils2:generator_lava",
            "extrautils2:generator_pink", "extrautils2:generator_netherstar", "extrautils2:generator_ender",
            "extrautils2:generator_redstone", "extrautils2:generator_overclock", "extrautils2:generator_dragonsbreath",
            "extrautils2:generator_ice", "extrautils2:generator_death", "extrautils2:generator_enchant",
            "extrautils2:generator_slime"
    };

    @ZenMethod
    public static void removeByGenerator(String name) {
        ModTweaker.LATE_REMOVALS.add(new Action(name, null, null, true));
    }

    @ZenMethod
    public static void remove(String name, IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Action(name, InputHelper.toStack(input), null, false));
    }

    @ZenMethod
    public static void remove(String name, ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Action(name, null, InputHelper.toFluid(input), false));
    }

    @ZenMethod
    public static void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new Action(null, null, null, true));
    }

    private static class Action implements crafttweaker.IAction {
        private final String name;
        private final ItemStack item;
        private final FluidStack fluid;
        private final boolean all;

        Action(String name, ItemStack item, FluidStack fluid, boolean all) {
            this.name = name;
            this.item = item;
            this.fluid = fluid;
            this.all = all;
        }

        @Override
        public void apply() {
            if (all && name == null) {
                for (String generator : GENERATOR_NAMES) {
                    Machine machine = MachineRegistry.getMachine(generator);
                    if (machine != null) clear(machine);
                }
                return;
            }
            Machine machine = MachineRegistry.getMachine(name);
            if (machine == null) return;
            if (all) clear(machine); else removeMatching(machine);
        }

        private void clear(Machine machine) {
            List<IMachineRecipe> recipes = new ArrayList<>();
            for (IMachineRecipe recipe : machine.recipes_registry) recipes.add(recipe);
            recipes.forEach(machine.recipes_registry::removeRecipe);
        }

        private void removeMatching(Machine machine) {
            List<IMachineRecipe> recipes = new ArrayList<>();
            for (IMachineRecipe recipe : machine.recipes_registry) {
                boolean match = recipe.getJEIInputItemExamples().stream()
                        .flatMap(x -> item == null ? x.getValue().get(XUMachineGenerators.INPUT_FLUID).stream()
                                : x.getKey().get(XUMachineGenerators.INPUT_ITEM).stream())
                        .anyMatch(x -> item == null ? fluid.isFluidEqual((FluidStack) x) : item.isItemEqual((ItemStack) x));
                if (match) recipes.add(recipe);
            }
            recipes.forEach(machine.recipes_registry::removeRecipe);
        }

        @Override
        public String describe() { return "Removing Extra Utilities 2 generator recipes"; }
    }
}
