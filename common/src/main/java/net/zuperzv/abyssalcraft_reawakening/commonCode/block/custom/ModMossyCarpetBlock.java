
package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MossyCarpetBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jspecify.annotations.Nullable;

import java.util.function.BooleanSupplier;

public class ModMossyCarpetBlock extends MossyCarpetBlock {

    public ModMossyCarpetBlock(Properties properties) {
        super(properties);
    }

    private BlockState getUpdatedModState(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            boolean createSides
    ) {
        BlockState aboveState = null;
        BlockState belowState = null;
        createSides |= state.getValue(BASE);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            EnumProperty<WallSide> property = getPropertyForFace(direction);

            WallSide side = MultifaceBlock.canAttachTo(level, pos, direction)
                    ? (createSides ? WallSide.LOW : state.getValue(property))
                    : WallSide.NONE;

            if (side == WallSide.LOW) {
                if (aboveState == null) {
                    aboveState = level.getBlockState(pos.above());
                }

                if (aboveState.is(this)
                        && aboveState.getValue(property) != WallSide.NONE
                        && !aboveState.getValue(BASE)) {
                    side = WallSide.TALL;
                }

                if (!state.getValue(BASE)) {
                    if (belowState == null) {
                        belowState = level.getBlockState(pos.below());
                    }

                    if (belowState.is(this)
                            && belowState.getValue(property) == WallSide.NONE) {
                        side = WallSide.NONE;
                    }
                }
            }

            state = state.setValue(property, side);
        }

        return state;
    }

    private static boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? false : MultifaceBlock.canAttachTo(level, pos, direction);
    }

    private BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos, boolean createSides) {
        BlockState aboveState = null;
        BlockState belowState = null;
        createSides |= state.getValue(BASE);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            EnumProperty<WallSide> property = getPropertyForFace(direction);
            WallSide side = canSupportAtFace(level, pos, direction) ? (createSides ? WallSide.LOW : state.getValue(property)) : WallSide.NONE;
            if (side == WallSide.LOW) {
                if (aboveState == null) {
                    aboveState = level.getBlockState(pos.above());
                }

                if (aboveState.is(this) && aboveState.getValue(property) != WallSide.NONE && !aboveState.getValue(BASE)) {
                    side = WallSide.TALL;
                }

                if (!state.getValue(BASE)) {
                    if (belowState == null) {
                        belowState = level.getBlockState(pos.below());
                    }

                    if (belowState.is(this) && belowState.getValue(property) == WallSide.NONE) {
                        side = WallSide.NONE;
                    }
                }
            }

            state = state.setValue(property, side);
        }

        return state;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getUpdatedState(this.defaultBlockState(), context.getLevel(), context.getClickedPos(), true);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            net.minecraft.world.level.LevelReader level,
            net.minecraft.world.level.ScheduledTickAccess ticks,
            BlockPos pos,
            Direction directionToNeighbour,
            BlockPos neighbourPos,
            BlockState neighbourState,
            RandomSource random
    ) {
        if (!state.canSurvive(level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }

        BlockState updated = getUpdatedModState(state, level, pos, false);

        return updated.getValue(BASE)
                || updated.getValue(NORTH) != WallSide.NONE
                || updated.getValue(EAST) != WallSide.NONE
                || updated.getValue(SOUTH) != WallSide.NONE
                || updated.getValue(WEST) != WallSide.NONE
                ? updated
                : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        if (!level.isClientSide()) {
            RandomSource random = level.getRandom();
            BlockState topper = createTopperWithSideChance(level, pos, random::nextBoolean);
            if (!topper.isAir()) {
                level.setBlock(pos.above(), topper, 3);
            }
        }
    }

    private BlockState createTopperWithSideChance(BlockGetter level, BlockPos pos, BooleanSupplier sideSurvivalTest) {
        BlockPos above = pos.above();
        BlockState abovePreviousState = level.getBlockState(above);
        boolean isMossyCarpetAbove = abovePreviousState.is(this);
        if ((!isMossyCarpetAbove || !abovePreviousState.getValue(BASE)) && (isMossyCarpetAbove || abovePreviousState.canBeReplaced())) {
            BlockState noCarpetBaseState = this.defaultBlockState().setValue(BASE, false);
            BlockState aboveState = getUpdatedState(noCarpetBaseState, level, pos.above(), true);

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                EnumProperty<WallSide> property = getPropertyForFace(direction);
                if (aboveState.getValue(property) != WallSide.NONE && !sideSurvivalTest.getAsBoolean()) {
                    aboveState = aboveState.setValue(property, WallSide.NONE);
                }
            }

            return hasFaces(aboveState) && aboveState != abovePreviousState ? aboveState : Blocks.AIR.defaultBlockState();
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    private static boolean hasFaces(BlockState blockState) {
        if (blockState.getValue(BASE)) {
            return true;
        } else {
            for (EnumProperty<WallSide> property : PROPERTY_BY_DIRECTION.values()) {
                if (blockState.getValue(property) != WallSide.NONE) {
                    return true;
                }
            }

            return false;
        }
    }
}