package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IBeeMutation;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.ISpeciesRoot;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.forestry.BeeMutations")
@ModOnly("forestry")
@ZenRegister
public class BeeMutations {

    @ZenMethod
    public static void add(String output, String first, String second, int chance) {
        ModTweaker.LATE_ADDITIONS.add(new Add(output, first, second, chance));
    }

    @ZenMethod
    public static void remove(String output, String first, String second) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output, first, second));
    }

    @ZenMethod
    public static void removeByOutput(String output) {
        ModTweaker.LATE_REMOVALS.add(new RemoveOutput(output));
    }

    @ZenMethod
    public static void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Forestry Bee Mutations") {
            @Override
            public void apply() {
                BeeManager.beeRoot.getMutations(false).clear();
            }

            @Override
            protected String getRecipeInfo() {
                return "all";
            }
        });
    }

    static IAlleleBeeSpecies species(String uid) {
        if (AlleleManager.alleleRegistry == null) return null;
        if (!(AlleleManager.alleleRegistry.getAllele(uid) instanceof IAlleleBeeSpecies)) {
            CraftTweakerAPI.logError("Unknown Forestry bee species: " + uid);
            return null;
        }
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(uid);
    }

    private static IBeeMutation mutation(String output, String first, String second, int chance) {
        IAlleleBeeSpecies result = species(output);
        IAlleleBeeSpecies a = species(first);
        IAlleleBeeSpecies b = species(second);
        if (result == null || a == null || b == null) return null;
        ISpeciesRoot root = result.getRoot();
        IAllele[] template = root.getTemplate(result);
        template[EnumBeeChromosome.SPECIES.ordinal()] = result;
        return BeeManager.beeMutationFactory.createMutation(a, b, template, chance).build();
    }

    private static class Add extends BaseAction {
        private final String output, first, second;
        private final int chance;

        private Add(String output, String first, String second, int chance) {
            super("Forestry Bee Mutations");
            this.output = output;
            this.first = first;
            this.second = second;
            this.chance = chance;
        }

        @Override
        public void apply() {
            IBeeMutation mutation = mutation(output, first, second, chance);
            if (mutation != null) BeeManager.beeRoot.registerMutation(mutation);
        }

        @Override
        protected String getRecipeInfo() {
            return first + " + " + second + " -> " + output;
        }
    }

    private static class Remove extends BaseAction {
        private final String output, first, second;

        private Remove(String output, String first, String second) {
            super("Forestry Bee Mutations");
            this.output = output;
            this.first = first;
            this.second = second;
        }

        @Override
        public void apply() {
            IAlleleBeeSpecies result = species(output);
            IAlleleBeeSpecies a = species(first);
            IAlleleBeeSpecies b = species(second);
            if (result == null || a == null || b == null) return;
            List<IBeeMutation> remove = new ArrayList<>();
            for (IBeeMutation mutation : BeeManager.beeRoot.getMutations(false)) {
                if (mutation.getTemplate()[EnumBeeChromosome.SPECIES.ordinal()] == result
                        && ((mutation.getAllele0() == a && mutation.getAllele1() == b)
                        || (mutation.getAllele0() == b && mutation.getAllele1() == a))) remove.add(mutation);
            }
            BeeManager.beeRoot.getMutations(false).removeAll(remove);
        }

        @Override
        protected String getRecipeInfo() {
            return first + " + " + second + " -> " + output;
        }
    }

    private static class RemoveOutput extends BaseAction {
        private final String output;

        private RemoveOutput(String output) {
            super("Forestry Bee Mutations");
            this.output = output;
        }

        @Override
        public void apply() {
            IAlleleBeeSpecies result = species(output);
            if (result == null) return;
            List<IBeeMutation> remove = new ArrayList<>();
            for (IBeeMutation mutation : BeeManager.beeRoot.getMutations(false)) {
                if (mutation.getTemplate()[EnumBeeChromosome.SPECIES.ordinal()] == result) remove.add(mutation);
            }
            BeeManager.beeRoot.getMutations(false).removeAll(remove);
        }

        @Override
        protected String getRecipeInfo() {
            return output;
        }
    }
}
