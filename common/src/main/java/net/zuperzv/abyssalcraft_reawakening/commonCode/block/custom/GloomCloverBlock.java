package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;

public class GloomCloverBlock extends FlowerBedBlock {

    public GloomCloverBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return this.canBeReplaced(state, context, AMOUNT) ? true : super.canBeReplaced(state, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    @Override
    public double getShapeHeight() {
        return 3.0;
    }

    @Override
    public IntegerProperty getSegmentAmountProperty() {
        return AMOUNT;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getStateForPlacement(context, this, AMOUNT, FACING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int currentAmount = state.getValue(AMOUNT);
        if (currentAmount < 4) {
            level.setBlock(pos, state.setValue(AMOUNT, currentAmount + 1), 2);
        } else {
            popResource(level, pos, new ItemStack(this));
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int flowerAmount = state.getValue(AMOUNT);
        if (flowerAmount >= 3 && random.nextInt(25) == 0) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            level.addParticle((SimpleParticleType)ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0.02, 0);
        } else if (flowerAmount <= 2 && random.nextInt(40) == 0) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            double y = pos.getY() + 0.3;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            level.addParticle((SimpleParticleType)ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, 0, 0, 0);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return mayPlaceOn(level.getBlockState(below), level, below);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.SUPPORTS_VEGETATION) || state.is(ModBlockTags.SUPPORTS_ABYSS_VEGETATION);
    }
}
