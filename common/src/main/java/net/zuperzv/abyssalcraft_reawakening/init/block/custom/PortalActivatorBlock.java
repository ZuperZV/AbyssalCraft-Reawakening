package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.portal.AbyssalWastelandPortalShape;
import net.zuperzv.abyssalcraft_reawakening.init.portal.BasePortalShape;

import java.util.Optional;

public class PortalActivatorBlock extends Block {
    public PortalActivatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.getItem() == ModItems.SHADOW_GEM.get()) {

            if (inPortalDimension(level)) {
                Optional<BasePortalShape> optionalShape =
                        AbyssalWastelandPortalShape.findEmptyPortalShape(level, pos.above(), Direction.Axis.X);

                if (optionalShape.isPresent()) {
                    optionalShape.get().createPortalBlocks(level);
                    return InteractionResult.SUCCESS;
                }
            }

        }
        return InteractionResult.FAIL;
    }

    private static boolean inPortalDimension(Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER;
    }
}
