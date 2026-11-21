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

    private static final SurfaceRules.RuleSource ABYSSAL_COBBLESTONE =
            makeStateRule(ModBlocks.ABYSSAL_COBBLESTONE.block().get());

    private static final SurfaceRules.RuleSource CORALIUM_STONE =
            makeStateRule(ModBlocks.CORALIUM_STONE.block().get());

    private static final SurfaceRules.RuleSource CORALIUM_COBBLESTONE =
            makeStateRule(ModBlocks.CORALIUM_COBBLESTONE.block().get());

    private static final SurfaceRules.RuleSource GRIMSTONE =
            makeStateRule(ModBlocks.GRIMESTONE.block().get());

    private static final SurfaceRules.RuleSource SCARLET_SHALE =
            makeStateRule(ModBlocks.SCARLET_SHALE.block().get());

    private static final SurfaceRules.RuleSource SITARITE =
            makeStateRule(ModBlocks.SITARITE.block().get());

    private static final SurfaceRules.RuleSource CORRUPTED_SOIL =
            makeStateRule(ModBlocks.CORRUPTED_SOIL.block().get());

    private static final SurfaceRules.RuleSource ABYSSAL_SAND =
            makeStateRule(ModBlocks.ABYSSAL_SAND.block().get());

    private static final SurfaceRules.RuleSource ABYSSAL_MUD =
            makeStateRule(Blocks.MUD);

    private static final SurfaceRules.RuleSource SINKING_MIRE =
            makeStateRule(ModBlocks.SINKING_MIRE.block().get());

    private static final SurfaceRules.RuleSource GLOOM_MOSS =
            makeStateRule(ModBlocks.GLOOM_MOSS.block().get());

    private static final SurfaceRules.RuleSource FUSED_ABYSSAL_SAND =
            makeStateRule(ModBlocks.FUSED_ABYSSAL_SAND.block().get());
    
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

        SurfaceRules.RuleSource abyssalPlateauGround = SurfaceRules.sequence(

                // Noise < 0.0
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_GROUND,
                                -1.0,
                                0.0
                        ),
                        CORALIUM_STONE
                ),

                // Noise 0.0 - 0.5
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_GROUND,
                                0.0,
                                0.5
                        ),
                        CORALIUM_COBBLESTONE
                ),

                // 4 BLOCKS UNDER SURFACE
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(
                                7,
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
                                        CORALIUM_COBBLESTONE
                                )
                        )
                ),

                // Noise >= 0.5
                CORALIUM_STONE
        );

        SurfaceRules.RuleSource abyssalSwampGround = SurfaceRules.sequence(

                // Noise >= 0.4 - Gloom Moss hills (top layer, elevated areas)
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_SWAMP_GROUND,
                                0.4,
                                1.0
                        ),
                        GLOOM_MOSS
                ),

                // Noise 0.25 - 0.4 - Sinking Mire (sinking mud, mid-elevation)
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_SWAMP_GROUND,
                                0.25,
                                0.4
                        ),
                        SINKING_MIRE
                ),

                // Noise 0.1 - 0.25 - Fused Abyssal Sand border (transition zone)
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_SWAMP_GROUND,
                                0.1,
                                0.25
                        ),
                        FUSED_ABYSSAL_SAND
                ),

                // Noise 0.0 - 0.1 - Abyssal Mud near water
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.ABYSSAL_SWAMP_GROUND,
                                0.0,
                                0.1
                        ),
                        ABYSSAL_MUD
                ),

                // Noise < 0.0 - Abyssal Stone valleys (water-filled depressions)
                ABYSSAL_STONE
        );

        SurfaceRules.RuleSource sitaritereGround = SurfaceRules.sequence(

                // Noise < 0.0
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.SITARITE_GROUND,
                                -1.0,
                                0.0
                        ),
                        SITARITE
                ),

                // Noise 0.0 - 0.5
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ModNoiseRouter.SITARITE_GROUND,
                                0.0,
                                0.5
                        ),
                        ABYSSAL_COBBLESTONE
                ),

                // 4 BLOCKS UNDER SURFACE
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(
                                7,
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
                                        ABYSSAL_COBBLESTONE
                                )
                        )
                ),

                // Noise >= 0.5
                CORALIUM_STONE
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

                SurfaceRules.ifTrue(
                        SurfaceRules.verticalGradient(
                                "scarlet_shale",
                                VerticalAnchor.bottom(),
                                VerticalAnchor.aboveBottom(7)
                        ),
                        SCARLET_SHALE
                ),

                SurfaceRules.ifTrue(
                        SurfaceRules.verticalGradient(
                                "grimstone",
                                VerticalAnchor.absolute(-12),
                                VerticalAnchor.absolute(12)
                        ),
                        GRIMSTONE
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

                // Abyssal Plateau
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                ModBiomes.ABYSSAL_PLATEAU
                        ),

                        SurfaceRules.ifTrue(

                                SurfaceRules.ON_FLOOR,
                                abyssalPlateauGround
                        )
                ),

                // Abyssal Swamp
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                ModBiomes.ABYSSAL_SWAMP
                        ),

                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,

                                SurfaceRules.sequence(
                                        abyssalSwampGround
                                )
                        )
                ),

                ABYSSAL_STONE
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}