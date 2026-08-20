package net.zuperzv.abyssalcraft_reawakening.commonCode.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class NecronomiconMenu extends AbstractContainerMenu {

    public NecronomiconMenu(int containerId, Inventory inventory) {
        super(ModMenuTypes.NECRONOMICON_MENU.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}