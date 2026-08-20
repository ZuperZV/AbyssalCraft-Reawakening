package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.biome.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.biome.ModBiomes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.ModNoiseRouter;

public class ModSurfaceRules {

    private static final SurfaceRules.RuleSource BEDROCK =
            makeStateRule(Blocks.BEDROCK);

    private static final SurfaceRules.RuleSource ABYSSAL_STONE =
            makeStateRule(ModBlocks.ABYSSAL_STONE.block().get());

    private static final SurfaceRules.RuleSource CORRUPTED_SOIL =
            makeStateRule(ModBlocks.CORRUPTED_SOIL.block().get());

    private static final SurfaceRules.RuleSource ABYSSAL_SAND =
            makeStateRule(ModBlocks.ABYSSAL_SAND.block().get());

    private static final SurfaceRules.RuleSource ABYSSAL_MUD =
            makeStateRule(Blocks.MUD);
    
    static SurfaceRules.RuleSource ABYSSAL_SAND_SANDSTONE =
            SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, CORRUPTED_SOIL), ABYSSAL_SAND);

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

        SurfaceRules.RuleSource abyssalForestGround = SurfaceRules.sequence(

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

                // 4 BLOCKS UNDER SURFACE
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(
                                3,
                                false,
                                CaveSurface.FLOOR
                        ),

                        SurfaceRules.sequence(

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_GROUND,
                                                0.0,
                                                0.5
                                        ),
                                        ABYSSAL_SAND_SANDSTONE
                                )
                        )
                ),

                // Noise >= 0.5
                ABYSSAL_STONE
        );

        SurfaceRules.RuleSource abyssalDesertGround = SurfaceRules.sequence(

                // TOP BLOCK
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(
                                0,
                                false,
                                CaveSurface.FLOOR
                        ),

                        SurfaceRules.sequence(

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                -1.0,
                                                -0.8
                                        ),
                                        CORRUPTED_SOIL
                                ),

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                -0.8,
                                                -0.1
                                        ),
                                        ABYSSAL_SAND_SANDSTONE
                                ),

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                -0.1,
                                                0.2
                                        ),
                                        CORRUPTED_SOIL
                                ),

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                0.2,
                                                0.4
                                        ),
                                        ABYSSAL_MUD
                                ),

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                0.4,
                                                0.5
                                        ),
                                        CORRUPTED_SOIL
                                ),

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                0.5,
                                                1.0
                                        ),
                                        ABYSSAL_SAND_SANDSTONE
                                ),


                                ABYSSAL_SAND_SANDSTONE
                        )
                ),

                // 4 BLOCKS UNDER SURFACE
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(
                                3,
                                false,
                                CaveSurface.FLOOR
                        ),

                        SurfaceRules.sequence(

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                -1.0,
                                                0.3
                                        ),
                                        ABYSSAL_SAND_SANDSTONE
                                ),

                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(
                                                ModNoiseRouter.ABYSSAL_DESERT_GROUND,
                                                -0.1,
                                                0.5
                                        ),
                                        CORRUPTED_SOIL
                                ),

                                ABYSSAL_SAND_SANDSTONE
                        )
                ),

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

                // Darklands Forest
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                ModBiomes.DARKLANDS_FOREST
                        ),

                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                abyssalForestGround
                        )
                ),

                // Abyssal Desert
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                ModBiomes.ABYSSAL_DESERT
                        ),

                        abyssalDesertGround
                ),

                ABYSSAL_STONE
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}