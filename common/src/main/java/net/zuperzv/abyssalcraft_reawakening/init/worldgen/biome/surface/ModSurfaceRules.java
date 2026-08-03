package net.zuperzv.abyssalcraft_reawakening.init.worldgen.biome.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.biome.ModBiomes;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.dimension.ModNoiseRouter;

public class ModSurfaceRules {

    private static final SurfaceRules.RuleSource BEDROCK =
            makeStateRule(Blocks.BEDROCK);

    private static final SurfaceRules.RuleSource ABYSSAL_STONE =
            makeStateRule(ModBlocks.ABYSSAL_STONE.block().get());

    private static final SurfaceRules.RuleSource ABYSSAL_MUD =
            makeStateRule(Blocks.MUD);

    private static final SurfaceRules.RuleSource CORRUPTED_SOIL =
            makeStateRule(ModBlocks.CORRUPTED_SOIL.block().get());

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.RuleSource abyssalGround = SurfaceRules.sequence(

                // Noise < 0.0
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_GROUND,
                                -1.0,
                                0.0
                        ),
                        ABYSSAL_STONE
                        //ABYSSAL_MUD
                ),

                // Noise 0.0 - 0.5
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_GROUND,
                                0.0,
                                0.5
                        ),
                        CORRUPTED_SOIL
                ),

                // Noise >= 0.5
                ABYSSAL_STONE
        );

        return SurfaceRules.sequence(

                // Bedrock
                SurfaceRules.ifTrue(
                        SurfaceRules.verticalGradient(
                                "abyssal_bedrock",
                                VerticalAnchor.bottom(),
                                VerticalAnchor.aboveBottom(5)
                        ),
                        BEDROCK
                ),

                // Abyssal Wastelands
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                ModBiomes.ABYSSAL_WASTELANDS_BIOME
                        ),

                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                abyssalGround
                        )
                ),

                ABYSSAL_STONE
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}