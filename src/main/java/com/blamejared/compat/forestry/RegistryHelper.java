package com.blamejared.compat.forestry;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;

import java.util.Collection;

final class RegistryHelper {
    private RegistryHelper() { }

    static void removeAll(String name, Collection<?> recipes) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction(name) {
            @Override
            public void apply() {
                recipes.clear();
            }

            @Override
            protected String getRecipeInfo() {
                return "all";
            }
        });
    }
}
