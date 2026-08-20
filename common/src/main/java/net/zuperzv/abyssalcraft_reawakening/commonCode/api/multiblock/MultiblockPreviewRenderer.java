package net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
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

    private static final Map<BlockState, BlockFaceSprites> SPRITE_CACHE =
            new HashMap<>();

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
        Minecraft mc =
                Minecraft.getInstance();

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
        Minecraft mc =
                Minecraft.getInstance();

        MultiblockPreviewInput.setScreenOffset(
                (int) Math.round(
                        scaledMouseX(mc) - mouseX
                ),
                (int) Math.round(
                        scaledMouseY(mc) - mouseY
                )
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

        MultiblockPreviewInput.setBounds(
                x,
                y,
                previewWidth,
                Math.max(1, height - 20),

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

        updateHoveredBlock(
                structure,
                x + 2,
                y + 2,
                Math.max(1, previewWidth - 4),
                Math.max(1, height - 22),
                mouseX,
                mouseY
        );

        drawStructure(
                graphics,
                structure,
                x + 2,
                y + 2,
                Math.max(1, previewWidth - 4),
                Math.max(1, height - 22)
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

        Item direct =
                block.asItem();

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

        for (
                int layer = 0;
                layer < structure.height();
                layer++
        ) {
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
                Math.max(
                        1,
                        structure.size().x()
                );

        float sizeY =
                Math.max(
                        1,
                        structure.size().y()
                );

        float sizeZ =
                Math.max(
                        1,
                        structure.size().z()
                );

        float horizontal =
                Math.max(
                        1.0f,
                        sizeX + sizeZ
                );

        float vertical =
                Math.max(
                        1.0f,
                        sizeY + (sizeX + sizeZ) * 0.5f
                );

        float sx =
                width / horizontal;

        float sy =
                height / vertical;

        return Math.max(
                8.0f,
                Math.min(
                        32.0f,
                        Math.min(sx, sy)
                )
        ) * MultiblockPreviewInput.getZoom();
    }

    private static void drawStructure(
            GuiGraphicsExtractor graphics,
            MultiblockStructure structure,
            int x,
            int y,
            int width,
            int height
    ) {
        List<MultiblockStructure.BlockEntry> blocks =
                getVisibleBlocks(structure);

        if (blocks.isEmpty()) {
            return;
        }

        float scale =
                calculateScale(
                        structure,
                        width,
                        height
                );

        float originX =
                x + width / 2.0f;

        float originY =
                y + height / 2.0f;

        List<MultiblockStructure.BlockEntry> sorted =
                new ArrayList<>(blocks);

        sorted.sort(
                Comparator
                        .comparingDouble(
                                (MultiblockStructure.BlockEntry entry) ->
                                        project(
                                                entry,
                                                structure,
                                                originX,
                                                originY,
                                                scale
                                        )[1]
                        )
                        .thenComparingInt(
                                entry ->
                                        entry.pos().getY()
                        )
        );

        for (MultiblockStructure.BlockEntry entry : sorted) {

            BlockState state =
                    entry.state();

            float[] point =
                    project(
                            entry,
                            structure,
                            originX,
                            originY,
                            scale
                    );

            int centerX =
                    Math.round(point[0]);

            int centerY =
                    Math.round(point[1]);

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

            int topCenterY =
                    centerY - half;

            int bottomY =
                    centerY + half;

            graphics.fill(
                    centerX - half - 1,
                    topCenterY - 1,
                    centerX + half + 1,
                    bottomY + 1,
                    0x22000000
            );

            drawBlockCube(
                    graphics,
                    state,
                    centerX,
                    centerY,
                    blockSize
            );

            if (entry == hoveredBlock) {

                graphics.outline(
                        centerX - half - 2,
                        topCenterY - 2,
                        blockSize + 4,
                        blockSize + half + 4,
                        0xFFFFFFFF
                );
            }
        }
    }

    private static void drawBlockCube(
            GuiGraphicsExtractor graphics,
            BlockState state,
            int centerX,
            int centerY,
            int size
    ) {
        Minecraft mc =
                Minecraft.getInstance();

        BlockStateModel model =
                mc.getModelManager()
                        .getBlockStateModelSet()
                        .get(state);

        if (model == null) {
            return;
        }

        float rotationX =
                MultiblockPreviewInput.getRotationX();

        float rotationY =
                MultiblockPreviewInput.getRotationY();

        GuiGraphicsExtractorAccess.of(graphics)
                .abyssalcraft$addGuiElement(
                        new BlockPreviewRenderState(
                                state,
                                model,
                                centerX,
                                centerY,
                                size,
                                rotationX,
                                rotationY
                        )
                );
    }

    /**
     * Isometric projection.
     */
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
                entry.pos().getX() - cx;

        float py =
                entry.pos().getY() - cy;

        float pz =
                entry.pos().getZ() - cz;

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
                        pitchedY * scale * 0.75f;

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

        for (
                int layer = 0;
                layer < structure.height();
                layer++
        ) {
            Map<Block, Integer> layerCounts =
                    structure.getCountsForLayer(layer);

            for (
                    Map.Entry<Block, Integer> entry
                    : layerCounts.entrySet()
            ) {
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

        for (
                int visibleIndex = 0;
                visibleIndex < visibleRows;
                visibleIndex++
        ) {
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
        if (MultiblockPreviewInput.isInsideList(
                mouseX,
                mouseY
        )) {
            hoveredListBlock =
                    getHoveredListBlock(
                            mouseX,
                            mouseY
                    );

            hoveredBlock = null;
            return;
        }

        hoveredListBlock = null;

        if (!MultiblockPreviewInput.isInsidePreview(
                mouseX,
                mouseY
        )) {
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

        for (
                MultiblockStructure.BlockEntry entry
                : blocks
        ) {
            float[] point =
                    project(
                            entry,
                            structure,
                            originX,
                            originY,
                            scale
                    );

            float left =
                    point[0] - 9;

            float top =
                    point[1] - 20;

            float right =
                    point[0] + 13;

            float bottom =
                    point[1] + 13;

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
                closest =
                        distance;

                closestBlock =
                        entry;
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
                            hoveredBlock.state().getBlock()
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

    private record BlockFaceSprites(
            TextureAtlasSprite top,
            TextureAtlasSprite north,
            TextureAtlasSprite east,
            TextureAtlasSprite south,
            TextureAtlasSprite west
    ) {
        private TextureAtlasSprite get(
                Direction direction
        ) {
            return switch (direction) {
                case EAST -> east;
                case SOUTH -> south;
                case WEST -> west;
                case NORTH -> north;
                default -> top;
            };
        }
    }
}
