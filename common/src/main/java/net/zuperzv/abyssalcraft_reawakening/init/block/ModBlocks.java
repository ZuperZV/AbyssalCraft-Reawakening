package net.zuperzv.abyssalcraft_reawakening.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.*;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.tree.ModTreeGrowers;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.BlockWithItemRegistryHandle;
import net.zuperzv.abyssalcraft_reawakening.services.util.ModBlockSetTypes;
import net.zuperzv.abyssalcraft_reawakening.services.util.ModWoodTypes;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

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
    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_STONE = Services.REGISTRY.registerBlockWithItem("abyssal_stone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_COBBLESTONE = Services.REGISTRY.registerBlockWithItem("abyssal_cobblestone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 5.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_SAND = Services.REGISTRY.registerBlockWithItem("abyssal_sand",
            properties -> new SandBlock(new ColorRGBA(14406560), properties.instrument(NoteBlockInstrument.SNARE).strength(0.5F, 4.0F).sound(SoundType.SAND)));

    public static final BlockWithItemRegistryHandle<Block> FUSED_ABYSSAL_SAND = Services.REGISTRY.registerBlockWithItem("fused_abyssal_sand",
            properties -> new SandBlock(new ColorRGBA(14406560), properties.instrument(NoteBlockInstrument.SNARE).strength(0.5F, 4.0F).sound(SoundType.SAND)));

    public static final BlockWithItemRegistryHandle<Block> CORRUPTED_SOIL = Services.REGISTRY.registerBlockWithItem("corrupted_soil",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLUE).strength(3.0F, 4.0F).sound(SoundType.MUD)));

    public static final BlockWithItemRegistryHandle<Block> WASTITE = Services.REGISTRY.registerBlockWithItem("wastite",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(8.0F, 7.0F).sound(SoundType.DEEPSLATE)));

    public static final BlockWithItemRegistryHandle<Block> STARITE = Services.REGISTRY.registerBlockWithItem("starite",
            properties -> new RotatedPillarBlock(properties.mapColor(MapColor.GLOW_LICHEN).strength(0.3F).lightLevel(statex -> 15).sound(SoundType.FROGLIGHT)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_WASTELAND_ACTIVATOR = Services.REGISTRY.registerBlockWithItem("abyssal_wasteland_activator",
            properties -> new PortalActivatorBlock(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(9.0F, 13.0F).sound(SoundType.VAULT)));

    public static final BlockWithItemRegistryHandle<Block> CORALIUM_TENDRILS = Services.REGISTRY.registerBlockWithItem("coralium_tendrils",
            properties -> new ModPlantBlock(ParticleTypes.FIREFLY, 0.8f, properties.mapColor(MapColor.PLANT).ignitedByLava().lightLevel((statex) -> 2).noCollision().noOcclusion().instabreak().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));

    public static final BlockWithItemRegistryHandle<Block> LUMINOUS_THISTLE = Services.REGISTRY.registerBlockWithItem("luminous_thistle",
            properties -> new ModPlantBlock(ParticleTypes.OMINOUS_SPAWNING, 0.9f, MobEffects.NAUSEA, true, properties.mapColor(MapColor.PLANT).ignitedByLava().lightLevel((statex) -> 2).noCollision().noOcclusion().instabreak().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));

    public static final BlockWithItemRegistryHandle<Block> WASTELANDS_THORN = Services.REGISTRY.registerBlockWithItem("wastelands_thorn",
            properties -> new ModPlantBlock(ParticleTypes.ASH, 0.95f, MobEffects.INSTANT_DAMAGE, true, properties.mapColor(MapColor.PLANT).ignitedByLava().lightLevel((statex) -> 2).noCollision().noOcclusion().instabreak().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));


        //Abyssal Witherwood Tree
    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_LOG = Services.REGISTRY.registerBlockWithItem("witherwood_log",
            properties -> new RotatedPillarBlock(properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2f).ignitedByLava()));
    
    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_WOOD = Services.REGISTRY.registerBlockWithItem("witherwood_wood",
            properties -> new RotatedPillarBlock(properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2f).ignitedByLava()));
    
    public static final BlockWithItemRegistryHandle<Block> STRIPPED_WITHERWOOD_LOG = Services.REGISTRY.registerBlockWithItem("stripped_witherwood_log",
            properties -> new RotatedPillarBlock(properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2f).ignitedByLava()));
    
    public static final BlockWithItemRegistryHandle<Block> STRIPPED_WITHERWOOD_WOOD = Services.REGISTRY.registerBlockWithItem("stripped_witherwood_wood",
            properties -> new RotatedPillarBlock(properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2f).ignitedByLava()));
    
    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_SHELF = Services.REGISTRY.registerBlockWithItem("witherwood_shelf",
            properties -> new ModShelfBlock(properties.mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).sound(SoundType.SHELF)
                    .ignitedByLava().strength(2.0F, 3.0F)));
    
    public static final RegistryHandle<Block> WITHERWOOD_SIGN = Services.REGISTRY.registerBlock("witherwood_sign",
            properties -> new ModStandingSignBlock(ModWoodTypes.WITHERWOOD, properties.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                    .noCollision().strength(1.0F).ignitedByLava()));
    
    public static final RegistryHandle<Block> WITHERWOOD_WALL_SIGN = Services.REGISTRY.registerBlock("witherwood_wall_sign",
            properties -> new ModWallSignBlock(ModWoodTypes.WITHERWOOD, properties.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                    .noCollision().strength(1.0F).ignitedByLava()));
    
    public static final RegistryHandle<Block> WITHERWOOD_HANGING_SIGN = Services.REGISTRY.registerBlock("witherwood_hanging_sign",
            properties -> new ModHangingSignBlock(ModWoodTypes.WITHERWOOD, properties.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                    .noCollision().strength(1.0F).ignitedByLava()));
    
    public static final RegistryHandle<Block> WITHERWOOD_WALL_HANGING_SIGN = Services.REGISTRY.registerBlock("witherwood_wall_hanging_sign",
            properties -> new ModWallHangingSignBlock(ModWoodTypes.WITHERWOOD, properties.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                    .noCollision().strength(1.0F).ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_PRESSURE_PLATE = Services.REGISTRY.registerBlockWithItem("witherwood_pressure_plate",
            properties -> new ModPressurePlateBlock(ModBlockSetTypes.WITHERWOOD, properties.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                    .noCollision().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_TRAPDOOR = Services.REGISTRY.registerBlockWithItem("witherwood_trapdoor",
            properties -> new ModTrapDoorBlock(ModBlockSetTypes.WITHERWOOD, properties.mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                    .strength(0.5F).ignitedByLava().isValidSpawn(ModBlocks::never).noOcclusion()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_PLANKS = Services.REGISTRY.registerBlockWithItem("witherwood_planks",
            properties -> new Block(properties.sound(SoundType.WOOD).strength(2f).ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_STAIRS = Services.REGISTRY.registerBlockWithItem("witherwood_stairs",
                    properties -> new ModStairBlock(WITHERWOOD_PLANKS.block().get().defaultBlockState(), properties.sound(SoundType.WOOD).strength(2F).ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_SLAB = Services.REGISTRY.registerBlockWithItem("witherwood_slab",
            properties -> new SlabBlock(properties.sound(SoundType.WOOD).strength(2F).ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_FENCE = Services.REGISTRY.registerBlockWithItem("witherwood_fence",
            properties -> new FenceBlock(properties.sound(SoundType.WOOD).strength(2F).ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_FENCE_GATE = Services.REGISTRY.registerBlockWithItem("witherwood_fence_gate",
            properties -> new FenceGateBlock(ModWoodTypes.WITHERWOOD, properties.sound(SoundType.WOOD).strength(2F).ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_BUTTON = Services.REGISTRY.registerBlockWithItem("witherwood_button",
            properties -> new ModButtonBlock(ModBlockSetTypes.WITHERWOOD, 30, properties.sound(SoundType.WOOD)
                    .noCollision().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_DOOR = Services.REGISTRY.registerBlockWithItem("witherwood_door",
            properties -> new ModDoorBlock(ModBlockSetTypes.WITHERWOOD, properties.sound(SoundType.WOOD).strength(3F).noOcclusion().ignitedByLava()));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_LEAVES = Services.REGISTRY.registerBlockWithItem("witherwood_leaves",properties -> new UntintedParticleLeavesBlock(0f,
            ParticleTypes.PALE_OAK_LEAVES, properties.mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.CHERRY_LEAVES).noOcclusion().isValidSpawn(ModBlocks::ocelotOrParrot)
            .isSuffocating((state, level, pos) -> false).isViewBlocking((state, level, pos) -> false).ignitedByLava().pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false)));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_LEAF_LITTER = Services.REGISTRY.registerBlockWithItem("witherwood_leaf_litter",
            properties -> new LeafLitterBlock(properties.mapColor(MapColor.COLOR_LIGHT_BLUE).replaceable().noCollision().sound(SoundType.LEAF_LITTER).pushReaction(PushReaction.DESTROY)));

    public static final BlockWithItemRegistryHandle<Block> WITHERWOOD_SAPLING = Services.REGISTRY.registerBlockWithItem("witherwood_sapling",
            properties -> new ModSaplingBlock(ModTreeGrowers.WITHERWOOD, properties.mapColor(MapColor.PLANT).noCollision()
                    .randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));

    public static final BlockWithItemRegistryHandle<Block> POTTED_WITHERWOOD_SAPLING = Services.REGISTRY.registerBlockWithItem("potted_witherwood_sapling",
            properties -> new FlowerPotBlock(WITHERWOOD_SAPLING.block().get(), properties.noOcclusion().instabreak().pushReaction(PushReaction.DESTROY)));



    public static final BlockWithItemRegistryHandle<Block> ABYSSAL_WASTELAND_PORTAL_BLOCK = Services.REGISTRY.registerBlockWithItem("abyssal_wasteland_portal_block",
            properties -> new AbyssalWastelandPortalBlock(properties.noCollision().randomTicks().strength(-1.0F).sound(SoundType.GLASS).lightLevel((statex) -> 11).pushReaction(PushReaction.BLOCK)));


    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> RAW_ABYSSALNITE_BLOCK = Services.REGISTRY.registerBlockWithItem("raw_abyssalnite_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_ORE = createOreBlock("abyssalnite_ore");

    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_OVERWORLD_ORE = createOreBlock("abyssalnite_overworld_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_DEEPSLATE_ORE = createOreBlock("abyssalnite_deepslate_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_NETHER_ORE = createOreBlock("abyssalnite_nether_ore");
    public static final BlockWithItemRegistryHandle<Block> ABYSSALNITE_END_ORE = createOreBlock("abyssalnite_end_ore");

    //Coralium

    public static final BlockWithItemRegistryHandle<Block> CORALIUM_STONE = Services.REGISTRY.registerBlockWithItem("coralium_stone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(5.0F, 4.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> CORALIUM_COBBLESTONE = Services.REGISTRY.registerBlockWithItem("coralium_cobblestone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 5.0F).sound(SoundType.STONE)));

    public static final BlockWithItemRegistryHandle<Block> REFINED_CORALIUM_BLOCK = Services.REGISTRY.registerBlockWithItem("refined_coralium_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0F, 7.0F).sound(SoundType.METAL)));

    public static final BlockWithItemRegistryHandle<Block> CORALIUM_BRICKS = Services.REGISTRY.registerBlockWithItem("coralium_bricks",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(6.0F, 8.0F).sound(SoundType.STONE)));

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

    private static Boolean ocelotOrParrot(BlockState state, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }

    private static Boolean never(BlockState state, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }
}
