package net.zuperzv.abyssalcraft_reawakening.init.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public class AbyssalWastelandPortalShape extends BasePortalShape{
    public AbyssalWastelandPortalShape(Axis axis, int portalBlockCount, Direction rightDir, BlockPos bottomLeft, int width, int height) {
        super(axis, portalBlockCount, rightDir, bottomLeft, width, height);
    }
}
