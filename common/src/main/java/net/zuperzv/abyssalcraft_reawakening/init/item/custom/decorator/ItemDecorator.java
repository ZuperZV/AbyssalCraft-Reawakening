package net.zuperzv.abyssalcraft_reawakening.init.item.custom.decorator;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public interface ItemDecorator {

    boolean render(
            GuiGraphicsExtractor guiGraphics,
            Font font,
            ItemStack stack,
            int x,
            int y
    );
}