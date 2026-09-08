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
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MultiblockPreviewRenderState
        implements GuiElementRenderState {

    public record Entry(
            BlockPos localPos,
            BlockState state,
            BlockStateModel model
    ) {
    }

    private final List<Entry> entries;

    private final Matrix3x2f guiPose;

    private final int x;
    private final int y;
    private final float scale;

    private final float rotationX;
    private final float rotationY;

    private final float centerX;
    private final float centerY;
    private final float centerZ;

    public MultiblockPreviewRenderState(
            List<Entry> entries,
            Matrix3x2f guiPose,
            int x,
            int y,
            float scale,
            float rotationX,
            float rotationY,
            float centerX,
            float centerY,
            float centerZ
    ) {
        this.entries = entries;
        this.guiPose = guiPose;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.centerX = centerX + 0.5f;
        this.centerY = centerY + 0.5f;
        this.centerZ = centerZ + 0.5f;
    }

    @Override
    public void buildVertices(
            VertexConsumer buffer
    ) {
        if (entries.isEmpty()) {
            return;
        }

        PoseStack poseStack =
                new PoseStack();

        Matrix4f outer = new Matrix4f(
                guiPose.m00(), guiPose.m01(), 0.0f, 0.0f,
                guiPose.m10(), guiPose.m11(), 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                guiPose.m20(), guiPose.m21(), 0.0f, 1.0f
        );

        poseStack.last().pose().mul(outer);

        poseStack.translate(
                x,
                y,
                0.0F
        );

        poseStack.scale(
                scale,
                -scale,
                scale
        );

        poseStack.mulPose(
                Axis.XP.rotationDegrees(
                        rotationX
                )
        );

        poseStack.mulPose(
                Axis.YP.rotationDegrees(
                        rotationY
                )
        );

        poseStack.translate(
                -centerX,
                -centerY,
                -centerZ
        );

        QuadInstance quad =
                new QuadInstance();

        quad.setLightCoords(
                0xF000F0
        );

        quad.setOverlayCoords(
                0
        );

        List<FaceEntry> faces =
                new ArrayList<>();

        for (Entry entry : entries) {

            RandomSource random =
                    RandomSource.create(
                            entry.state().getSeed(
                                    entry.localPos()
                            )
                    );

            List<BlockStateModelPart> parts =
                    new ArrayList<>();

            entry.model().collectParts(
                    random,
                    parts
            );

            if (parts.isEmpty()) {
                continue;
            }

            for (BlockStateModelPart part : parts) {

                for (Direction direction : Direction.values()) {

                    if (!isFaceVisible(direction, rotationX, rotationY)) {
                        continue;
                    }

                    List<BakedQuad> quads =
                            part.getQuads(direction);

                    if (quads.isEmpty()) {
                        continue;
                    }

                    double depth =
                            faceDepth(
                                    entry,
                                    direction
                            );

                    for (BakedQuad bakedQuad : quads) {

                        faces.add(
                                new FaceEntry(
                                        entry,
                                        bakedQuad,
                                        direction,
                                        depth
                                )
                        );
                    }
                }

                List<BakedQuad> general =
                        part.getQuads(null);

                if (!general.isEmpty()) {

                    double depth =
                            blockDepth(entry);

                    for (BakedQuad bakedQuad : general) {

                        faces.add(
                                new FaceEntry(
                                        entry,
                                        bakedQuad,
                                        null,
                                        depth
                                )
                        );
                    }
                }
            }
        }

        faces.sort(
                Comparator.comparingDouble(
                        FaceEntry::depth
                )
        );

        for (FaceEntry face : faces) {

            Entry entry =
                    face.entry();

            poseStack.pushPose();

            poseStack.translate(
                    entry.localPos().getX(),
                    entry.localPos().getY(),
                    entry.localPos().getZ()
            );

            if (face.direction() != null) {

                quad.setColor(
                        getShade(
                                face.direction()
                        )
                );

            } else {

                quad.setColor(
                        0xFFFFFFFF
                );
            }

            buffer.putBakedQuad(
                    poseStack.last(),
                    face.quad(),
                    quad
            );

            poseStack.popPose();
        }
    }

    private record FaceEntry(
            Entry entry,
            BakedQuad quad,
            Direction direction,
            double depth
    ) {
    }

    private double blockDepth(
            Entry entry
    ) {
        double wx =
                entry.localPos().getX()
                        + 0.5
                        - centerX;

        double wy =
                entry.localPos().getY()
                        + 0.5
                        - centerY;

        double wz =
                entry.localPos().getZ()
                        + 0.5
                        - centerZ;

        double yaw =
                Math.toRadians(rotationY);

        double pitch =
                Math.toRadians(rotationX);

        double cosYaw =
                Math.cos(yaw);

        double sinYaw =
                Math.sin(yaw);

        double cosPitch =
                Math.cos(pitch);

        double sinPitch =
                Math.sin(pitch);

        double rotatedZ =
                -wx * sinYaw
                        +
                        wz * cosYaw;

        return wy * sinPitch
                +
                rotatedZ * cosPitch;
    }

    private double faceDepth(
            Entry entry,
            Direction direction
    ) {
        double wx =
                entry.localPos().getX()
                        + 0.5
                        - centerX;

        double wy =
                entry.localPos().getY()
                        + 0.5
                        - centerY;

        double wz =
                entry.localPos().getZ()
                        + 0.5
                        - centerZ;

        wx += direction.getStepX() * 0.5;
        wy += direction.getStepY() * 0.5;
        wz += direction.getStepZ() * 0.5;

        double yaw =
                Math.toRadians(rotationY);

        double pitch =
                Math.toRadians(rotationX);

        double cosYaw =
                Math.cos(yaw);

        double sinYaw =
                Math.sin(yaw);

        double cosPitch =
                Math.cos(pitch);

        double sinPitch =
                Math.sin(pitch);

        double rotatedZ =
                -wx * sinYaw
                        +
                        wz * cosYaw;

        return wy * sinPitch
                +
                rotatedZ * cosPitch;
    }

    private static boolean isFaceVisible(
            Direction direction,
            float rotationX,
            float rotationY
    ) {
        double yaw =
                Math.toRadians(rotationY);

        double pitch =
                Math.toRadians(rotationX);

        double sinYaw =
                Math.sin(yaw);

        double cosYaw =
                Math.cos(yaw);

        double sinPitch =
                Math.sin(pitch);

        double cosPitch =
                Math.cos(pitch);


        double cameraX =
                sinYaw * cosPitch;

        double cameraY =
                sinPitch;

        double cameraZ =
                cosYaw * cosPitch;

        double normalX =
                direction.getStepX();

        double normalY =
                direction.getStepY();

        double normalZ =
                direction.getStepZ();

        double dot =
                normalX * cameraX
                        +
                        normalY * cameraY
                        +
                        normalZ * cameraZ;

        return dot > 0.0;
    }

    private List<Entry> sortBackToFront() {
        double yaw =
                Math.toRadians(rotationY);

        double pitch =
                Math.toRadians(rotationX);

        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);

        List<Entry> sorted =
                new ArrayList<>(entries);

        sorted.sort(
                Comparator.comparingDouble(
                        entry -> depthOf(
                                entry,
                                cosYaw,
                                sinYaw,
                                cosPitch,
                                sinPitch
                        )
                )
        );

        return sorted;
    }

    private double depthOf(
            Entry entry,
            double cosYaw,
            double sinYaw,
            double cosPitch,
            double sinPitch
    ) {
        double wx =
                entry.localPos().getX() + 0.5 - centerX;

        double wy =
                entry.localPos().getY() + 0.5 - centerY;

        double wz =
                entry.localPos().getZ() + 0.5 - centerZ;

        double rotatedZ =
                wx * sinYaw + wz * cosYaw;

        return wy * sinPitch + rotatedZ * cosPitch;
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
        if (entries.isEmpty()) {
            return null;
        }

        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;

        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        PoseStack poseStack = new PoseStack();

        Matrix4f outer = new Matrix4f(
                guiPose.m00(), guiPose.m01(), 0.0f, 0.0f,
                guiPose.m10(), guiPose.m11(), 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                guiPose.m20(), guiPose.m21(), 0.0f, 1.0f
        );

        poseStack.last().pose().mul(outer);

        poseStack.translate(
                x,
                y,
                0.0f
        );

        poseStack.scale(
                scale,
                -scale,
                scale
        );

        poseStack.mulPose(
                Axis.XP.rotationDegrees(rotationX)
        );

        poseStack.mulPose(
                Axis.YP.rotationDegrees(rotationY)
        );

        poseStack.translate(
                -centerX,
                -centerY,
                -centerZ
        );

        Matrix4f matrix =
                new Matrix4f(
                        poseStack.last().pose()
                );

        for (Entry entry : entries) {

            float bx = entry.localPos().getX();
            float by = entry.localPos().getY();
            float bz = entry.localPos().getZ();

            for (int ix = 0; ix <= 1; ix++) {
                for (int iy = 0; iy <= 1; iy++) {
                    for (int iz = 0; iz <= 1; iz++) {

                        float px = bx + ix;
                        float py = by + iy;
                        float pz = bz + iz;

                        Vector2f point =
                                new Vector2f();

                        Matrix4f temp =
                                new Matrix4f(matrix);

                        org.joml.Vector4f transformed =
                                new org.joml.Vector4f(
                                        px,
                                        py,
                                        pz,
                                        1.0f
                                ).mul(temp);

                        point.x = transformed.x;
                        point.y = transformed.y;

                        minX =
                                Math.min(
                                        minX,
                                        point.x
                                );

                        minY =
                                Math.min(
                                        minY,
                                        point.y
                                );

                        maxX =
                                Math.max(
                                        maxX,
                                        point.x
                                );

                        maxY =
                                Math.max(
                                        maxY,
                                        point.y
                                );
                    }
                }
            }
        }

        if (!Float.isFinite(minX)
                || !Float.isFinite(minY)
                || !Float.isFinite(maxX)
                || !Float.isFinite(maxY)) {

            return null;
        }

        int padding = 4;

        int left =
                (int) Math.floor(minX) - padding;

        int top =
                (int) Math.floor(minY) - padding;

        int right =
                (int) Math.ceil(maxX) + padding;

        int bottom =
                (int) Math.ceil(maxY) + padding;

        return new ScreenRectangle(
                left,
                top,
                Math.max(1, right - left),
                Math.max(1, bottom - top)
        );
    }

    @Override
    public ScreenRectangle scissorArea() {
        return null;
    }
}