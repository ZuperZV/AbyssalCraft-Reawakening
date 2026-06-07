package net.zuperzv.abyssalcraft_reawakening.init.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;

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