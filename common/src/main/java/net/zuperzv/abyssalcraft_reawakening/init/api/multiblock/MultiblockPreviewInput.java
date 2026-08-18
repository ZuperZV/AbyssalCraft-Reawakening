package net.zuperzv.abyssalcraft_reawakening.init.api.multiblock;

import org.lwjgl.glfw.GLFW;

public final class MultiblockPreviewInput {

    public enum ViewMode {
        FULL,
        LAYER
    }

    private static int previewX;
    private static int previewY;
    private static int previewWidth;
    private static int previewHeight;

    private static int listX;
    private static int listY;
    private static int listWidth;
    private static int listHeight;

    private static int controlsX;
    private static int controlsY;
    private static int controlsWidth;

    private static int maxLayer = 1;
    private static int layer = 0;

    private static int listRows = 0;
    private static int listScroll = 0;

    private static float rotationX = 25.0f;
    private static float rotationY = -35.0f;

    private static float zoom = 1.0f;

    private static boolean dragging = false;
    private static double dragDistance = 0.0;

    private static ViewMode viewMode = ViewMode.FULL;

    private MultiblockPreviewInput() {
    }

    private static int screenOffsetX;
    private static int screenOffsetY;

    public static void setScreenOffset(
            int x,
            int y
    ) {
        screenOffsetX = x;
        screenOffsetY = y;
    }

    private static double localMouseX(
            double mouseX
    ) {
        return mouseX - screenOffsetX;
    }

    private static double localMouseY(
            double mouseY
    ) {
        return mouseY - screenOffsetY;
    }

    public static void setBounds(
            int previewX,
            int previewY,
            int previewWidth,
            int previewHeight,

            int listX,
            int listY,
            int listWidth,
            int listHeight,

            int controlsX,
            int controlsY,
            int controlsWidth,

            int maxLayer
    ) {
        MultiblockPreviewInput.previewX = previewX;
        MultiblockPreviewInput.previewY = previewY;
        MultiblockPreviewInput.previewWidth = previewWidth;
        MultiblockPreviewInput.previewHeight = previewHeight;

        MultiblockPreviewInput.listX = listX;
        MultiblockPreviewInput.listY = listY;
        MultiblockPreviewInput.listWidth = listWidth;
        MultiblockPreviewInput.listHeight = listHeight;

        MultiblockPreviewInput.controlsX = controlsX;
        MultiblockPreviewInput.controlsY = controlsY;
        MultiblockPreviewInput.controlsWidth = controlsWidth;

        MultiblockPreviewInput.maxLayer =
                Math.max(1, maxLayer);

        if (layer >= MultiblockPreviewInput.maxLayer) {
            layer = MultiblockPreviewInput.maxLayer - 1;
        }

        clampScroll();
    }

    public static void setListRows(int rows) {
        listRows = Math.max(0, rows);
        clampScroll();
    }

    private static void clampScroll() {
        int visibleRows =
                Math.max(
                        1,
                        (listHeight - 18) / 20
                );

        int maxScroll =
                Math.max(
                        0,
                        listRows - visibleRows
                );

        listScroll =
                Math.max(
                        0,
                        Math.min(
                                listScroll,
                                maxScroll
                        )
                );
    }

    public static boolean isInsidePreview(
            double mouseX,
            double mouseY
    ) {
        return inside(
                mouseX,
                mouseY,
                previewX,
                previewY,
                previewWidth,
                previewHeight
        );
    }

    public static boolean isInsideList(
            double mouseX,
            double mouseY
    ) {
        return inside(
                mouseX,
                mouseY,
                listX,
                listY,
                listWidth,
                listHeight
        );
    }

    public static boolean isInsideControls(
            double mouseX,
            double mouseY
    ) {
        return inside(
                mouseX,
                mouseY,
                controlsX,
                controlsY,
                controlsWidth,
                18
        );
    }

    public static int getLayer() {
        return layer;
    }

    public static int getMaxLayer() {
        return maxLayer;
    }

    public static int getListScroll() {
        return listScroll;
    }

    public static ViewMode getViewMode() {
        return viewMode;
    }

    public static boolean isFullView() {
        return viewMode == ViewMode.FULL;
    }

    public static boolean isLayerView() {
        return viewMode == ViewMode.LAYER;
    }

    public static float getZoom() {
        return zoom;
    }

