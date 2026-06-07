package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;

public class BackPageButton extends Button {
    private static final Identifier PAGE_BACK_HIGHLIGHTED_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/page_back_highlighted.png");
    private static final Identifier PAGE_BACK_SPRITE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/page_back.png");
    private final boolean playTurnSound;

    public BackPageButton(int p_99225_, int p_99226_, OnPress p_99228_, boolean p_99229_) {
        super(p_99225_, p_99226_, 23, 13, CommonComponents.EMPTY, p_99228_, DEFAULT_NARRATION);
        this.playTurnSound = p_99229_;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Identifier texture = this.isHoveredOrFocused() ? PAGE_BACK_HIGHLIGHTED_SPRITE : PAGE_BACK_SPRITE;

        graphics.pose().translate(0, 0);
        graphics.blit(texture, this.getX(), this.getY(), 0, 0, 23, 13, 23, 13);
    }

    @Override
    public void playDownSound(SoundManager p_99231_) {
        if (this.playTurnSound) {
            p_99231_.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}