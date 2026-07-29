package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public class BackPageButton extends Button {
    private static final Identifier PAGE_BACK_HIGHLIGHTED_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "widget/page_back_highlighted");
    private static final Identifier PAGE_BACK_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "widget/page_back");
    private final boolean playTurnSound;

    public BackPageButton(int x, int y, OnPress onPress, boolean playSound) {
        super(x, y, 23, 13, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.playTurnSound = playSound;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Identifier texture = this.isHoveredOrFocused() ? PAGE_BACK_HIGHLIGHTED_SPRITE : PAGE_BACK_SPRITE;

        graphics.pose().translate(0, 0);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(), 23, 13);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.playTurnSound) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}