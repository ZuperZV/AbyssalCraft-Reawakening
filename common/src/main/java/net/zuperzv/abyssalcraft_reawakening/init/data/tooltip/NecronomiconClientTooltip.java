package net.zuperzv.abyssalcraft_reawakening.init.data.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.NecronomiconItem;

import java.awt.*;

public class NecronomiconClientTooltip implements ClientTooltipComponent {
    private final NecronomiconTooltipComponent component;

    private final int linesY = 12;
    
    public NecronomiconClientTooltip(NecronomiconTooltipComponent component) {
        this.component = component;
    }

    @Override
    public int getWidth(Font font) {
        return 90;
    }

    @Override
    public int getHeight(Font font) {
        return linesY;
    }

    @Override
    public void extractImage(
            Font font,
            int x,
            int y,
            int w,
            int h,
            GuiGraphicsExtractor graphics
    ) {

        renderLineWithItem(component.energy().getPotentialEnergy(), ModItems.POTENTIAL_ENERGY.get(), 0xff00ffff, Color.lightGray.getRGB(),
                    x, y - 3, graphics);
    }

    private void renderLineWithItem(
            int energy,
            Item essence,
            int color,
            int texColor,
            int x,
            int y,
            GuiGraphicsExtractor graphics
    ) {
        renderLineWithItem(energy, essence.getDefaultInstance(), color, texColor, x, y, graphics);
    }

    private void renderLineWithItem(
            int energy,
            ItemStack essence,
            int color,
            int texColor,
            int x,
            int y,
            GuiGraphicsExtractor graphics
    ) {

        int maxEnergy = NecronomiconItem.getMaxPotentialEnergy(component.item());

        float percent = Math.min(
                1.0f,
                (float) energy / (float) maxEnergy
        );

        renderEnergyItem(
                essence,
                percent,
                x,
                y - 1,
                graphics
        );

        int width = 70;

        graphics.fill(
                x + 18 + 1,
                y + 3 + 1,
                x + 18 + 1 + (int)(width * percent),
                y + 11 + 1,
                darkenColor(color, 0.6f)
        );

        graphics.fill(
                x + 18,
                y + 3,
                x + 18 + (int)(width * percent),
                y + 11,
                color
        );

        graphics.text(
                Minecraft.getInstance().font,
                (int)energy + "/" + maxEnergy + " PE",
                x + 18 + 4,
                y + 3,
                texColor
        );
    }

    private void renderEnergyItem(
            ItemStack item,
            float progress,
            int x,
            int y,
            GuiGraphicsExtractor graphics
    ) {
        int size = 16;
        ItemStack gray = item.copy();
        gray.set(ModDataComponentTypes.GRAYSCALE.get(), Boolean.TRUE);

        graphics.item(gray, x, y, 0);

        int visibleHeight = (int)((size - 2) * progress);

        graphics.enableScissor(
                x,
                y + (size - 2 - visibleHeight),
                x + size,
                y + size
        );

        graphics.item(item, x, y, 0);

        graphics.disableScissor();
    }

    private int darkenColor(int color, float factor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = (int)(red * factor);
        green = (int)(green * factor);
        blue = (int)(blue * factor);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}