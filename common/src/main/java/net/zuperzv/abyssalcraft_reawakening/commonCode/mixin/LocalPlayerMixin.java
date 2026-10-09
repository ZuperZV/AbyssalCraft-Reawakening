package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.ModHangingSignBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.CustomFluidTints;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.ColorBubbleData;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Unique
    private Fluid abyssalcraft$previousCustomFluid;

    @Unique
    private BlockPos abyssalcraft$customFluidContactPos;

    @Unique
    private boolean abyssalcraft$roseInCustomFluid;

    @Inject(
            method = "openTextEdit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void abyssalcraft$customSignScreen(
            SignBlockEntity sign,
            boolean isFrontText,
            CallbackInfo ci
    ) {

        if (sign instanceof ModHangingSignBlockEntity modSign) {

            Minecraft minecraft = Minecraft.getInstance();

            minecraft.setScreen(
                    new HangingSignEditScreen(
                            modSign,
                            isFrontText,
                            minecraft.isTextFilteringEnabled()
                    )
            );

            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void abyssalcraft$tintPotionFluidEntryBubbles(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        Fluid currentFluid = abyssalcraft$getCustomFluid(player);
        double verticalSpeed = player.getDeltaMovement().y;
        boolean movingUp = verticalSpeed > 0.06D;
        boolean movingDown = verticalSpeed < -0.06D;
        boolean enteringFluid = currentFluid != null
                && currentFluid != abyssalcraft$previousCustomFluid;
        boolean fallingBackIntoFluid = currentFluid != null
                && abyssalcraft$roseInCustomFluid
                && movingDown;

        if (currentFluid != null && (enteringFluid && !movingUp || fallingBackIntoFluid)) {
            BlockPos fluidPos = abyssalcraft$customFluidContactPos;
            int color = CustomFluidTints.getColorForParticles(
                    currentFluid,
                    (BlockAndTintGetter) player.level(),
                    fluidPos
            );
            float red = ((color >> 16) & 0xFF) / 255.0F;
            float green = ((color >> 8) & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;
            double centerX = fluidPos.getX() + 0.5D;
            double centerY = Math.max(
                    fluidPos.getY() + 0.05D,
                    Math.min(player.getY() + 0.25D, fluidPos.getY() + 0.8D)
            );
            double centerZ = fluidPos.getZ() + 0.5D;

            for (int i = 0; i < 8; i++) {
                double angle = (Math.PI * 2.0D * i) / 8.0D;
                double offsetX = Math.cos(angle) * 0.22D;
                double offsetZ = Math.sin(angle) * 0.22D;
                player.level().addParticle(
                        new ColorBubbleData(red, green, blue),
                        centerX + offsetX,
                        centerY + (i % 3) * 0.12D,
                        centerZ + offsetZ,
                        offsetX * 0.012D,
                        0.025D,
                        offsetZ * 0.012D
                );
            }
            abyssalcraft$roseInCustomFluid = false;
        } else if (currentFluid != null && movingUp) {
            abyssalcraft$roseInCustomFluid = true;
        }

        abyssalcraft$previousCustomFluid = currentFluid;
    }

    @Unique
    private Fluid abyssalcraft$getCustomFluid(LocalPlayer player) {
        AABB bounds = player.getBoundingBox();
        int minX = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).getX();
        int maxX = BlockPos.containing(bounds.maxX - 1.0E-4D, bounds.maxY - 1.0E-4D, bounds.maxZ - 1.0E-4D).getX();
        int minY = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).getY();
        int maxY = BlockPos.containing(bounds.maxX - 1.0E-4D, bounds.maxY - 1.0E-4D, bounds.maxZ - 1.0E-4D).getY();
        int minZ = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).getZ();
        int maxZ = BlockPos.containing(bounds.maxX - 1.0E-4D, bounds.maxY - 1.0E-4D, bounds.maxZ - 1.0E-4D).getZ();

        Fluid closestFluid = null;
        BlockPos closestPos = null;
        double closestDistance = Double.MAX_VALUE;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Fluid fluid = player.level().getFluidState(pos).getType();
                    if (CustomFluidTints.getColor(fluid) == null) {
                        continue;
                    }

                    double dx = x + 0.5D - player.getX();
                    double dy = y + 0.5D - (player.getY() + player.getBbHeight() * 0.5D);
                    double dz = z + 0.5D - player.getZ();
                    double distance = dx * dx + dy * dy + dz * dz;
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestFluid = fluid;
                        closestPos = pos;
                    }
                }
            }
        }

        abyssalcraft$customFluidContactPos = closestPos;
        return closestFluid;
    }
}
