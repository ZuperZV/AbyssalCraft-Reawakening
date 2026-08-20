package net.zuperzv.abyssalcraft_reawakening.services;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock.MultiblockPreviewInput;
import net.zuperzv.abyssalcraft_reawakening.services.types.IMultiblockInput;

public final class NeoForgeMultiblockInput implements IMultiblockInput {
    public NeoForgeMultiblockInput() {
    }

    @Override
    public void register() {
        NeoForge.EVENT_BUS.register(NeoForgeMultiblockInput.class);
    }

    @SubscribeEvent
    public static void mousePressed(
            ScreenEvent.MouseButtonPressed.Pre event
    ) {
        if (MultiblockPreviewInput.mouseClicked(
                event.getMouseX(),
                event.getMouseY(),
                event.getButton()
        )) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void mouseDragged(
            ScreenEvent.MouseDragged.Pre event
    ) {
        if (MultiblockPreviewInput.mouseDragged(
                event.getMouseX(),
                event.getMouseY(),
                event.getMouseButton(),
                event.getDragX(),
                event.getDragY()
        )) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void mouseReleased(
            ScreenEvent.MouseButtonReleased.Pre event
    ) {
        if (MultiblockPreviewInput.mouseReleased(
                event.getMouseX(),
                event.getMouseY(),
                event.getButton()
        )) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void mouseScrolled(
            ScreenEvent.MouseScrolled.Pre event
    ) {
        if (MultiblockPreviewInput.mouseScrolled(
                event.getMouseX(),
                event.getMouseY(),
                event.getScrollDeltaY()
        )) {
            event.setCanceled(true);
        }
    }
}