package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravity_fossils;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.placement.NoFluidBelowPlacement;

import java.util.List;

public class FossilWorldgenHelper {

    public static void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            FossilGenerator fossil
    ){

        registerMarker(context,fossil);

        registerDisk(context,fossil);

        registerFossil(context,fossil);

        registerCleanup(context,fossil);

        registerMarkerCleanup(context,fossil);
    }

    private static void registerMarker(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            FossilGenerator fossil
    ){

        context.register(

                ModWorldgen.fossilMarker(fossil.name()),

                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        Blocks.JUNGLE_LEAVES.defaultBlockState()
                                                .setValue(LeavesBlock.DISTANCE, 7)
                                                .setValue(LeavesBlock.PERSISTENT, true)
                                )
                        )
                )
        );
    }

    private static void registerCleanup(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            FossilGenerator fossil
    ){

        context.register(

                ModWorldgen.fossilCleanup(fossil.name()),

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(

                                BlockStateProvider.simple(
                                        Blocks.AIR
                                ),

                                BlockPredicate.matchesBlocks(Blocks.LIGHT),

                                ConstantInt.of(5),

                                1
                        )
                )
        );
    }

    private static void registerMarkerCleanup(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            FossilGenerator fossil
    ){

        context.register(

                ModWorldgen.fossilMarkerCleanup(fossil.name()),

                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,

                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        Blocks.AIR
                                )
                        )
                )
        );
    }

    public static void registerPlaced(
            BootstrapContext<PlacedFeature> context,
            FossilGenerator fossil
    ){

        registerMarkerPlaced(context,fossil);

        registerDiskPlaced(context,fossil);

        registerFossilPlaced(context,fossil);

        registerCleanupPlaced(context,fossil);

        registerMarkerCleanupPlaced(context,fossil);
    }

    private static void registerDisk(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            FossilGenerator fossil
    ){
        context.register(
                ModWorldgen.fossilDisk(fossil.name()),

                new ConfiguredFeature<>(
                        Feature.DISK,

                        new DiskConfiguration(
                                BlockStateProvider.simple(
                                        Blocks.LIGHT.defaultBlockState()
                                                .setValue(LightBlock.LEVEL, 0)
                                ),

                                BlockPredicate.matchesBlocks(
                                        Blocks.AIR
                                ),

                                ConstantInt.of(5),
                                1
                        )
                )
        );
    }

    private static void registerFossil(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            FossilGenerator fossil
    ){

        HolderGetter<StructureProcessorList> processors =
                context.lookup(Registries.PROCESSOR_LIST);


        context.register(

                ModWorldgen.fossil(fossil.name()),

                new ConfiguredFeature<>(
                        Feature.FOSSIL,

                        new FossilFeatureConfiguration(
                                fossil.fossils(),
                                fossil.overlays(),
                                processors.getOrThrow(
                                        fossil.fossilProcessor()
                                ),
                                processors.getOrThrow(
                                        fossil.overlayProcessor()
                                ),
                                7
                        )
                )
        );
    }

    private static void registerFossilPlaced(
            BootstrapContext<PlacedFeature> context,
            FossilGenerator fossil
    ){

        HolderGetter<ConfiguredFeature<?, ?>> features =
                context.lookup(Registries.CONFIGURED_FEATURE);


        context.register(

                ModWorldgen.fossilPlaced(fossil.name()),

                new PlacedFeature(

                        features.getOrThrow(
                                ModWorldgen.fossil(fossil.name())
                        ),

                        List.of(

                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(254),
                                        VerticalAnchor.absolute(254)
                                ),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.matchesBlocks(
                                                Blocks.JUNGLE_LEAVES
                                        )
                                ),

                                new NoFluidBelowPlacement(),

                                RandomOffsetPlacement.horizontal(
                                        ConstantInt.of(8)
                                ),

                                RandomOffsetPlacement.vertical(
                                        ConstantInt.of(10)
                                ),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                )
                        )
                )
        );
    }

    private static void registerMarkerPlaced(
            BootstrapContext<PlacedFeature> context,
            FossilGenerator fossil
    ){

        HolderGetter<ConfiguredFeature<?, ?>> features =
                context.lookup(Registries.CONFIGURED_FEATURE);


        context.register(

                ModWorldgen.fossilMarkerPlaced(fossil.name()),

                new PlacedFeature(

                        features.getOrThrow(
                                ModWorldgen.fossilMarker(fossil.name())
                        ),

                        List.of(

                                fossil.rarityFilter(),

                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(254),
                                        VerticalAnchor.absolute(254)
                                ),

                                BiomeFilter.biome()
                        )
                )
        );
    }

    private static void registerMarkerCleanupPlaced(
            BootstrapContext<PlacedFeature> context,
            FossilGenerator fossil
    ){

        HolderGetter<ConfiguredFeature<?, ?>> features =
                context.lookup(Registries.CONFIGURED_FEATURE);


        context.register(

                ModWorldgen.fossilMarkerCleanupPlaced(fossil.name()),

                new PlacedFeature(

                        features.getOrThrow(
                                ModWorldgen.fossilMarkerCleanup(fossil.name())
                        ),

                        List.of(
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(254),
                                        VerticalAnchor.absolute(254)
                                ),

                                BiomeFilter.biome()
                        )
                )
        );
    }

    private static void registerDiskPlaced(
            BootstrapContext<PlacedFeature> context,
            FossilGenerator fossil
    ){

        HolderGetter<ConfiguredFeature<?, ?>> features =
                context.lookup(Registries.CONFIGURED_FEATURE);


        context.register(

                ModWorldgen.fossilDiskPlaced(fossil.name()),

                new PlacedFeature(

                        features.getOrThrow(
                                ModWorldgen.fossilDisk(fossil.name())
                        ),

                        List.of(

                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(254),
                                        VerticalAnchor.absolute(254)
                                ),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.matchesBlocks(
                                                Blocks.JUNGLE_LEAVES
                                        )
                                ),

                                RandomOffsetPlacement.horizontal(
                                        ConstantInt.of(8)
                                ),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                ),

                                RandomOffsetPlacement.vertical(
                                        ConstantInt.of(6)
                                )
                        )
                )
        );
    }

    private static void registerCleanupPlaced(
            BootstrapContext<PlacedFeature> context,
            FossilGenerator fossil
    ){

        HolderGetter<ConfiguredFeature<?, ?>> features =
                context.lookup(Registries.CONFIGURED_FEATURE);


        context.register(

                ModWorldgen.fossilCleanupPlaced(fossil.name()),

                new PlacedFeature(

                        features.getOrThrow(
                                ModWorldgen.fossilCleanup(fossil.name())
                        ),

                        List.of(

                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(254),
                                        VerticalAnchor.absolute(254)
                                ),

                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.matchesBlocks(
                                                Blocks.JUNGLE_LEAVES
                                        )
                                ),

                                RandomOffsetPlacement.horizontal(
                                        ConstantInt.of(8)
                                ),

                                HeightmapPlacement.onHeightmap(
                                        Heightmap.Types.WORLD_SURFACE
                                )
                        )
                )
        );
    }
}