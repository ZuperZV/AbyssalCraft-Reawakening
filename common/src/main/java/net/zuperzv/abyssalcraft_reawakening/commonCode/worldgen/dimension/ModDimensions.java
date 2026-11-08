package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension;

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
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.Optional;

public class ModDimensions {

    public static final ResourceKey<LevelStem> THE_ABYSSAL_WASTELAND_KEY =
            ResourceKey.create(
                    Registries.LEVEL_STEM,
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "abyssal_wasteland"
                    )
            );

    public static final ResourceKey<Level> THE_ABYSSAL_WASTELAND_LEVEL_KEY =
            ResourceKey.create(
                    Registries.DIMENSION,
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "abyssal_wasteland"
                    )
            );

    public static final ResourceKey<DimensionType> THE_ABYSSAL_WASTELAND_DIM_TYPE_KEY =
            ResourceKey.create(
                    Registries.DIMENSION_TYPE,
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "abyssal_wasteland"
                    )
            );

    public static final ResourceKey<NoiseGeneratorSettings> ABYSSAL_WASTELAND_NOISE =
            ResourceKey.create(
                    Registries.NOISE_SETTINGS,
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "abyssal_wasteland"
                    )
            );

    protected static final NoiseSettings ABYSSAL_WASTELAND_SETTINGS =
            create(-80, 368, 1, 2);

    public static NoiseSettings create(
            int minY,
            int height,
            int noiseSizeHorizontal,
            int noiseSizeVertical
    ) {
        NoiseSettings noiseSettings =
                new NoiseSettings(
                        minY,
                        height,
                        noiseSizeHorizontal,
                        noiseSizeVertical
                );

        guardY(noiseSettings)
                .error()
                .ifPresent(error -> {
                    throw new IllegalStateException(
                            error.message()
                    );
                });

        return noiseSettings;
    }

    public static void bootstrapType(
            BootstrapContext<DimensionType> context
    ) {
        var timelines =
                context.lookup(Registries.TIMELINE);

        var clocks =
                context.lookup(Registries.WORLD_CLOCK);

        context.register(
                THE_ABYSSAL_WASTELAND_DIM_TYPE_KEY,
                new DimensionType(
                        false,
                        false,
                        false,
                        false,
                        1.0,
                        -80,
                        368,
                        368,
                        BlockTags.INFINIBURN_OVERWORLD,
                        1.0f,

                        new DimensionType.MonsterSettings(
                                UniformInt.of(15, 15),
                                15
                        ),

                        DimensionType.Skybox.NONE,
                        CardinalLighting.Type.DEFAULT,

                        EnvironmentAttributeMap.builder()
                                .set(
                                        EnvironmentAttributes.FOG_COLOR,
                                        ARGB.color(0, 0, 30, 0)
                                )
                                .set(
                                        EnvironmentAttributes.SKY_COLOR,
                                        ARGB.color(0, 30, 30, 30)
                                )
                                .set(
                                        EnvironmentAttributes.AMBIENT_LIGHT_COLOR,
                                        ARGB.color(70, 20, 30, 20)
                                )
                                .set(
                                        EnvironmentAttributes.CLOUD_COLOR,
                                        ARGB.color(0, 0, 0, 0)
                                )
                                .set(
                                        EnvironmentAttributes.STAR_BRIGHTNESS,
                                        1.0f
                                )
                                .set(
                                        EnvironmentAttributes.SKY_LIGHT_COLOR,
                                        ARGB.color(50, 50, 140, 50)
                                )
                                .set(
                                        EnvironmentAttributes.SUNRISE_SUNSET_COLOR,
                                        ARGB.color(100, 50, 110, 5)
                                )
                                .build(),

                        timelines.getOrThrow(
                                TimelineTags.IN_OVERWORLD
                        ),

                        Optional.of(
                                clocks.getOrThrow(
                                        WorldClocks.OVERWORLD
                                )
                        )
                )
        );
    }
    public static void bootstrapStem(
            BootstrapContext<LevelStem> context
    ) {
        var biomes =
                context.lookup(Registries.BIOME);

        var dimensionTypes =
                context.lookup(Registries.DIMENSION_TYPE);

        var noiseGenSettings =
                context.lookup(Registries.NOISE_SETTINGS);

        Climate.Parameter depth =
                Climate.Parameter.span(
                        -1.5f,
                        1.5f
                );

        Climate.Parameter weirdness =
                Climate.Parameter.span(
                        -1.5f,
                        1.5f
                );

        NoiseBasedChunkGenerator multiBiomeGenerator =
                new NoiseBasedChunkGenerator(

                        MultiNoiseBiomeSource.createFromList(
                                new Climate.ParameterList<>(
                                        List.of(

                                                // CORALLIUM LAKE - Very cold (temperature -1.0 to -0.6)
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(-1.0f, -0.6f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.CORALLIUM_LAKE)
                                                ),

                                                // ABYSSAL SWAMP - Cold (temperature -0.6 to -0.4)
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(-0.6f, -0.4f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.ABYSSAL_SWAMP)
                                                ),

                                                // DARKLANDS FOREST - Cool (temperature -0.4 to -0.2)
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(-0.4f, -0.2f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.DARKLANDS_FOREST)
                                                ),

                                                // ABYSSAL DESERT - Warm (temperature -0.2 to 0.0)
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(-0.2f, 0.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.ABYSSAL_DESERT)
                                                ),

                                                // DARKLANDS MOUNTAINS - Hot (temperature 0.0 to 0.4)
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(0.0f, 0.4f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.DARKLANDS_MOUNTAINS)
                                                ),

                                                // ABYSSAL PLATEAU - Very hot (temperature 0.4 to 1.0)
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(0.4f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.ABYSSAL_PLATEAU)
                                                ),

                                                // ABYSSAL WASTELANDS - Fallback biome, catches everything else
                                                Pair.of(
                                                        new Climate.ParameterPoint(
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                Climate.Parameter.span(-1.0f, 1.0f),
                                                                depth,
                                                                weirdness,
                                                                Climate.quantizeCoord(0.0f)
                                                        ),
                                                        biomes.getOrThrow(ModBiomes.ABYSSAL_WASTELANDS_BIOME)
                                                )
                                        )
                                )
                        ),

                        noiseGenSettings.getOrThrow(
                                ABYSSAL_WASTELAND_NOISE
                        )
                );

        context.register(
                THE_ABYSSAL_WASTELAND_KEY,
                new LevelStem(
                        dimensionTypes.getOrThrow(
                                THE_ABYSSAL_WASTELAND_DIM_TYPE_KEY
                        ),
                        multiBiomeGenerator
                )
        );
    }

    private static DataResult<NoiseSettings> guardY(
            NoiseSettings dimensionType
    ) {
        if (
                dimensionType.minY()
                        + dimensionType.height()
                        > DimensionType.MAX_Y + 1
        ) {
            return DataResult.error(
                    () ->
                            "min_y + height cannot be higher than: "
                                    + (DimensionType.MAX_Y + 1)
            );
        }

        if (dimensionType.height() % 16 != 0) {
            return DataResult.error(
                    () ->
                            "height has to be a multiple of 16"
            );
        }

        if (dimensionType.minY() % 16 != 0) {
            return DataResult.error(
                    () ->
                            "min_y has to be a multiple of 16"
            );
        }

        return DataResult.success(
                dimensionType
        );
    }
}
