package com.blamejared.compat.extrautils2;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.rwtema.extrautils2.api.machine.IMachineRecipe;
import com.rwtema.extrautils2.api.machine.MachineSlotFluid;
import com.rwtema.extrautils2.api.machine.MachineSlotItem;
import com.rwtema.extrautils2.api.machine.XUMachineFurnace;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ZenClass("mods.extrautils2.Furnace")
@ModOnly("extrautils2")
@ZenRegister
public class Furnace {

    @ZenMethod
    public static void add(IItemStack output, IIngredient input) {
        ModTweaker.LATE_ADDITIONS.add(new Add(output, input));
    }

    @ZenMethod
    public static void remove(IIngredient input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input));
    }

    @ZenMethod
    public static void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new RemoveAll());
    }

    private static class Add extends BaseAction {
        private final IItemStack output;
        private final IIngredient input;

        private Add(IItemStack output, IIngredient input) {
            super("ExtraUtils2 Furnace");
            this.output = output;
            this.input = input;
        }

        @Override
        public void apply() {
            for(IItemStack stack : input.getItems()) {
                XUMachineFurnace.addRecipe(InputHelper.toStack(stack), InputHelper.toStack(output));
            }
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient input;

        private Remove(IIngredient input) {
            super("ExtraUtils2 Furnace");
            this.input = input;
        }

        @Override
        public void apply() {
            List<IMachineRecipe> recipes = new ArrayList<>();
            for(IMachineRecipe recipe : XUMachineFurnace.INSTANCE.recipes_registry) {
                for(Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>> examples : recipe.getJEIInputItemExamples()) {
                    List<ItemStack> inputs = examples.getKey().get(XUMachineFurnace.INPUT);
                    if(inputs != null && inputs.stream().anyMatch(stack -> StackHelper.matches(input, InputHelper.toIItemStack(stack)))) {
                        recipes.add(recipe);
                        break;
                    }
                }
            }
            recipes.forEach(XUMachineFurnace.INSTANCE.recipes_registry::removeRecipe);
        }

        @Override
        protected String getRecipeInfo() {
            return input.toString();
        }
    }

    private static class RemoveAll extends BaseAction {
        private RemoveAll() {
            super("ExtraUtils2 Furnace");
        }

        @Override
        public void apply() {
            List<IMachineRecipe> recipes = new ArrayList<>();
            XUMachineFurnace.INSTANCE.recipes_registry.forEach(recipes::add);
            recipes.forEach(XUMachineFurnace.INSTANCE.recipes_registry::removeRecipe);
        }

        @Override
        protected String getRecipeInfo() {
            return "all recipes";
        }
    }
}
