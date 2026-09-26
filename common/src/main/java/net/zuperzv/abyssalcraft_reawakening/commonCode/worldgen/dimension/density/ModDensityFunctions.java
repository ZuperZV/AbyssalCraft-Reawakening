package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.density;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.ModNoiseRouter;

public class ModDensityFunctions {

    public static void bootstrap(
            BootstrapContext<DensityFunction> context
    ) {

        HolderGetter<NormalNoise.NoiseParameters> noises =
                context.lookup(
                        Registries.NOISE
                );

        /*
         * ============================================================
         * Y
         * ============================================================
         */
        context.register(
                ModNoiseRouter.Y,
                DensityFunctions.yClampedGradient(
                        -80,
                        288,
                        -0.6,
                        0.6
                )
        );

        /*
         * ============================================================
         * SHIFT
         * ============================================================
         */
        DensityFunction shiftX =
                DensityFunctions.shiftA(
                        noises.getOrThrow(
                                Noises.SHIFT
                        )
                );

        DensityFunction shiftZ =
                DensityFunctions.shiftB(
                        noises.getOrThrow(
                                Noises.SHIFT
                        )
                );

        context.register(
                ModNoiseRouter.SHIFT_X,
                shiftX
        );

        context.register(
                ModNoiseRouter.SHIFT_Z,
                shiftZ
        );

        DensityFunction continents =
                DensityFunctions.flatCache(
                        DensityFunctions.shiftedNoise2d(
                                shiftX,
                                shiftZ,
                                0.25F,
                                noises.getOrThrow(
                                        Noises.CONTINENTALNESS
                                )
                        )
                );

        context.register(
                ModNoiseRouter.CONTINENTS,
                continents
        );

        DensityFunction oceanDepth =
                DensityFunctions.mul(
                        DensityFunctions.min(
                                continents,
                                DensityFunctions.constant(0.0)
                        ),
                        DensityFunctions.constant(0.35)
                );

        DensityFunction erosion =
                DensityFunctions.flatCache(
                        DensityFunctions.shiftedNoise2d(
                                shiftX,
                                shiftZ,
                                0.25F,
                                noises.getOrThrow(
                                        Noises.EROSION
                                )
                        )
                );

        context.register(
                ModNoiseRouter.EROSION,
                erosion
        );

        DensityFunction ridges =
                DensityFunctions.flatCache(
                        DensityFunctions.shiftedNoise2d(
                                shiftX,
                                shiftZ,
                                0.35F,
                                noises.getOrThrow(
                                        Noises.RIDGE
                                )
                        )
                );

        context.register(
                ModNoiseRouter.RIDGES,
                ridges
        );


        DensityFunction deepOceanOffset =
                remap(
                        continents,
                        -1.0,
                        -0.20,
                        -0.65,
                        -0.34
                ).clamp(
                        -0.65,
                        -0.34
                );

        DensityFunction coastOffset =
                remap(
                        continents,
                        -0.20,
                        0.0,
                        -0.34,
                        0.0
                ).clamp(
                        -0.34,
                        0.0
                );

        DensityFunction oceanOffset =
                DensityFunctions.rangeChoice(
                        continents,
                        -1.0,
                        -0.20,

                        deepOceanOffset,

                        DensityFunctions.rangeChoice(
                                continents,
                                -0.20,
                                0.0,

                                coastOffset,

                                DensityFunctions.constant(
                                        0.0
                                )
                        )
                );

        context.register(
                ModNoiseRouter.OFFSET,
                oceanOffset
        );

        DensityFunction factor =
                DensityFunctions.constant(
                        0.65
                );

        context.register(
                ModNoiseRouter.FACTOR,
                factor
        );

        DensityFunction depth =
                DensityFunctions.add(

                        DensityFunctions.yClampedGradient(
                                -17,
                                200,
                                1.5,
                                -1.5
                        ),

                        DensityFunctions.add(
                                oceanOffset,

                                DensityFunctions.mul(
                                        ridges,
                                        DensityFunctions.constant(
                                                0.05
                                        )
                                )
                        )
                );

        context.register(
                ModNoiseRouter.DEPTH,
                depth
        );

        DensityFunction baseNoise =
                BlendedNoise.createUnseeded(
                        0.25,
                        0.125,
                        80.0,
                        130.0,
                        8.0
                );

        context.register(
                ModNoiseRouter.BASE_3D_NOISE,
                baseNoise
        );

        DensityFunction slopedCheese =
                DensityFunctions.add(
                        baseNoise,

                        DensityFunctions.add(
                                depth,

                                DensityFunctions.add(
                                        oceanDepth,

                                        DensityFunctions.mul(
                                                ridges,
                                                DensityFunctions.constant(-0.25)
                                        )
                                )
                        )
                );

        context.register(
                ModNoiseRouter.SLOPED_CHEESE,
                slopedCheese
        );
    }

    private static DensityFunction remap(
            DensityFunction input,
            double fromMin,
            double fromMax,
            double toMin,
            double toMax
    ) {
        double factor =
                (toMax - toMin)
                        / (fromMax - fromMin);

        double offset =
                toMin
                        - fromMin * factor;

        return DensityFunctions.add(
                DensityFunctions.mul(
                        input,
                        DensityFunctions.constant(
                                factor
                        )
                ),
                DensityFunctions.constant(
                        offset
                )
        );
    }
}