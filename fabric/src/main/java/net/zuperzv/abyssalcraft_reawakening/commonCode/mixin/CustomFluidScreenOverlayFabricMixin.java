package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.CustomFluidTints;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScreenEffectRenderer.class)
public abstract class CustomFluidScreenOverlayFabricMixin {

    @ModifyArg(
            method = "renderWater",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            ),
            index = 0
    )
    private static int abyssalcraft$tintWaterOverlay(int originalColor) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return originalColor;
        }

        var player = minecraft.player;
        BlockPos pos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
        Fluid fluid = minecraft.level.getFluidState(pos).getType();
        Integer tint = CustomFluidTints.getColor(fluid, minecraft.level, pos);
        if (tint == null) {
            return originalColor;
        }

        int alpha = originalColor & 0xFF000000;
        int red = (((originalColor >> 16) & 0xFF) * ((tint >> 16) & 0xFF)) / 255;
        int green = (((originalColor >> 8) & 0xFF) * ((tint >> 8) & 0xFF)) / 255;
        int blue = ((originalColor & 0xFF) * (tint & 0xFF)) / 255;
        return alpha | (red << 16) | (green << 8) | blue;
    }
}
