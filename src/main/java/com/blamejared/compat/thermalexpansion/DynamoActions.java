package com.blamejared.compat.thermalexpansion;

import java.lang.reflect.Field;

/** Shared access for the private Thermal Expansion dynamo fuel registries. */
public final class DynamoActions {
    private DynamoActions() {
    }

    public static void clear(String managerClass, String fieldName) {
        try {
            Class<?> manager = Class.forName(managerClass);
            Field field = manager.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object registry = field.get(null);
            registry.getClass().getMethod("clear").invoke(registry);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to clear Thermal Expansion dynamo registry " + fieldName, e);
        }
    }
}
