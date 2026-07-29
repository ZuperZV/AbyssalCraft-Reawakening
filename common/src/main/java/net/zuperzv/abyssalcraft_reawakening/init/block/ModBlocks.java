package net.zuperzv.abyssalcraft_reawakening.init.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.AbyssalWastelandPortalBlock;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.PortalActivatorBlock;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.StoneRitualAltarBlock;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.StoneRitualPedestalBlock;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.BlockWithItemRegistryHandle;

public final class ModBlocks {
    private ModBlocks() {}

    public static void load() {}

    public static final BlockWithItemRegistryHandle<Block> STONE_RITUAL_ALTAR = Services.REGISTRY.registerBlockWithItem("stone_ritual_altar",
            properties -> new StoneRitualAltarBlock(properties.requiresCorrectToolForDrops().strength(5.0F, 6.0F)
                    .sound(SoundType.DEEPSLATE_TILES).noOcclusion()));

    public static final BlockWithItemRegistryHandle<Block> STONE_RITUAL_PEDESTAL = Services.REGISTRY.registerBlockWithItem("stone_ritual_pedestal",
            properties -> new StoneRitualPedestalBlock(properties.requiresCorrectToolForDrops().strength(3.0F, 4.0F)
                    .sound(SoundType.TUFF_BRICKS).noOcclusion()));

    //Abyssal
    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_WASTELAND_ACTIVATOR = Services.REGISTRY.registerBlockWithItem("abyssal_wasteland_activator",
            properties -> new PortalActivatorBlock(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(9.0F, 13.0F).sound(SoundType.VAULT)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_STONE = Services.REGISTRY.registerBlockWithItem("abyssal_stone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_WASTELAND_PORTAL_BLOCK = Services.REGISTRY.registerBlockWithItem("abyssal_wasteland_portal_block",
            properties -> new AbyssalWastelandPortalBlock(properties.noCollision().randomTicks().strength(-1.0F).sound(SoundType.GLASS).lightLevel((statex) -> 11).pushReaction(PushReaction.BLOCK)));


    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> RAW_ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("raw_abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_OVERWORLD_ORE = createOreBlock("abyssalnite_overworld_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_DEEPSLATE_ORE = createOreBlock("abyssalnite_deepslate_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_NETHER_ORE = createOreBlock("abyssalnite_nether_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_END_ORE = createOreBlock("abyssalnite_end_ore");

    //Coralium
    public static final BlockWithItemRegistryHandle<Block> REFINED_CORALIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("refined_coralium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    //Dread
    public static final BlockWithItemRegistryHandle<Block> DREADIUM_STONE = Services.REGISTRY.registerBlockWithItem("dreadium_stone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> DREADIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("dreadium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    //Ethaxium
    public static final BlockWithItemRegistryHandle<Block> OMOTHOL_STONE = Services.REGISTRY.registerBlockWithItem("omothol_stone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> ETHAXIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("ethaxium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> ETHAXIUM_ORE = Services.REGISTRY.registerBlockWithItem("ethaxium_ore",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));



    private static BlockWithItemRegistryHandle<Block> createOreBlock(String name) {
        return Services.REGISTRY.registerBlockWithItem(name,
                properties -> new DropExperienceBlock(UniformInt.of(0, 2), properties.strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
    }
}
