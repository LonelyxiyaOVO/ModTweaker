package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;

import java.util.Collection;
import java.util.function.Predicate;

final class RecipeRemoval {
    private RecipeRemoval() { }

    static <T> void add(String name, Collection<T> recipes, Predicate<T> predicate, String info) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction(name) {
            @Override
            public void apply() {
                recipes.removeIf(predicate);
            }

            @Override
            protected String getRecipeInfo() {
                return info;
            }
        });
    }
}
