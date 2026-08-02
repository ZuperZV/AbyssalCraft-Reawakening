package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.density;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
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
                        -80,
                        320,
                        -1.2,
                        1.0
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
                                DensityFunctions.constant(2.2)
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
                                1.1,
                                0.8
                        ),
                        DensityFunctions.constant(0.65)
                )
        );

        // Ridges klipper/revner
        context.register(
                ModNoiseRouter.RIDGES,
                DensityFunctions.mul(
                        DensityFunctions.noise(
                                noises.getOrThrow(Noises.RIDGE),
                                1.4,
                                0.9
                        ),
                        DensityFunctions.constant(1.6)
                )
        );

        context.register(
                ModNoiseRouter.FACTOR,
                DensityFunctions.constant(2.0)
        );

        context.register(
                ModNoiseRouter.OFFSET,
                DensityFunctions.constant(-0.35)
        );

        context.register(
                ModNoiseRouter.DEPTH,
                DensityFunctions.add(

                        DensityFunctions.yClampedGradient(
                                -80,
                                320,
                                1.0,
                                -1.2
                        ),

                        DensityFunctions.mul(
                                DensityFunctions.noise(
                                        noises.getOrThrow(Noises.RIDGE),
                                        1.4,
                                        0.9
                                ),
                                DensityFunctions.constant(0.5)
                        )
                )
        );
    }
}