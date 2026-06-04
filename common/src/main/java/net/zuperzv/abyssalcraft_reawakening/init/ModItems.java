package net.zuperzv.abyssalcraft_reawakening.init;

import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.Function;

public class ModItems {
    private ModItems() {}

    public static void load() {}

    public static final RegistryHandle<Item> ABYSSALNITE_INGOT = Services.REGISTRY.registerItem("abyssalnite_ingot",
            properties -> new Item(properties));
}
