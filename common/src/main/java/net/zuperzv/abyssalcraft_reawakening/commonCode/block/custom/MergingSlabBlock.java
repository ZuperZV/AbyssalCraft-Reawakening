package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class MergingSlabBlock extends SlabBlock {
    public MergingSlabBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (state.getValue(TYPE) != SlabType.DOUBLE
                && context.getItemInHand().is(this.asItem())) {
            return true;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existingState = context.getLevel().getBlockState(context.getClickedPos());
        if (existingState.is(this)
                && existingState.getValue(TYPE) != SlabType.DOUBLE
                && context.getItemInHand().is(this.asItem())) {
            return existingState.setValue(TYPE, SlabType.DOUBLE);
        }
        return super.getStateForPlacement(context);
    }
}
