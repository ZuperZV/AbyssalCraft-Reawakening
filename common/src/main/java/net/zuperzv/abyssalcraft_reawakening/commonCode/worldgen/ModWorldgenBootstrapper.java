package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen;

import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.TrapezoidInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.PlaceOnGroundDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.feature.MonsterRoomFeatureConfiguration;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravityFossils.FossilGenerator;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravityFossils.FossilRegistry;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravityFossils.FossilWorldgenHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.helpers.ModWorldgenPredicates;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.tree.decorator.TinyRootDecorator;

import java.util.List;

public final class ModWorldgenBootstrapper {
    private ModWorldgenBootstrapper() {
    }

    public static void bootstrapProcessorLists(BootstrapContext<StructureProcessorList> context) {
        ModProcessorLists.bootstrap(context);
    }

    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for(FossilGenerator fossil : FossilRegistry.ALL){

            FossilWorldgenHelper.register(
                    context,
                    fossil
            );
        }
        HolderGetter<PlacedFeature> placedFeatures =
                context.lookup(Registries.PLACED_FEATURE);

        context.register(
                ModWorldgen.ABYSSAL_MONSTER_ROOM,

                new ConfiguredFeature<>(
                        ModFeatures.MONSTER_ROOM.get(),
                        new MonsterRoomFeatureConfiguration(
                                List.of(
                                        Constants.id("monster_room/abyssal/monster_room_1")
                                        //Constants.id("monster_room/abyssal/monster_room_2"),
                                        //Constants.id("monster_room/abyssal/monster_room_3")
                                ),
                                5
                        )
                )
        );

