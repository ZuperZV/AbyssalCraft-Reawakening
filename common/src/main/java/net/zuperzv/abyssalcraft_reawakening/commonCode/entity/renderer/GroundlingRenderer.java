package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.GroundlingEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.layers.GroundlingEyesLayer;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.GroundlingModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.pipeline.GroundlingRenderPipelines;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.state.GroundlingRenderState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.RenderTypeAccessor;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class GroundlingRenderer extends MobRenderer<GroundlingEntity, GroundlingRenderState, GroundlingModel> {

    private static final Identifier TEXTURE_LOCATION =
            Constants.entityId("groundling/groundling");

    private static final Function<Identifier, RenderType> DEPTH_RENDER_TYPE =
            Util.memoize(texture -> {
                RenderSetup setup = RenderSetup.builder(
                                GroundlingRenderPipelines.GROUNDLING_DEPTH
                        )
                        .withTexture("Sampler0", texture)
                        .useLightmap()
                        .useOverlay()
                        .affectsCrumbling()
                        .createRenderSetup();

                return RenderTypeAccessor.abyssalcraft$create(
                        "groundling_depth",
                        setup
                );
            });

    private static final Function<Identifier, RenderType> TRANSLUCENT_RENDER_TYPE =
            Util.memoize(texture -> {
                RenderSetup setup = RenderSetup.builder(
                                GroundlingRenderPipelines.GROUNDLING_TRANSLUCENT
                        )
                        .withTexture("Sampler0", texture)
                        .useLightmap()
                        .useOverlay()
                        .affectsCrumbling()
                        .sortOnUpload()
                        .createRenderSetup();

                return RenderTypeAccessor.abyssalcraft$create(
                        "groundling_translucent",
                        setup
                );
            });

    public GroundlingRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new GroundlingModel(
                        context.bakeLayer(GroundlingModel.LAYER_LOCATION)
                ),
                0.3F
        );
        this.addLayer(new GroundlingEyesLayer(this));
    }

    @Override
    public GroundlingRenderState createRenderState() {
        return new GroundlingRenderState();
    }

    @Override
    public Identifier getTextureLocation(GroundlingRenderState state) {
        return TEXTURE_LOCATION;
    }

    @Override
    public RenderType getRenderType(
            GroundlingRenderState state,
            boolean translucent,
            boolean glowing,
            boolean outline
    ) {
        return TRANSLUCENT_RENDER_TYPE.apply(
                getTextureLocation(state)
        );
    }

    @Override
    public void submit(
            GroundlingRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState camera
    ) {
        poseStack.pushPose();

        float scale = state.scale;

        this.setupRotations(
                state,
                poseStack,
                state.bodyRot,
                scale
        );

        poseStack.scale(-1.0F, -1.0F, 1.0F);

        this.scale(state, poseStack);

        poseStack.translate(
                0.0F,
                -1.501F,
                0.0F
        );

        int overlayCoords = getOverlayCoords(
                state,
                this.getWhiteOverlayProgress(state)
        );

        int tint = ARGB.multiply(
                -1,
                this.getModelTint(state)
        );

        Identifier texture = getTextureLocation(state);

        collector
                .order(-1)
                .submitModel(
                        this.model,
                        state,
                        poseStack,
                        DEPTH_RENDER_TYPE.apply(texture),
                        state.lightCoords,
                        overlayCoords,
                        tint,
                        (TextureAtlasSprite) null,
                        state.outlineColor,
                        (ModelFeatureRenderer.CrumblingOverlay) null
                );

        collector
                .order(0)
                .submitModel(
                        this.model,
                        state,
                        poseStack,
                        TRANSLUCENT_RENDER_TYPE.apply(texture),
                        state.lightCoords,
                        overlayCoords,
                        tint,
                        (TextureAtlasSprite) null,
                        state.outlineColor,
                        (ModelFeatureRenderer.CrumblingOverlay) null
                );

        poseStack.popPose();


        if (state.leashStates != null) {
            for (LivingEntityRenderState.LeashState leashState : state.leashStates) {
                collector.submitLeash(poseStack, leashState);
            }
        }

        this.submitNameDisplay(
                state,
                poseStack,
                collector,
                camera
        );
    }

    @Override
    public void extractRenderState(
            @NonNull GroundlingEntity entity,
            @NonNull GroundlingRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.idleAnimationState.copyFrom(
                entity.idleAnimationState
        );

        state.walkAnimationState.copyFrom(
                entity.walkAnimationState
        );

        state.attackAnimationState.copyFrom(
                entity.attackAnimationState
        );

        state.hideAnimationState.copyFrom(
                entity.hideAnimationState
        );

        state.hiddenAnimationState.copyFrom(
                entity.hiddenAnimationState
        );

        state.wakeUpAnimationState.copyFrom(
                entity.wakeUpAnimationState
        );
    }
}