package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.BasePortalBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;

public class PortalBlockEntity extends TheEndPortalBlockEntity {
    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTAL_BE.get(), pos, state);
    }

    @Override
    public boolean shouldRenderFace(Direction direction) {
        Direction.Axis axis = getBlockState().getValue(BasePortalBlock.AXIS);
        if (axis == Direction.Axis.X) {
            return direction == Direction.NORTH || direction == Direction.SOUTH;
        } else {
            return direction == Direction.EAST || direction == Direction.WEST;
        }
    }
}
