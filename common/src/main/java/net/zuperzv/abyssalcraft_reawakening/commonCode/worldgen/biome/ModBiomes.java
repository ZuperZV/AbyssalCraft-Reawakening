package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravityFossils.FossilBiomeHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravityFossils.FossilRegistry;

public final class ModBiomes {

    protected static final int NORMAL_WATER_COLOR = 4159204;
    protected static final int NORMAL_WATER_FOG_COLOR = 329011;
    private static final int OVERWORLD_FOG_COLOR = 12638463;

    private static final Music NORMAL_MUSIC = null;

    public static final ResourceKey<Biome> ABYSSAL_DESERT =
            register("abyssal_desert");

    public static final ResourceKey<Biome> DARKLANDS_FOREST =
            register("darklands_forest");

    public static final ResourceKey<Biome> CORALLIUM_LAKE =
            register("corallium_lake");

    public static final ResourceKey<Biome> ABYSSAL_PLATEAU =
            register("abyssal_plateau");

    public static final ResourceKey<Biome> DARKLANDS_MOUNTAINS =
            register("darklands_mountains");

    public static final ResourceKey<Biome> ABYSSAL_SWAMP =
            register("abyssal_swamp");

    public static final ResourceKey<Biome> ABYSSAL_WASTELANDS_BIOME =
            register("abyssal_wastelands");


    public static void bootstrap(BootstrapContext<Biome> context) {

        context.register(
                ABYSSAL_DESERT,
                AbyssalDesertBiome(context)
        );

        context.register(
                DARKLANDS_FOREST,
                DarklandsForestBiome(context)
        );

        context.register(
                CORALLIUM_LAKE,
                CoralliumLakeBiome(context)
        );

        context.register(
                ABYSSAL_PLATEAU,
                AbyssalPlateauBiome(context)
        );

        context.register(
                DARKLANDS_MOUNTAINS,
                DarklandsMountainsBiome(context)
        );

        context.register(
                ABYSSAL_SWAMP,
                AbyssalSwampBiome(context)
        );

        context.register(
                ABYSSAL_WASTELANDS_BIOME,
                AbyssalWastelandsBiome(context)
        );
    }

    //Done
    public static Biome AbyssalDesertBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.CORALIUM_ORE_PLACED
        );

        // SURFACE
            //ground elevation
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_STONE_SPIKE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_STONE_FOREST_ROCK_PLACED
        );

            //Top layer
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.RARE_ABYSSAL_MUD_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.WASTITE_CLUSTER_PLACED
        );

            //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_DRY_GRASS_PLACED);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_DEAD_BUSH_PLACED);

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.FALLEN_CHAIN
        );

        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.IN_GROUND
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(1.2f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x25152C)
                                .grassColorOverride(0x3B293F)
                                .foliageColorOverride(0x4A304A)
                                .build()
                )
                .build();
    }

    //Okay done
    public static Biome DarklandsForestBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.SCARLET_SHALE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED
        );


        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );


        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.CORALIUM_ORE_PLACED
        );

        // SURFACE
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_SAND_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.FOREST_WITHERWOOD_TREE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.1f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x182026)
                                .grassColorOverride(0x25372C)
                                .foliageColorOverride(0x344A38)
                                .build()
                )
                .build();
    }

    //TODO
    public static Biome CoralliumLakeBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.SCARLET_SHALE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED
        );

        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.CORALIUM_ORE_PLACED
        );

        // SURFACE
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x5A143D)
                                .grassColorOverride(0x48233E)
                                .foliageColorOverride(0x682B55)
                                .build()
                )
                .build();
    }

    //TODO
    public static Biome AbyssalPlateauBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.SCARLET_SHALE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED
        );

        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        // SURFACE
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.FALLEN_CHAIN
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.6f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x171321)
                                .grassColorOverride(0x2B2734)
                                .foliageColorOverride(0x3C354A)
                                .build()
                )
                .build();
    }

    //TODO
    public static Biome DarklandsMountainsBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.SCARLET_SHALE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED
        );

        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        // SURFACE
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.35f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x11101A)
                                .grassColorOverride(0x25232D)
                                .foliageColorOverride(0x302E3A)
                                .build()
                )
                .build();
    }

    //TODO
    public static Biome AbyssalSwampBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.CORALIUM_ORE_PLACED
        );

        // SURFACE
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(1.0f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x111C24)
                                .grassColorOverride(0x263C2D)
                                .foliageColorOverride(0x304D38)
                                .build()
                )
                .build();
    }

    //Done
    public static Biome AbyssalWastelandsBiome(
            BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder =
                new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // UNDERGROUND
        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.SCARLET_SHALE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED
        );

        // ORES
        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.CORALIUM_ORE_PLACED
        );

        // SURFACE
        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        //FOSSILS
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.VANILA_FOSSIL
        );

        // VEGETATION
        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        //SKY LAST THING
        FossilBiomeHelper.add(
                biomeBuilder,
                FossilRegistry.CHAIN
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.6f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x130F1C)
                                .grassColorOverride(0x1B1B1B)
                                .foliageColorOverride(0x463E57)
                                .build()
                )
                .build();
    }


    private static BiomeGenerationSettings.Builder baseGeneration(
            BootstrapContext<Biome> context) {

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        biomeBuilder.addCarver(ModWorldgen.CAVE);
        biomeBuilder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(ModWorldgen.CANYON);

        biomeBuilder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        biomeBuilder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        return biomeBuilder;
    }


    public static void globalOverworldGeneration(
            BiomeGenerationSettings.Builder builder) {

        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
    }


    private static BiomeGenerationSettings.Builder baseOceanGeneration(
            HolderGetter<PlacedFeature> pPlacedFeatures,
            HolderGetter<ConfiguredWorldCarver<?>> pWorldCarvers) {

        BiomeGenerationSettings.Builder builder =
                new BiomeGenerationSettings.Builder(
                        pPlacedFeatures,
                        pWorldCarvers
                );

        globalOverworldGeneration(builder);

        BiomeDefaultFeatures.addDefaultOres(builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder);
        BiomeDefaultFeatures.addWaterTrees(builder);
        BiomeDefaultFeatures.addDefaultFlowers(builder);
        BiomeDefaultFeatures.addDefaultGrass(builder);
        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);

        return builder;
    }

    public static ResourceKey<Biome> register(String name) {

        return ResourceKey.create(
                Registries.BIOME,
                Identifier.fromNamespaceAndPath(
                        Constants.MOD_ID,
                        name
                )
        );
    }
}