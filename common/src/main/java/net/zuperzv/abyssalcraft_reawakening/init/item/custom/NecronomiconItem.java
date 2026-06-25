package net.zuperzv.abyssalcraft_reawakening.init.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconMenu;

public class NecronomiconItem extends Item {

    public NecronomiconItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            player.openMenu(this.getMenuProvider(stack));
        }

        return level.isClientSide()
                ? InteractionResult.SUCCESS
                : InteractionResult.CONSUME;
    }

    private MenuProvider getMenuProvider(ItemStack itemStack) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return itemStack.getHoverName();
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new NecronomiconMenu(i, inventory);
            }
        };
    }
}