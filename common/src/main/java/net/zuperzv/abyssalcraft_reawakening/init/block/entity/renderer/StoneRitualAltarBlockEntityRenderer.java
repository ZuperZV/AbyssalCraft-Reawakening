package net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualAltarBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualPedestalBlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.zuperzv.abyssalcraft_reawakening.init.block.custom.StoneRitualAltarBlock.CRAFTING;

public class StoneRitualAltarBlockEntityRenderer
        implements BlockEntityRenderer<StoneRitualAltarBlockEntity, StoneRitualAltarBlockEntityRenderer.StoneRitualAltarBlockEntityRenderState> {

    public static final ModelLayerLocation MAGIC_AURA_LAYER =
            new ModelLayerLocation(
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "magic_aura"),
                    "main"
            );

    private final ModelPart magicPlane;
    private final ItemModelResolver itemModelResolver;

    public StoneRitualAltarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.magicPlane = context.bakeLayer(MAGIC_AURA_LAYER).getChild("plane");
    }

    public static LayerDefinition createMagicAuraLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "plane",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24.0F, 0.0F, -24.0F, 48.0F, 0.1F, 48.0F),
                PartPose.offset(0.0F, -16.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 48, 48);
    }

    // =========================
    // STATE
    // =========================

    @Override
    public StoneRitualAltarBlockEntityRenderState createRenderState() {
        return new StoneRitualAltarBlockEntityRenderState();
    }

    // =========================
    // EXTRACT STATE
    // =========================

    @Override
    public void extractRenderState(
            StoneRitualAltarBlockEntity blockEntity,
            StoneRitualAltarBlockEntityRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {

        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        Level level = blockEntity.getLevel();
        if (level == null) return;

        float gameTime = level.getGameTime() + partialTicks;

        state.level = level;
        state.gameTime = (long) gameTime;
        state.rotation = gameTime % 360f;

        // ITEM
        ItemStack stack = blockEntity.inventory.getStackInSlot(0);

        itemModelResolver.updateForTopItem(
                state.itemStackRenderState,
                stack,
                ItemDisplayContext.FIXED,
                level,
                null,
                0
        );

        // AURA
        state.crafting = blockEntity.getBlockState().getValue(CRAFTING);

        state.auraAlpha = state.crafting
                ? Mth.clamp((float) blockEntity.progress / (blockEntity.maxProgress * 5f), 0f, 1f)
                : 0f;

        blockEntity.entityLastSacrificed.ifPresent(ref -> {
            Entity entity = ref.value().create(level, EntitySpawnReason.TRIGGERED);
            if (entity != null) {
                state.entityRenderState =
                        Minecraft.getInstance()
                                .getEntityRenderDispatcher()
                                .extractEntity(entity, partialTicks);
            }
        });

        state.mergingItems.clear();
        if (state.crafting) {
            BlockPos altarPos = blockEntity.getBlockPos();
            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockEntity be = level.getBlockEntity(altarPos.offset(dx, 0, dz));
                    if (!(be instanceof StoneRitualPedestalBlockEntity pedestal)) continue;
                    if (!pedestal.isUsedInActiveCraft()) continue;

                    float mergeProgress = pedestal.getMergeProgress(partialTicks);
                    if (mergeProgress <= 0f) continue;

                    ItemStack stackToRender = pedestal.inventory.getStackInSlot(0);
                    if (stackToRender.isEmpty()) continue;

                    MergingItemState mergingItem = new MergingItemState();
                    mergingItem.mergeProgress = mergeProgress;
                    mergingItem.orbitAngle = (float) Math.atan2(dz, dx);

                    itemModelResolver.updateForTopItem(
                            mergingItem.renderState,
                            stackToRender,
                            ItemDisplayContext.FIXED,
                            level,
                            null,
                            0
                    );

                    state.mergingItems.add(mergingItem);
                }
            }
        }
    }

    @Override
    public void submit(
            StoneRitualAltarBlockEntityRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState) {

        Level level = state.level;
        if (level == null) return;

        MultiBufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        int light = state.lightCoords;

        // =========================
        // AURA
        // =========================
        if (state.crafting) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.01, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));

            VertexConsumer vc = buffers.getBuffer(
                    RenderTypes.entityTranslucent(
                            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/magic_aura.png")
                    )
            );

            magicPlane.render(
                    poseStack,
                    vc,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    ((int)(state.auraAlpha * 255) << 24) | 0xFFFFFF
            );

            poseStack.popPose();
        }

        // =========================
        // CENTER ITEM
        // =========================
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

        // =========================
        // MERGING PEDESTAL ITEMS
        // =========================
        for (MergingItemState mergingItem : state.mergingItems) {
            float mergeProgress = mergingItem.mergeProgress;
            float smoothMerge = mergeProgress * mergeProgress * (3f - 2f * mergeProgress);
            float orbitRadius = 0.35f * (1f - smoothMerge);
            float scale = 0.5f * (1f - smoothMerge);
            int alpha = (int) ((1f - smoothMerge) * 255f) << 24;

            float x = 0.5f + Mth.cos(mergingItem.orbitAngle) * orbitRadius;
            float y = 1.15f + 0.1f * (1f - smoothMerge);
            float z = 0.5f + Mth.sin(mergingItem.orbitAngle) * orbitRadius;

            poseStack.pushPose();
            poseStack.translate(x, y, z);
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation + smoothMerge * 180f));

            mergingItem.renderState.submit(
                    poseStack,
                    submitNodeCollector,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    alpha | 0xFFFFFF
            );

            poseStack.popPose();
        }

        // =========================
        // ENTITY (FIXED SIGNATURE)
        // =========================
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

        // =========================
        // FLOATING BLOCKS (FIXED - NO renderStatic)
        // =========================
        Block[] blocks = {
                Blocks.GRASS_BLOCK,
                Blocks.MOSS_BLOCK,
                Blocks.DIRT,
                Blocks.COARSE_DIRT,
                Blocks.ROOTED_DIRT
        };

        int count = 5;
        double radiusBase = 0.75;

        for (int i = 0; i < count; i++) {

            double t = (state.gameTime + i * 11) / 100.0;

            if (Math.sin(t * 2 * Math.PI) < -0.6) continue;

            double angle = (i / (double) count) * Math.PI * 2 + t;
            double radius = radiusBase + Math.sin(t * 1.5) * 0.05;

            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            double y = 0.2 + (Math.sin(t * Math.PI * 2) + 1.0) * 0.5;

            float spin = (float)((t * 360) % 360);
            float scale = 0.15f + (float)((Math.sin(t * Math.PI * 2) + 1.0) * 0.065f);

            ItemStack stack = new ItemStack(blocks[(i + (int)(state.gameTime / 40)) % blocks.length]);

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
            poseStack.mulPose(Axis.YP.rotationDegrees(spin));

            temp.submit(
                    poseStack,
                    submitNodeCollector,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    0
            );

            poseStack.popPose();
        }
    }

    // =========================
    // STATE CLASS
    // =========================

    public class StoneRitualAltarBlockEntityRenderState extends BlockEntityRenderState {
        public Level level;
        public float rotation;
        public long gameTime;

        public final ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
        public final List<MergingItemState> mergingItems = new ArrayList<>();

        public float auraAlpha;
        public boolean crafting;

        public @Nullable Entity entity;
        public EntityRenderState entityRenderState;
    }

    public static class MergingItemState {
        public final ItemStackRenderState renderState = new ItemStackRenderState();
        public float mergeProgress;
        public float orbitAngle;
    }
}