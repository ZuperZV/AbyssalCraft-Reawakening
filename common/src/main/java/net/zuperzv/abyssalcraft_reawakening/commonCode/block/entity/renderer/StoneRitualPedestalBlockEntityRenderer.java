package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.StoneRitualPedestalBlockEntity;

public class StoneRitualPedestalBlockEntityRenderer
        implements BlockEntityRenderer<StoneRitualPedestalBlockEntity, StoneRitualPedestalBlockEntityRenderer.NexusState> {

    private final ItemModelResolver itemModelResolver;

    public StoneRitualPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class NexusState extends BlockEntityRenderState {
        public Level level;
        public float rotation;
        public long gameTime;
        public boolean hasItem;
        public boolean isFlying;
        public boolean isMergingAtAltar;
        public float flyLocalX;
        public float flyLocalY;
        public float flyLocalZ;

        public final ItemStackRenderState itemStackRenderState = new ItemStackRenderState();

        public @org.jspecify.annotations.Nullable EntityRenderState entityRenderState;
    }

    @Override
    public NexusState createRenderState() {
        return new NexusState();
    }

    @Override
    public void extractRenderState(
            StoneRitualPedestalBlockEntity be,
            NexusState state,
            float partialTicks,
            Vec3 cameraPosition,
            net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);

        Level level = be.getLevel();
        if (level == null) return;

        float time = level.getGameTime() + partialTicks;

        state.level = level;
        state.gameTime = (long) time;
        state.rotation = time % 360f;
        state.isFlying = false;
        state.isMergingAtAltar = false;
        state.hasItem = false;

        ItemStack stack = be.inventory.getStackInSlot(0);
        if (stack.isEmpty()) return;

        state.hasItem = true;

        itemModelResolver.updateForTopItem(
                state.itemStackRenderState,
                stack,
                ItemDisplayContext.FIXED,
                level,
                null,
                0
        );

        if (be.isUsedInActiveCraft()) {
            float mergeProgress = be.getMergeProgress(partialTicks);
            if (mergeProgress > 0f) {
                state.isMergingAtAltar = true;
            } else {
                float flyProgress = be.getFlyProgress(partialTicks);
                if (flyProgress > 0f) {
                    be.getFlyingItemPosition(partialTicks).ifPresent(worldPos -> {
                        state.isFlying = true;
                        state.flyLocalX = (float) (worldPos.x - be.getBlockPos().getX());
                        state.flyLocalY = (float) (worldPos.y - be.getBlockPos().getY());
                        state.flyLocalZ = (float) (worldPos.z - be.getBlockPos().getZ());
                    });
                }
            }
        }
    }

    @Override
    public void submit(
            NexusState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState
    ) {
        Level level = state.level;
        if (level == null || !state.hasItem) return;

        MultiBufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        int light = state.lightCoords;

        if (state.isFlying) {
            poseStack.pushPose();
            poseStack.translate(state.flyLocalX, state.flyLocalY, state.flyLocalZ);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));

            state.itemStackRenderState.submit(
                    poseStack,
                    submitNodeCollector,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    0
            );

            poseStack.popPose();
            return;
        }

        if (state.isMergingAtAltar) {
            return;
        }

        if (state.entityRenderState != null) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.2f, 0.5f);
            poseStack.scale(0.35f, 0.35f, 0.35f);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation + 135));

            Minecraft.getInstance()
                    .getEntityRenderDispatcher()
                    .submit(
                            state.entityRenderState,
                            cameraRenderState,
                            0, 0, 0,
                            poseStack,
                            submitNodeCollector
                    );

            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(0.5f, 1.15f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));

        state.itemStackRenderState.submit(
                poseStack,
                submitNodeCollector,
                light,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}
