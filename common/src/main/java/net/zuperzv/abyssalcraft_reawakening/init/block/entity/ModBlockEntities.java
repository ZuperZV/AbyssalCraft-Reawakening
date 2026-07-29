package net.zuperzv.abyssalcraft_reawakening.init.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualAltarBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualPedestalBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModBlockEntities {
    private ModBlockEntities() {}

    public static void load() {}

    public static final RegistryHandle<BlockEntityType<StoneRitualAltarBlockEntity>> STONE_RITUAL_ALTAR_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "stone_ritual_altar_be",
                    StoneRitualAltarBlockEntity::new,
                    ModBlocks.STONE_RITUAL_ALTAR.block()
            );

    public static final RegistryHandle<BlockEntityType<StoneRitualPedestalBlockEntity>> STONE_RITUAL_PEDESTAL_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "stone_ritual_pedestal_be",
                    StoneRitualPedestalBlockEntity::new,
                    ModBlocks.STONE_RITUAL_PEDESTAL.block()
            );
}