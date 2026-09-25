package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class MobEffectOverlayImageMixin {

    @Unique
    private static final Identifier ABYSSALCRAFT$HARMFUL_OVERLAY =
            Identifier.fromNamespaceAndPath(
                    Constants.MOD_ID,
                    "mob_effect/harmful_overlay"
            );

    @Unique
    private static MobEffect abyssalcraft$currentEffect;

    @Redirect(
            method = "extractEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffect;isBeneficial()Z"
            )
    )
    private boolean abyssalcraft$captureEffect(
            MobEffect effect
    ) {
        abyssalcraft$currentEffect = effect;

        return effect.isBeneficial();
    }

    @Redirect(
            method = "extractEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(" +
                            "Lcom/mojang/blaze3d/pipeline/RenderPipeline;" +
                            "Lnet/minecraft/resources/Identifier;" +
                            "IIII)V"
            )
    )
    private void abyssalcraft$renderBackground(
            GuiGraphicsExtractor graphics,
            RenderPipeline pipeline,
            Identifier sprite,
            int x,
            int y,
            int width,
            int height
    ) {
        if (abyssalcraft$isHarmful()) {
            abyssalcraft$pushShake(
                    graphics,
                    x + width / 2.0F,
                    y + height / 2.0F
            );

            graphics.blitSprite(
                    pipeline,
                    sprite,
                    x,
                    y,
                    width,
                    height
            );

            graphics.pose().popMatrix();
            return;
        }

        graphics.blitSprite(
                pipeline,
                sprite,
                x,
                y,
                width,
                height
        );
    }

    @Redirect(
            method = "extractEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(" +
                            "Lcom/mojang/blaze3d/pipeline/RenderPipeline;" +
                            "Lnet/minecraft/resources/Identifier;" +
                            "IIIII)V"
            )
    )
    private void abyssalcraft$renderEffectIcon(
            GuiGraphicsExtractor graphics,
            RenderPipeline pipeline,
            Identifier sprite,
            int x,
            int y,
            int width,
            int height,
            int color
    ) {
        if (abyssalcraft$isHarmful()) {
            abyssalcraft$pushShake(
                    graphics,
                    x + width / 2.0F,
                    y + height / 2.0F
            );

            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    ABYSSALCRAFT$HARMFUL_OVERLAY,
                    x,
                    y,
                    width,
                    height
            );

            graphics.blitSprite(
                    pipeline,
                    sprite,
                    x,
                    y,
                    width,
                    height,
                    color
            );

            graphics.pose().popMatrix();
            return;
        }

        graphics.blitSprite(
                pipeline,
                sprite,
                x,
                y,
                width,
                height,
                color
        );
    }

    @Unique
    private static boolean abyssalcraft$isHarmful() {
        return abyssalcraft$currentEffect != null
                && abyssalcraft$currentEffect.getCategory()
                == MobEffectCategory.HARMFUL;
    }

    @Unique
    private static void abyssalcraft$pushShake(
            GuiGraphicsExtractor graphics,
            float centerX,
            float centerY
    ) {
        float time =
                (System.currentTimeMillis() % 100000L) / 7000.0F;

        float xShake =
                Mth.sin(time * 18.0F) * 0.015F;

        float yShake =
                Mth.cos(time * 21.0F) * 0.009F;

        float rotation =
                Mth.sin(time * 16.0F) * 0.015F;

        graphics.pose().pushMatrix();

        graphics.pose().translate(
                xShake,
                yShake
        );

        graphics.pose().rotateAbout(
                rotation,
                centerX,
                centerY
        );
    }
}