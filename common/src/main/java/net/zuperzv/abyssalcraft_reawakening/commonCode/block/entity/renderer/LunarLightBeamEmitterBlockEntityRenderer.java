package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.LunarLightBeamEmitterBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.LunarLightBeamEmitterBlockEntity;
import org.jetbrains.annotations.Nullable;

public final class LunarLightBeamEmitterBlockEntityRenderer implements
        BlockEntityRenderer<LunarLightBeamEmitterBlockEntity, LunarLightBeamEmitterBlockEntityRenderer.RenderState> {

    public LunarLightBeamEmitterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            LunarLightBeamEmitterBlockEntity blockEntity,
            RenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity, state, partialTicks, cameraPosition, breakProgress
        );
        state.beamLength = blockEntity.getBeamLength();
        state.alpha = (float) blockEntity.getBeamTicksRemaining()
                / LunarLightBeamEmitterBlockEntity.MAX_BEAM_TICKS;
        state.facing = blockEntity.getBlockState().getValue(LunarLightBeamEmitterBlock.FACING);
    }

    @Override
    public void submit(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState camera
    ) {
        if (state.beamLength <= 0 || state.alpha <= 0) return;

        Vec3 start = new Vec3(0.5, 0.5, 0.5);
        Vec3 direction = new Vec3(
                state.facing.getStepX(),
                state.facing.getStepY(),
                state.facing.getStepZ()
        );
        Vec3 end = start.add(direction.scale(state.beamLength));

        collector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, consumer) -> {
            drawBeamSegment(pose, consumer, start, end, direction, 0.14F,
                    0.35F, 0.75F, 1.0F, 0.55F * state.alpha);
            drawBeamSegment(pose, consumer, start, end, direction, 0.055F,
                    0.85F, 0.97F, 1.0F, state.alpha);
        });
    }

    private static void drawBeamSegment(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            Vec3 start,
            Vec3 end,
            Vec3 direction,
            float thickness,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        Vec3 up = Math.abs(direction.y) > 0.99
                ? new Vec3(1, 0, 0)
                : new Vec3(0, 1, 0);
        Vec3 right = direction.cross(up).normalize().scale(thickness);
        up = right.cross(direction).normalize().scale(thickness);

        Vec3[] startCorners = {
                start.subtract(right).subtract(up),
                start.add(right).subtract(up),
                start.add(right).add(up),
                start.subtract(right).add(up)
        };
        Vec3[] endCorners = {
                end.subtract(right).subtract(up),
                end.add(right).subtract(up),
                end.add(right).add(up),
                end.subtract(right).add(up)
        };

        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;
            addVertex(consumer, pose, startCorners[i], red, green, blue, alpha);
            addVertex(consumer, pose, endCorners[i], red, green, blue, alpha);
            addVertex(consumer, pose, endCorners[next], red, green, blue, alpha);
            addVertex(consumer, pose, startCorners[next], red, green, blue, alpha);
        }

        addVertex(consumer, pose, startCorners[0], red, green, blue, alpha);
        addVertex(consumer, pose, startCorners[3], red, green, blue, alpha);
        addVertex(consumer, pose, startCorners[2], red, green, blue, alpha);
        addVertex(consumer, pose, startCorners[1], red, green, blue, alpha);
        addVertex(consumer, pose, endCorners[0], red, green, blue, alpha);
        addVertex(consumer, pose, endCorners[1], red, green, blue, alpha);
        addVertex(consumer, pose, endCorners[2], red, green, blue, alpha);
        addVertex(consumer, pose, endCorners[3], red, green, blue, alpha);
    }

    private static void addVertex(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            Vec3 pos,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        consumer.addVertex(pose, (float) pos.x, (float) pos.y, (float) pos.z)
                .setColor(red, green, blue, alpha);
    }

    public static class RenderState extends BlockEntityRenderState {
        private int beamLength;
        private float alpha;
        private Direction facing;
    }
}
