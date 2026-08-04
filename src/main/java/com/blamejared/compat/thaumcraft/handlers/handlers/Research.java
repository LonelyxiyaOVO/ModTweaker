package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.ThaumCraft;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.research.ScanBlock;
import thaumcraft.api.research.ScanItem;
import thaumcraft.api.research.ScanningManager;

import java.util.Arrays;
/** CRT access to Thaumcraft research entries and their parent connections. */
@ZenClass("mods.thaumcraft.Research")
@ZenRegister
@ModOnly("thaumcraft")
public class Research {

    @ZenMethod
    public static void addCategory(String key, String researchKey, CTAspectStack[] formula,
                                   String icon, String background) {
        addCategory(key, researchKey, formula, icon, background, null);
    }

    @ZenMethod
    public static void addCategory(String key, String researchKey, CTAspectStack[] formula,
                                   String icon, String background, String background2) {
        ModTweaker.LATE_ADDITIONS.add(new AddCategory(key, researchKey, ThaumCraft.getAspects(formula),
                new ResourceLocation(icon), new ResourceLocation(background),
                background2 == null ? null : new ResourceLocation(background2)));
    }

    @ZenMethod
    public static void removeCategory(String key) {
        ModTweaker.LATE_REMOVALS.add(new CategoryAction(key, false));
    }

