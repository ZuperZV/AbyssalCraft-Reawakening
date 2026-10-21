package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.tags.BlockTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.ModEffects;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.GlowingLiquidBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;

public final class CoraliumLiquidBlock extends GlowingLiquidBlock {
    private static final ThreadLocal<Boolean> TRANSMUTING_NEIGHBORS =
            ThreadLocal.withInitial(() -> false);

    public CoraliumLiquidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        transmuteNeighbors(level, pos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        transmuteNeighbors(level, pos);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
        Holder<net.minecraft.world.effect.MobEffect> plague = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.CORALIUM_PLAGUE.get());
        if (!level.isClientSide() && entity instanceof LivingEntity living && !living.hasEffect(plague)) {
            living.addEffect(new MobEffectInstance(plague, 200), null);
        }
    }

    private static void transmuteNeighbors(Level level, BlockPos pos) {
        if (level.isClientSide() || TRANSMUTING_NEIGHBORS.get()) {
            return;
        }

        TRANSMUTING_NEIGHBORS.set(true);
        try {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockState neighbor = level.getBlockState(neighborPos);
                if (neighbor.getFluidState().is(net.minecraft.tags.FluidTags.WATER)) {
                    level.setBlock(neighborPos, ModFluids.CORALIUM_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
                } else if (neighbor.is(BlockTags.BASE_STONE_OVERWORLD)
                        || neighbor.is(BlockTags.BASE_STONE_NETHER)
                        || neighbor.is(Blocks.COBBLESTONE)
                        || neighbor.is(Blocks.COBBLED_DEEPSLATE)) {
                    level.setBlock(neighborPos, ModBlocks.ABYSSAL_STONE.block().get().defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        } finally {
            TRANSMUTING_NEIGHBORS.set(false);
        }
    }
}
