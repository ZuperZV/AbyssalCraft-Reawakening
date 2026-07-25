package net.zuperzv.abyssalcraft_reawakening.datagen.custom;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import net.minecraft.network.chat.Component;

public record ToolTip(
        int x,
        int y,
        int width,
        int height,
        Component... lines
) {

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x
                && mouseX < x + width
                && mouseY >= y
                && mouseY < y + height;
    }

    public void addTo(ITooltipBuilder tooltip) {
        for (Component line : lines) {
            tooltip.add(line);
        }
    }
}