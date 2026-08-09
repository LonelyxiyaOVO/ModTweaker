package com.blamejared.compat.botania;

import java.util.List;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

import static com.blamejared.mtlib.helpers.InputHelper.toObject;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;

public class BotaniaHelper {

    /**
     * Converts CRT ingredients to the input representation expected by
     * Botania's Rune Altar recipe. Reuse ingredients do not have a direct
     * object representation, so their first matching item is used as a
     * fallback for the native recipe object.
     */
    public static Object[] toRuneAltarInputs(IIngredient[] ingredients) {
        Object[] inputs = new Object[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            IIngredient ingredient = ingredients[i];
            if (ingredient == null) {
                inputs[i] = "";
                continue;
            }

            Object input = toObject(ingredient);
            if (input == null && isReuseIngredient(ingredient)) {
                Object internal = ingredient.getInternal();
                if (internal instanceof String) {
                    input = internal;
                }
                List<IItemStack> items = ingredient.getItems();
                if (input == null && !items.isEmpty()) {
                    input = toStack(items.get(0).withAmount(1));
                }
            }
            if (input == null) {
                throw new IllegalArgumentException(
                        "Unsupported Botania Rune Altar input: " + ingredient);
            }
            inputs[i] = input;
        }
        return inputs;
    }

    /**
     * Returns true only for a no-op new-style transformer such as
     * {@code .reuse()}. Other transformers change the item consumed by the
     * recipe and must continue to use Botania's normal consumption behavior.
     */
    public static boolean isReuseIngredient(IIngredient ingredient) {
        if (ingredient == null || !ingredient.hasNewTransformers()
                || ingredient.hasTransformers()) {
            return false;
        }

        List<IItemStack> items = ingredient.getItems();
        if (items.isEmpty()) {
            return false;
        }
        for (IItemStack stack : items) {
            IItemStack item = stack.withAmount(1);
            IItemStack transformed = ingredient.applyNewTransform(item);
            if (transformed == null || transformed.getAmount() != 1
                    || !item.matchesExact(transformed)) {
                return false;
            }
        }
        return true;
    }
    
    public static LexiconCategory findCatagory(String name) {
        List<LexiconCategory> catagories = BotaniaAPI.getAllCategories();
        for(LexiconCategory catagory : catagories) {
            if(catagory.getUnlocalizedName().equalsIgnoreCase(name))
                return catagory;
        }
        return null;
    }
    
    public static LexiconEntry findEntry(String name) {
        List<LexiconEntry> entries = BotaniaAPI.getAllEntries();
        for(LexiconEntry entry : entries) {
            if(entry.getUnlocalizedName().equalsIgnoreCase(name))
                return entry;
        }
        return null;
    }
    
    public static KnowledgeType findKnowledgeType(String name) {
        if(BotaniaAPI.knowledgeTypes.containsKey(name))
            return BotaniaAPI.knowledgeTypes.get(name);
        return null;
    }
}
