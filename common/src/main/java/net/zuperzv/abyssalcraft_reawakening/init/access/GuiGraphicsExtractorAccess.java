package net.zuperzv.abyssalcraft_reawakening.init.access;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;

public interface GuiGraphicsExtractorAccess {

    void abyssalcraft$addGuiElement(
            GuiElementRenderState renderState
    );

    static GuiGraphicsExtractorAccess of(
            GuiGraphicsExtractor graphics
    ) {
        return (GuiGraphicsExtractorAccess) graphics;
    }
}