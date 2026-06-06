package net.zuperzv.abyssalcraft_reawakening.init;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.BlockWithItemRegistryHandle;

public final class ModBlocks {
    private ModBlocks() {}

    public static void load() {}

    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> RAW_ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("raw_abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    // just for testing
    public static final BlockWithItemRegistryHandle<Block> REFINED_CORALIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("refined_coralium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> DREADIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("dreadium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> ETHAXIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("ethaxium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> ETHAXIUM_ORE = Services.REGISTRY.registerBlockWithItem("ethaxium_ore",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));


    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_OVERWORLD_ORE = createOreBlock("abyssalnite_overworld_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_DEEPSLATE_ORE = createOreBlock("abyssalnite_deepslate_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_NETHER_ORE = createOreBlock("abyssalnite_nether_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_END_ORE = createOreBlock("abyssalnite_end_ore");

    private static BlockWithItemRegistryHandle<Block> createOreBlock(String name) {
        return Services.REGISTRY.registerBlockWithItem(name,
                properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties.strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
    }
}
