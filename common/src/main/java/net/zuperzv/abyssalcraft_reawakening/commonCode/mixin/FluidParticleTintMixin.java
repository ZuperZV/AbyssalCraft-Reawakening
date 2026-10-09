package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.CustomFluidTints;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.ColorBubbleParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SingleQuadParticle.class)
public abstract class FluidParticleTintMixin {
    @Shadow protected float rCol;
    @Shadow protected float gCol;
    @Shadow protected float bCol;

    @Inject(method = "extract", at = @At("HEAD"))
    private void abyssalcraft$tintNearCustomFluid(
            QuadParticleRenderState renderState,
            Camera camera,
            float partialTick,
            CallbackInfo ci
    ) {
        SingleQuadParticle particle = (SingleQuadParticle) (Object) this;
        if (!(particle instanceof WaterDropParticle)
                && !(particle instanceof BubbleParticle)
                && !(particle instanceof ColorBubbleParticle)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        ParticlePositionAccessor position = (ParticlePositionAccessor) particle;
        double x = position.abyssalcraft$getParticleX();
        double y = position.abyssalcraft$getParticleY();
        double z = position.abyssalcraft$getParticleZ();
        BlockPos particlePos = BlockPos.containing(x, y, z);
        BlockPos tintPos = null;
        double closestDistance = Double.MAX_VALUE;
        for (BlockPos candidate : BlockPos.betweenClosed(
                particlePos.offset(-1, -1, -1),
                particlePos.offset(1, 1, 1)
        )) {
            Fluid fluid = minecraft.level.getFluidState(candidate).getType();
            if (CustomFluidTints.getColor(fluid) == null) {
                continue;
            }
            double distance = candidate.distToCenterSqr(x, y, z);
            if (distance < closestDistance) {
                closestDistance = distance;
                tintPos = candidate.immutable();
            }
        }

        if (tintPos == null) {
            return;
        }

        Fluid fluid = minecraft.level.getFluidState(tintPos).getType();
        int color = CustomFluidTints.getColorForParticles(fluid, minecraft.level, tintPos);
        rCol = ((color >> 16) & 0xFF) / 255.0F;
        gCol = ((color >> 8) & 0xFF) / 255.0F;
        bCol = (color & 0xFF) / 255.0F;
    }
}
