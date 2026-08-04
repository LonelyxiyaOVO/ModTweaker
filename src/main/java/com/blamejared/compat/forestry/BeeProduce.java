package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import forestry.api.apiculture.IAlleleBeeSpecies;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ZenClass("mods.forestry.BeeProduce")
@ModOnly("forestry")
@ZenRegister
public class BeeProduce {

    @ZenMethod
    public static void add(String species, IItemStack output, float chance) {
        add(species, output, chance, false);
    }

    @ZenMethod
    public static void add(String species, IItemStack output, float chance, boolean specialty) {
        ModTweaker.LATE_ADDITIONS.add(new Add(species, InputHelper.toStack(output), chance, specialty));
    }

    @ZenMethod
    public static void removeProduct(String species, IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(species, output, false));
    }

    @ZenMethod
    public static void removeSpecialty(String species, IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(species, output, true));
    }

    @ZenMethod
    public static void removeAll(String species) {
        ModTweaker.LATE_REMOVALS.add(new Clear(species));
    }

    @ZenMethod
    public static void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Forestry Bee Produce") {
            @Override
            public void apply() {
                for (IAlleleBeeSpecies species : speciesList()) {
                    species.getProductChances().clear();
                    species.getSpecialtyChances().clear();
                }
            }

            @Override
            protected String getRecipeInfo() {
                return "all";
            }
        });
    }

    private static List<IAlleleBeeSpecies> speciesList() {
        List<IAlleleBeeSpecies> result = new ArrayList<>();
        for (forestry.api.genetics.IAllele allele : forestry.api.genetics.AlleleManager.alleleRegistry.getRegisteredAlleles().values()) {
            if (allele instanceof IAlleleBeeSpecies) result.add((IAlleleBeeSpecies) allele);
        }
        return result;
    }

    private static class Add extends BaseAction {
        private final String species;
        private final ItemStack output;
        private final float chance;
        private final boolean specialty;

        private Add(String species, ItemStack output, float chance, boolean specialty) {
            super("Forestry Bee Produce");
            this.species = species;
            this.output = output;
            this.chance = chance;
            this.specialty = specialty;
        }

        @Override
        public void apply() {
            IAlleleBeeSpecies bee = BeeMutations.species(species);
            if (bee == null) return;
            (specialty ? bee.getSpecialtyChances() : bee.getProductChances()).put(output, chance);
        }

        @Override
        protected String getRecipeInfo() {
            return species + " -> " + output.getDisplayName();
        }
    }

    private static class Remove extends BaseAction {
        private final String species;
        private final IIngredient output;
        private final boolean specialty;

        private Remove(String species, IIngredient output, boolean specialty) {
            super("Forestry Bee Produce");
            this.species = species;
            this.output = output;
            this.specialty = specialty;
        }

        @Override
        public void apply() {
            IAlleleBeeSpecies bee = BeeMutations.species(species);
            if (bee == null) return;
            Map<ItemStack, Float> products = specialty ? bee.getSpecialtyChances() : bee.getProductChances();
            products.entrySet().removeIf(entry -> output.matches(InputHelper.toIItemStack(entry.getKey())));
        }

        @Override
        protected String getRecipeInfo() {
            return species + " -> " + output;
        }
    }

    private static class Clear extends BaseAction {
        private final String species;

        private Clear(String species) {
            super("Forestry Bee Produce");
            this.species = species;
        }

        @Override
        public void apply() {
            IAlleleBeeSpecies bee = BeeMutations.species(species);
            if (bee != null) {
                bee.getProductChances().clear();
                bee.getSpecialtyChances().clear();
            }
        }

        @Override
        protected String getRecipeInfo() {
            return species;
        }
    }
}
