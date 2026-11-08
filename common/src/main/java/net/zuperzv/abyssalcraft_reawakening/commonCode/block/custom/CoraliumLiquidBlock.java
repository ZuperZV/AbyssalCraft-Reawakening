package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.tags.BlockTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.CoraliumPlagueHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.GlowingLiquidBlock;

public final class CoraliumLiquidBlock extends GlowingLiquidBlock {
    public CoraliumLiquidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        transmuteNeighbors(level, pos);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
        CoraliumPlagueHelper.applyTo(entity, level);
    }

    private static void transmuteNeighbors(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighbor = level.getBlockState(neighborPos);
            if (neighbor.is(BlockTags.BASE_STONE_OVERWORLD)
                    || neighbor.is(BlockTags.BASE_STONE_NETHER)
                    || neighbor.is(Blocks.COBBLESTONE)
                    || neighbor.is(Blocks.COBBLED_DEEPSLATE)) {
                level.setBlock(neighborPos, ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
