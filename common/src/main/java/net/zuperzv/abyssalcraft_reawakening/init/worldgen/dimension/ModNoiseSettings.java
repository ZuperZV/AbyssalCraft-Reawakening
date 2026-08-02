package net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension;

import net.minecraft.core.Holder;
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
            HolderGetter<NormalNoise.NoiseParameters> noiseGetter
    ) {

        NoiseRouter router =
                ModNoiseRouter.abyssalWasteland(noiseGetter);


        return new NoiseGeneratorSettings(
                NoiseSettings.create(
                        0,
                        384,
                        1,
                        2
                ),

                ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(),
                Blocks.WATER.defaultBlockState(),

                router,

                ModSurfaceRules.makeRules(),

                // spawn Target
                List.of(),

                63,
                false,
                false,
                false,
                false
        );
    }
}