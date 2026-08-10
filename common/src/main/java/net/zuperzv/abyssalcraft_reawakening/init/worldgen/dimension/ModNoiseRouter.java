package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.zuperzv.abyssalcraft_reawakening.Constants;

import java.util.stream.Stream;


public class ModNoiseRouter {
    public static final ResourceKey<DensityFunction> Y = createKeyWithDefaultNamespace("y");
    public static final ResourceKey<DensityFunction> SHIFT_X = createKeyWithDefaultNamespace("shift_x");
    public static final ResourceKey<DensityFunction> SHIFT_Z = createKeyWithDefaultNamespace("shift_z");
    public static final ResourceKey<DensityFunction> CONTINENTS = createKey("abyssal/continents");
    public static final ResourceKey<DensityFunction> EROSION = createKey("abyssal/erosion");
    public static final ResourceKey<DensityFunction> RIDGES = createKey("abyssal/ridges");
    public static final ResourceKey<DensityFunction> OFFSET = createKey("abyssal/offset");
    public static final ResourceKey<DensityFunction> FACTOR = createKey("abyssal/factor");
    public static final ResourceKey<DensityFunction> DEPTH = createKey("abyssal/depth");
    public static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("abyssal/sloped_cheese");
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE = createKey("abyssal/base_3d_noise");
    public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKeyWithDefaultNamespace("overworld_large_biomes/continents");
    public static final ResourceKey<DensityFunction> EROSION_LARGE = createKeyWithDefaultNamespace("overworld_large_biomes/erosion");
    private static final ResourceKey<DensityFunction> OFFSET_LARGE = createKeyWithDefaultNamespace("overworld_large_biomes/offset");
    private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKeyWithDefaultNamespace("overworld_large_biomes/factor");
    private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKeyWithDefaultNamespace("overworld_large_biomes/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKeyWithDefaultNamespace("overworld_large_biomes/sloped_cheese");
    private static final ResourceKey<DensityFunction> OFFSET_AMPLIFIED = createKeyWithDefaultNamespace("overworld_amplified/offset");
    private static final ResourceKey<DensityFunction> FACTOR_AMPLIFIED = createKeyWithDefaultNamespace("overworld_amplified/factor");
    private static final ResourceKey<DensityFunction> DEPTH_AMPLIFIED = createKeyWithDefaultNamespace("overworld_amplified/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = createKeyWithDefaultNamespace("overworld_amplified/sloped_cheese");
    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKeyWithDefaultNamespace("overworld/caves/spaghetti_roughness_function");
    private static final ResourceKey<DensityFunction> ENTRANCES = createKeyWithDefaultNamespace("overworld/caves/entrances");
    private static final ResourceKey<DensityFunction> NOODLE = createKeyWithDefaultNamespace("overworld/caves/noodle");
    private static final ResourceKey<DensityFunction> PILLARS = createKeyWithDefaultNamespace("overworld/caves/pillars");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKeyWithDefaultNamespace("overworld/caves/spaghetti_2d");

    private static ResourceKey<DensityFunction> createKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
    private static ResourceKey<DensityFunction> createKeyWithDefaultNamespace(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION,
                Identifier.withDefaultNamespace(name));
    }

    public static final ResourceKey<NormalNoise.NoiseParameters> ABYSSAL_GROUND =
            ResourceKey.create(Registries.NOISE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssal_ground"));

    public static final ResourceKey<NormalNoise.NoiseParameters> ABYSSAL_DESERT_GROUND =
            ResourceKey.create(Registries.NOISE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssal_desert_ground"));


    public static NoiseRouter abyssalWasteland(
            HolderGetter<DensityFunction> functions,
            HolderGetter<NormalNoise.NoiseParameters> noises
    ) {

        NoiseRouter abyssalWasteland = AbyssalWasteland(
                functions,
                noises,
                false,
                false
        );

        return new NoiseRouter(
                abyssalWasteland.barrierNoise(),
                abyssalWasteland.fluidLevelFloodednessNoise(),
                abyssalWasteland.fluidLevelSpreadNoise(),
                abyssalWasteland.lavaNoise(),

                abyssalWasteland.temperature(),
                abyssalWasteland.vegetation(),

                abyssalWasteland.continents(),
                abyssalWasteland.erosion(),

                abyssalWasteland.depth(),
                abyssalWasteland.ridges(),

                abyssalWasteland.preliminarySurfaceLevel(),

                abyssalWasteland.finalDensity(),

                abyssalWasteland.veinToggle(),
                abyssalWasteland.veinRidged(),
                abyssalWasteland.veinGap()
        );
    }

    protected static NoiseRouter AbyssalWasteland(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, boolean largeBiomes, boolean amplified) {
        DensityFunction barrierNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_BARRIER), (double)0.5F);
        DensityFunction fluidLevelFloodednessNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction fluidLevelSpreadNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction lavaNoise = DensityFunctions.noise(noises.getOrThrow(Noises.AQUIFER_LAVA));

        DensityFunction shiftX = DensityFunctions.mul(
                getFunction(functions, SHIFT_X),
                DensityFunctions.constant(1)
        );

        DensityFunction shiftZ = DensityFunctions.mul(
                getFunction(functions, SHIFT_Z),
                DensityFunctions.constant(1)
        );

        DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, (double)0.25F, noises.getOrThrow(largeBiomes ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, (double)0.25F, noises.getOrThrow(largeBiomes ? Noises.VEGETATION_LARGE : Noises.VEGETATION));

        DensityFunction offset = getFunction(functions,
                largeBiomes ? OFFSET_LARGE :
                        (amplified ? OFFSET_AMPLIFIED : OFFSET));

        DensityFunction factor = DensityFunctions.mul(
                getFunction(functions,
                        largeBiomes ? FACTOR_LARGE :
                                (amplified ? FACTOR_AMPLIFIED : FACTOR)),
                DensityFunctions.constant(1)
        );

        DensityFunction depth = DensityFunctions.mul(
                getFunction(functions,
                        largeBiomes ? DEPTH_LARGE :
                                (amplified ? DEPTH_AMPLIFIED : DEPTH)),
                DensityFunctions.constant(1)
        );

        DensityFunction preliminarySurfaceLevel =
                preliminarySurfaceLevel(offset, factor, amplified);

        DensityFunction slopedCheese = getFunction(functions, largeBiomes ? SLOPED_CHEESE_LARGE : (amplified ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));
        DensityFunction surfaceWithEntrances = DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant((double)5.0F), getFunction(functions, ENTRANCES)));
        DensityFunction caves = DensityFunctions.rangeChoice(slopedCheese, (double)-1000000.0F, (double)1.5625F, surfaceWithEntrances, underground(functions, noises, slopedCheese));

