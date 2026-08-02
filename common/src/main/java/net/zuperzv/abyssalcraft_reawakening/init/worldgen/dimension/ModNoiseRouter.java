package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.zuperzv.abyssalcraft_reawakening.Constants;


public class ModNoiseRouter {

    public static final ResourceKey<NormalNoise.NoiseParameters>
            ABYSSAL_TERRAIN =
            ResourceKey.create(
                    Registries.NOISE,
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "abyssal_terrain"
                    )
            );


    public static NoiseRouter abyssalWasteland(
            HolderGetter<NormalNoise.NoiseParameters> noises
    ) {
        // Aquifer
        DensityFunction barrier =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.AQUIFER_BARRIER),
                        0.5
                );

        DensityFunction fluidFloodedness =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS),
                        0.67
                );

        DensityFunction fluidSpread =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD),
                        0.714
                );

        DensityFunction lava =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.AQUIFER_LAVA)
                );

        /*
         * Climate
         */
        DensityFunction temperature =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.TEMPERATURE),
                        0.25
                );

        DensityFunction vegetation =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.VEGETATION),
                        0.25
                );

        /*
         * Overworld terrain
         */
        DensityFunction continents =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.CONTINENTALNESS)
                );

        DensityFunction erosion =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.EROSION)
                );

        DensityFunction ridges =
                DensityFunctions.noise(
                        noises.getOrThrow(Noises.RIDGE)
                );

        DensityFunction depth =
                DensityFunctions.constant(0);

        /*
         * Abyssal terrain
         */
        DensityFunction abyssNoise =
                DensityFunctions.noise(
                        noises.getOrThrow(
                                ABYSSAL_TERRAIN
                        ),
                        0.8,
                        0.8
                );

        DensityFunction height =
                DensityFunctions.yClampedGradient(
                        -64,
                        320,
                        1.5,
                        -1.5
                );

        DensityFunction finalDensity =
                DensityFunctions.add(
                        height,
                        DensityFunctions.mul(
                                abyssNoise,
                                DensityFunctions.constant(1.5)
                        )
                );

        return new NoiseRouter(
                barrier,
                fluidFloodedness,
                fluidSpread,
                lava,

                temperature,
                vegetation,

                continents,
                erosion,

                depth,
                ridges,

                // surface level
                DensityFunctions.constant(64),

                finalDensity,

                // ores
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero()
        );
    }
}