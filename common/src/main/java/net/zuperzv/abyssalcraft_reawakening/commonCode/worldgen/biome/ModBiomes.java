package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        // SURFACE - GROUND ELEVATION
        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_STONE_SPIKE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_STONE_FOREST_ROCK_PLACED
        );

        // SURFACE - TOP LAYER
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.RARE_ABYSSAL_MUD_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.WASTITE_CLUSTER_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_DRY_GRASS_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.ABYSSAL_DEAD_BUSH_PLACED
        );

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY / HANGING FOSSILS
        addChainFossils(
                builder,
                true,
                true,
                true
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(1.2f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        addDarklandsUnderground(builder);

        // SURFACE
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_SAND_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.FOREST_WITHERWOOD_TREE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        addCommonVegetation(builder);

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY
        addChainFossils(
                builder,
                true,
                false,
                false
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.1f)
                .temperature(0.7f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        addDarklandsUnderground(builder);

        // SURFACE
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        addCommonVegetation(builder);

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY
        addChainFossils(
                builder,
                true,
                false,
                false
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.5f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        addDarklandsUnderground(builder);

        // SURFACE
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        addCommonVegetation(builder);

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY
        addChainFossils(
                builder,
                true,
                true,
                false
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.6f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        addDarklandsUnderground(builder);

        // SURFACE
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        addCommonVegetation(builder);

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY
        addChainFossils(
                builder,
                true,
                false,
                false
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.35f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        // SURFACE
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        addCommonVegetation(builder);

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY
        addChainFossils(
                builder,
                true,
                false,
                false
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(1.0f)
                .temperature(0.8f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                baseGeneration(context);

        addDarklandsUnderground(builder);

        // SURFACE
        builder.addFeature(
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModWorldgen.ABYSSAL_MUD_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WITHERWOOD_TREE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTITE_SPIKE_PLACED
        );

        // FOSSILS
        addVanillaFossil(builder);

        // VEGETATION
        addCommonVegetation(builder);

        //ENTITY
        addCommonEntities(spawnBuilder);

        // SKY
        addChainFossils(
                builder,
                true,
                false,
                false
        );

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.6f)
                .generationSettings(builder.build())
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

        BiomeGenerationSettings.Builder builder =
                new BiomeGenerationSettings.Builder(
                        context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER)
                );

        // CARVERS
        builder.addCarver(ModWorldgen.CAVE);
        builder.addCarver(ModWorldgen.CAVE_EXTRA_UNDERGROUND);
        builder.addCarver(ModWorldgen.CANYON);

        // ORES
        builder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_ORE_PLACED
        );

        return builder;
    }

    private static void addCommonEntities(
            MobSpawnSettings.Builder spawnBuilder) {

        spawnBuilder.addSpawn(
                MobCategory.MONSTER,
                4,
                new MobSpawnSettings.SpawnerData(
                        ModEntityTypes.ABYSSAL_ZOMBIE.get(),
                        1,
                        4
                )
        );

        spawnBuilder.addSpawn(
                MobCategory.MONSTER,
                2,
                new MobSpawnSettings.SpawnerData(
                        ModEntityTypes.GROUNDLING.get(),
                        1,
                        2
                )
        );
    }


    private static void addDarklandsUnderground(
            BiomeGenerationSettings.Builder builder) {

        builder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.SCARLET_SHALE_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ModWorldgen.AZURE_WASTE_STONE_DISK_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                ModWorldgen.ABYSSAL_MONSTER_ROOM_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.CORALIUM_INFUSED_ORE_PLACED
        );
    }

    private static void addCommonVegetation(
            BiomeGenerationSettings.Builder builder) {

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.CORALIUM_TENDRILS_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.LUMINOUS_THISTLE_PLACED
        );

        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.WASTELANDS_THORN_PLACED
        );
    }

    private static void addVanillaFossil(
            BiomeGenerationSettings.Builder builder) {

        FossilBiomeHelper.add(
                builder,
                FossilRegistry.VANILA_FOSSIL
        );
    }

    private static void addChainFossils(
            BiomeGenerationSettings.Builder builder,
            boolean chain,
            boolean fallenChain,
            boolean inGround) {

        if (chain) {
            FossilBiomeHelper.add(
                    builder,
                    FossilRegistry.CHAIN
            );
        }

        if (fallenChain) {
            FossilBiomeHelper.add(
                    builder,
                    FossilRegistry.FALLEN_CHAIN
            );
        }

        if (inGround) {
            FossilBiomeHelper.add(
                    builder,
                    FossilRegistry.IN_GROUND
            );
        }
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