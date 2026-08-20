package net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class BlockPreviewRenderState
        implements GuiElementRenderState {

    private final BlockState state;
    private final BlockStateModel model;

    private final int x;
    private final int y;
    private final int size;

    private final float rotationX;
    private final float rotationY;

    public BlockPreviewRenderState(
            BlockState state,
            BlockStateModel model,
            int x,
            int y,
            int size,
            float rotationX,
            float rotationY
    ) {
        this.state = state;
        this.model = model;
        this.x = x;
        this.y = y;
        this.size = size;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
    }

    @Override
    public void buildVertices(
            VertexConsumer buffer
    ) {
        RandomSource random =
                RandomSource.create(
                        state.getSeed(BlockPos.ZERO)
                );

        List<BlockStateModelPart> parts =
                new ArrayList<>();

        model.collectParts(
                random,
                parts
        );

        if (parts.isEmpty()) {
            return;
        }

        PoseStack poseStack =
                new PoseStack();

        poseStack.translate(
                x,
                y,
                0.0F
        );

        poseStack.scale(
                size,
                -size,
                size
        );

        poseStack.translate(
                0.5F,
                0.5F,
                0.5F
        );

        poseStack.mulPose(
                Axis.YP.rotationDegrees(
                        rotationY
                )
        );

        poseStack.mulPose(
                Axis.XP.rotationDegrees(
                        rotationX
                )
        );

        PoseStack.Pose pose =
                poseStack.last();

        QuadInstance quad =
                new QuadInstance();

        quad.setColor(
                0xFFFFFFFF
        );

        quad.setLightCoords(
                0xF000F0
        );

        quad.setOverlayCoords(
                0
        );

        for (BlockStateModelPart part : parts) {

            for (Direction direction :
                    Direction.values()) {

                List<BakedQuad> quads =
                        part.getQuads(
                                direction
                        );

                if (quads.isEmpty()) {
                    continue;
                }

                int shade =
                        getShade(
                                direction
                        );

                quad.setColor(
                        shade
                );

                for (BakedQuad bakedQuad :
                        quads) {

                    buffer.putBakedQuad(
                            pose,
                            bakedQuad,
                            quad
                    );
                }
            }

            List<BakedQuad> general =
                    part.getQuads(
                            null
                    );

            if (!general.isEmpty()) {

                quad.setColor(
                        0xFFFFFFFF
                );

                for (BakedQuad bakedQuad :
                        general) {

                    buffer.putBakedQuad(
                            pose,
                            bakedQuad,
                            quad
                    );
                }
            }
        }
    }

    private static int getShade(
            Direction direction
    ) {
        return switch (direction) {
            case DOWN ->
                    0xFF707070;

            case UP ->
                    0xFFFFFFFF;

            case NORTH, SOUTH ->
                    0xFFD0D0D0;

            case WEST, EAST ->
                    0xFFB8B8B8;
        };
    }

    @Override
    public RenderPipeline pipeline() {
        return RenderPipelines.GUI_TEXTURED;
    }

    @Override
    public TextureSetup textureSetup() {
        Minecraft mc =
                Minecraft.getInstance();

        return TextureSetup.singleTexture(
                mc.getAtlasManager()
                        .getAtlasOrThrow(
                                AtlasIds.BLOCKS
                        )
                        .getTextureView(),

                RenderSystem.getSamplerCache()
                        .getClampToEdge(
                                FilterMode.NEAREST
                        )
        );
    }

    @Override
    public ScreenRectangle bounds() {
        int radius =
                Math.max(
                        16,
                        size * 2
                );

        return new ScreenRectangle(
                x - radius,
                y - radius,
                radius * 2,
                radius * 2
        );
    }

    @Override
    public ScreenRectangle scissorArea() {
        return bounds();
    }
}