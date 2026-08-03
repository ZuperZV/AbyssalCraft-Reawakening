package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.Optional;

public class ModDimensions {
    public static final ResourceKey<LevelStem> THE_ABYSSAL_WASTELAND_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssal_wasteland"));
    public static final ResourceKey<Level> THE_ABYSSAL_WASTELAND_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssal_wasteland"));
    public static final ResourceKey<DimensionType> THE_ABYSSAL_WASTELAND_DIM_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssal_wasteland"));

    public static final ResourceKey<NoiseGeneratorSettings> ABYSSAL_WASTELAND_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssal_wasteland"));

    protected static final NoiseSettings ABYSSAL_WASTELAND_SETTINGS = create(-64, 384, 1, 2);

    public static NoiseSettings create(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
        NoiseSettings noiseSettings = new NoiseSettings(minY, height, noiseSizeHorizontal, noiseSizeVertical);
        guardY(noiseSettings).error().ifPresent((error) -> {
            throw new IllegalStateException(error.message());
        });
        return noiseSettings;
    }

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        var timelines = context.lookup(Registries.TIMELINE);
        var clocks = context.lookup(Registries.WORLD_CLOCK);

        context.register(THE_ABYSSAL_WASTELAND_DIM_TYPE_KEY, new DimensionType(
                false,
                true,
                false,
                false,
                1.0,
                -80,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                1.0f,
                new DimensionType.MonsterSettings(ConstantInt.of(0), 0),
                DimensionType.Skybox.OVERWORLD,
                CardinalLighting.Type.DEFAULT,
                EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.FOG_COLOR, -6168523)
                        .set(EnvironmentAttributes.SKY_COLOR, OverworldBiomes.calculateSkyColor(2.5f))
                        .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, -4212331)
                        .set(EnvironmentAttributes.CLOUD_COLOR, ARGB.color(155, 200, 31, 25))
                        .build(),
                timelines.getOrThrow(TimelineTags.IN_OVERWORLD),
                Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))));
    }


    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        var biomes = context.lookup(Registries.BIOME);
        var dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        var noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator singleBiomeGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)),
                noiseGenSettings.getOrThrow(ABYSSAL_WASTELAND_NOISE));

        NoiseBasedChunkGenerator multiBiomeGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(

                                // Dybt ocean (lav continentalness)
                                Pair.of(
                                        Climate.parameters(-1.0f, -0.5f, 0f, 0f, 0f, 0f, 0f),
                                        biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)
                                ),

                                // Kyst / strand overgang
                                Pair.of(
                                        Climate.parameters(-0.5f, -0.1f, 0f, 0f, 0f, 0f, 0f),
                                        biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)
                                ),

                                // Normalt land
                                Pair.of(
                                        Climate.parameters(-0.1f, 0.3f, 0f, 0f, 0f, 0f, 0f),
                                        biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)
                                ),

                                Pair.of(
                                        Climate.parameters(0.3f, 0.6f, 0f, 0f, 0f, 0f, 0f),
                                        biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)
                                ),

                                Pair.of(
                                        Climate.parameters(0.6f, 1.0f, 0f, 0f, 0f, 0f, 0f),
                                        biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)
                                )
                        ))
                ),
                noiseGenSettings.getOrThrow(ABYSSAL_WASTELAND_NOISE));

        context.register(THE_ABYSSAL_WASTELAND_KEY, new LevelStem(dimensionTypes.getOrThrow(ModDimensions.THE_ABYSSAL_WASTELAND_DIM_TYPE_KEY), singleBiomeGenerator));
    }


    private static DataResult<NoiseSettings> guardY(NoiseSettings dimensionType) {
        if (dimensionType.minY() + dimensionType.height() > DimensionType.MAX_Y + 1) {
            return DataResult.error(() -> "min_y + height cannot be higher than: " + (DimensionType.MAX_Y + 1));
        } else if (dimensionType.height() % 16 != 0) {
            return DataResult.error(() -> "height has to be a multiple of 16");
        } else {
            return dimensionType.minY() % 16 != 0 ? DataResult.error(() -> "min_y has to be a multiple of 16") : DataResult.success(dimensionType);
        }
    }

}