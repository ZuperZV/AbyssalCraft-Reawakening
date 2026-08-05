package net.zuperzv.abyssalcraft_reawakening.init.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class ModBlockTags {
    private ModBlockTags() {
    }

    public static final TagKey<Block> ABYSSALCRAFT_ORES = create("abyssalcraft_ores");

    public static final TagKey<Block> NEEDS_ABYSSALNITE_TOOL =
            create("needs_abyssalnite_tool");

    public static final TagKey<Block> INCORRECT_FOR_ABYSSALNITE_TOOL =
            create("incorrect_for_abyssalnite_tool");

    public static final TagKey<Block> NEEDS_REFINED_CORALIUM_TOOL =
            create("needs_refined_coralium_tool");

    public static final TagKey<Block> INCORRECT_FOR_REFINED_CORALIUM_TOOL =
            create("incorrect_for_refined_coralium_tool");

    public static final TagKey<Block> NEEDS_DREADIUM_TOOL =
            create("needs_dreadium_tool");

    public static final TagKey<Block> INCORRECT_FOR_DREADIUM_TOOL =
            create("incorrect_for_dreadium_tool");

    public static final TagKey<Block> NEEDS_ETHAXIUM_TOOL =
            create("needs_ethaxium_tool");

    public static final TagKey<Block> INCORRECT_FOR_ETHAXIUM_TOOL =
            create("incorrect_for_ethaxium_tool");

    public static final TagKey<Block> ABYSSAL_STONE_ORE_REPLACEABLES =
            create("abyssal_stone_ore_replaceables");

    public static final TagKey<Block> ABYSSAL_DEEPSLATE_ORE_REPLACEABLES =
            create("abyssal_deepslate_ore_replaceables");

    public static final TagKey<Block> ABYSSAL_WAISTLAND_SURFACES =
            create("abyssal_waistland_surfaces");

    public static final TagKey<Block> WASTITE_SPIKE_REPLACEABLE =
            create("wastite_spike_replaceable");

    public static final TagKey<Block> WITHERWOOD_LOGS =
            create("witherwood_logs");

    public static final TagKey<Block> RED_WOOL_REPLACEABLE =
            create("red_wool_replaceable");

    public static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Constants.id(name));
    }
}