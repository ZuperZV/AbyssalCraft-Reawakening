package net.zuperzv.abyssalcraft_reawakening.services;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock.MultiblockPreviewInput;
import net.zuperzv.abyssalcraft_reawakening.services.types.IMultiblockInput;

public final class FabricMultiblockInput implements IMultiblockInput {
    public FabricMultiblockInput() {
    }

    @Override
    public void register() {
        ScreenEvents.BEFORE_INIT.register(
                (client, screen, scaledWidth, scaledHeight) -> {

                    //Click
                    ScreenMouseEvents
                            .allowMouseClick(screen)
                            .register(
                                    (screenInstance, event) ->
                                            !MultiblockPreviewInput.mouseClicked(
                                                    event.x(),
                                                    event.y(),
                                                    event.button()
                                            )
                            );

                    //Drag
                    ScreenMouseEvents
                            .allowMouseDrag(screen)
                            .register(
                                    (
                                            screenInstance,
                                            event,
                                            deltaX,
                                            deltaY
                                    ) ->
                                            !MultiblockPreviewInput.mouseDragged(
                                                    event.x(),
                                                    event.y(),
                                                    event.button(),
                                                    deltaX,
                                                    deltaY
                                            )
                            );

                    //Mouse Released
                    ScreenMouseEvents
                            .allowMouseRelease(screen)
                            .register(
                                    (screenInstance, event) ->
                                            !MultiblockPreviewInput.mouseReleased(
                                                    event.x(),
                                                    event.y(),
                                                    event.button()
                                            )
                            );

                    //Scroll
                    ScreenMouseEvents
                            .allowMouseScroll(screen)
                            .register(
                                    (
                                            screenInstance,
                                            mouseX,
                                            mouseY,
                                            horizontalAmount,
                                            verticalAmount
                                    ) ->
                                            !MultiblockPreviewInput.mouseScrolled(
                                                    mouseX,
                                                    mouseY,
                                                    verticalAmount
                                            )
                            );
                }
        );
    }
}