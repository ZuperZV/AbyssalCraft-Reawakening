package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.biome.surface.ModSurfaceRules;

import java.util.List;

public class ModNoiseSettings {

    public static NoiseGeneratorSettings create(
            HolderGetter<DensityFunction> functions,
            HolderGetter<NormalNoise.NoiseParameters> noises
    ) {

        NoiseRouter router =
                ModNoiseRouter.abyssalWasteland(
                        functions,
                        noises
                );


        return new NoiseGeneratorSettings(
                NoiseSettings.create(
                        -80,
                        384,
                        1,
                        2
                ),

                ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(),
                Blocks.WATER.defaultBlockState(),

                router,

                ModSurfaceRules.makeRules(),

                List.of(),

                63,
                false,
                true,
                true,
                false
        );
    }
}