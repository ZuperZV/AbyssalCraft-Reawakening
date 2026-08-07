package net.zuperzv.abyssalcraft_reawakening.services.util;

import net.minecraft.resources.Identifier;

public interface RegistryHandle<T> {

    Identifier id();

    T get();
}