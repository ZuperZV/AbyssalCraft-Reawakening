package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.BoilerTipBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.BoilerTipBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluidColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BoilerTipBlockEntityRenderer implements
        BlockEntityRenderer<BoilerTipBlockEntity, BoilerTipBlockEntityRenderer.RenderState> {
    private static final int MAX_TIPS = Direction.values().length;
    private static final float MIN_X = 6.5F / 16.0F;
    private static final float MAX_X = 9.5F / 16.0F;
    private static final float MIN_Z = 12.0F / 16.0F;
    private static final float MAX_Z = 14.0F / 16.0F;
    private static final float TOP_Y = 13.0F / 16.0F;

    public BoilerTipBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            BoilerTipBlockEntity blockEntity,
            RenderState state,
            float partialTicks,
            net.minecraft.world.phys.Vec3 cameraPosition,
            @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                state,
                partialTicks,
                cameraPosition,
                breakProgress
        );

        Level level = blockEntity.getLevel();
        state.tipCount = MAX_TIPS;
        state.models.clear();
        for (int lane = 0; lane < MAX_TIPS; lane++) {
            state.sprites[lane] = null;
            state.colors[lane] = 0xFFFFFFFF;
            state.columnLengths[lane] = 0;
        }
        if (level == null) {
            return;
        }

        state.light = state.lightCoords;
        BlockStateModel tipModel = Minecraft.getInstance().getModelManager()
                .getBlockStateModelSet().get(blockEntity.getBlockState());
        tipModel.collectParts(RandomSource.create(0L), state.models);
        for (Direction direction : Direction.values()) {
            int lane = direction.ordinal();
            if (!BoilerTipBlock.hasAttachment(blockEntity.getBlockState(), direction)) {
                continue;
            }
            EssenceBoilerFluid fluid = blockEntity.getVisualFluid(lane);
            if (fluid.isEmpty()) {
                continue;
            }
            BlockPos boilerPos = blockEntity.findBoilerBelow(level, blockEntity.getBlockPos());
            if (boilerPos == null) {
                continue;
            }
            FluidModel model = Minecraft.getInstance().getModelManager()
                    .getFluidStateModelSet().get(fluid.fluid().defaultFluidState());
            Material.Baked still = model.stillMaterial();
            if (still == null) {
                continue;
            }
            state.sprites[lane] = still.sprite();
            state.colors[lane] = EssenceBoilerFluidColor.getTint(
                    model, fluid, level, blockEntity.getBlockPos()
            );
            if (state.colors[lane] == EssenceBoilerFluidColor.NO_TINT) {
                state.colors[lane] = 0xFFFFFFFF;
            }
            state.columnLengths[lane] = blockEntity.getBlockPos().getY() - boilerPos.getY() + 1;
        }
    }

    @Override
    public void submit(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            CameraRenderState camera
    ) {
        submitTipModel(state, poseStack, collector);
        for (int lane = 0; lane < state.tipCount; lane++) {
            TextureAtlasSprite sprite = state.sprites[lane];
            if (sprite != null && state.columnLengths[lane] > 0) {
                float laneBottomY = 1.0F - state.columnLengths[lane];
                float y = state.columnLengths[lane] == state.columnLengths.length ? laneBottomY + 0.1F : laneBottomY;
                int color = state.colors[lane];
                int light = state.light;
                Direction facing = Direction.values()[lane];
                poseStack.pushPose();
                rotateFluidPose(poseStack, facing);
                collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(),
                        (pose, builder) -> {
                            drawFluidBox(
                                    builder, pose, sprite,
                                    MIN_X, y, MIN_Z, MAX_X, TOP_Y, MAX_Z, light, color
                            );
                            drawFluidBox(
                                    builder, pose, sprite,
                                    MIN_X, 11.0F / 16.0F, MAX_Z, MAX_X, TOP_Y, 17.0F / 16.0F, light, color
                            );
                        });
                poseStack.popPose();
            }
        }
    }

    private static void rotateFluidPose(PoseStack poseStack, Direction facing) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        switch (facing) {
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(270.0F));
            default -> {
            }
        }
        poseStack.translate(-0.5F, -0.5F, -0.5F);
    }

    private static void submitTipModel(
            RenderState state,
            PoseStack poseStack,
            SubmitNodeCollector collector
    ) {
        collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, builder) -> {
            QuadInstance quad = new QuadInstance();
            quad.setColor(0xFFFFFFFF);
            quad.setLightCoords(state.light);
            quad.setOverlayCoords(0);
            for (BlockStateModelPart part : state.models) {
                for (Direction direction : Direction.values()) {
                    for (var bakedQuad : part.getQuads(direction)) {
                        builder.putBakedQuad(pose, bakedQuad, quad);
                    }
                }
                for (var bakedQuad : part.getQuads(null)) {
                    builder.putBakedQuad(pose, bakedQuad, quad);
                }
            }
        });
    }

    private static void drawFluidBox(
            VertexConsumer builder,
            PoseStack.Pose pose,
            TextureAtlasSprite sprite,
            float x0,
            float y0,
            float z0,
            float x1,
            float y1,
            float z1,
            int light,
            int color
    ) {
        float uX = spriteU(sprite, (x1 - x0) * 16.0F);
        float uZ = spriteU(sprite, (z1 - z0) * 16.0F);
        float vZ = spriteV(sprite, (z1 - z0) * 16.0F);
        float u0 = sprite.getU0();
        float v0 = sprite.getV0();

        for (float segmentBottom = y0; segmentBottom < y1; ) {
            float segmentTop = Math.min(segmentBottom + 1.0F, y1);
            float segmentV = spriteV(sprite, (segmentTop - segmentBottom) * 16.0F);
            float bottomV = sprite.getV0();
            float topV = sprite.getV0() + segmentV;

            quad(builder, pose,
                    x0, segmentBottom, z0, x0, segmentTop, z0,
                    x1, segmentTop, z0, x1, segmentBottom, z0,
                    u0, topV, uX, bottomV, 0, 0, -1, light, color);
            quad(builder, pose,
                    x1, segmentBottom, z1, x1, segmentTop, z1,
                    x0, segmentTop, z1, x0, segmentBottom, z1,
                    u0, topV, uX, bottomV, 0, 0, 1, light, color);
            quad(builder, pose,
                    x0, segmentBottom, z1, x0, segmentTop, z1,
                    x0, segmentTop, z0, x0, segmentBottom, z0,
                    u0, topV, uZ, bottomV, -1, 0, 0, light, color);
            quad(builder, pose,
                    x1, segmentBottom, z0, x1, segmentTop, z0,
                    x1, segmentTop, z1, x1, segmentBottom, z1,
                    u0, topV, uZ, bottomV, 1, 0, 0, light, color);
            segmentBottom = segmentTop;
        }

        quad(builder, pose,
                x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0,
                u0, v0 + vZ, uX, v0,
                0, 1, 0, light, color);
        quad(builder, pose,
                x0, y0, z1, x0, y0, z0, x1, y0, z0, x1, y0, z1,
                u0, v0 + vZ, uX, v0,
                0, -1, 0, light, color);
    }

    private static float spriteU(TextureAtlasSprite sprite, float pixels) {
        return sprite.getU0() + (sprite.getU1() - sprite.getU0()) * pixels / 16.0F;
    }

    private static float spriteV(TextureAtlasSprite sprite, float pixels) {
        return (sprite.getV1() - sprite.getV0()) * pixels / 16.0F;
    }

    private static void quad(
            VertexConsumer builder,
            PoseStack.Pose pose,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float u0, float v0, float u1, float v1,
            float nx, float ny, float nz,
            int light,
            int color
    ) {
        vertex(builder, pose, x0, y0, z0, u0, v1, nx, ny, nz, light, color);
        vertex(builder, pose, x1, y1, z1, u0, v0, nx, ny, nz, light, color);
        vertex(builder, pose, x2, y2, z2, u1, v0, nx, ny, nz, light, color);
        vertex(builder, pose, x3, y3, z3, u1, v1, nx, ny, nz, light, color);
    }

    private static void vertex(
            VertexConsumer builder,
            PoseStack.Pose pose,
            float x, float y, float z,
            float u, float v,
            float nx, float ny, float nz,
            int light,
            int color
    ) {
        builder.addVertex(pose, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(0)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }

    public static class RenderState extends BlockEntityRenderState {
        private final List<BlockStateModelPart> models = new ArrayList<>();
        @Nullable
        private final TextureAtlasSprite[] sprites = new TextureAtlasSprite[MAX_TIPS];
        private final int[] colors = new int[MAX_TIPS];
        private int light;
        private final int[] columnLengths = new int[MAX_TIPS];
        private int tipCount;
    }
}
