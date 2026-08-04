package net.zuperzv.abyssalcraft_reawakening.services.util;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface RegistryHandle<T> {

    Identifier id();

    T get();

    default boolean isBound() {
        try {
            get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}