    public static float getRotationX() {
        return rotationX;
    }

    public static float getRotationY() {
        return rotationY;
    }

    public static void resetRotation() {
        rotationX = 25.0f;
        rotationY = -35.0f;
    }

    public static void resetZoom() {
        zoom = 1.0f;
    }

    private static boolean inside(
            double mouseX,
            double mouseY,
            int x,
            int y,
            int width,
            int height
    ) {
        return mouseX >= x
                && mouseX < x + width
                && mouseY >= y
                && mouseY < y + height;
    }

    private static void nextLayer() {
        layer++;

        if (layer >= maxLayer) {
            layer = 0;
        }
    }

    private static void previousLayer() {
        layer--;

        if (layer < 0) {
            layer = maxLayer - 1;
        }
    }

    private static void toggleViewMode() {
        viewMode =
                viewMode == ViewMode.FULL
                        ? ViewMode.LAYER
                        : ViewMode.FULL;

        listScroll = 0;
    }

    public static boolean mouseClicked(
            double mouseX,
            double mouseY,
            int button
    ) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        mouseX =
                localMouseX(mouseX);

        mouseY =
                localMouseY(mouseY);

        if (isInsideControls(mouseX, mouseY)) {

            int leftButtonX =
                    controlsX + 4;

            int rightButtonX =
                    controlsX + controlsWidth - 18;

            int centerX =
                    controlsX +
                            controlsWidth / 2 -
                            34;

            if (inside(
                    mouseX,
                    mouseY,
                    leftButtonX,
                    controlsY + 2,
                    14,
                    14
            )) {
                if (viewMode == ViewMode.LAYER) {
                    previousLayer();
                } else {
                    viewMode = ViewMode.LAYER;
                    previousLayer();
                }

                return true;
            }

            if (inside(
                    mouseX,
                    mouseY,
                    rightButtonX,
                    controlsY + 2,
                    14,
                    14
            )) {
                if (viewMode == ViewMode.LAYER) {
                    nextLayer();
                } else {
                    viewMode = ViewMode.LAYER;
                    nextLayer();
                }

                return true;
            }

            if (inside(
                    mouseX,
                    mouseY,
                    centerX,
                    controlsY + 1,
                    68,
                    16
            )) {
                toggleViewMode();
                return true;
            }
        }

        if (isInsidePreview(mouseX, mouseY)) {
            dragging = true;
            dragDistance = 0.0;
            return true;
        }

        if (isInsideList(mouseX, mouseY)) {
            return MultiblockPreviewRenderer.handleBlockListClick(
                    mouseX,
                    mouseY
            );
        }

        return false;
    }

    public static boolean mouseDragged(
            double mouseX,
            double mouseY,
            int button,
            double dragX,
            double dragY
    ) {
        if (!dragging) {
            return false;
        }

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        dragDistance +=
                Math.abs(dragX) +
                        Math.abs(dragY);

        rotationY += (float) dragX * 0.8f;
        rotationX += (float) dragY * 0.6f;

        rotationX =
                Math.max(
                        -80.0f,
                        Math.min(
                                80.0f,
                                rotationX
                        )
                );

        return true;
    }

    public static boolean mouseReleased(
            double mouseX,
            double mouseY,
            int button
    ) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        if (!dragging) {
            return false;
        }

        mouseX =
                localMouseX(mouseX);

        mouseY =
                localMouseY(mouseY);

        dragging = false;

        if (dragDistance <= 3.0
                && isInsidePreview(
                mouseX,
                mouseY
        )) {
            dragDistance = 0.0;

            return MultiblockPreviewRenderer.handlePreviewClick(
                    mouseX,
                    mouseY
            );
        }

        dragDistance = 0.0;
        return true;
    }

    public static boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double scrollY
    ) {
        mouseX =
                localMouseX(mouseX);

        mouseY =
                localMouseY(mouseY);

        if (isInsidePreview(mouseX, mouseY)) {

            zoom += (float) scrollY * 0.10f;

            zoom =
                    Math.max(
                            0.35f,
                            Math.min(
                                    3.5f,
                                    zoom
                            )
                    );

            return true;
        }

        if (isInsideList(mouseX, mouseY)) {

            listScroll -=
                    (int) Math.signum(scrollY);

            clampScroll();

            return true;
        }

        return false;
    }
}
