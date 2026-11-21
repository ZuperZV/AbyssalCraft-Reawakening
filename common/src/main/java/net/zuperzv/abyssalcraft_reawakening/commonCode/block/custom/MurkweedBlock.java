package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluidTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;

public class MurkweedBlock extends LilyPadBlock implements BonemealableBlock {

    private static final VoxelShape ALGAE_SHAPE = Block.box(0, 14, 0, 16, 15, 16);

    public MurkweedBlock(BlockBehaviour.Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ALGAE_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ALGAE_SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (entity instanceof LivingEntity) {
            Vec3 slowSpeed = new Vec3(0.1, 0.05F, 0.1);
            entity.makeStuckInBlock(state, slowSpeed);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(40) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 0.9;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle((SimpleParticleType)ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        for (BlockPos adjacent : BlockPos.betweenClosed(pos.offset(1, 0, 0), pos.offset(-1, 0, -1))) {
            if (adjacent.equals(pos)) continue;
            BlockState adjState = level.getBlockState(adjacent);
            if (adjState.isAir() && this.canSurvive(this.defaultBlockState(), level, adjacent)) {
                level.setBlockAndUpdate(adjacent, this.defaultBlockState());
                return;
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.getFluidState().is(FluidTags.WATER)
                || below.getFluidState().is(ModFluidTags.CORALIUM);
    }
}
