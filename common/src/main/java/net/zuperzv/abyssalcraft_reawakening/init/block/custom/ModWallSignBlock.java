package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class ModWallSignBlock extends ModSignBlock implements PlainSignBlock {
    public static final MapCodec<ModWallSignBlock> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(WoodType.CODEC.fieldOf("wood_type").forGetter(ModSignBlock::type), propertiesCodec()).apply(i, ModWallSignBlock::new));
    public static final EnumProperty<Direction> FACING;
    private static final Map<Direction, VoxelShape> SHAPES;

    public MapCodec<ModWallSignBlock> codec() {
        return CODEC;
    }

    public ModWallSignBlock(WoodType type, BlockBehaviour.Properties properties) {
        super(type, properties.sound(type.soundType()));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, false));
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape)SHAPES.get(state.getValue(FACING));
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(((Direction)state.getValue(FACING)).getOpposite())).isSolid();
    }

    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction[] directions = context.getNearestLookingDirections();

        for(Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction facing = direction.getOpposite();
                state = (BlockState)state.setValue(FACING, facing);
                if (state.canSurvive(level, pos)) {
                    return (BlockState)state.setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
                }
            }
        }

        return null;
    }

    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    public float getYRotationDegrees(BlockState state) {
        return ((Direction)state.getValue(FACING)).toYRot();
    }

    public Vec3 getSignHitboxCenterPosition(BlockState state) {
        return ((VoxelShape)SHAPES.get(state.getValue(FACING))).bounds().getCenter();
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, WATERLOGGED});
    }

    public PlainSignBlock.Attachment attachmentPoint(BlockState state) {
        return Attachment.WALL;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        SHAPES = Shapes.rotateHorizontal(Block.boxZ((double)16.0F, (double)4.5F, (double)12.5F, (double)14.0F, (double)16.0F));
    }
}