        // PLANTS
        context.register(
                ModWorldgen.CORALIUM_TENDRILS,

                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.CORALIUM_TENDRILS.block().get()
                                )
                        )
                )
        );

        context.register(
                ModWorldgen.LUMINOUS_THISTLE,

                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.LUMINOUS_THISTLE.block().get()
                                )
                        )
                )
        );

        context.register(
                ModWorldgen.WASTELANDS_THORN,

                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.WASTELANDS_THORN.block().get()
                                )
                        )
                )
        );

        PlaceOnGroundDecorator sparseLeafLitter = new PlaceOnGroundDecorator(
                96, 4, 2, new WeightedStateProvider(leafLitterPatchBuilder(1, 3, ModBlocks.WITHERWOOD_LEAF_LITTER.block().get()))
        );

        context.register(ModWorldgen.WITHERWOOD_TREE, new ConfiguredFeature<>(Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(ModBlocks.WITHERWOOD_LOG.block().get()),
                                new FancyTrunkPlacer(
                                        7,
                                        4,
                                        9),

                                BlockStateProvider.simple(Blocks.AIR),
                                new BlobFoliagePlacer(
                                        UniformInt.of(0, 0),
                                        UniformInt.of(0, 0),
                                        0
                                ),

                                new TwoLayersFeatureSize(1, 0, 2))
                                .decorators(
                                        List.of(
                                                new TinyRootDecorator(
                                                        BlockStateProvider.simple(
                                                                ModBlocks.WITHERWOOD_WOOD.block().get()
                                                        ),
                                                        0.75F
                                                ),
                                                sparseLeafLitter
                                        )
                                ).build()));

        context.register(ModWorldgen.LEAVES_WITHERWOOD_TREE, new ConfiguredFeature<>(Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(

                                BlockStateProvider.simple(ModBlocks.WITHERWOOD_LOG.block().get()),
                                new FancyTrunkPlacer(
                                        7,
                                        4,
                                        9),

                                BlockStateProvider.simple(ModBlocks.WITHERWOOD_LEAVES.block().get()),
                                new BlobFoliagePlacer(
                                        UniformInt.of(2, 2),
                                        UniformInt.of(2, 2),
                                        2
                                ),

                                new TwoLayersFeatureSize(1, 0, 2))
                                .decorators(
                                        List.of(
                                                new TinyRootDecorator(
                                                        BlockStateProvider.simple(
                                                                ModBlocks.WITHERWOOD_WOOD.block().get()
                                                        ),
                                                        0.75F
                                                ),
                                                sparseLeafLitter
                                        )
                                ).build()));


        context.register(
                ModWorldgen.WASTITE_CLUSTER_BLOCK,

                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.WASTITE.block().get()
                                )
                        )
                )
        );

        Holder<PlacedFeature> WASTITE_cluster_block_placed=
                placedFeatures.getOrThrow(
                        ModWorldgen.WASTITE_CLUSTER_BLOCK_PLACED
                );

        context.register(
                ModWorldgen.WASTITE_CLUSTER,

                new ConfiguredFeature<>(
                        Feature.VEGETATION_PATCH,

                        new VegetationPatchConfiguration(
                                ModBlockTags.WASTITE_SPIKE_REPLACEABLE,

                                BlockStateProvider.simple(
                                        ModBlocks.WASTITE.block().get().defaultBlockState()
                                ),

                                WASTITE_cluster_block_placed,
                                CaveSurface.FLOOR,
                                ConstantInt.of(4),
                                0.3F,
                                27,
                                1.0F,
                                UniformInt.of(0, 2),
                                0.5F
                        )
                )
        );

        context.register(
                ModWorldgen.ABYSSAL_MUD_DISK,

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        Blocks.MUD
                                ),

                                BlockPredicate.matchesBlocks(
                                        ModBlocks.ABYSSAL_STONE.block().get(),
                                        ModBlocks.GRIMESTONE.block().get(),
                                        ModBlocks.ABYSSAL_SAND.block().get(),
                                        Blocks.MUD
                                ),

                                ConstantInt.of(5),

                                2
                        )
                )
        );

        context.register(
                ModWorldgen.SCARLET_SHALE_DISK,

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.SCARLET_SHALE.block().get()
                                ),

                                BlockPredicate.matchesBlocks(
                                        ModBlocks.ABYSSAL_STONE.block().get(),
                                        ModBlocks.GRIMESTONE.block().get(),
                                        ModBlocks.ABYSSAL_SAND.block().get(),
                                        Blocks.MUD
                                ),

                                ConstantInt.of(4),

                                4
                        )
                )
        );

        context.register(
                ModWorldgen.AZURE_WASTE_STONE_DISK,

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.AZURE_WASTE_STONE.block().get()
                                ),

                                BlockPredicate.matchesBlocks(
                                        ModBlocks.ABYSSAL_STONE.block().get(),
                                        ModBlocks.GRIMESTONE.block().get(),
                                        ModBlocks.ABYSSAL_SAND.block().get(),
                                        Blocks.MUD
                                ),

                                ConstantInt.of(6),

                                3
                        )
                )
        );

        context.register(
                ModWorldgen.ABYSSAL_SAND_DISK,

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.ABYSSAL_SAND.block().get()
                                ),

                                BlockPredicate.allOf(
                                        BlockPredicate.matchesBlocks(
                                        ModBlocks.ABYSSAL_STONE.block().get(),
                                        ModBlocks.ABYSSAL_SAND.block().get(),
                                        Blocks.MUD
                                        ),

                                        BlockPredicate.not(
                                                BlockPredicate.matchesTag(
                                                        new Vec3i(0, -1, 0),
                                                        BlockTags.AIR
                                                )
                                        )
                                ),

                                ConstantInt.of(7),

                                3
                        )
                )
        );

        context.register(
                ModWorldgen.FUSED_ABYSSAL_SAND_DISK,

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.FUSED_ABYSSAL_SAND.block().get()
                                ),

                                BlockPredicate.allOf(
                                        BlockPredicate.matchesBlocks(
                                                ModBlocks.ABYSSAL_SAND.block().get()
                                        ),

                                        BlockPredicate.matchesTag(
                                                new Vec3i(0, 1, 0),
                                                BlockTags.AIR
                                        ),

                                        BlockPredicate.not(
                                                BlockPredicate.matchesTag(
                                                        new Vec3i(0, -1, 0),
                                                        BlockTags.AIR
                                                )
                                        )
                                ),
                                ConstantInt.of(5),

                                2
                        )
                )
        );

        context.register(ModWorldgen.ABYSSALNITE_ORE, new ConfiguredFeature<>(Feature.SCATTERED_ORE,
                new OreConfiguration(List.of(
                        OreConfiguration.target(new TagMatchTest(ModBlockTags.ABYSSAL_STONE_ORE_REPLACEABLES), ModBlocks.ABYSSALNITE_ORE.block().get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(ModBlockTags.GRIMSTONE_ORE_REPLACEABLES), ModBlocks.ABYSSALNITE_GRIMESTONE_ORE.block().get().defaultBlockState())
                ),
                        7,
                        0.0F)
        ));

        context.register(ModWorldgen.CORALIUM_ORE, new ConfiguredFeature<>(Feature.SCATTERED_ORE,
                new OreConfiguration(List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.CORALIUM_ORE.block().get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.CORALIUM_DEEPSLATE_ORE.block().get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(ModBlockTags.ABYSSAL_STONE_ORE_REPLACEABLES), ModBlocks.CORALIUM_ABYSSAL_ORE.block().get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(ModBlockTags.GRIMSTONE_ORE_REPLACEABLES), ModBlocks.CORALIUM_GRIMESTONE_ORE.block().get().defaultBlockState())
                ),
                        5,
                        0.4F)
        ));

        //Spike generation

