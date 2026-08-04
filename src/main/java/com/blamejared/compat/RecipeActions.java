package com.blamejared.compat;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;

import java.util.Collection;

/** Shared actions for recipe compatibility handlers. */
public final class RecipeActions {

    private RecipeActions() {
    }

    public static void removeAll(String name, Collection<?> recipes) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction(name) {
            @Override
            public void apply() {
                recipes.clear();
            }

            @Override
            protected String getRecipeInfo() {
                return "all recipes";
            }
        });
    }

    public static void removeAll(String name, Runnable remover) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction(name) {
            @Override
            public void apply() {
                remover.run();
            }

            @Override
            protected String getRecipeInfo() {
                return "all recipes";
            }
        });
    }
}
