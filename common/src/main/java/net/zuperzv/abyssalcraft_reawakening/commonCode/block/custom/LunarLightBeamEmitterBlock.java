package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.LunarLightBeamEmitterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class LunarLightBeamEmitterBlock extends BaseEntityBlock {
    public static final MapCodec<LunarLightBeamEmitterBlock> CODEC =
            simpleCodec(LunarLightBeamEmitterBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final VoxelShape SOUTH_SHAPE = Shapes.or(
            box(5, 5, 17, 11, 11, 20),
            box(6, 6, 13, 10, 10, 15),
            box(9.75, 9.75, 14, 11.75, 11.75, 19),
            box(7, 7, 9, 9, 9, 14),
            box(4.25, 9.75, 14, 6.25, 11.75, 19),
            box(4.25, 4.25, 14, 6.25, 6.25, 19),
            box(9.75, 4.25, 14, 11.75, 6.25, 19)
    );

    public LunarLightBeamEmitterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LunarLightBeamEmitterBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return rotateShape(SOUTH_SHAPE, state.getValue(FACING));
    }

    private static VoxelShape rotateShape(VoxelShape shape, Direction facing) {
        if (facing == Direction.NORTH) return shape;

        VoxelShape[] result = {Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double rotatedMinX = Double.POSITIVE_INFINITY;
            double rotatedMinY = Double.POSITIVE_INFINITY;
            double rotatedMinZ = Double.POSITIVE_INFINITY;
            double rotatedMaxX = Double.NEGATIVE_INFINITY;
            double rotatedMaxY = Double.NEGATIVE_INFINITY;
            double rotatedMaxZ = Double.NEGATIVE_INFINITY;
            for (double x : new double[]{minX, maxX}) {
                for (double y : new double[]{minY, maxY}) {
                    for (double z : new double[]{minZ, maxZ}) {
                        double rx;
                        double ry;
                        double rz;
                        switch (facing) {
                            case SOUTH -> { rx = 1 - x; ry = y; rz = 1 - z; }
                            case NORTH -> { rx = x; ry = y; rz = z; }
                            case WEST -> { rx = z; ry = y; rz = 1 - x; }
                            case EAST -> { rx = 1 - z; ry = y; rz = x; }
                            case UP -> { rx = x; ry = z; rz = 1 - y; }
                            case DOWN -> { rx = x; ry = 1 - z; rz = y; }
                            default -> { rx = x; ry = y; rz = z; }
                        }
                        rotatedMinX = Math.min(rotatedMinX, rx);
                        rotatedMinY = Math.min(rotatedMinY, ry);
                        rotatedMinZ = Math.min(rotatedMinZ, rz);
                        rotatedMaxX = Math.max(rotatedMaxX, rx);
                        rotatedMaxY = Math.max(rotatedMaxY, ry);
                        rotatedMaxZ = Math.max(rotatedMaxZ, rz);
                    }
                }
            }
            result[0] = Shapes.or(result[0], Shapes.box(
                    rotatedMinX, rotatedMinY, rotatedMinZ,
                    rotatedMaxX, rotatedMaxY, rotatedMaxZ
            ));
        });
        return result[0];
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        if (type != ModBlockEntities.LUNAR_LIGHT_BEAM_EMITTER_BE.get()) return null;
        return (world, pos, blockState, entity) -> {
            if (entity instanceof LunarLightBeamEmitterBlockEntity emitter) {
                LunarLightBeamEmitterBlockEntity.tick(world, pos, blockState, emitter);
            }
        };
    }
}
