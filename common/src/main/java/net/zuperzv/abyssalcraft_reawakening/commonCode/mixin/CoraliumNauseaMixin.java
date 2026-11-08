package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.client.renderer.GameRenderer.class)
public abstract class CoraliumNauseaMixin {
    private static final float CORALIUM_NAUSEA_STRENGTH = 0.2F;

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F"
            )
    )
    private float abyssalcraft$reduceCoraliumNauseaWhileTicking(float nauseaStrength) {
        return abyssalcraft$applyCoraliumNausea(nauseaStrength);
    }

    @ModifyExpressionValue(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F"
            )
    )
    private float abyssalcraft$reduceCoraliumNauseaWhileRendering(float nauseaStrength) {
        return abyssalcraft$applyCoraliumNausea(nauseaStrength);
    }

    private static float abyssalcraft$applyCoraliumNausea(float nauseaStrength) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return nauseaStrength;
        }

        MobEffect coraliumPlague = ModEffects.CORALIUM_PLAGUE.get();
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(coraliumPlague))) {
            return Math.max(nauseaStrength, CORALIUM_NAUSEA_STRENGTH);
        }

        return nauseaStrength;
    }
}
