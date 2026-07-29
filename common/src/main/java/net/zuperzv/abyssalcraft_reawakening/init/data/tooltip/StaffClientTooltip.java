package net.zuperzv.abyssalcraft_reawakening.init.data.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyEntry;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.StaffOfRendingItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StaffClientTooltip implements ClientTooltipComponent {

    private final StaffTooltipComponent component;

    private final int linesY = 16;

    private final List<EnergyEntry> visibleEnergy = new ArrayList<>();

    public StaffClientTooltip(StaffTooltipComponent component) {
        this.component = component;

        if (component.energy() == null) {
            return;
        }

        if (component.energy().energy() == null) {
            return;
        }

        for (EnergyEntry entry : component.energy().energy()) {
            if (entry != null && entry.getAmount() > 0) {
                visibleEnergy.add(entry);
            }
        }
    }

    @Override
    public int getWidth(Font font) {
        return visibleEnergy.isEmpty() ? 0 : 93;
    }

    private int getLineCount() {
        if (component.energy() == null) {
            return 0;
        }

        int lines = 0;

        for (EnergyEntry entry : component.energy().energy()) {
            if (entry.getAmount() > 0) {
                lines++;
            }
        }

        return lines;
    }

    @Override
    public int getHeight(Font font) {
        return getLineCount() * linesY;
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

        int line = 0;

        for (EnergyEntry entry : visibleEnergy) {
            switch (entry.getType()) {

                case CORALIUM ->
                        renderLineWithItem(entry.getAmount(), ModItems.ABYSSAL_WASTELAND_ESSENCE.get(), 0xff00ffff, Color.lightGray.getRGB(), //CORALIUM_FRAGMENT
                                x, y + line * linesY, graphics);

                case DREAD ->
                        renderLineWithItem(entry.getAmount(), ModItems.DREADLANDS_ESSENCE.get(), 0xffaa0000, Color.lightGray.getRGB(), //DREAD_FRAGMENT
                                x, y + line * linesY, graphics);

                case OMOTHOL ->
                        renderLineWithItem(entry.getAmount(), ModItems.OMOTHOL_ESSENCE.get(), 0xff5500ff, Color.lightGray.getRGB(), //OMOTHOL_FRAGMENT
                                x, y + line * linesY, graphics);

                case SHADOW ->
                        renderLineWithItem(entry.getAmount(), ModItems.SHADOW_GEM.get(), 0xffff3c5b, Color.lightGray.getRGB(),
                                x, y + line * linesY, graphics);
            }

            line++;
        }
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

        int maxEnergy = 1000;

        if (component.item().getItem() instanceof StaffOfRendingItem staffOfRendingItem) {
            maxEnergy = staffOfRendingItem.getMaxEnergy();
        }

        float percent = Math.min(
                1.0f,
                (float) energy / (float) maxEnergy
        );

        renderEnergyItem(
                essence,
                percent,
                x,
                y,
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
                (int)energy + "/" + maxEnergy,
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