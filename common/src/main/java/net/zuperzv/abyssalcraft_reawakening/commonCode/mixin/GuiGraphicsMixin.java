package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.decorator.ItemDecoratorRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsMixin {

    @Inject(
            method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V",
            at = @At("TAIL")
    )
    private void abyssalcraft$renderDecorations(
            Font font,
            ItemStack stack,
            int x,
            int y,
            CallbackInfo ci
    ) {

        GuiGraphicsExtractor guiGraphics =
                (GuiGraphicsExtractor)(Object)this;


        for (var decorator : ItemDecoratorRegistry.getDecorators()) {

            decorator.render(
                    guiGraphics,
                    font,
                    stack,
                    x,
                    y
            );

        }
    }
}