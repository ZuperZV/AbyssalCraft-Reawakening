package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.CustomFluidTints;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.fog.FogRenderer")
public abstract class CustomFluidFogColorMixin {

    @Inject(method = "computeFogColor", at = @At("TAIL"))
    private void abyssalcraft$tintCustomFluidFog(
            Camera camera,
            float partialTick,
            ClientLevel level,
            int renderDistance,
            float darkenWorldAmount,
            Vector4f fogColor,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        var player = minecraft.player;
        BlockPos pos = BlockPos.containing(
                player.getX(),
                player.getEyeY(),
                player.getZ()
        );
        Integer tint = CustomFluidTints.getColor(
                minecraft.level.getFluidState(pos).getType(),
                minecraft.level,
                pos
        );
        if (tint == null) {
            return;
        }

        fogColor.set(
                ((tint >> 16) & 0xFF) / 255.0F,
                ((tint >> 8) & 0xFF) / 255.0F,
                (tint & 0xFF) / 255.0F,
                fogColor.w()
        );
    }
}
