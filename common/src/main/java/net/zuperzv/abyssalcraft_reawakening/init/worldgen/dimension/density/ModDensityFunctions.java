package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.density;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.ModNoiseRouter;

public class ModDensityFunctions {

    public static void bootstrap(BootstrapContext<DensityFunction> context) {

        HolderGetter<NormalNoise.NoiseParameters> noises =
                context.lookup(Registries.NOISE);

        // Needed by NoiseRouter
        context.register(
                ModNoiseRouter.Y,
                DensityFunctions.yClampedGradient(
                        -64,
                        200,
                        -1.0,
                        0.6
                )
        );

        context.register(
                ModNoiseRouter.SHIFT_X,
                DensityFunctions.shiftA(
                        noises.getOrThrow(Noises.SHIFT)
                )
        );

        context.register(
                ModNoiseRouter.SHIFT_Z,
                DensityFunctions.shiftB(
                        noises.getOrThrow(Noises.SHIFT)
                )
        );

        context.register(
                ModNoiseRouter.CONTINENTS,
                DensityFunctions.add(
                        DensityFunctions.mul(
                                DensityFunctions.noise(
                                        noises.getOrThrow(Noises.CONTINENTALNESS),
                                        0.9,
                                        0.4
                                ),
                                DensityFunctions.constant(0.6)
                        ),

                        DensityFunctions.mul(
                                DensityFunctions.noise(
                                        noises.getOrThrow(Noises.CONTINENTALNESS_LARGE),
                                        1.3,
                                        0.9
                                ),
                                DensityFunctions.constant(0.35)
                        )
                )
        );

        context.register(
                ModNoiseRouter.EROSION,
                DensityFunctions.mul(
                        DensityFunctions.noise(
                                noises.getOrThrow(Noises.EROSION),
                                1.0,
                                0.5
                        ),
                        DensityFunctions.constant(0.35)
                )
        );

        // Ridges klipper/revner
        DensityFunction ridges =
                DensityFunctions.mul(

                        DensityFunctions.noise(
                                noises.getOrThrow(Noises.RIDGE),
                                0.35,
                                0.25
                        ),

                        DensityFunctions.constant(0.35)
                );


        context.register(
                ModNoiseRouter.RIDGES,
                ridges
        );

        DensityFunction depth =
                DensityFunctions.add(

                        DensityFunctions.yClampedGradient(
                                -64,
                                200,
                                0.6,
                                -0.6
                        ),

                        DensityFunctions.mul(
                                DensityFunctions.noise(
                                        noises.getOrThrow(Noises.RIDGE),
                                        0.35,
                                        0.25
                                ),
                                DensityFunctions.constant(0.25)
                        )
                );

        context.register(
                ModNoiseRouter.DEPTH,
                depth
        );

        context.register(
                ModNoiseRouter.FACTOR,
                DensityFunctions.constant(0.65)
        );

        context.register(
                ModNoiseRouter.OFFSET,
                DensityFunctions.constant(-7.5)
        );

        context.register(
                ModNoiseRouter.BASE_3D_NOISE,
                BlendedNoise.createUnseeded(
                        0.25, 0.125, 80.0, 130.0, 8.0)
        );

        HolderGetter<DensityFunction> functions =
                context.lookup(Registries.DENSITY_FUNCTION);

        DensityFunction baseNoise =
                new DensityFunctions.HolderHolder(
                        functions.getOrThrow(ModNoiseRouter.BASE_3D_NOISE)
                );

        // SLOPED CHEESE
        DensityFunction slopedCheese =
                DensityFunctions.add(
                        baseNoise,

                        DensityFunctions.add(
                                depth,
                                DensityFunctions.mul(
                                        ridges,
                                        DensityFunctions.constant(-0.25)
                                )
                        )
                );


        context.register(
                ModNoiseRouter.SLOPED_CHEESE,
                slopedCheese
        );
    }
}