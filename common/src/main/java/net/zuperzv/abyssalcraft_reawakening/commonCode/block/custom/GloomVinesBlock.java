package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;

public class GloomVinesBlock extends VineBlock implements BonemealableBlock {

    public static final BooleanProperty HAS_TOP = BooleanProperty.create("has_top");
    public static final BooleanProperty HAS_BOTTOM = BooleanProperty.create("has_bottom");

    public GloomVinesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(VineBlock.EAST, false)
                .setValue(VineBlock.WEST, false)
                .setValue(VineBlock.NORTH, false)
                .setValue(VineBlock.SOUTH, false)
                .setValue(VineBlock.UP, false)
                .setValue(HAS_TOP, false)
                .setValue(HAS_BOTTOM, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VineBlock.EAST, VineBlock.WEST, VineBlock.NORTH, VineBlock.SOUTH, VineBlock.UP, HAS_TOP, HAS_BOTTOM);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        BlockPos belowPos = blockPos.below();
        BlockState belowState = levelReader.getBlockState(belowPos);
        return belowState.isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return randomSource.nextFloat() < 0.5F;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        growVineDownward(serverLevel, randomSource, blockPos, blockState);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        if (!level.isClientSide()) {
            boolean hasTop = level.getBlockState(pos.above()).getBlock() instanceof GloomVinesBlock;
            boolean hasBottom = level.getBlockState(pos.below()).getBlock() instanceof GloomVinesBlock;
            level.setBlock(pos, state.setValue(HAS_TOP, hasTop).setValue(HAS_BOTTOM, hasBottom), 3);
            // Update the vine above so it knows if it still has a bottom neighbor
            level.blockEvent(pos.above(), block, 1, 0);
            // Update the vine below so it knows if it still has a top neighbor
            level.blockEvent(pos.below(), block, 1, 0);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        // Notify adjacent vines that a neighbor was removed so they update their texture
        level.setBlock(pos.above(), level.getBlockState(pos.above()), 3);
        level.setBlock(pos.below(), level.getBlockState(pos.below()), 3);
    }

    private void growVineDownward(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        boolean east = state.getValue(VineBlock.EAST);
        boolean west = state.getValue(VineBlock.WEST);
        boolean north = state.getValue(VineBlock.NORTH);
        boolean south = state.getValue(VineBlock.SOUTH);

        int growLength = random.nextInt(1, 2); // Grow 1-2 blocks down
        for (int i = 1; i <= growLength; i++) {
            BlockPos belowPos = pos.below(i);
            BlockState belowState = level.getBlockState(belowPos);

            if (!belowState.isAir()) {
                break;
            }

            BlockState newVineState = this.defaultBlockState()
                    .setValue(VineBlock.EAST, east)
                    .setValue(VineBlock.WEST, west)
                    .setValue(VineBlock.NORTH, north)
                    .setValue(VineBlock.SOUTH, south)
                    .setValue(VineBlock.UP, false)
                    .setValue(HAS_TOP, true)
                    .setValue(HAS_BOTTOM, false);

            level.setBlock(belowPos, newVineState, 19);
        }
    }
}
