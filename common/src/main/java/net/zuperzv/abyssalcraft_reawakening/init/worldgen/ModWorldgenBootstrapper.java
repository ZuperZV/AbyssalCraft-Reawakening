package net.zuperzv.abyssalcraft_reawakening.init.worldgen;

import net.minecraft.core.Direction;import net.minecraft.core.Holder;import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;import net.minecraft.util.valueproviders.BiasedToBottomInt;import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.TrapezoidInt;
import net.minecraft.util.valueproviders.UniformInt;import net.minecraft.world.level.block.Block;import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;import net.minecraft.world.level.levelgen.Heightmap;import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlockTags;import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.ModWorldgen;

import java.util.List;

public final class ModWorldgenBootstrapper {
    private ModWorldgenBootstrapper() {
    }

    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> placedFeatures =
                context.lookup(Registries.PLACED_FEATURE);

        context.register(ModWorldgen.ABYSSAL_MUD_DISK,
                new ConfiguredFeature<>(
                        Feature.DISK,
                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        Blocks.MUD
                                ),
                                BlockPredicate.matchesBlocks(
                                        ModBlocks.ABYSSAL_STONE.block().get(),
                                        Blocks.MUD
                                ),
                                ConstantInt.of(5),
                                2
                        )
                )
        );

        context.register(ModWorldgen.ABYSSALNITE_ORE, new ConfiguredFeature<>(Feature.SCATTERED_ORE,
                new OreConfiguration(List.of(
                        OreConfiguration.target(new TagMatchTest(ModBlockTags.ABYSSAL_STONE_ORE_REPLACEABLES), ModBlocks.ABYSSALNITE_ORE.block().get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(ModBlockTags.ABYSSAL_DEEPSLATE_ORE_REPLACEABLES), ModBlocks.ABYSSALNITE_DEEPSLATE_ORE.block().get().defaultBlockState())
                ),
                        7,
                        0.0F)
        ));

        context.register(ModWorldgen.ABYSSALNITE_OVERWORLD_ORE, new ConfiguredFeature<>(Feature.SCATTERED_ORE,
                new OreConfiguration(List.of(
                        OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.ABYSSALNITE_OVERWORLD_ORE.block().get().defaultBlockState()),
                        OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.ABYSSALNITE_DEEPSLATE_ORE.block().get().defaultBlockState())
                ),
                        9,
                        0.0F)
        ));
        context.register(ModWorldgen.ABYSSALNITE_NETHER_ORE, new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.NETHERRACK), ModBlocks.ABYSSALNITE_NETHER_ORE.block().get().defaultBlockState())),
                        10,
                        0.0F)));
        context.register(ModWorldgen.ABYSSALNITE_END_ORE, new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(List.of(
                        OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), ModBlocks.ABYSSALNITE_END_ORE.block().get().defaultBlockState())),
                        7,
                        0.0F)));


        //Spike generation
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

        BlockPredicate WASTITESpikePlacement =
                BlockPredicate.not(surroundingAir);

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

                                WASTITESpikePlacement,

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
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

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

        context.register(ModWorldgen.ABYSSALNITE_ORE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_ORE),
                List.of(
                        CountPlacement.of(TrapezoidInt.of(1, 20, 7)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(150)),
                        BiomeFilter.biome()
                )
        ));

        context.register(ModWorldgen.ABYSSALNITE_OVERWORLD_ORE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_OVERWORLD_ORE),
                List.of(
                        CountPlacement.of(ConstantInt.of(18)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(100)),
                        BiomeFilter.biome()
                )
        ));
        
        context.register(ModWorldgen.ABYSSALNITE_NETHER_ORE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_NETHER_ORE),
                List.of(
                        CountPlacement.of(ConstantInt.of(14)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
                        BiomeFilter.biome())));
        
        context.register(ModWorldgen.ABYSSALNITE_END_ORE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_END_ORE),
                List.of(
                        CountPlacement.of(TrapezoidInt.of(0, 30, 15)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(20), VerticalAnchor.absolute(80)),
                        BiomeFilter.biome())));


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
    }
}