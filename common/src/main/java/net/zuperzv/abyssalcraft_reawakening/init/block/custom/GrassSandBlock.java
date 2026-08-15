package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;

import java.util.List;
import java.util.Optional;

public class GrassSandBlock extends FallingBlock implements BonemealableBlock {

    public static final MapCodec<GrassSandBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            ColorRGBA.CODEC
                                    .fieldOf("falling_dust_color")
                                    .forGetter(block -> block.dustColor),

                            propertiesCodec()
                    ).apply(instance, GrassSandBlock::new)
            );

    protected final ColorRGBA dustColor;

    public GrassSandBlock(
            ColorRGBA dustColor,
            BlockBehaviour.Properties properties
    ) {
        super(properties);
        this.dustColor = dustColor;
    }

    @Override
    public MapCodec<GrassSandBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return this.dustColor.rgba();
    }

    @Override
    protected void randomTick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        if (level.getMaxLocalRawBrightness(pos.above()) < 9) {
            level.setBlockAndUpdate(
                    pos,
                    ModBlocks.ABYSSAL_SAND.block().get().defaultBlockState()
            );
            return;
        }

        // Spread
        for (int i = 0; i < 4; i++) {

            BlockPos targetPos = pos.offset(
                    random.nextInt(3) - 1,
                    random.nextInt(5) - 3,
                    random.nextInt(3) - 1
            );

            BlockState targetState = level.getBlockState(targetPos);

            if (!targetState.is(ModBlocks.ABYSSAL_SAND.block().get())) {
                continue;
            }

            BlockPos abovePos = targetPos.above();

            if (!level.getFluidState(abovePos).isEmpty()) {
                continue;
            }

            if (level.getMaxLocalRawBrightness(abovePos) < 9) {
                continue;
            }

            level.setBlockAndUpdate(
                    targetPos,
                    this.defaultBlockState()
            );
        }
    }
    
    //BoneMeal

    @Override
    public boolean isValidBonemealTarget(
            LevelReader level,
            BlockPos pos,
            BlockState state
    ) {
        return level.getBlockState(pos.above()).isAir()
                && level.isInsideBuildHeight(pos.above());
    }

    @Override
    public boolean isBonemealSuccess(
            Level level,
            RandomSource random,
            BlockPos pos,
            BlockState state
    ) {
        return true;
    }

    @Override
    public void performBonemeal(
            ServerLevel level,
            RandomSource random,
            BlockPos pos,
            BlockState state
    ) {
        BlockPos above = pos.above();

        Optional<Holder.Reference<PlacedFeature>> grassFeature =
                level.registryAccess()
                        .lookupOrThrow(Registries.PLACED_FEATURE)
                        .get(VegetationPlacements.GRASS_BONEMEAL);

        for (int j = 0; j < 128; ++j) {

            BlockPos testPos = above;

            for (int i = 0; i < j / 16; ++i) {

                testPos = testPos.offset(
                        random.nextInt(3) - 1,
                        (random.nextInt(3) - 1)
                                * random.nextInt(3) / 2,
                        random.nextInt(3) - 1
                );

                if (!level.getBlockState(testPos.below()).is(this)) {
                    continue;
                }

                if (level.getBlockState(testPos)
                        .isCollisionShapeFullBlock(level, testPos)) {
                    continue;
                }
            }

            BlockState testState = level.getBlockState(testPos);

            if (testState.is(Blocks.SHORT_GRASS)
                    && random.nextInt(10) == 0) {

                BonemealableBlock bonemealable =
                        (BonemealableBlock) testState.getBlock();

                if (bonemealable.isValidBonemealTarget(
                        level,
                        testPos,
                        testState
                )) {
                    bonemealable.performBonemeal(
                            level,
                            random,
                            testPos,
                            testState
                    );
                }
            }

            if (testState.isAir()
                    && !level.isOutsideBuildHeight(testPos)) {

                if (random.nextInt(8) == 0) {

                    List<ConfiguredFeature<?, ?>> features =
                            level.getBiome(testPos)
                                    .value()
                                    .getGenerationSettings()
                                    .getBoneMealFeatures();

                    if (!features.isEmpty()) {
                        ConfiguredFeature<?, ?> feature =
                                Util.getRandom(features, random);

                        feature.place(
                                level,
                                level.getChunkSource().getGenerator(),
                                random,
                                testPos
                        );
                    }

                } else if (grassFeature.isPresent()) {

                    grassFeature.get().value().place(
                            level,
                            level.getChunkSource().getGenerator(),
                            random,
                            testPos
                    );
                }
            }
        }
    }

    @Override
    public BonemealableBlock.Type getType() {
        return BonemealableBlock.Type.NEIGHBOR_SPREADER;
    }
}