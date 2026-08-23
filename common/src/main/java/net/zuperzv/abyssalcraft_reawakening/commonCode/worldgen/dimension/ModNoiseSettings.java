package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.biome.surface.ModSurfaceRules;

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
                Blocks.AIR.defaultBlockState(), //WATER/FLUID //TODO MAKE Coralium fluid and make the caves air to make monnster room
                router,

                ModSurfaceRules.makeRules(),

                List.of(),

                63,
                false,
                true,
                false,
                false
        );
    }
}