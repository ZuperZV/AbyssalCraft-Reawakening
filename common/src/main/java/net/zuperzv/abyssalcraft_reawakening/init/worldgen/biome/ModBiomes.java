package net.zuperzv.abyssalcraft_reawakening.init.worldgen.biome;

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
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.ModWorldgen;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.gravity_fossils.FossilBiomeHelper;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.gravity_fossils.FossilRegistry;

public final class ModBiomes {
    protected static final int NORMAL_WATER_COLOR = 4159204;
    protected static final int NORMAL_WATER_FOG_COLOR = 329011;
    private static final int OVERWORLD_FOG_COLOR = 12638463;

    private static final Music NORMAL_MUSIC = null;

    public static final ResourceKey<Biome> ABYSSAL_WASTELANDS_BIOME = register("abyssal_wastelands");

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(ABYSSAL_WASTELANDS_BIOME, AbyssalWastelandsBiome(context));
    }

    public static Biome AbyssalWastelandsBiome(BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        //UNDERGROUND
        biomeBuilder.addCarver(Carvers.CAVE);
        biomeBuilder.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(Carvers.CANYON);

            // ORES
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModWorldgen.ABYSSALNITE_ORE_PLACED);

        // SURFACE
        biomeBuilder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModWorldgen.ABYSSAL_MUD_DISK_PLACED);

            // VEGETAL DECORATION
        FossilBiomeHelper.add(biomeBuilder, FossilRegistry.VANILA_FOSSIL);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldgen.WITHERWOOD_TREE_PLACED);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldgen.WASTITE_SPIKE_PLACED);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldgen.CORALIUM_TENDRILS_PLACED);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldgen.LUMINOUS_THISTLE_PLACED);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldgen.WASTELANDS_THORN_PLACED);

        //SKY
            //LAST THING SO NOTHING GETS ONTO IT
        FossilBiomeHelper.add(biomeBuilder, FossilRegistry.CHAIN);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.0f)
                .temperature(0.6f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x130f1c)
                        .grassColorOverride(0x1b1b1b)
                        .foliageColorOverride(0x463e57)
                        .build())
                .build();
    }



    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
    }

    private static BiomeGenerationSettings.Builder baseOceanGeneration(HolderGetter<PlacedFeature> pPlacedFeatures, HolderGetter<ConfiguredWorldCarver<?>> pWorldCarvers) {
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder(pPlacedFeatures, pWorldCarvers);
        globalOverworldGeneration(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addWaterTrees(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultGrass(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder, true);
        return biomegenerationsettings$builder;
    }

    public static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
