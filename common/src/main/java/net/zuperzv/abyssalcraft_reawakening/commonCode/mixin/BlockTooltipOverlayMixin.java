package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.zuperzv.abyssalcraft_reawakening.commonCode.screen.BlockTooltipScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class BlockTooltipOverlayMixin {

    @Inject(
            method = "extractRenderState",
            at = @At("TAIL")
    )
    private void abyssalcraft$renderBlockTooltip(
            GuiGraphicsExtractor graphics,
            net.minecraft.client.DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen != null || minecraft.options.hideGui) {
            return;
        }

        BlockTooltipScreen.renderOverlay(graphics);
    }
}
