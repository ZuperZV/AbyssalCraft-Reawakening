package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.ModDimensions;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {

    @Nullable
    private TextureAtlasSprite cachedAbyssalPortalSprite;

    @Inject(
            method = "extractBackground",
            at = @At("HEAD"),
            cancellable = true
    )
    private void abyssalcraft$tintBackgroundForAbyssalPortal(
            GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        Level level = minecraft.player.level();
        boolean involvingAbyssalWasteland = level.dimension() == ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY;

        if (involvingAbyssalWasteland) {
            TextureAtlasSprite sprite = getAbyssalPortalSprite(minecraft);
            graphics.blitSprite(
                    RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND,
                    sprite,
                    0, 0,
                    graphics.guiWidth(), graphics.guiHeight()
            );
            ci.cancel();
        }
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