        DensityFunction fullNoise = DensityFunctions.min(postProcess(slideOverworld(amplified, caves)), getFunction(functions, NOODLE));

        DensityFunction y = getFunction(functions, Y);
        int veinMinY = Stream.of(ModOreVeinifier.VeinType.values()).mapToInt((t) -> t.minY).min().orElse(-DimensionType.MIN_Y * 2);
        int veinMaxY = Stream.of(ModOreVeinifier.VeinType.values()).mapToInt((t) -> t.maxY).max().orElse(-DimensionType.MIN_Y * 2);
        DensityFunction veinToggle = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.ORE_VEININESS), (double)1.5F, (double)1.5F), veinMinY, veinMaxY, 0);
        float oreRidgeFrequency = 4.0F;
        DensityFunction veinA = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.ORE_VEIN_A), (double)4.0F, (double)4.0F), veinMinY, veinMaxY, 0).abs();
        DensityFunction veinB = yLimitedInterpolatable(y, DensityFunctions.noise(noises.getOrThrow(Noises.ORE_VEIN_B), (double)4.0F, (double)4.0F), veinMinY, veinMaxY, 0).abs();
        DensityFunction veinRidged = DensityFunctions.add(DensityFunctions.constant((double)-0.08F), DensityFunctions.max(veinA, veinB));
        DensityFunction veinGap = DensityFunctions.noise(noises.getOrThrow(Noises.ORE_GAP));
        return new NoiseRouter(barrierNoise, fluidLevelFloodednessNoise, fluidLevelSpreadNoise, lavaNoise, temperature, vegetation, getFunction(functions, largeBiomes ? CONTINENTS_LARGE : CONTINENTS), getFunction(functions, largeBiomes ? EROSION_LARGE : EROSION), depth, getFunction(functions, RIDGES), preliminarySurfaceLevel, fullNoise, veinToggle, veinRidged, veinGap);
    }


    private static DensityFunction offsetToDepth(DensityFunction offset) {
        return DensityFunctions.add(DensityFunctions.yClampedGradient(-80, 320, (double)1.5F, (double)-1.5F), offset);
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> functions, ResourceKey<DensityFunction> name) {
        return new DensityFunctions.HolderHolder(functions.getOrThrow(name));
    }

    private static DensityFunction underground(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, DensityFunction slopedCheese) {
        DensityFunction spaghetti2DFunction = getFunction(functions, SPAGHETTI_2D);
        DensityFunction spaghettiRoughnessFunction = getFunction(functions, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction layerNoiseSource = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_LAYER), (double)8.0F);
        DensityFunction layerizedCavernsFunction = DensityFunctions.mul(DensityFunctions.constant((double)4.0F), layerNoiseSource.square());
        DensityFunction cheese = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666);
        DensityFunction solidifedCheeseWithTopSlide = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27), cheese).clamp((double)-1.0F, (double)1.0F), DensityFunctions.add(DensityFunctions.constant((double)1.5F), DensityFunctions.mul(DensityFunctions.constant(-0.64), slopedCheese)).clamp((double)0.0F, (double)0.5F));
        DensityFunction baseCaveDensity = DensityFunctions.add(layerizedCavernsFunction, solidifedCheeseWithTopSlide);
        DensityFunction undergroundSubtractions = DensityFunctions.min(DensityFunctions.min(baseCaveDensity, getFunction(functions, ENTRANCES)), DensityFunctions.add(spaghetti2DFunction, spaghettiRoughnessFunction));
        DensityFunction pillarsWithoutCutoff = getFunction(functions, PILLARS);
        DensityFunction pillars = DensityFunctions.rangeChoice(pillarsWithoutCutoff, (double)-1000000.0F, 0.03, DensityFunctions.constant((double)-1000000.0F), pillarsWithoutCutoff);
        return DensityFunctions.max(undergroundSubtractions, pillars);
    }

    private static DensityFunction postProcess(DensityFunction slide) {
        DensityFunction blended = DensityFunctions.blendDensity(slide);
        return DensityFunctions.mul(DensityFunctions.interpolated(blended), DensityFunctions.constant(0.64)).squeeze();
    }

    private static DensityFunction remap(DensityFunction input, double fromMin, double fromMax, double toMin, double toMax) {
        double factor = (toMax - toMin) / (fromMax - fromMin);
        double offset = toMin - fromMin * factor;
        return DensityFunctions.add(DensityFunctions.mul(input, DensityFunctions.constant(factor)), DensityFunctions.constant(offset));
    }

    private static DensityFunction slideOverworld(boolean isAmplified, DensityFunction caves) {
        return slide(caves, -64, 384, isAmplified ? 16 : 80, isAmplified ? 0 : 64, (double)-0.078125F, 0, 24, isAmplified ? 0.4 : (double)0.1171875F);
    }

    private static DensityFunction noiseGradientDensity(DensityFunction factor, DensityFunction depthWithJaggedness) {
        DensityFunction gradientUnscaled = DensityFunctions.mul(depthWithJaggedness, factor);
        return DensityFunctions.mul(DensityFunctions.constant((double)4.0F), gradientUnscaled.quarterNegative());
    }

    private static DensityFunction preliminarySurfaceLevel(DensityFunction offset, DensityFunction factor, boolean amplified) {
        DensityFunction cachedFactor = DensityFunctions.cache2d(factor);
        DensityFunction cachedOffset = DensityFunctions.cache2d(offset);
        DensityFunction upperBound = remap(DensityFunctions.add(DensityFunctions.mul(DensityFunctions.constant((double)0.2734375F), cachedFactor.invert()), DensityFunctions.mul(DensityFunctions.constant((double)-1.0F), cachedOffset)), (double)1.5F, (double)-1.5F, (double)-64.0F, (double)320.0F);
        upperBound = upperBound.clamp((double)-40.0F, (double)320.0F);
        DensityFunction density = DensityFunctions.add(slideOverworld(amplified, DensityFunctions.add(noiseGradientDensity(cachedFactor, offsetToDepth(cachedOffset)), DensityFunctions.constant((double)-0.703125F)).clamp((double)-64.0F, (double)64.0F)), DensityFunctions.constant((double)-0.390625F));
        return DensityFunctions.findTopSurface(density, upperBound, -64, ModDimensions.ABYSSAL_WASTELAND_SETTINGS.getCellHeight());
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction y, DensityFunction whenInRange, int minYInclusive, int maxYInclusive, int whenOutOfRange) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(y, (double)minYInclusive, (double)(maxYInclusive + 1), whenInRange, DensityFunctions.constant((double)whenOutOfRange)));
    }

    private static DensityFunction slide(DensityFunction caves, int minY, int height, int topStartY, int topEndY, double topTarget, int bottomStartY, int bottomEndY, double bottomTarget) {
        DensityFunction topFactor = DensityFunctions.yClampedGradient(minY + height - topStartY, minY + height - topEndY, (double)1.0F, (double)0.0F);
        DensityFunction noiseValue = DensityFunctions.lerp(topFactor, topTarget, caves);
        DensityFunction bottomFactor = DensityFunctions.yClampedGradient(minY + bottomStartY, minY + bottomEndY, (double)0.0F, (double)1.0F);
        return DensityFunctions.lerp(bottomFactor, bottomTarget, noiseValue);
    }
}