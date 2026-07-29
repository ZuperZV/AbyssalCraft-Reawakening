package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.TeleportTransition;
import org.jspecify.annotations.Nullable;

public class AbyssalWastelandPortalBlock extends BasePortalBlock{
    public AbyssalWastelandPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        ResourceKey<Level> newDimension = currentLevel.dimension() == Level.END ? Level.OVERWORLD : Level.END;
        ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
        if (newLevel == null) {
            return null;
        } else {
            boolean toNether = newLevel.dimension() == Level.END;
            WorldBorder newWorldBorder = newLevel.getWorldBorder();
            double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
            BlockPos approximateExitPos = newWorldBorder.clampToBounds(entity.getX() * teleportationScale, entity.getY(), entity.getZ() * teleportationScale);
            return this.getExitPortal(newLevel, entity, portalEntryPos, approximateExitPos, toNether, newWorldBorder);
        }
    }
}
