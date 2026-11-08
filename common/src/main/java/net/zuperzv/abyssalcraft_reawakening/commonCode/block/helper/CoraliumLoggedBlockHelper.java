package net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.state.properties.ModBlockStateProperties;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;

/**
 * Helper class providing utility methods for blocks that can be "Coralium logged".
 * Similar to how {@link net.minecraft.world.level.block.SimpleWaterloggedBlock} works for water,
 * this class helps manage the CORALIUM_LOGGED property for the Coralium fluid.
 *
 * <p>To make a block support Coralium logging, implement {@link net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.SimpleCoraliumLoggedBlock}
 * and add these to your block class:</p>
 * <ul>
 *   <li>Add {@code CORALIUM_LOGGED} to {@code createBlockStateDefinition()}</li>
 *   <li>Set default state to {@code false} in the constructor</li>
 *   <li>Use {@link #getStateForPlacement(BlockState, LevelReader, BlockPos)} in {@code getStateForPlacement()}</li>
 *   <li>Override {@code getFluidState()} to return Coralium fluid when logged</li>
 *   <li>Override {@code updateShape()} to schedule fluid ticks when logged</li>
 * </ul>
 */
public final class CoraliumLoggedBlockHelper {
    private CoraliumLoggedBlockHelper() {}

    /**
     * The CORALIUM_LOGGED property to add to block state definitions.
     */
    public static final BooleanProperty CORALIUM_LOGGED = ModBlockStateProperties.CORALIUM_LOGGED;

    /**
     * Checks if a fluid state is a Coralium source.
     */
    public static boolean isCoralium(FluidState fluidState) {
        return fluidState.isSource() && isCoralium(fluidState.getType());
    }

    public static boolean isCoralium(Fluid fluid) {
        return fluid == ModFluids.SOURCE_CORALIUM.get();
    }

    public static boolean canBeCoraliumLogged(BlockState state) {
        return state.hasProperty(CORALIUM_LOGGED)
                && !state.getValue(CORALIUM_LOGGED)
                && (!state.hasProperty(BlockStateProperties.WATERLOGGED)
                || !state.getValue(BlockStateProperties.WATERLOGGED));
    }

    /**
     * Gets the source Coralium fluid state for use in getFluidState().
     */
    public static FluidState getSourceCoraliumState() {
        return ModFluids.CORALIUM_BLOCK.get().defaultBlockState().getFluidState();
    }

    /**
     * Checks if a block state is Coralium logged.
     */
    public static boolean isCoraliumLogged(BlockState state) {
        return state.hasProperty(CORALIUM_LOGGED) && state.getValue(CORALIUM_LOGGED);
    }

    /**
     * Sets the CORALIUM_LOGGED property on a block state.
     */
    public static BlockState setCoraliumLogged(BlockState state, boolean logged) {
        return state.setValue(CORALIUM_LOGGED, logged);
    }

    /**
     * Checks if any face of the given position has a Coralium fluid source.
     * Useful for neighborChanged or randomTick methods.
     */
    public static boolean hasCoraliumNeighbor(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            FluidState fluidState = level.getFluidState(pos.relative(direction));
            if (isCoralium(fluidState)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the appropriate block state for placement, considering whether
     * the position is filled with Coralium fluid.
     *
     * @param baseState the base block state (before setting CORALIUM_LOGGED)
     * @param level the level
     * @param pos the position being placed at
     * @return the block state with CORALIUM_LOGGED set appropriately
     */
    public static BlockState getStateForPlacement(BlockState baseState, LevelReader level, BlockPos pos) {
        if (!baseState.hasProperty(CORALIUM_LOGGED)) {
            return baseState;
        }
        if (baseState.hasProperty(BlockStateProperties.WATERLOGGED)
                && baseState.getValue(BlockStateProperties.WATERLOGGED)) {
            return baseState.setValue(CORALIUM_LOGGED, false);
        }
        FluidState fluidState = level.getFluidState(pos);
        return baseState.setValue(CORALIUM_LOGGED, isCoralium(fluidState));
    }

    /**
     * Updates the CORALIUM_LOGGED state based on the current fluid at the position.
     * Call this from neighborChanged or randomTick to keep the state in sync.
     */
    public static BlockState updateCoraliumLoggedState(BlockState state, LevelReader level, BlockPos pos) {
        if (!state.hasProperty(CORALIUM_LOGGED)) {
            return state;
        }
        FluidState fluidState = level.getFluidState(pos);
        boolean shouldLog = (!state.hasProperty(BlockStateProperties.WATERLOGGED)
                || !state.getValue(BlockStateProperties.WATERLOGGED))
                && isCoralium(fluidState);
        boolean isLogged = state.getValue(CORALIUM_LOGGED);
        if (isLogged != shouldLog) {
            return state.setValue(CORALIUM_LOGGED, shouldLog);
        }
        return state;
    }
}
