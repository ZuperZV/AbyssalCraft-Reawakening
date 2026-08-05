package net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.ModSignBlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class ModAbstractStandingSignRenderer<S extends SignRenderState> implements BlockEntityRenderer<ModSignBlockEntity, S> {
    private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
    private final Font font;
    private final SpriteGetter sprites;

    public ModAbstractStandingSignRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
        this.sprites = context.sprites();
    }

    protected abstract Model.Simple getSignModel(S var1);

    protected abstract SpriteId getSignSprite(WoodType var1);

    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.submitSignWithText(state, poseStack, state.breakProgress, submitNodeCollector);
    }

    private void submitSignWithText(S state, PoseStack poseStack, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector) {
        Model.Simple bodyModel = this.getSignModel(state);
        poseStack.pushPose();
        poseStack.mulPose(state.transformations.body());
        this.submitSign(poseStack, state.lightCoords, state.woodType, bodyModel, breakProgress, submitNodeCollector);
        poseStack.popPose();
        if (state.frontText != null) {
            poseStack.pushPose();
            poseStack.mulPose(state.transformations.frontText());
            this.submitSignText(state, poseStack, submitNodeCollector, state.frontText);
            poseStack.popPose();
        }

        if (state.backText != null) {
            poseStack.pushPose();
            poseStack.mulPose(state.transformations.backText());
            this.submitSignText(state, poseStack, submitNodeCollector, state.backText);
            poseStack.popPose();
        }

    }

    protected void submitSign(PoseStack poseStack, int lightCoords, WoodType type, Model.Simple signModel, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector) {
        SpriteId sprite = this.getSignSprite(type);
        submitNodeCollector.submitModel(signModel, Unit.INSTANCE, poseStack, lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, this.sprites, 0, breakProgress);
    }

    private void submitSignText(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, SignText signText) {
        int darkColor = getDarkColor(signText);
        int signMidpoint = 4 * state.textLineHeight / 2;
        FormattedCharSequence[] formattedLines = signText.getRenderMessages(state.isTextFilteringEnabled, (input) -> {
            List<FormattedCharSequence> components = this.font.split(input, state.maxTextLineWidth);
            return components.isEmpty() ? FormattedCharSequence.EMPTY : (FormattedCharSequence)components.get(0);
        });
        int textColor;
        boolean drawOutline;
        int lightVal;
        if (signText.hasGlowingText()) {
            textColor = signText.getColor().getTextColor();
            drawOutline = textColor == DyeColor.BLACK.getTextColor() || state.drawOutline;
            lightVal = 15728880;
        } else {
            textColor = darkColor;
            drawOutline = false;
            lightVal = state.lightCoords;
        }

        for(int i = 0; i < 4; ++i) {
            FormattedCharSequence actualLine = formattedLines[i];
            float x1 = (float)(-this.font.width(actualLine) / 2);
            submitNodeCollector.submitText(poseStack, x1, (float)(i * state.textLineHeight - signMidpoint), actualLine, false, Font.DisplayMode.POLYGON_OFFSET, lightVal, textColor, 0, drawOutline ? darkColor : 0);
        }

    }

    private static boolean isOutlineVisible(BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null && minecraft.options.getCameraType().isFirstPerson() && player.isScoping()) {
            return true;
        } else {
            Entity camera = minecraft.getCameraEntity();
            return camera != null && camera.distanceToSqr(Vec3.atCenterOf(pos)) < (double)OUTLINE_RENDER_DISTANCE;
        }
    }

    public static int getDarkColor(SignText signText) {
        int color = signText.getColor().getTextColor();
        return color == DyeColor.BLACK.getTextColor() && signText.hasGlowingText() ? -988212 : ARGB.scaleRGB(color, 0.4F);
    }

    @Override
    public void extractRenderState(
            ModSignBlockEntity blockEntity,
            S state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);

        state.maxTextLineWidth = blockEntity.getMaxTextLineWidth();
        state.textLineHeight = blockEntity.getTextLineHeight();
        state.frontText = blockEntity.getFrontText();
        state.backText = blockEntity.getBackText();
        state.isTextFilteringEnabled = Minecraft.getInstance().isTextFilteringEnabled();
        state.drawOutline = isOutlineVisible(blockEntity.getBlockPos());
        state.woodType = SignBlock.getWoodType(blockEntity.getBlockState().getBlock());
    }
}
