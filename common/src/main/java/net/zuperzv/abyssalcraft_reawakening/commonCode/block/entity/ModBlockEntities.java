package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.*;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModBlockEntities {
    private ModBlockEntities() {}

    public static void load() {}

    public static final RegistryHandle<BlockEntityType<StoneRitualAltarBlockEntity>> STONE_RITUAL_ALTAR_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "stone_ritual_altar_be",
                    StoneRitualAltarBlockEntity::new,
                    ModBlocks.STONE_RITUAL_ALTAR.block()::get
            );

    public static final RegistryHandle<BlockEntityType<StoneRitualPedestalBlockEntity>> STONE_RITUAL_PEDESTAL_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "stone_ritual_pedestal_be",
                    StoneRitualPedestalBlockEntity::new,
                    ModBlocks.STONE_RITUAL_PEDESTAL.block()::get
            );

    public static final RegistryHandle<BlockEntityType<EssenceBoilerBlockEntity>> ESSENCE_BOILER_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "essence_boiler_be",
                    EssenceBoilerBlockEntity::new,
                    ModBlocks.ESSENCE_BOILER.block()::get
            );

    public static final RegistryHandle<BlockEntityType<BoilerTipBlockEntity>> BOILER_TIP_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "boiler_tip_be",
                    BoilerTipBlockEntity::new,
                    ModBlocks.BOILER_TIP.block()::get
            );

    public static final RegistryHandle<BlockEntityType<CrystalGrowthBlockEntity>> CRYSTAL_GROWTH_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "crystal_growth_chamber_be",
                    CrystalGrowthBlockEntity::new,
                    ModBlocks.CRYSTAL_GROWTH_CHAMBER.block()::get
            );

    public static final RegistryHandle<BlockEntityType<PotionFluidBlockEntity>> POTION_FLUID_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "potion_fluid_be",
                    PotionFluidBlockEntity::new,
                    ModFluids.POTION_BLOCK::get
            );

    /*
    public static final RegistryHandle<BlockEntityType<StoneRitualPedestalBlockEntity>> PE_STATUE_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "pe_statue_pe",
                    StoneRitualPedestalBlockEntity::new,
                    ModBlocks..block()::get
            );
     */

    public static final RegistryHandle<BlockEntityType<ModShelfBlockEntity>> MOD_SHELF_BE =
            Services.REGISTRY.registerBlockEntityType(
                    "mod_shelf",
                    ModShelfBlockEntity::new,
                    ModBlocks.WITHERWOOD_SHELF.block()::get
            );

    public static final RegistryHandle<BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
            Services.REGISTRY.registerBlockEntityType(
                    "mod_sign",
                    ModSignBlockEntity::new,
                    ModBlocks.WITHERWOOD_SIGN::get,
                    ModBlocks.WITHERWOOD_WALL_SIGN::get
            );

    public static final RegistryHandle<BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN =
            Services.REGISTRY.registerBlockEntityType(
                    "mod_hanging_sign",
                    ModHangingSignBlockEntity::new,
                    ModBlocks.WITHERWOOD_HANGING_SIGN::get,
                    ModBlocks.WITHERWOOD_WALL_HANGING_SIGN::get
            );
}
