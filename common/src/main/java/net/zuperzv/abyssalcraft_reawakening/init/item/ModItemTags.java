package net.zuperzv.abyssalcraft_reawakening.init.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class ModItemTags {
    private ModItemTags() {
    }

    public static final TagKey<Item> ABYSSALNITE_MATERIALS = create("abyssalnite_materials");
    public static final TagKey<Item> REFINED_CORALIUM_MATERIALS = create("refined_coralium_materials");
    public static final TagKey<Item> DREADIUM_MATERIALS = create("dreadium_materials");
    public static final TagKey<Item> ETHAXIUM_MATERIALS = create("ethaxium_materials");
    public static final TagKey<Item> WITHERWOOD_LOGS = create("witherwood_logs");

    public static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Constants.id(name));
    }
}