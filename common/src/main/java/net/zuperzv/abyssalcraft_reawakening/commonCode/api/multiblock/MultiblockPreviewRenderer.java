package net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;

import java.lang.reflect.Method;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.access.GuiGraphicsExtractorAccess;
import org.joml.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MultiblockPreviewRenderer {

    public static final int WIDTH = 220;
    public static final int HEIGHT = 122;

    private static MultiblockStructure currentStructure;

    private static MultiblockStructure.BlockEntry hoveredBlock;
    private static Block hoveredListBlock;

    private static int currentListX;
    private static int currentListY;
    private static int currentListWidth;
    private static int currentListHeight;

    private static List<Map.Entry<Block, Integer>> currentListEntries =
            List.of();

    private MultiblockPreviewRenderer() {
    }

    public static void render(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            int width,
            int height,
            Identifier structureId
    ) {
        Minecraft mc = Minecraft.getInstance();

        render(
                graphics,
                x,
                y,
                width,
                height,
                structureId,
                scaledMouseX(mc),
                scaledMouseY(mc)
        );
    }

    public static void render(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            int width,
            int height,
            Identifier structureId,
            double mouseX,
            double mouseY
    ) {
        Minecraft mc = Minecraft.getInstance();

        MultiblockPreviewInput.setScreenOffset(
                (int) Math.round(
                        scaledMouseX(mc) - mouseX
                ) - x,
                (int) Math.round(
                        scaledMouseY(mc) - mouseY
                ) - y
        );

        MultiblockStructure structure =
                MultiblockStructure.load(structureId);

        if (structure == null) {
            graphics.text(
                    mc.font,
                    Component.literal(
                            "Missing: " + structureId
                    ),
                    x,
                    y,
                    0xFFFF5555
            );
            return;
        }

        currentStructure = structure;

        int previewWidth =
                Math.min(
                        122,
                        Math.max(
                                100,
                                width / 2
                        )
                );

        int listX =
                x + previewWidth + 4;

        int listWidth =
                Math.max(
                        0,
                        width - previewWidth - 4
                );

        int controlsY =
                y + height - 18;

        int previewHeight =
                Math.max(
                        1,
                        height - 20
                );

        MultiblockPreviewInput.setBounds(
                x,
                y,
                previewWidth,
                previewHeight,

                listX,
                y + 2,
                listWidth,
                Math.max(1, height - 24),

                x,
                controlsY,
                width,

                structure.height()
        );

        drawBackground(
                graphics,
                x,
                y,
                width,
                height,
                previewWidth
        );

        hoveredBlock = null;
        hoveredListBlock = null;

        /*
         * Find hovered block before submitting the render
         */
        updateHoveredBlock(
                structure,
                x + 2,
                y + 2,
                Math.max(1, previewWidth - 4),
                previewHeight,
                mouseX,
                mouseY
        );

        /*
         * Render the complete multiblock
         */
        drawStructure(
                graphics,
                structure,
                x + 2,
                y + 2,
                Math.max(1, previewWidth - 4),
                previewHeight
        );

        drawBlockList(
                graphics,
                structure,
                listX,
                y + 2,
                listWidth,
                Math.max(1, height - 24)
        );

        drawControls(
                graphics,
                structure,
                x,
                controlsY,
                width
        );

        drawTooltip(
                graphics,
                mouseX,
                mouseY
        );
    }

    private static ItemStack getRecipeItem(
            Block block
    ) {
        if (block == null) {
            return ItemStack.EMPTY;
        }

        Item direct = block.asItem();

        if (direct != Items.AIR) {
            return new ItemStack(direct);
        }

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BlockItem blockItem
                    && blockItem.getBlock() == block) {

                return new ItemStack(item);
            }
        }

        return ItemStack.EMPTY;
    }

    private static void drawBackground(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            int width,
            int height,
            int previewWidth
    ) {
        graphics.fill(
                x,
                y,
                x + width,
                y + height,
                0xAA101010
        );

        graphics.fill(
                x + 1,
                y + 1,
                x + previewWidth - 1,
                y + height - 19,
                0x66202020
        );

        graphics.fill(
                x + previewWidth + 2,
                y + 1,
                x + width - 1,
                y + height - 19,
                0x66202020
        );
    }

    private static List<MultiblockStructure.BlockEntry> getVisibleBlocks(
            MultiblockStructure structure
    ) {
        if (MultiblockPreviewInput.isLayerView()) {
            return new ArrayList<>(
                    structure.getLayer(
                            MultiblockPreviewInput.getLayer()
                    )
            );
        }

        List<MultiblockStructure.BlockEntry> result =
                new ArrayList<>();

        for (int layer = 0;
             layer < structure.height();
             layer++) {

            result.addAll(
                    structure.getLayer(layer)
            );
        }

        return result;
    }

    private static float calculateScale(
            MultiblockStructure structure,
            int width,
            int height
    ) {
        float sizeX =
                Math.max(1, structure.size().x());

        float sizeY =
                Math.max(1, structure.size().y());

        float sizeZ =
                Math.max(1, structure.size().z());

        float horizontal =
                sizeX + sizeZ + 2.0f;

        float vertical =
                sizeY
                        + (sizeX + sizeZ) * 0.5f
                        + 2.0f;

        float sx =
                width / horizontal;

        float sy =
                height / vertical;

        float baseScale =
                Math.min(sx, sy);

        float zoom =
                MultiblockPreviewInput.getZoom();

        return Math.max(
                1.0f,
                baseScale * zoom * 0.90f
        );
    }

    private static void drawStructure(
            GuiGraphicsExtractor graphics,
            MultiblockStructure structure,
            int x,
            int y,
            int width,
            int height
    ) {
        // First try BlockRenderDispatcher rendering with scissor disabled so GUI clipping
        // doesn't cut off faces. If dispatcher isn't available, fall back to GUI-element path.
        boolean scissorWasEnabled = false;
        try {
            scissorWasEnabled = org.lwjgl.opengl.GL11.glIsEnabled(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        } catch (Throwable ignored) {
        }

        try {
            org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        } catch (Throwable ignored) {
        }

        boolean dispatched = false;
        try {
            dispatched = drawStructureWithBlockRenderer(graphics, structure, x, y, width, height);
        } catch (Throwable ignored) {
        }

        try {
            if (scissorWasEnabled) org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        } catch (Throwable ignored) {
        }

        if (dispatched) return;

        // Fallback GUI-element rendering path (works reliably in JEI). This builds a
        // MultiblockPreviewRenderState and submits it to the GUI so that all faces
        // are rendered in correct depth order.
        List<MultiblockStructure.BlockEntry> blocks =
                getVisibleBlocks(structure);

        if (blocks.isEmpty()) {
            return;
        }

        int padding = 12;

        int renderWidth =
                width - padding * 2;

        int renderHeight =
                height - padding * 2;

        if (renderWidth <= 0 || renderHeight <= 0) {
            return;
        }

        float scale =
                calculateScale(
                        structure,
                        renderWidth,
                        renderHeight
                );

        float centerX =
                (structure.size().x() - 1) / 2.0f;

        float centerY;

        if (MultiblockPreviewInput.isLayerView()) {
            centerY =
                    MultiblockPreviewInput.getLayer();
        } else {
            centerY =
                    (structure.size().y() - 1) / 2.0f;
        }

        float centerZ =
                (structure.size().z() - 1) / 2.0f;

        List<MultiblockPreviewRenderState.Entry> entries =
                new ArrayList<>(blocks.size());

        Minecraft mc =
                Minecraft.getInstance();

        for (MultiblockStructure.BlockEntry entry : blocks) {

            BlockState state =
                    entry.state();

            BlockStateModel model =
                    mc.getModelManager()
                            .getBlockStateModelSet()
                            .get(state);

            if (model == null) {
                continue;
            }

            entries.add(
                    new MultiblockPreviewRenderState.Entry(
                            new BlockPos(
                                    entry.pos().getX(),
                                    entry.pos().getY(),
                                    entry.pos().getZ()
                            ),
                            state,
                            model
                    )
            );
        }

        if (entries.isEmpty()) {
            try { org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_DEPTH_TEST); } catch (Throwable ignored) {}
            return false;
        }

        org.joml.Matrix3x2f guiPose =
                new org.joml.Matrix3x2f(
                        graphics.pose()
                );

        float rotationX =
                MultiblockPreviewInput.getRotationX();

        float rotationY =
                MultiblockPreviewInput.getRotationY();

        float renderX =
                x + width / 2.0f;

        float renderY =
                y + height / 2.0f;

        GuiGraphicsExtractorAccess.of(graphics)
                .abyssalcraft$addGuiElement(
                        new MultiblockPreviewRenderState(
                                entries,
                                guiPose,
                                Math.round(renderX),
                                Math.round(renderY),
                                scale,
                                rotationX,
                                rotationY,
                                centerX,
                                centerY,
                                centerZ
                        )
                );

        if (hoveredBlock != null) {

            float[] point =
                    project(
                            hoveredBlock,
                            structure,
                            renderX,
                            renderY,
                            scale
                    );

            int blockSize =
                    Math.max(
                            8,
                            Math.min(
                                    24,
                                    Math.round(scale * 0.55f)
                            )
                    );

            int half =
                    blockSize / 2;

            int centerBlockX =
                    Math.round(point[0]);

            int centerBlockY =
                    Math.round(point[1]);

            graphics.outline(
                    centerBlockX - half - 2,
                    centerBlockY - half - 2,
                    blockSize + 4,
                    blockSize + half + 4,
                    0xFFFFFFFF
            );
        }
    }

    // New rendering path using Minecraft's BlockRenderDispatcher. This keeps the same
    // centering/scale/rotation logic but delegates actual block rendering to the
    // vanilla dispatcher which avoids many clipping/frustum issues.
    private static boolean drawStructureWithBlockRenderer(
            GuiGraphicsExtractor graphics,
            MultiblockStructure structure,
            int x,
            int y,
            int width,
            int height
    ) {
        List<MultiblockStructure.BlockEntry> blocks =
                getVisibleBlocks(structure);

        if (blocks.isEmpty()) return false;

        int padding = 12;
        int renderWidth = width - padding * 2;
        int renderHeight = height - padding * 2;
        if (renderWidth <= 0 || renderHeight <= 0) return false;

        float scale = calculateScale(structure, renderWidth, renderHeight);

        float centerX = (structure.size().x() - 1) / 2.0f;
        float centerY = MultiblockPreviewInput.isLayerView() ? MultiblockPreviewInput.getLayer() : (structure.size().y() - 1) / 2.0f;
        float centerZ = (structure.size().z() - 1) / 2.0f;

        Minecraft mc = Minecraft.getInstance();
        Object blockDispatcher = null;
        try {
            Method getter = mc.getClass().getMethod("getBlockRenderer");
            blockDispatcher = getter.invoke(mc);
        } catch (Exception ignored) {
            try {
                Method getter = mc.getClass().getMethod("getBlockRendererDispatcher");
                blockDispatcher = getter.invoke(mc);
            } catch (Exception ignored2) {
                // leave null and fall back to legacy path
            }
        }

        MultiBufferSource buffers = mc.renderBuffers().bufferSource();

        // Enable depth testing so the full 3D multiblock renders correctly in GUIs
        try {
            org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_DEPTH_TEST);
        } catch (Throwable ignored) {
        }

        // Prepare pose stack matching legacy transforms
        PoseStack pose = new PoseStack();

        org.joml.Matrix3x2f guiPose = new org.joml.Matrix3x2f(graphics.pose());
        Matrix4f outer = new Matrix4f(
                guiPose.m00(), guiPose.m01(), 0.0f, 0.0f,
                guiPose.m10(), guiPose.m11(), 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                guiPose.m20(), guiPose.m21(), 0.0f, 1.0f
        );

        pose.last().pose().set(outer);

        float renderX = x + width / 2.0f;
        float renderY = y + height / 2.0f;

        pose.translate(renderX, renderY, 0.0f);
        pose.scale(scale, -scale, scale);
        pose.mulPose(Axis.XP.rotationDegrees(MultiblockPreviewInput.getRotationX()));
        pose.mulPose(Axis.YP.rotationDegrees(MultiblockPreviewInput.getRotationY()));
        pose.translate(-centerX, -centerY, -centerZ);

        int light = 0xF000F0;

        Method renderMethod = null;
        if (blockDispatcher != null) {
            for (Method m : blockDispatcher.getClass().getMethods()) {
                if (m.getName().equals("renderSingleBlock")) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length >= 4) { // loose check
                        renderMethod = m;
                        break;
                    }
                }
            }
        }

        // If no dispatcher or method found, fall back to GUI-element path
        if (blockDispatcher == null || renderMethod == null) {
            try { org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_DEPTH_TEST); } catch (Throwable ignored) {}
            return false;
        }

        for (MultiblockStructure.BlockEntry be : blocks) {
            pose.pushPose();
            pose.translate(be.pos().getX(), be.pos().getY(), be.pos().getZ());
            try {
                if (renderMethod != null && blockDispatcher != null) {
                    // try invoke the found method
                    try {
                        // common signature: (BlockState, PoseStack, MultiBufferSource, int, int)
                        renderMethod.invoke(blockDispatcher, be.state(), pose, buffers, light, OverlayTexture.NO_OVERLAY);
                    } catch (IllegalArgumentException iae) {
                        // try alternative with fewer args
                        renderMethod.invoke(blockDispatcher, be.state(), pose, buffers, light);
                    }
                } else {
                    // No dispatcher available: fallback to legacy GUI element path already present elsewhere.
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            pose.popPose();
        }

        // Flush buffers
        try {
            mc.renderBuffers().bufferSource().endBatch();
        } catch (Throwable ignored) {
        }

        try {
            org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_DEPTH_TEST);
        } catch (Throwable ignored) {
        }

        return true;
    }

    private static float[] project(
            MultiblockStructure.BlockEntry entry,
            MultiblockStructure structure,
            float originX,
            float originY,
            float scale
    ) {
        float cx =
                (structure.size().x() - 1) / 2.0f;

        float cy;

        if (MultiblockPreviewInput.isLayerView()) {
            cy =
                    MultiblockPreviewInput.getLayer();
        } else {
            cy =
                    (structure.size().y() - 1) / 2.0f;
        }

        float cz =
                (structure.size().z() - 1) / 2.0f;

        float px =
                entry.pos().getX() + 0.5f - cx;

        float py =
                entry.pos().getY() + 0.5f - cy;

        float pz =
                entry.pos().getZ() + 0.5f - cz;

        double yaw =
                Math.toRadians(
                        MultiblockPreviewInput.getRotationY()
                );

        float rotatedX =
                (float) (
                        px * Math.cos(yaw)
                                -
                                pz * Math.sin(yaw)
                );

        float rotatedZ =
                (float) (
                        px * Math.sin(yaw)
                                +
                                pz * Math.cos(yaw)
                );

        double pitch =
                Math.toRadians(
                        MultiblockPreviewInput.getRotationX()
                );

        float pitchedY =
                (float) (
                        py * Math.cos(pitch)
                                -
                                rotatedZ * Math.sin(pitch)
                );

        float pitchedZ =
                (float) (
                        py * Math.sin(pitch)
                                +
                                rotatedZ * Math.cos(pitch)
                );

        float screenX =
                originX
                        +
                        (rotatedX - pitchedZ)
                                * scale
                                * 0.5f;

        float screenY =
                originY
                        +
                        (rotatedX + pitchedZ)
                                * scale
                                * 0.25f
                        -
                        pitchedY
                                * scale
                                * 0.75f;

        return new float[]{
                screenX,
                screenY
        };
    }

    private static Map<Block, Integer> getCounts(
            MultiblockStructure structure
    ) {
        if (MultiblockPreviewInput.isLayerView()) {
            return structure.getCountsForLayer(
                    MultiblockPreviewInput.getLayer()
            );
        }

        Map<Block, Integer> result =
                new HashMap<>();

        for (int layer = 0;
             layer < structure.height();
             layer++) {

            Map<Block, Integer> layerCounts =
                    structure.getCountsForLayer(layer);

            for (Map.Entry<Block, Integer> entry :
                    layerCounts.entrySet()) {

                result.merge(
                        entry.getKey(),
                        entry.getValue(),
                        Integer::sum
                );
            }
        }

        return result;
    }

    private static void drawBlockList(
            GuiGraphicsExtractor graphics,
            MultiblockStructure structure,
            int x,
            int y,
            int width,
            int height
    ) {
        Minecraft mc =
                Minecraft.getInstance();

        Font font =
                mc.font;

        Map<Block, Integer> counts =
                getCounts(structure);

        currentListEntries =
                counts.entrySet()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        entry ->
                                                BuiltInRegistries.BLOCK
                                                        .getKey(
                                                                entry.getKey()
                                                        )
                                                        .getNamespace()
                                                        .toString()
                                )
                        )
                        .toList();

        currentListX = x;
        currentListY = y;
        currentListWidth = width;
        currentListHeight = height;

        MultiblockPreviewInput.setListRows(
                currentListEntries.size()
        );

        if (currentListEntries.isEmpty()) {
            graphics.text(
                    font,
                    Component.literal("No blocks"),
                    x + 6,
                    y + 8,
                    0xFFAAAAAA
            );

            return;
        }

        int firstRow =
                MultiblockPreviewInput.getListScroll();

        int visibleRows =
                Math.max(
                        1,
                        (height - 4) / 20
                );

        int maxNameWidth =
                Math.max(
                        20,
                        width - 58
                );

        for (int visibleIndex = 0;
             visibleIndex < visibleRows;
             visibleIndex++) {

            int index =
                    firstRow + visibleIndex;

            if (index >= currentListEntries.size()) {
                break;
            }

            Map.Entry<Block, Integer> entry =
                    currentListEntries.get(index);

            Block block =
                    entry.getKey();

            int count =
                    entry.getValue();

            ItemStack stack =
                    getRecipeItem(block);

            int yy =
                    y + visibleIndex * 20 + 1;

            boolean hovered =
                    hoveredListBlock == block;

            if (hovered) {
                graphics.fill(
                        x + 2,
                        yy,
                        x + width - 3,
                        yy + 19,
                        0x55333333
                );
            }

            if (!stack.isEmpty()) {
                graphics.item(
                        stack,
                        x + 3,
                        yy + 1
                );
            }

            String name =
                    block.getName()
                            .getString();

            if (font.width(name) > maxNameWidth) {
                name =
                        font.plainSubstrByWidth(
                                name,
                                Math.max(
                                        4,
                                        maxNameWidth - 8
                                )
                        ) + "...";
            }

            String amount =
                    "x" + count;

            int amountWidth =
                    font.width(amount);

            graphics.text(
                    font,
                    Component.literal(name),
                    x + 22,
                    yy + 6,
                    0xFFFFFFFF
            );

            graphics.text(
                    font,
                    Component.literal(amount),
                    x + width - amountWidth - 5,
                    yy + 6,
                    0xFFAAAAAA
            );
        }

        int totalRows =
                currentListEntries.size();

        if (totalRows > visibleRows) {

            int barAreaHeight =
                    Math.max(
                            10,
                            height - 4
                    );

            int maxScroll =
                    totalRows - visibleRows;

            int barHeight =
                    Math.max(
                            10,
                            barAreaHeight *
                                    visibleRows /
                                    totalRows
                    );

            int barTravel =
                    barAreaHeight -
                            barHeight;

            int barY =
                    y + 2
                            +
                            (int) (
                                    barTravel *
                                            (
                                                    MultiblockPreviewInput
                                                            .getListScroll()
                                                            /
                                                            (float) maxScroll
                                            )
                            );

            graphics.fill(
                    x + width - 3,
                    barY,
                    x + width - 1,
                    barY + barHeight,
                    0xFF888888
            );
        }
    }

    private static void drawControls(
            GuiGraphicsExtractor graphics,
            MultiblockStructure structure,
            int x,
            int y,
            int width
    ) {
        Minecraft mc =
                Minecraft.getInstance();

        int leftX =
                x + 4;

        int rightX =
                x + width - 18;

        int centerX =
                x + width / 2 - 34;

        graphics.fill(
                leftX,
                y + 2,
                leftX + 14,
                y + 16,
                0xFF404040
        );

        graphics.fill(
                rightX,
                y + 2,
                rightX + 14,
                y + 16,
                0xFF404040
        );

        graphics.fill(
                centerX,
                y + 1,
                centerX + 68,
                y + 17,
                0xFF303030
        );

        graphics.centeredText(
                mc.font,
                Component.literal("<"),
                leftX + 7,
                y + 4,
                0xFFFFFFFF
        );

        graphics.centeredText(
                mc.font,
                Component.literal(">"),
                rightX + 7,
                y + 4,
                0xFFFFFFFF
        );

        Component modeText;

        if (MultiblockPreviewInput.isFullView()) {
            modeText =
                    Component.literal("FULL");
        } else {
            modeText =
                    Component.literal(
                            "LAYER "
                                    +
                                    (MultiblockPreviewInput.getLayer() + 1)
                                    +
                                    "/"
                                    +
                                    structure.height()
                    );
        }

        graphics.centeredText(
                mc.font,
                modeText,
                x + width / 2,
                y + 4,
                0xFFFFFFFF
        );
    }

    private static void updateHoveredBlock(
            MultiblockStructure structure,
            int previewX,
            int previewY,
            int width,
            int height,
            double mouseX,
            double mouseY
    ) {
        if (mouseX < previewX
                || mouseX > previewX + width
                || mouseY < previewY
                || mouseY > previewY + height) {

            hoveredBlock = null;
            return;
        }

        List<MultiblockStructure.BlockEntry> blocks =
                getVisibleBlocks(structure);

        if (blocks.isEmpty()) {
            hoveredBlock = null;
            return;
        }

        float scale =
                calculateScale(
                        structure,
                        width,
                        height
                );

        float originX =
                previewX + width / 2.0f;

        float originY =
                previewY + height / 2.0f;

        double closest =
                Double.MAX_VALUE;

        MultiblockStructure.BlockEntry closestBlock =
                null;

        for (MultiblockStructure.BlockEntry entry :
                blocks) {

            float[] point =
                    project(
                            entry,
                            structure,
                            originX,
                            originY,
                            scale
                    );

            float left =
                    point[0] - 10;

            float top =
                    point[1] - 18;

            float right =
                    point[0] + 14;

            float bottom =
                    point[1] + 14;

            if (mouseX < left
                    || mouseX > right
                    || mouseY < top
                    || mouseY > bottom) {
                continue;
            }

            double distance =
                    Math.hypot(
                            mouseX - point[0],
                            mouseY - point[1]
                    );

            if (distance < closest) {
                closest = distance;
                closestBlock = entry;
            }
        }

        hoveredBlock =
                closestBlock;
    }

    private static Block getHoveredListBlock(
            double mouseX,
            double mouseY
    ) {
        if (mouseX < currentListX
                || mouseX >= currentListX + currentListWidth) {
            return null;
        }

        if (mouseY < currentListY
                || mouseY >= currentListY + currentListHeight) {
            return null;
        }

        int localY =
                (int) mouseY -
                        currentListY;

        int row =
                Math.max(
                        0,
                        localY / 20
                );

        int index =
                MultiblockPreviewInput.getListScroll()
                        + row;

        if (index < 0
                || index >= currentListEntries.size()) {
            return null;
        }

        return currentListEntries
                .get(index)
                .getKey();
    }

    private static void drawTooltip(
            GuiGraphicsExtractor graphics,
            double mouseX,
            double mouseY
    ) {
        if (hoveredBlock != null) {

            ItemStack stack =
                    getRecipeItem(
                            hoveredBlock.state()
                                    .getBlock()
                    );

            if (!stack.isEmpty()) {
                graphics.setTooltipForNextFrame(
                        Minecraft.getInstance().font,
                        stack,
                        (int) mouseX,
                        (int) mouseY
                );
            }

            return;
        }

        if (hoveredListBlock != null) {

            ItemStack stack =
                    getRecipeItem(
                            hoveredListBlock
                    );

            if (!stack.isEmpty()) {
                graphics.setTooltipForNextFrame(
                        Minecraft.getInstance().font,
                        stack,
                        (int) mouseX,
                        (int) mouseY
                );
            }
        }
    }

    public static boolean handleBlockListClick(
            double mouseX,
            double mouseY
    ) {
        Block block =
                getHoveredListBlock(
                        mouseX,
                        mouseY
                );

        if (block == null) {
            return false;
        }

        ItemStack stack =
                getRecipeItem(block);

        if (stack.isEmpty()) {
            return false;
        }

        MultiblockRecipeViewer.showRecipes(
                stack
        );

        return true;
    }

    public static boolean handlePreviewClick(
            double mouseX,
            double mouseY
    ) {
        if (hoveredBlock == null) {
            return false;
        }

        ItemStack stack =
                getRecipeItem(
                        hoveredBlock.state()
                                .getBlock()
                );

        if (stack.isEmpty()) {
            return false;
        }

        MultiblockRecipeViewer.showRecipes(
                stack
        );

        return true;
    }

    private static double scaledMouseX(
            Minecraft mc
    ) {
        return mc.mouseHandler.xpos()
                / mc.getWindow().getGuiScale();
    }

    private static double scaledMouseY(
            Minecraft mc
    ) {
        return mc.mouseHandler.ypos()
                / mc.getWindow().getGuiScale();
    }
}