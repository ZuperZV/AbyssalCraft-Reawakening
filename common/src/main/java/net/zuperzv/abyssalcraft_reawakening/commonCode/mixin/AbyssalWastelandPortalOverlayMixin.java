
package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.AbyssalWastelandPortalBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class AbyssalWastelandPortalOverlayMixin {

    @Nullable
    private TextureAtlasSprite cachedAbyssalPortalSprite;

    @Inject(
            method = "extractPortalOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    private void abyssalcraft$tintPortalOverlay(GuiGraphicsExtractor graphics, float alpha, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        // Check if player is in a portal effect
        if (minecraft.player.portalEffectIntensity <= 0.0F) {
            return;
        }

        // Check if player is in a normal Nether portal - if so, don't override
        BlockPos playerPos = BlockPos.containing(
                minecraft.player.getX(),
                minecraft.player.getEyeY(),
                minecraft.player.getZ()
        );
        BlockState playerState = minecraft.level.getBlockState(playerPos);
        if (playerState.is(Blocks.NETHER_PORTAL)) {
            return;
        }

        // Check if there's an Abyssal Wasteland portal within 3 blocks
        boolean nearAbyssalPortal = false;
        for (int dx = -3; dx <= 3 && !nearAbyssalPortal; dx++) {
            for (int dy = -3; dy <= 3 && !nearAbyssalPortal; dy++) {
                for (int dz = -3; dz <= 3 && !nearAbyssalPortal; dz++) {
                    BlockPos checkPos = playerPos.offset(dx, dy, dz);
                    BlockState state = minecraft.level.getBlockState(checkPos);
                    if (state.getBlock() instanceof AbyssalWastelandPortalBlock) {
                        double distance = playerPos.distSqr(checkPos);
                        if (distance <= 9.0) { // 3 blocks squared
                            nearAbyssalPortal = true;
                        }
                    }
                }
            }
        }

        if (!nearAbyssalPortal) {
            return;
        }

        // Render portal overlay using our custom portal block's texture (already green)
        TextureAtlasSprite sprite = getAbyssalPortalSprite(minecraft);

        // Calculate alpha (same as vanilla portal overlay)
        if (alpha < 1.0F) {
            alpha *= alpha;
            alpha *= alpha;
            alpha = alpha * 0.8F + 0.2F;
        }

        // Use white color with alpha to show the texture's natural green color
        int color = (int) (alpha * 255.0F) << 24 | 0xFFFFFF;
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                sprite,
                0, 0,
                graphics.guiWidth(), graphics.guiHeight(),
                color
        );
        ci.cancel();
    }

    private TextureAtlasSprite getAbyssalPortalSprite(Minecraft minecraft) {
        if (this.cachedAbyssalPortalSprite != null) {
            return this.cachedAbyssalPortalSprite;
        }
        this.cachedAbyssalPortalSprite = minecraft
                .getModelManager()
                .getBlockStateModelSet()
                .getParticleMaterial(ModBlocks.ABYSSAL_WASTELAND_PORTAL_BLOCK.block().get().defaultBlockState())
                .sprite();
        return this.cachedAbyssalPortalSprite;
    }
}
