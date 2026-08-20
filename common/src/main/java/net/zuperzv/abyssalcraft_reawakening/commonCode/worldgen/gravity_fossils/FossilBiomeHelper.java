package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravity_fossils;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;

public class FossilBiomeHelper {


    public static void add(
            BiomeGenerationSettings.Builder builder,
            FossilGenerator fossil
    ){

        String name = fossil.name();


        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.fossilMarkerPlaced(name)
        );


        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.fossilDiskPlaced(name)
        );


        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.fossilPlaced(name)
        );


        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.fossilCleanupPlaced(name)
        );


        builder.addFeature(
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModWorldgen.fossilMarkerCleanupPlaced(name)
        );
    }
}