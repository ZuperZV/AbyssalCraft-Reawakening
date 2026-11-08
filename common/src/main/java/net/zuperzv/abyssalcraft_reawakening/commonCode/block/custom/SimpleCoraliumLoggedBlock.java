package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper.CoraliumLoggedBlockHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.jspecify.annotations.Nullable;

/**
 * Interface for blocks that can be "Coralium logged" — filled with Coralium fluid,
 * similar to how {@link net.minecraft.world.level.block.SimpleWaterloggedBlock} works for water.
 *
 * <p>To make a block support Coralium logging:</p>
 * <ol>
 *   <li>Implement this interface on your block class</li>
 *   <li>Add {@code CoraliumLoggedBlockHelper.CORALIUM_LOGGED} to {@code createBlockStateDefinition()}</li>
 *   <li>Set default state to {@code false} in the constructor</li>
 *   <li>Override {@link Block#getFluidState(BlockState)} to return Coralium fluid when logged</li>
 *   <li>Override {@link Block#updateShape} to schedule fluid ticks when logged</li>
 *   <li>Use {@link CoraliumLoggedBlockHelper#getStateForPlacement(BlockState, BlockGetter, BlockPos)} in {@code getStateForPlacement()}</li>
 * </ol>
 *
 * <p>Example getFluidState():</p>
 * <pre>
 * {@code
 * protected FluidState getFluidState(BlockState state) {
 *     if (state.getValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)) {
 *         return CoraliumLoggedBlockHelper.getSourceCoraliumState();
 *     }
 *     return super.getFluidState(state);
 * }
 * }
 * </pre>
 *
 * <p>Example updateShape():</p>
 * <pre>
 * {@code
 * protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
 *         BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos,
 *         BlockState neighbourState, RandomSource random) {
 *     if (state.getValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)) {
 *         ticks.scheduleTick(pos, ModFluids.SOURCE_CORALIUM.get(),
 *                 ModFluids.SOURCE_CORALIUM.get().getTickDelay(level));
 *     }
 *     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
 * }
 * }
 * </pre>
 */
public interface SimpleCoraliumLoggedBlock extends BucketPickup, LiquidBlockContainer {

    @Override
    default boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos,
            BlockState state, Fluid type) {
        return CoraliumLoggedBlockHelper.isCoralium(type)
                && CoraliumLoggedBlockHelper.canBeCoraliumLogged(state);
    }

    @Override
    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        BooleanProperty coraliumLogged = CoraliumLoggedBlockHelper.CORALIUM_LOGGED;
        if (CoraliumLoggedBlockHelper.canBeCoraliumLogged(state)
                && CoraliumLoggedBlockHelper.isCoralium(fluidState)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(coraliumLogged, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }
            return true;
        }
        return false;
    }

    @Override
    default ItemStack pickupBlock(@Nullable LivingEntity entity, LevelAccessor level, BlockPos pos,
            BlockState state) {
        if (state.hasProperty(CoraliumLoggedBlockHelper.CORALIUM_LOGGED) && state.getValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)) {
            level.setBlock(pos, state.setValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED, false), 3);
            return new ItemStack(ModFluids.CORALIUM_BUCKET.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    default java.util.Optional<SoundEvent> getPickupSound() {
        return java.util.Optional.of(SoundEvents.BUCKET_EMPTY_LAVA);
    }
}
