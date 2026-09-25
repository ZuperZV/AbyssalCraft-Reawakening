package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MobEffectOverlayMixin {

    @Shadow
    private Minecraft minecraft;

    @Unique
    private static final Identifier CORALIUM_PLAGUE_OVERLAY =
            Identifier.fromNamespaceAndPath(
                    Constants.MOD_ID,
                    "textures/misc/coralium_plague.png"
            );

    @Inject(
            method = "extractCameraOverlays",
            at = @At("TAIL")
    )
    private void abyssalcraft$renderCoraliumPlagueOverlay(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        Player player = this.minecraft.player;

        if (player == null) {
            return;
        }

        MobEffectInstance plague = null;

        for (MobEffectInstance instance : player.getActiveEffects()) {
            Holder<MobEffect> effect = instance.getEffect();

            /*
            if (effect.value() == ModEffects.CORALIUM_PLAGUE.get()) {
                plague = instance;
                break;
            }
             */
        }

        if (plague == null) {
            return;
        }

        float strength = 0.35F;

        abyssalcraft$renderOverlay(
                graphics,
                strength,
                CORALIUM_PLAGUE_OVERLAY,
                this.minecraft.level.getGameTime()
                        + deltaTracker.getGameTimeDeltaPartialTick(false)
        );
    }

    @Unique
    private static void abyssalcraft$renderOverlay(
            GuiGraphicsExtractor graphics,
            float strength,
            Identifier texture,
            float time
    ) {
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        graphics.pose().pushMatrix();

        float size = Mth.lerp(
                strength,
                1.35F,
                1.0F
        );

        graphics.pose().translate(
                screenWidth / 2.0F,
                screenHeight / 2.0F
        );

        graphics.pose().scale(
                size,
                size
        );

        graphics.pose().translate(
                -screenWidth / 2.0F,
                -screenHeight / 2.0F
        );

        float pulse = (Mth.sin(time * 0.08F) + 1.0F) * 0.5F;

        float alpha = Mth.lerp(
                pulse,
                0.05F,
                0.35F
        );

        graphics.blit(
                RenderPipelines.GUI_NAUSEA_OVERLAY,
                texture,
                0,
                0,
                0.0F,
                0.0F,
                screenWidth,
                screenHeight,
                screenWidth,
                screenHeight,
                ARGB.colorFromFloat(
                        alpha,
                        1.0F,
                        1.0F,
                        1.0F
                )
        );

        graphics.pose().popMatrix();
    }
}