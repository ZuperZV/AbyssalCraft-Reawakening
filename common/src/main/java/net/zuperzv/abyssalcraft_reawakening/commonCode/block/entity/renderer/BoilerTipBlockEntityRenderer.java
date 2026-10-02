package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
    private static final int MAX_TIPS = 4;
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
        if (level == null) {
            return;
        }

        state.light = state.lightCoords;
        state.models.clear();
        BlockStateModel tipModel = Minecraft.getInstance().getModelManager()
                .getBlockStateModelSet().get(blockEntity.getBlockState());
        tipModel.collectParts(RandomSource.create(0L), state.models);
        for (int lane = 0; lane < state.tipCount; lane++) {
            EssenceBoilerFluid fluid = blockEntity.getVisualFluid(lane);
            state.sprites[lane] = null;
            state.columnLengths[lane] = 0;
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
        for (int lane = 0; lane < state.tipCount; lane++) {
            float[] offset = laneOffset(lane, state.tipCount);
            poseStack.pushPose();
            if (state.tipCount > 1) {
                poseStack.translate(0.5F + offset[0], 0.5F, 0.5F + offset[1]);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.translate(-0.5F, -0.5F, -0.5F);
            }
            submitTipModel(state, poseStack, collector);
            TextureAtlasSprite sprite = state.sprites[lane];
            if (sprite != null && state.columnLengths[lane] > 0) {
                float laneBottomY = 0.5F - state.columnLengths[lane];
                int color = state.colors[lane];
                int light = state.light;
                collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(),
                        (pose, builder) -> drawColumn(
                                builder, pose, sprite, MIN_X, laneBottomY, MIN_Z,
                                MAX_X, TOP_Y, MAX_Z, light, color
                        ));
            }
            poseStack.popPose();
        }
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

    private static float[] laneOffset(int lane, int count) {
        if (count == 1) {
            return new float[]{0.0F, 0.0F};
        }
        return switch (lane) {
            case 0 -> new float[]{-0.25F, -0.25F};
            case 1 -> new float[]{0.25F, -0.25F};
            case 2 -> new float[]{-0.25F, 0.25F};
            default -> new float[]{0.25F, 0.25F};
        };
    }

    private static void drawColumn(
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
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        quad(builder, pose, x0, y0, z0, x0, y1, z0, x1, y1, z0, x1, y0, z0, u0, v0, u1, v1, 0, 0, -1, light, color);
        quad(builder, pose, x1, y0, z1, x1, y1, z1, x0, y1, z1, x0, y0, z1, u0, v0, u1, v1, 0, 0, 1, light, color);
        quad(builder, pose, x0, y0, z1, x0, y1, z1, x0, y1, z0, x0, y0, z0, u0, v0, u1, v1, -1, 0, 0, light, color);
        quad(builder, pose, x1, y0, z0, x1, y1, z0, x1, y1, z1, x1, y0, z1, u0, v0, u1, v1, 1, 0, 0, light, color);
        quad(builder, pose, x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0, u0, v0, u1, v1, 0, 1, 0, light, color);
        quad(builder, pose, x0, y0, z1, x0, y0, z0, x1, y0, z0, x1, y0, z1, u0, v0, u1, v1, 0, -1, 0, light, color);
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
        private final int[] colors = {0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF};
        private int light;
        private final int[] columnLengths = new int[MAX_TIPS];
        private int tipCount;
    }
}
