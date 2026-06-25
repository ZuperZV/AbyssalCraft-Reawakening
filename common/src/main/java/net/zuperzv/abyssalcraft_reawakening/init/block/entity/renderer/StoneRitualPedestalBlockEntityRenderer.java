package net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualPedestalBlockEntity;

public class StoneRitualPedestalBlockEntityRenderer
        implements BlockEntityRenderer<StoneRitualPedestalBlockEntity, StoneRitualPedestalBlockEntityRenderer.NexusState> {

    private final ItemModelResolver itemModelResolver;

    public StoneRitualPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    // =========================
    // STATE
    // =========================

    public static class NexusState extends BlockEntityRenderState {
        public Level level;
        public float rotation;
        public long gameTime;

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

        ItemStack stack = be.inventory.getStackInSlot(0);

        itemModelResolver.updateForTopItem(
                state.itemStackRenderState,
                stack,
                ItemDisplayContext.FIXED,
                level,
                null,
                0
        );
    }

    @Override
    public void submit(
            NexusState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState
    ) {

        Level level = state.level;
        if (level == null) return;

        MultiBufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        int light = state.lightCoords;

        // =========================
        // CENTER ITEM
        // =========================

        poseStack.pushPose();
        poseStack.translate(0.5f, 1.15f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.rotation));

        state.itemStackRenderState.submit(
                poseStack,
                submitNodeCollector,
                light,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();

        // =========================
        // ENTITY
        // =========================

        if (state.entityRenderState != null) {

            poseStack.pushPose();
            poseStack.translate(0.5f, 1.2f, 0.5f);
            poseStack.scale(0.35f, 0.35f, 0.35f);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.rotation + 135));

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

        // =========================
        // FLOATING BLOCKS (FIXED PIPELINE)
        // =========================

        /*
        Block[] blocks = {
                Blocks.GRASS_BLOCK,
                Blocks.MOSS_BLOCK,
                Blocks.DIRT,
                Blocks.COARSE_DIRT,
                Blocks.ROOTED_DIRT
        };

        int count = 5;
        double radius = 0.75;

        for (int i = 0; i < count; i++) {

            double t = (state.gameTime + i * 11) / 100.0;

            double angle = (i / (double) count) * Math.PI * 2 + t;

            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            double y = 0.2 + (Math.sin(t * Math.PI * 2) + 1.0) * 0.5;

            float spin = (float)((t * 360) % 360);
            float scale = 0.15f;

            ItemStack stack = new ItemStack(blocks[i % blocks.length]);

            ItemStackRenderState temp = new ItemStackRenderState();

            itemModelResolver.updateForTopItem(
                    temp,
                    stack,
                    ItemDisplayContext.FIXED,
                    level,
                    null,
                    0
            );

            poseStack.pushPose();
            poseStack.translate(0.5 + x, y, 0.5 + z);
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(spin));

            temp.submit(
                    poseStack,
                    submitNodeCollector,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    0
            );

            poseStack.popPose();
        }
         */
    }
}