    @ZenMethod
    public static void removeAllCategories() {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Thaumcraft Research Categories") {
            @Override
            public void apply() {
                ResearchCategories.researchCategories.clear();
            }

            @Override
            protected String getRecipeInfo() {
                return "all";
            }
        });
    }

    @ZenMethod
    public static void addResearchLocation(String location) {
        ModTweaker.LATE_ADDITIONS.add(new ResearchLocation(new ResourceLocation(location)));
    }

    @ZenMethod
    public static void addResearchLocation(String mod, String location) {
        ModTweaker.LATE_ADDITIONS.add(new ResearchLocation(new ResourceLocation(mod, location)));
    }

    @ZenMethod
    public static void addScannable(String researchKey, IItemStack item) {
        ModTweaker.LATE_ADDITIONS.add(new AddScannable(new ScanItem(researchKey, com.blamejared.mtlib.helpers.InputHelper.toStack(item))));
    }

    @ZenMethod
    public static void addScannable(IBlock block) {
        ModTweaker.LATE_ADDITIONS.add(new AddScannable(new ScanBlock(CraftTweakerMC.getBlock(block))));
    }

    @ZenMethod
    public static void addScannable(String researchKey, IBlock block) {
        ModTweaker.LATE_ADDITIONS.add(new AddScannable(new ScanBlock(researchKey, CraftTweakerMC.getBlock(block))));
    }

    @ZenMethod
    public static void addNode(String category, String key, String name, int column, int row) {
        addNode(category, key, name, column, row, new String[0]);
    }

    @ZenMethod
    public static void addNode(String category, String key, String name, int column, int row,
                               String[] parents) {
        ModTweaker.LATE_ADDITIONS.add(new AddNode(category, key, name, column, row, parents));
    }

    @ZenMethod
    public static void removeNode(String category, String key) {
        ModTweaker.LATE_REMOVALS.add(new RemoveNode(category, key));
    }

    @ZenMethod
    public static void removeAllNodes(String category) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Thaumcraft Research") {
            @Override
            public void apply() {
                ResearchCategory researchCategory = category(category);
                if (researchCategory != null) researchCategory.research.clear();
            }

            @Override
            protected String getRecipeInfo() {
                return category;
            }
        });
    }

    /** Adds a parent connection to the child node. */
    @ZenMethod
    public static void connectNodes(String category, String parent, String child) {
        ModTweaker.LATE_ADDITIONS.add(new ConnectNodes(category, parent, child, true));
    }

    /** Removes a parent connection from the child node. */
    @ZenMethod
    public static void disconnectNodes(String category, String parent, String child) {
        ModTweaker.LATE_REMOVALS.add(new ConnectNodes(category, parent, child, false));
    }

    private static ResearchCategory category(String key) {
        ResearchCategory result = ResearchCategories.researchCategories.get(key);
        if (result == null) CraftTweakerAPI.logError("Unknown Thaumcraft research category: " + key);
        return result;
    }

    private static ResearchEntry node(String category, String key) {
        ResearchCategory result = category(category);
        if (result == null) return null;
        ResearchEntry node = result.research.get(key);
        if (node == null) CraftTweakerAPI.logError("Unknown Thaumcraft research node: " + category + ":" + key);
        return node;
    }

    private static class AddCategory extends BaseAction {
        private final String key, researchKey;
        private final thaumcraft.api.aspects.AspectList formula;
        private final ResourceLocation icon, background, background2;

        private AddCategory(String key, String researchKey, thaumcraft.api.aspects.AspectList formula,
                            ResourceLocation icon, ResourceLocation background, ResourceLocation background2) {
            super("Thaumcraft Research Category");
            this.key = key;
            this.researchKey = researchKey;
            this.formula = formula;
            this.icon = icon;
            this.background = background;
            this.background2 = background2;
        }

        @Override
        public void apply() {
            ResearchCategories.researchCategories.put(key, background2 == null
                    ? new ResearchCategory(key, researchKey, formula, icon, background)
                    : new ResearchCategory(key, researchKey, formula, icon, background, background2));
        }

        @Override
        protected String getRecipeInfo() {
            return key;
        }
    }

    private static class CategoryAction extends BaseAction {
        private final String key;
        private final boolean add;

        private CategoryAction(String key, boolean add) {
            super("Thaumcraft Research Category");
            this.key = key;
            this.add = add;
        }

        @Override
        public void apply() {
            if (!add) ResearchCategories.researchCategories.remove(key);
        }

        @Override
        protected String getRecipeInfo() {
            return key;
        }
    }

    private static class ResearchLocation extends BaseAction {
        private final ResourceLocation location;

        private ResearchLocation(ResourceLocation location) {
            super("Thaumcraft Research Location");
            this.location = location;
        }

        @Override
        public void apply() {
            ThaumcraftApi.registerResearchLocation(location);
            thaumcraft.common.lib.research.ResearchManager.parseAllResearch();
        }

        @Override
        protected String getRecipeInfo() {
            return location.toString();
        }
    }

    private static class AddScannable extends BaseAction {
        private final thaumcraft.api.research.IScanThing scanThing;

        private AddScannable(thaumcraft.api.research.IScanThing scanThing) {
            super("Thaumcraft Research Scannable");
            this.scanThing = scanThing;
        }

        @Override
        public void apply() {
            ScanningManager.addScannableThing(scanThing);
        }

        @Override
        protected String getRecipeInfo() {
            return scanThing.toString();
        }
    }

    private static class AddNode extends BaseAction {
        private final String category, key, name;
        private final int column, row;
        private final String[] parents;

        private AddNode(String category, String key, String name, int column, int row, String[] parents) {
            super("Thaumcraft Research");
            this.category = category;
            this.key = key;
            this.name = name;
            this.column = column;
            this.row = row;
            this.parents = parents == null ? new String[0] : parents.clone();
        }

        @Override
        public void apply() {
            ResearchCategory target = category(category);
            if (target == null || target.research.containsKey(key)) {
                if (target != null) CraftTweakerAPI.logError("Thaumcraft research node already exists: " + category + ":" + key);
                return;
            }
            ResearchEntry entry = new ResearchEntry();
            entry.setKey(key);
            entry.setCategory(category);
            entry.setName(name);
            entry.setDisplayColumn(column);
            entry.setDisplayRow(row);
            entry.setParents(parents);
            target.research.put(key, entry);
        }

        @Override
        protected String getRecipeInfo() {
            return category + ":" + key;
        }
    }

    private static class RemoveNode extends BaseAction {
        private final String category, key;

        private RemoveNode(String category, String key) {
            super("Thaumcraft Research");
            this.category = category;
            this.key = key;
        }

        @Override
        public void apply() {
            ResearchCategory target = category(category);
            if (target != null) target.research.remove(key);
        }

        @Override
        protected String getRecipeInfo() {
            return category + ":" + key;
        }
    }

    private static class ConnectNodes extends BaseAction {
        private final String category, parent, child;
        private final boolean connect;

        private ConnectNodes(String category, String parent, String child, boolean connect) {
            super("Thaumcraft Research");
            this.category = category;
            this.parent = parent;
            this.child = child;
            this.connect = connect;
        }

        @Override
        public void apply() {
            ResearchEntry childNode = node(category, child);
            if (childNode == null) return;
            String[] current = childNode.getParents();
            if (current == null) current = new String[0];
            if (connect) {
                for (String value : current) if (parent.equals(value)) return;
                String[] updated = Arrays.copyOf(current, current.length + 1);
                updated[current.length] = parent;
                childNode.setParents(updated);
            } else {
                childNode.setParents(Arrays.stream(current)
                        .filter(value -> !parent.equals(value))
                        .toArray(String[]::new));
            }
        }

        @Override
        protected String getRecipeInfo() {
            return parent + " -> " + category + ":" + child;
        }
    }
}
