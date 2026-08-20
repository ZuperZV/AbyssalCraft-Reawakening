package net.zuperzv.abyssalcraft_reawakening.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;

public final class FabricModWorldgen {
    private FabricModWorldgen() {
    }

    public static void load() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_OVERWORLD_ORE_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_NETHER_ORE_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ModWorldgen.ABYSSALNITE_END_ORE_PLACED
        );
    }
}