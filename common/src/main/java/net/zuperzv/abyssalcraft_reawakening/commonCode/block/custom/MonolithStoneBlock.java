package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.NecronomiconItem;

public class MonolithStoneBlock extends Block {
    public MonolithStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(stack.getItem() instanceof NecronomiconItem) || !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        boolean valid = PlaceOfPowerStructure.matches(level, pos);
        player.sendSystemMessage(net.minecraft.network.chat.Component.translatable(
                valid ? "message.abyssalcraft_reawakening.place_of_power_active"
                        : "message.abyssalcraft_reawakening.place_of_power_incomplete"
        ));
        return InteractionResult.SUCCESS;
    }
}