//     air at  0,-2,-1
//     AND air at  0,-2, 1
//     AND air at  1,-2, 0
//     AND air at -1,-2, 0

        BlockPredicate surroundingAir =
                BlockPredicate.allOf(
                        BlockPredicate.matchesBlocks(
                                new Vec3i(0, -2, -1),
                                Blocks.AIR
                        ),

                        BlockPredicate.matchesBlocks(
                                new Vec3i(0, -2, 1),
                                Blocks.AIR
                        ),

                        BlockPredicate.matchesBlocks(
                                new Vec3i(1, -2, 0),
                                Blocks.AIR
                        ),

                        BlockPredicate.matchesBlocks(
                                new Vec3i(-1, -2, 0),
                                Blocks.AIR
                        )
                );

        BlockPredicate SpikePlacement =
                BlockPredicate.not(surroundingAir);

        //WASTITE
        WeightedStateProvider WASTITESpikeProvider =
                new WeightedStateProvider(
                        WeightedList.<BlockState>builder()
                                .add(
                                        ModBlocks.WASTITE.block().get().defaultBlockState(),
                                        19
                                )
                                .add(
                                        ModBlocks.STARITE.block().get().defaultBlockState(),
                                        1
                                )
                                .build()
                );

        context.register(
                ModWorldgen.WASTITE_SPIKE_COLUMN,

                new ConfiguredFeature<>(
                        Feature.BLOCK_COLUMN,

                        new BlockColumnConfiguration(
                                List.of(
                                        BlockColumnConfiguration.layer(
                                                BiasedToBottomInt.of(1, 3),
                                                WASTITESpikeProvider
                                        )
                                ),

                                Direction.UP,

                                SpikePlacement,

                                false
                        )
                )
        );

        Holder<PlacedFeature> WASTITESpikeColumn =
                placedFeatures.getOrThrow(
                        ModWorldgen.WASTITE_SPIKE_COLUMN_PLACED
                );

        context.register(
                ModWorldgen.WASTITE_SPIKE_PATCH,

                new ConfiguredFeature<>(
                        Feature.VEGETATION_PATCH,

                        new VegetationPatchConfiguration(
                                ModBlockTags.WASTITE_SPIKE_REPLACEABLE,

                                BlockStateProvider.simple(
                                        ModBlocks.WASTITE.block().get().defaultBlockState()
                                ),

                                WASTITESpikeColumn,
                                CaveSurface.FLOOR,
                                ConstantInt.of(7),
                                0.3F,
                                27,
                                1.0F,
                                UniformInt.of(0, 2),
                                0.5F
                        )
                )
        );

        //Abyssal Stone Spike generation
        WeightedStateProvider ABYSSAL_STONESpikeProvider =
                new WeightedStateProvider(
                        WeightedList.<BlockState>builder()
                                .add(
                                        ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(),
                                        19
                                )
                                .add(
                                        ModBlocks.ABYSSAL_COBBLESTONE.block().get().defaultBlockState(),
                                        1
                                )
                                .build()
                );

        context.register(
                ModWorldgen.ABYSSAL_STONE_SPIKE_COLUMN,

                new ConfiguredFeature<>(
                        Feature.BLOCK_COLUMN,

                        new BlockColumnConfiguration(
                                List.of(
                                        BlockColumnConfiguration.layer(
                                                BiasedToBottomInt.of(1, 3),
                                                ABYSSAL_STONESpikeProvider
                                        )
                                ),

                                Direction.UP,

                                SpikePlacement,

                                false
                        )
                )
        );

        Holder<PlacedFeature> ABYSSAL_STONESpikeColumn =
                placedFeatures.getOrThrow(
                        ModWorldgen.ABYSSAL_STONE_SPIKE_COLUMN_PLACED
                );

        context.register(
                ModWorldgen.ABYSSAL_STONE_SPIKE_PATCH,

                new ConfiguredFeature<>(
                        Feature.VEGETATION_PATCH,

                        new VegetationPatchConfiguration(
                                ModBlockTags.ABYSSAL_WAISTLAND_SURFACES,

                                BlockStateProvider.simple(
                                        ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState()
                                ),

                                ABYSSAL_STONESpikeColumn,
                                CaveSurface.FLOOR,
                                ConstantInt.of(7),
                                0.3F,
                                27,
                                1.0F,
                                UniformInt.of(0, 2),
                                0.5F
                        )
                )
        );

        context.register(
                ModWorldgen.ABYSSAL_STONE_FOREST_ROCK,

                new ConfiguredFeature<>(
                        Feature.BLOCK_BLOB,
                        new BlockBlobConfiguration(
                                ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(),
                                BlockPredicate.matchesTag(ModBlockTags.ABYSSAL_WAISTLAND_SURFACES)
                        )
                )
        );
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        for(FossilGenerator fossil : FossilRegistry.ALL){

            FossilWorldgenHelper.registerPlaced(
                    context,
                    fossil
            );
        }

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
                context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.ABYSSAL_MONSTER_ROOM
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(8),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-40),
                                        VerticalAnchor.absolute(30)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        // PLANTS
        context.register(
                ModWorldgen.CORALIUM_TENDRILS_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.CORALIUM_TENDRILS
                        ),

                        List.of(
                                CountPlacement.of(90),
                                InSquarePlacement.spread(),

                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(55),
                                        VerticalAnchor.absolute(66)
                                ),

                                PlacementUtils.HEIGHTMAP_NO_LEAVES,
                                BiomeFilter.biome(),

                                BlockPredicateFilter.forPredicate(
                                        ModWorldgenPredicates.nearCustomWaterPredicate(
                                                Blocks.WATER,
                                                3
                                        )
                                ),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                                )
                        )
                )
        );

        context.register(
                ModWorldgen.LUMINOUS_THISTLE_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.LUMINOUS_THISTLE
                        ),

                        List.of(
                                CountPlacement.of(1),

                                RandomOffsetPlacement.ofTriangle(5, 3),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                ),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.allOf(
                                                BlockPredicate.ONLY_IN_AIR_PREDICATE,

                                                BlockPredicate.wouldSurvive(
                                                        ModBlocks.LUMINOUS_THISTLE.block()
                                                                .get()
                                                                .defaultBlockState(),
                                                        BlockPos.ZERO
                                                )
                                        )
                                ),

                                BiomeFilter.biome()
                        )
                )
        );

        context.register(
                ModWorldgen.WASTELANDS_THORN_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.WASTELANDS_THORN
                        ),

                        List.of(
                                CountPlacement.of(1),
                                InSquarePlacement.spread(),

                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(55),
                                        VerticalAnchor.absolute(66)
                                ),

                                PlacementUtils.HEIGHTMAP_NO_LEAVES,
                                BiomeFilter.biome(),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                                )
                        )
                )
        );

        context.register(
                ModWorldgen.ABYSSAL_DRY_GRASS_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                VegetationFeatures.DRY_GRASS
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(1),
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome(),

                                CountPlacement.of(2),
                                RandomOffsetPlacement.ofTriangle(5, 2),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                                )
                        )
                )
        );

        context.register(
                ModWorldgen.ABYSSAL_DEAD_BUSH_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                VegetationFeatures.DEAD_BUSH
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(2),
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome(),

                                CountPlacement.of(2),
                                RandomOffsetPlacement.ofTriangle(5, 2),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                                )
                        )
                )
        );

        context.register(
                ModWorldgen.WITHERWOOD_TREE_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.WITHERWOOD_TREE),
                        VegetationPlacements.treePlacement(
                                PlacementUtils.countExtra(3, 0.2f, 2),
                                ModBlocks.WITHERWOOD_SAPLING.block().get()
                        )
                )
        );

        context.register(
                ModWorldgen.FOREST_WITHERWOOD_TREE_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.LEAVES_WITHERWOOD_TREE),

                        VegetationPlacements.treePlacement(
                                PlacementUtils.countExtra(80, 0.2f, 2),
                                ModBlocks.WITHERWOOD_SAPLING.block().get()
                        )
                )
        );

        context.register(
                ModWorldgen.WASTITE_CLUSTER_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.WASTITE_CLUSTER
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(3),

                                InSquarePlacement.spread(),
                                CountPlacement.of(1),
                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(
                ModWorldgen.WASTITE_CLUSTER_BLOCK_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.WASTITE_CLUSTER_BLOCK
                        ),

                        List.of(
                                CountPlacement.of(1),
                                InSquarePlacement.spread(),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.allOf(

                                                BlockPredicate.matchesTag(
                                                        ModBlockTags.WASTITE_SPIKE_REPLACEABLE
                                                ),

                                                BlockPredicate.not(
                                                        BlockPredicate.matchesBlocks(
                                                                new Vec3i(0, -1, 0),
                                                                Blocks.AIR
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        context.register(ModWorldgen.ABYSSAL_MUD_DISK_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.ABYSSAL_MUD_DISK),
                        List.of(
                                CountPlacement.of(ConstantInt.of(5)),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(50),
                                        VerticalAnchor.absolute(100)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(ModWorldgen.SCARLET_SHALE_DISK_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.SCARLET_SHALE_DISK),
                        List.of(
                                CountPlacement.of(ConstantInt.of(13)),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-78),
                                        VerticalAnchor.absolute(50)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.AZURE_WASTE_STONE_DISK),
                        List.of(
                                CountPlacement.of(ConstantInt.of(9)),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-78),
                                        VerticalAnchor.absolute(50)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(ModWorldgen.RARE_ABYSSAL_MUD_DISK_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.ABYSSAL_MUD_DISK),
                        List.of(
                                CountPlacement.of(ConstantInt.of(2)),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(30),
                                        VerticalAnchor.absolute(120)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(ModWorldgen.ABYSSAL_SAND_DISK_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.ABYSSAL_SAND_DISK),
                        List.of(
                                CountPlacement.of(ConstantInt.of(25)),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(40),
                                        VerticalAnchor.absolute(100)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(ModWorldgen.FUSED_ABYSSAL_SAND_DISK_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldgen.FUSED_ABYSSAL_SAND_DISK),
                        List.of(
                                CountPlacement.of(ConstantInt.of(18)),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(40),
                                        VerticalAnchor.absolute(100)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(ModWorldgen.ABYSSALNITE_ORE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_ORE),
                List.of(
                        CountPlacement.of(TrapezoidInt.of(1, 20, 7)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(150)),
                        BiomeFilter.biome()
                )
        ));

        context.register(ModWorldgen.CORALIUM_ORE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModWorldgen.CORALIUM_ORE),
                List.of(
                        CountPlacement.of(TrapezoidInt.of(1, 15, 6)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(30), VerticalAnchor.belowTop(180)),
                        BiomeFilter.biome()
                )
        ));


        //Spike Gen
        context.register(
                ModWorldgen.WASTITE_SPIKE_COLUMN_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.WASTITE_SPIKE_COLUMN
                        ),
                        List.of()
                )
        );

        context.register(
                ModWorldgen.WASTITE_SPIKE_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.WASTITE_SPIKE_PATCH
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(5),

                                InSquarePlacement.spread(),

                                CountPlacement.of(10),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                ),

                                BiomeFilter.biome()
                        )
                )
        );

        //Abyssal stone Spike Gen
        context.register(
                ModWorldgen.ABYSSAL_STONE_SPIKE_COLUMN_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.ABYSSAL_STONE_SPIKE_COLUMN
                        ),
                        List.of()
                )
        );

        context.register(
                ModWorldgen.ABYSSAL_STONE_SPIKE_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.ABYSSAL_STONE_SPIKE_PATCH
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(5),

                                InSquarePlacement.spread(),

                                CountPlacement.of(10),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                ),

                                BiomeFilter.biome()
                        )
                )
        );

        //FOREST_ROCK
        context.register(
                ModWorldgen.ABYSSAL_STONE_FOREST_ROCK_PLACED,

                new PlacedFeature(
                        configuredFeatures.getOrThrow(
                                ModWorldgen.ABYSSAL_STONE_FOREST_ROCK
                        ),

                        List.of(
                                RarityFilter.onAverageOnceEvery(7),

                                InSquarePlacement.spread(),

                                CountPlacement.of(7),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                ),

                                BiomeFilter.biome()
                        )
                )
        );
    }

    public static WeightedList.Builder<BlockState> leafLitterPatchBuilder(int minState, int maxState) {
        WeightedList.Builder<BlockState> builder = WeightedList.builder();

        leafLitterPatchBuilder(minState, maxState, Blocks.LEAF_LITTER);

        return builder;
    }

    public static WeightedList.Builder<BlockState> leafLitterPatchBuilder(int minState, int maxState, Block leafLitter) {
        WeightedList.Builder<BlockState> builder = WeightedList.builder();

        for(int amount = minState; amount <= maxState; ++amount) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                builder.add(
                        leafLitter.defaultBlockState()
                                .setValue(LeafLitterBlock.AMOUNT, amount)
                                .setValue(LeafLitterBlock.FACING, direction),
                        1
                );
            }
        }

        return builder;
    }
}