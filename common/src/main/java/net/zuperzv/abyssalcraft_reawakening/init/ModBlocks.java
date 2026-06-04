package net.zuperzv.abyssalcraft_reawakening.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.BlockWithItemRegistryHandle;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModBlocks {
    private ModBlocks() {}

    public static void load() {}

    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
}
