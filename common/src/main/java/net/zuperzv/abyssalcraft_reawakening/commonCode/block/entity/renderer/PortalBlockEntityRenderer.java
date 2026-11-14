package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PortalBlockEntity;

import java.util.Collections;

public class PortalBlockEntityRenderer extends AbstractEndPortalRenderer<PortalBlockEntity, EndPortalRenderState> {

    public PortalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public EndPortalRenderState createRenderState() {
        return new EndPortalRenderState();
    }


    // 5.99 pixels toward center (block is 16 units, so offset = 5.99/16 ≈ 0.3744)
    private static final float OFFSET = 0.3744F;

    @Override
    public void submit(EndPortalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.facesToShow.isEmpty()) {
            return;
        }

        for (Direction direction : state.facesToShow) {
            poseStack.pushPose();
            // Translate this face toward the center of the block, only along the axis perpendicular to the face. AHAHAHAHAHHAH MATH
            poseStack.translate(
                    direction.getStepX() * -OFFSET,
                    direction.getStepY() * -OFFSET,
                    direction.getStepZ() * -OFFSET
            );
            submitCube(Collections.singleton(direction), RenderTypes.endPortal(), poseStack, submitNodeCollector);
            poseStack.popPose();
        }
    }
}
