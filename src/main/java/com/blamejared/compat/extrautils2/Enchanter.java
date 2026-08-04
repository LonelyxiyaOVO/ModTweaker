package com.blamejared.compat.extrautils2;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.rwtema.extrautils2.api.machine.IMachineRecipe;
import com.rwtema.extrautils2.api.machine.MachineSlotFluid;
import com.rwtema.extrautils2.api.machine.MachineSlotItem;
import com.rwtema.extrautils2.api.machine.XUMachineEnchanter;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ZenClass("mods.extrautils2.Enchanter")
@ModOnly("extrautils2")
@ZenRegister
public class Enchanter {

    @ZenMethod
    public static void add(IItemStack output, IIngredient input, int lapis, int energy, int time, @Optional String enchantName) {
        ModTweaker.LATE_ADDITIONS.add(new Add(output, input, lapis, energy, time, enchantName));
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
        private final int lapis, energy, time;
        private final String enchantName;

        private Add(IItemStack output, IIngredient input, int lapis, int energy, int time, String enchantName) {
            super("ExtraUtils2 Enchanter");
            this.output = output;
            this.input = input;
            this.lapis = lapis;
            this.energy = energy;
            this.time = time;
            this.enchantName = enchantName == null ? "" : enchantName;
        }

        @Override
        public void apply() {
            List<ItemStack> inputs = input.getItems().stream().map(InputHelper::toStack).collect(Collectors.toList());
            XUMachineEnchanter.addRecipe(inputs, lapis, InputHelper.toStack(output), energy, time, enchantName);
        }

        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }

    private static class Remove extends BaseAction {
        private final IIngredient input;

        private Remove(IIngredient input) {
            super("ExtraUtils2 Enchanter");
            this.input = input;
        }

        @Override
        public void apply() {
            List<IMachineRecipe> recipes = findRecipes();
            recipes.forEach(XUMachineEnchanter.INSTANCE.recipes_registry::removeRecipe);
        }

        private List<IMachineRecipe> findRecipes() {
            List<IMachineRecipe> recipes = new ArrayList<>();
            for(IMachineRecipe recipe : XUMachineEnchanter.INSTANCE.recipes_registry) {
                for(Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>> examples : recipe.getJEIInputItemExamples()) {
                    List<ItemStack> inputs = examples.getKey().get(XUMachineEnchanter.INPUT);
                    if(inputs != null && inputs.stream().anyMatch(stack -> StackHelper.matches(input, InputHelper.toIItemStack(stack)))) {
                        recipes.add(recipe);
                        break;
                    }
                }
            }
            return recipes;
        }

        @Override
        protected String getRecipeInfo() {
            return input.toString();
        }
    }

    private static class RemoveAll extends BaseAction {
        private RemoveAll() {
            super("ExtraUtils2 Enchanter");
        }

        @Override
        public void apply() {
            List<IMachineRecipe> recipes = new ArrayList<>();
            XUMachineEnchanter.INSTANCE.recipes_registry.forEach(recipes::add);
            recipes.forEach(XUMachineEnchanter.INSTANCE.recipes_registry::removeRecipe);
        }

        @Override
        protected String getRecipeInfo() {
            return "all recipes";
        }
    }
}
