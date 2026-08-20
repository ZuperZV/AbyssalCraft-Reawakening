package net.zuperzv.abyssalcraft_reawakening.commonCode.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;

import java.util.Optional;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack itemStack = player.getMainHandItem();

        if (event.getLevel().isClientSide()) return;

        if (itemStack.canPerformAction(ItemAbilities.AXE_SCRAPE)) {
            itemStack.hurtAndBreak(1, player, itemStack.getEquipmentSlot());

            Optional<BlockState> stripped = getStripped(event.getLevel().getBlockState(pos));

            if (stripped.isPresent()) {
                event.getLevel().setBlockAndUpdate(pos, stripped.get());

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }


    private static Optional<BlockState> getStripped(BlockState state) {

        if (state.is(ModBlocks.WITHERWOOD_LOG.block().get())) {
            return Optional.of(
                    ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get()
                            .defaultBlockState()
                            .setValue(
                                    RotatedPillarBlock.AXIS,
                                    state.getValue(RotatedPillarBlock.AXIS)
                            )
            );
        }

        if (state.is(ModBlocks.WITHERWOOD_WOOD.block().get())) {
            return Optional.of(
                    ModBlocks.STRIPPED_WITHERWOOD_WOOD.block().get()
                            .defaultBlockState()
                            .setValue(
                                    RotatedPillarBlock.AXIS,
                                    state.getValue(RotatedPillarBlock.AXIS)
                            )
            );
        }

        return Optional.empty();
    }
}
