package com.blamejared.compat.botania.handlers;

import com.blamejared.compat.RecipeActions;

import static com.blamejared.compat.botania.BotaniaHelper.isReuseIngredient;
import static com.blamejared.compat.botania.BotaniaHelper.toRuneAltarInputs;
import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toObject;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.IItemHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.item.ModItems;

@ZenClass("mods.botania.RuneAltar")
@ModOnly("botania")
@ZenRegister
public class RuneAltar {

    private static final List<PendingReturn> PENDING_RETURNS = new LinkedList<>();
    private static boolean initialized;

    @ZenMethod
    public static void removeAll() {
        RecipeActions.removeAll("RuneAltar", BotaniaAPI.runeAltarRecipes);
    }
    
    
    protected static final String name = "Botania Rune Altar";
    
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input, int mana) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Add(new TweakerRecipe(toStack(output), mana, input)));
    }

    private static void init() {
        if (!initialized) {
            MinecraftForge.EVENT_BUS.register(RuneAltar.class);
            initialized = true;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if (world.isRemote || event.isCanceled()
                || event.getEntityPlayer().capabilities.isCreativeMode
                || event.getItemStack().getItem() != ModItems.twigWand) {
            return;
        }

        TileEntity tile = world.getTileEntity(event.getPos());
        if (!(tile instanceof TileRuneAltar)) {
            return;
        }

        TileRuneAltar altar = (TileRuneAltar) tile;
        if (altar.getTargetMana() <= 0
                || altar.getCurrentMana() < altar.getTargetMana()
                || !hasLivingrock(world, event.getPos())) {
            return;
        }

        for (RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
            if (recipe.matches(altar.getItemHandler())) {
                if (recipe instanceof TweakerRecipe) {
                    List<ItemStack> returns = ((TweakerRecipe) recipe)
                            .getReusableStacks(altar.getItemHandler());
                    if (!returns.isEmpty()) {
                        PENDING_RETURNS.add(new PendingReturn(
                                world, event.getPos(),
                                altar.getCurrentMana() - recipe.getManaUsage(), returns));
                    }
                }
                return;
            }
        }
    }

    private static boolean hasLivingrock(World world, BlockPos pos) {
        for (EntityItem entity : world.getEntitiesWithinAABB(
                EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)))) {
            if (!entity.isDead && !entity.getItem().isEmpty()
                    && entity.getItem().getItem()
                    == Item.getItemFromBlock(ModBlocks.livingrock)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Iterator<PendingReturn> iterator = PENDING_RETURNS.iterator();
        while (iterator.hasNext()) {
            PendingReturn pending = iterator.next();
            TileEntity tile = pending.world.getTileEntity(pending.pos);
            if (tile instanceof TileRuneAltar) {
                TileRuneAltar altar = (TileRuneAltar) tile;
                if (altar.isEmpty()
                        && altar.getCurrentMana() == pending.expectedMana) {
                    for (ItemStack stack : pending.stacks) {
                        pending.world.spawnEntity(new EntityItem(
                                pending.world,
                                pending.pos.getX() + 0.5,
                                pending.pos.getY() + 1.5,
                                pending.pos.getZ() + 0.5,
                                stack));
                    }
                }
            }
            iterator.remove();
        }
    }

    private static class TweakerRecipe extends RecipeRuneAltar {

        private final IIngredient[] ingredients;

        private TweakerRecipe(ItemStack output, int mana, IIngredient[] ingredients) {
            super(output, mana, toRuneAltarInputs(ingredients));
            this.ingredients = ingredients.clone();
        }

        @Override
        public boolean matches(IItemHandler inventory) {
            return matchInputs(inventory, false) != null;
        }

        private List<ItemStack> getReusableStacks(IItemHandler inventory) {
            List<ItemStack> stacks = matchInputs(inventory, true);
            return stacks == null ? Collections.emptyList() : stacks;
        }

        private List<ItemStack> matchInputs(IItemHandler inventory,
                                            boolean collectReusable) {
            List<Integer> missing = new ArrayList<>();
            for (int i = 0; i < ingredients.length; i++) {
                missing.add(i);
            }

            List<ItemStack> reusable = new ArrayList<>();
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (stack.isEmpty()) {
                    break;
                }

                int matched = -1;
                for (int i = 0; i < missing.size(); i++) {
                    IIngredient ingredient = ingredients[missing.get(i)];
                    if (ingredient.matches(toIItemStack(stack))) {
                        matched = i;
                        if (collectReusable && isReuseIngredient(ingredient)
                                && stack.getItem() != ModItems.rune) {
                            reusable.add(stack.copy());
                        }
                        break;
                    }
                }

                if (matched < 0) {
                    return null;
                }
                missing.remove(matched);
            }
            return missing.isEmpty() ? reusable : null;
        }
    }

    private static class PendingReturn {

        private final World world;
        private final BlockPos pos;
        private final int expectedMana;
        private final List<ItemStack> stacks;

        private PendingReturn(World world, BlockPos pos, int expectedMana,
                              List<ItemStack> stacks) {
            this.world = world;
            this.pos = pos;
            this.expectedMana = expectedMana;
            this.stacks = stacks;
        }
    }
    
    
    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }

    @ZenMethod
    public static void removeRecipeByInput(IIngredient[] inputs) {
        ModTweaker.LATE_REMOVALS.add(new RemoveByInput(inputs));
    }

    @ZenMethod
    public static void removeRecipeByInputs(IIngredient[] inputs) {
        removeRecipeByInput(inputs);
    }
    
    
    private static class Add extends BaseListAddition<RecipeRuneAltar> {
        
        public Add(RecipeRuneAltar recipe) {
            super(RuneAltar.name, BotaniaAPI.runeAltarRecipes, Collections.singletonList(recipe));
            
        }
        
        @Override
        public String getRecipeInfo(RecipeRuneAltar recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
    
    private static class Remove extends BaseListRemoval<RecipeRuneAltar> {
        
        final IIngredient output;
        
        public Remove(IIngredient output) {
            super(RuneAltar.name, BotaniaAPI.runeAltarRecipes);
            this.output = output;
        }
        
        @Override
        public String getRecipeInfo(RecipeRuneAltar recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
        
        @Override
        public void apply() {
            // Get list of existing recipes, matching with parameter
            List<RecipeRuneAltar> recipes = new LinkedList<>();
            
            for(RecipeRuneAltar r : BotaniaAPI.runeAltarRecipes) {
                if(r != null && r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
                    recipes.add(r);
                }
            }
            
            // Check if we found the recipes and apply the action
            if(!recipes.isEmpty()) {
                this.recipes.addAll(recipes);
                super.apply();
            } else {
                LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", RuneAltar.name, output.toString()));
            }
            CraftTweakerAPI.getLogger().logInfo(super.describe());
        }
        
        @Override
        public String describe() {
            return "Attempting to remove Rune Altar recipe for " + output.getItems();
        }
    }

    private static class RemoveByInput extends BaseListRemoval<RecipeRuneAltar> {
        private final IIngredient[] inputs;

        RemoveByInput(IIngredient[] inputs) {
            super(RuneAltar.name, BotaniaAPI.runeAltarRecipes, Collections.emptyList());
            this.inputs = inputs == null ? new IIngredient[0] : inputs;
        }

        @Override
        public void apply() {
            for (RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
                boolean matches = true;
                for (IIngredient input : inputs) {
                    Object expected = toObject(input);
                    boolean found = false;
                    for (Object actual : recipe.getInputs()) {
                        if (expected instanceof String || actual instanceof String) {
                            found |= expected.equals(actual);
                        } else if (actual instanceof ItemStack) {
                            found |= input.matches(toIItemStack((ItemStack) actual));
                        }
                    }
                    if (!found) {
                        matches = false;
                        break;
                    }
                }
                if (matches && inputs.length > 0) recipes.add(recipe);
            }
            super.apply();
        }

        @Override
        public String getRecipeInfo(RecipeRuneAltar recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}
