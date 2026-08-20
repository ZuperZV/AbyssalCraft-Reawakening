package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.portal.AbyssalWastelandPortalShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.portal.BasePortalShape;

import java.util.Optional;

public class GatewayKeyItem extends Item {
    static ResourceKey<Level> fromDim = null;
    static ResourceKey<Level> toDim = null;

    public GatewayKeyItem(ResourceKey<Level> FromDim, ResourceKey<Level> ToDim, Properties properties) {
        super(properties);

        fromDim = FromDim;
        toDim = ToDim;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clickedPos = context.getClickedPos();

        if (inPortalDimension(level)) {
            Optional<BasePortalShape> optionalShape =
                    AbyssalWastelandPortalShape.findEmptyPortalShape(level, clickedPos.above(), Direction.Axis.X);

            if (optionalShape.isPresent()) {
                optionalShape.get().createPortalBlocks(level);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean inPortalDimension(Level level) {
        return level.dimension() == fromDim || level.dimension() == toDim;
    }
}