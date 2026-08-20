package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.decorator;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StaffOfRenderingOverlay implements ItemDecorator {

    private final Identifier texture;
    private final Item targetItem;

    public StaffOfRenderingOverlay(Identifier texture, Item Item) {
        this.texture = texture;
        this.targetItem = Item;
    }

    public StaffOfRenderingOverlay(Item Item) {
        this.targetItem = Item;

        Identifier id = BuiltInRegistries.ITEM.getKey(Item);

        this.texture = Identifier.fromNamespaceAndPath(
                id.getNamespace(),
                "textures/item/" + id.getPath() + "_overlay.png"
        );
    }


    @Override
    public boolean render(
            GuiGraphicsExtractor guiGraphics,
            Font font,
            ItemStack stack,
            int x,
            int y
    ) {
        if (stack.getItem() != targetItem)
            return false;

        int value = getValue(stack);
        int max = getMaxValue(stack);

        if (value <= 0 || max <= 0)
            return false;


        float alpha = Math.min(1f, (float)value / max);

        int color = ARGB.white(alpha);


        guiGraphics.pose().pushMatrix();

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                0,
                0,
                16,
                16,
                16,
                16,
                color
        );

        guiGraphics.pose().popMatrix();

        return true;
    }


    private int getValue(ItemStack stack) {
        return 50;
    }


    private int getMaxValue(ItemStack stack) {
        return 100;
    }
}