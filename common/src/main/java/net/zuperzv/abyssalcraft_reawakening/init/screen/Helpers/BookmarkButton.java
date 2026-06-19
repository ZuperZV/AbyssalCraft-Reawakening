package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.ModItems;

public class BookmarkButton extends Button {
    private static final Identifier BOOK_TEXTURE =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/book.png");
    private static final Identifier BOOK_TEXTURE_GRAY =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/book_gray.png");
    int zLayer = 0;

    public BookmarkButton(int x, int y, int zLayer, OnPress onPress) {
        super(x, y, 11, 13, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.zLayer = zLayer;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTick) {
        boolean hovered = this.isHoveredOrFocused();
        int texU = 0;
        int texV = hovered ? 223 : 210;
        int drawW = hovered ? 11 : 7;
        int drawH = 13;

        gui.pose().translate(0, 0);
        gui.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                this.getX(),
                this.getY(),
                texU,
                texV,
                drawW,
                drawH,
                256,
                256
        );
        drawColoredOverlay(gui, this.getX(), this.getY(), texU, texV, drawW, drawH, zLayer);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private void drawColoredOverlay(GuiGraphicsExtractor guiGraphics,
                                    int x_p, int y_p,
                                    int x, int y,
                                    int width, int height,
                                    int z_Layer) {

        var minecraft = net.minecraft.client.Minecraft.getInstance();
        if (minecraft.player == null) return;

        ItemStack stack = minecraft.player.getMainHandItem();
        if (stack.isEmpty()) stack = minecraft.player.getOffhandItem();

        if (stack.isEmpty() || !stack.is(ModItems.NECRONOMICON.get())) {
            stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory()
                    .findSlotMatchingItem(new ItemStack(ModItems.NECRONOMICON.get())));
        }

        if (!stack.is(ModItems.NECRONOMICON.get())) return;

        DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);

        int rgb = (dyedColor != null) ? dyedColor.rgb() : 0x643732;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE_GRAY,
                x_p,
                y_p,
                x,
                y,
                width,
                height,
                256,
                256,
                rgb
        );
    }
}