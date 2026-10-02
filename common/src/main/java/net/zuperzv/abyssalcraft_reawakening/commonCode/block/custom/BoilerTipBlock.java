package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.BoilerTipBlockEntity;
import org.jetbrains.annotations.Nullable;

public class BoilerTipBlock extends Block implements EntityBlock {
    public static final MapCodec<BoilerTipBlock> CODEC = simpleCodec(BoilerTipBlock::new);
    public static final BooleanProperty ON = BooleanProperty.create("on");
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            box(5.5, 10, 14, 6.5, 13, 17),
            box(9.5, 10, 14, 10.5, 13, 17),
            box(6.5, 10, 14, 9.5, 11, 17)
    );
    public BoilerTipBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ON, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return rotateShape(Direction.NORTH, state.getValue(FACING), SHAPE_NORTH);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(pos) instanceof BoilerTipBlockEntity tip)) {
            return InteractionResult.PASS;
        }

        int slot = 0;
        if (!state.getValue(ON)
                || !tip.isExtracting(slot) && tip.findBoilerBelow(level, pos) == null) {
            return InteractionResult.FAIL;
        }

        tip.setExtracting(slot, !tip.isExtracting(slot));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        BlockPos sourcePos = context.getClickedPos().relative(facing.getOpposite());
        boolean connected = context.getLevel().getBlockState(sourcePos).is(ModBlocks.ESSENCE_BOILER.block().get());
        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(ON, connected);
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            @Nullable net.minecraft.world.level.redstone.Orientation orientation,
            boolean movedByPiston
    ) {
        if (level.isClientSide()) {
            return;
        }

        Direction facing = state.getValue(FACING);
        BlockPos sourcePos = pos.relative(facing.getOpposite());
        boolean connected = level.getBlockState(sourcePos).is(ModBlocks.ESSENCE_BOILER.block().get());
        if (state.getValue(ON) != connected) {
            level.setBlock(pos, state.setValue(ON, connected), Block.UPDATE_ALL);
        }

        if (level.getBlockEntity(pos) instanceof BoilerTipBlockEntity tip && !connected) {
            tip.clearConnections();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ON);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BoilerTipBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide()) {
            return null;
        }

        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof BoilerTipBlockEntity tip) {
                BoilerTipBlockEntity.tick(lvl, pos, blockState, tip);
            }
        };
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        if (from == to) {
            return shape;
        }

        VoxelShape[] buffer = {shape, Shapes.empty()};
        if (to.getAxis().isHorizontal()) {
            int turns = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
            for (int i = 0; i < turns; i++) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                        buffer[1] = Shapes.or(
                                buffer[1],
                                Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)
                        ));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }
        } else if (to == Direction.UP) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = Shapes.or(
                            buffer[1],
                            Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY)
                    ));
            buffer[0] = buffer[1];
        } else if (to == Direction.DOWN) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = Shapes.or(
                            buffer[1],
                            Shapes.box(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY)
                    ));
            buffer[0] = buffer[1];
        }

        return buffer[0];
    }
}
