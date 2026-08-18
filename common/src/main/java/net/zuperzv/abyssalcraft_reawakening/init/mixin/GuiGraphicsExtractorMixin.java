package net.zuperzv.abyssalcraft_reawakening.init.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.zuperzv.abyssalcraft_reawakening.init.access.GuiGraphicsExtractorAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin
        implements GuiGraphicsExtractorAccess {

    @Shadow
    private GuiRenderState guiRenderState;

    @Override
    public void abyssalcraft$addGuiElement(
            GuiElementRenderState renderState
    ) {
        guiRenderState.addGuiElement(renderState);
    }
}