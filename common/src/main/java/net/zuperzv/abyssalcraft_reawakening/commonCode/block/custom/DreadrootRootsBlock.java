package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class DreadrootRootsBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<DreadrootRootsBlock> CODEC = simpleCodec(DreadrootRootsBlock::new);
    public static final EnumProperty<AttachFace> FACE =
            EnumProperty.create("face", AttachFace.class, AttachFace.FLOOR, AttachFace.CEILING);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape FLOOR_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 9.0, 14.0);
    private static final VoxelShape CEILING_SHAPE = Block.box(2.0, 7.0, 2.0, 14.0, 16.0, 14.0);

    public DreadrootRootsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACE, AttachFace.FLOOR)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public MapCodec<DreadrootRootsBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        AttachFace face = switch (context.getClickedFace()) {
            case UP -> AttachFace.FLOOR;
            case DOWN -> AttachFace.CEILING;
            default -> null;
        };
        if (face == null) {
            return null;
        }

        return defaultBlockState()
                .setValue(FACE, face)
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean onFloor = state.getValue(FACE) == AttachFace.FLOOR;
        BlockPos supportPos = onFloor ? pos.below() : pos.above();
        Direction supportFace = onFloor ? Direction.UP : Direction.DOWN;
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, supportFace);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            net.minecraft.world.level.ScheduledTickAccess ticks,
            BlockPos pos,
            Direction directionToNeighbour,
            BlockPos neighbourPos,
            BlockState neighbourState,
            RandomSource random
    ) {
        boolean supportRemoved = state.getValue(FACE) == AttachFace.FLOOR
                ? directionToNeighbour == Direction.DOWN
                : directionToNeighbour == Direction.UP;
        if (supportRemoved && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACE) == AttachFace.FLOOR ? FLOOR_SHAPE : CEILING_SHAPE;
    }
}
