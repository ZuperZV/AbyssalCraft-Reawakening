package net.zuperzv.abyssalcraft_reawakening.init.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.zuperzv.abyssalcraft_reawakening.init.entity.custom.WitherwoodBoat;
import org.joml.Quaternionf;

public class WitherwoodBoatRenderer
        extends EntityRenderer<WitherwoodBoat, BoatRenderState> {

    private final Identifier texture;
    private final BoatModel model;

    public WitherwoodBoatRenderer(
            EntityRendererProvider.Context context,
            Identifier texture
    ) {
        super(context);

        this.texture = texture;
        this.shadowRadius = 0.8F;

        this.model = new BoatModel(
                context.bakeLayer(ModelLayers.ACACIA_BOAT)
        );
    }

    @Override
    public BoatRenderState createRenderState() {
        return new BoatRenderState();
    }

    @Override
    public void extractRenderState(
            WitherwoodBoat entity,
            BoatRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.yRot = entity.getYRot(partialTicks);
        state.hurtTime = (float) entity.getHurtTime() - partialTicks;
        state.hurtDir = entity.getHurtDir();
        state.damageTime = Math.max(
                entity.getDamage() - partialTicks,
                0.0F
        );
        state.bubbleAngle = entity.getBubbleAngle(partialTicks);
        state.isUnderWater = entity.isUnderWater();
        state.rowingTimeLeft = entity.getRowingTime(0, partialTicks);
        state.rowingTimeRight = entity.getRowingTime(1, partialTicks);
    }

    @Override
    public void submit(
            BoatRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        poseStack.pushPose();

        poseStack.translate(0.0F, 0.375F, 0.0F);

        poseStack.mulPose(
                Axis.YP.rotationDegrees(180.0F - state.yRot)
        );

        float hurt = state.hurtTime;

        if (hurt > 0.0F) {
            poseStack.mulPose(
                    Axis.XP.rotationDegrees(
                            Mth.sin(hurt)
                                    * hurt
                                    * state.damageTime
                                    / 10.0F
                                    * state.hurtDir
                    )
            );
        }

        if (!state.isUnderWater
                && !Mth.equal(state.bubbleAngle, 0.0F)) {

            poseStack.mulPose(
                    new Quaternionf()
                            .setAngleAxis(
                                    state.bubbleAngle
                                            * ((float) Math.PI / 180F),
                                    1.0F,
                                    0.0F,
                                    1.0F
                            )
            );
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);

        poseStack.mulPose(
                Axis.YP.rotationDegrees(90.0F)
        );

        submitNodeCollector.submitModel(
                this.model,
                state,
                poseStack,
                this.texture,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                (ModelFeatureRenderer.CrumblingOverlay) null
        );

        poseStack.popPose();

        super.submit(
                state,
                poseStack,
                submitNodeCollector,
                camera
        );
    }
}