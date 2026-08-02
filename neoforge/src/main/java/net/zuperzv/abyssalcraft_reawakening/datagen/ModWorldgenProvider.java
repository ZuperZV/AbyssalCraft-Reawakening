package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.datagen.bootstrap.ModWorldgenBootstrapper;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.ModDimensions;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.ModNoiseRouter;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.ModNoiseSettings;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.density.ModDensityFunctions;
import net.zuperzv.abyssalcraft_reawakening.worldgen.ModWorldgen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ModWorldgenProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModWorldgenBootstrapper::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, ModWorldgenBootstrapper::bootstrapPlacedFeatures)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModWorldgenProvider::bootstrapBiomeModifiers)

            .add(Registries.NOISE, ModWorldgenProvider::bootstrapNoise)
            .add(Registries.DENSITY_FUNCTION, ModDensityFunctions::bootstrap)
            .add(Registries.NOISE_SETTINGS, ModWorldgenProvider::bootstrapNoiseSettings)
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem);

    private static final ResourceKey<BiomeModifier> ABYSSALNITE_OVERWORLD_ORE_MODIFIER = biomeModifierKey("abyssalnite_overworld_ore");
    private static final ResourceKey<BiomeModifier> ABYSSALNITE_NETHER_ORE_MODIFIER = biomeModifierKey("abyssalnite_nether_ore");
    private static final ResourceKey<BiomeModifier> ABYSSALNITE_END_ORE_MODIFIER = biomeModifierKey("abyssalnite_end_ore");

    public ModWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID));
    }

    public static void bootstrapNoise(BootstrapContext<NormalNoise.NoiseParameters> context) {

        context.register(
                ModNoiseRouter.ABYSSAL_TERRAIN,
                new NormalNoise.NoiseParameters(
                        0,
                        1.0,
                        2,
                        1.0,
                        0.5
                )
        );
    }

    public static void bootstrapNoiseSettings(
            BootstrapContext<NoiseGeneratorSettings> context
    ) {

        HolderGetter<NormalNoise.NoiseParameters> noises =
                context.lookup(Registries.NOISE);

        HolderGetter<DensityFunction> functions =
                context.lookup(Registries.DENSITY_FUNCTION);


        context.register(
                ModDimensions.ABYSSAL_WASTELAND_NOISE,
                ModNoiseSettings.create(
                        functions,
                        noises
                )
        );
    }

    private static void bootstrapBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(ABYSSALNITE_OVERWORLD_ORE_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_OVERWORLD_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ABYSSALNITE_NETHER_ORE_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_NETHER_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ABYSSALNITE_END_ORE_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placedFeatures.getOrThrow(ModWorldgen.ABYSSALNITE_END_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> biomeModifierKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Constants.id(name));
    }
}