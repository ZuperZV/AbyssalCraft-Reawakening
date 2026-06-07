package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.zuperzv.abyssalcraft_reawakening.init.mixin.AdvancementTabMixin;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;
import net.zuperzv.abyssalcraft_reawakening.services.util.MouseUtil;

import java.util.Map;

public class CustomAdvancementRenderer {

    public static void renderTooltipsOnly(AdvancementsScreen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, int guiLeft, int guiTop, NecronomiconScreen codexScreen, int X, int Y) {
        AdvancementTab selected = getSelectedTab(screen);
        if (selected == null) return;

        int x = guiLeft - 5;
        int y = guiTop + 20;

        int codexX = (codexScreen.width - codexScreen.getImageWidth()) / 2;
        int codexY = (codexScreen.height - codexScreen.getImageHeight()) / 2;

        drawContents(graphics, x, y, selected);

        if (codexScreen.showAdvancement && screen != null) {
            graphics.pose().translate(codexScreen.advancementX, codexScreen.advancementY);

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    codexScreen.BOOK_TEXTURE,
                    codexX - 10,
                    codexY + 15,
                    12,
                    211,
                    85,
                    12,
                    256,
                    256
            );

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    codexScreen.BOOK_TEXTURE,
                    codexX - 10,
                    codexY + 127,
                    12,
                    224,
                    85,
                    9,
                    256,
                    256
            );
        }

        graphics.pose().translate(x, y);

        int advX = X - 20;
        int advY = Y + 10;
        int advW = 99;
        int advH = 127;

        if (MouseUtil.isMouseOver(mouseX, mouseY, advX, advY, advW, advH)) {
            drawTooltips(graphics, mouseX - x, mouseY - y, x, y, selected);
        }


        //RenderSystem.enableBlend();
        //if (screenMixin.getTabs().size() > 1) {
        //    for (AdvancementTab advancementtab : screenMixin.getTabs().values()) {
        //        if (advancementtab.getPage() == AdvancementsScreenMixin.getTabPage())
        //            advancementtab.drawTab(graphics, x, y, advancementtab == selected);
        //    }
        //
        //    for (AdvancementTab advancementtab1 : screenMixin.getTabs().values()) {
        //        if (advancementtab1.getPage() == AdvancementsScreenMixin.getTabPage())
        //            advancementtab1.drawIcon(graphics, x, y);
        //    }
        //}
    }

    public static AdvancementTab getSelectedTab(AdvancementsScreen screen) {
        try {
            var field = AdvancementsScreen.class.getDeclaredField("selectedTab");
            field.setAccessible(true);
            return (AdvancementTab) field.get(screen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void drawTooltips(GuiGraphicsExtractor p_282892_, int mouseX, int mouseY, int p_282652_, int p_283595_, AdvancementTab selected) {
        int areaX = -13;
        int areaY = -20;
        int areaWidth = 172;
        int areaHeight = 112;

        boolean insideArea = mouseX >= areaX && mouseX <= areaX + areaWidth
                && mouseY >= areaY && mouseY <= areaY + areaHeight;

        if (!insideArea) {
            return;
        }

        p_282892_.pose().translate(30.0F, 18.0F);

        AdvancementTabMixin mixin = (AdvancementTabMixin) selected;

        int i = Mth.floor(mixin.getScrollX());
        int j = Mth.floor(mixin.getScrollY());
        float fade = mixin.getFade();

        Map<?, AdvancementWidget> widgets = mixin.getWidgets();

        p_282892_.fill(0, 0, 234, 113, Mth.floor(fade * 255.0F) << 24);

        for (AdvancementWidget widget : widgets.values()) {
            if (widget.isMouseOver(i, j, mouseX, mouseY)) {
                widget.extractHover(p_282892_, i, j, fade, p_282652_, p_283595_);
                break;
            }
        }
    }

    public static void drawContents(GuiGraphicsExtractor graphics, int x, int y, AdvancementTab tab) {
        AdvancementTabMixin mixin = (AdvancementTabMixin) tab;

        int viewWidth = 102;
        int viewHeight = 112;

        if (!mixin.isCentered()) {
            mixin.setScrollX(46 - (mixin.getMaxX() + mixin.getMinX()) / 2.0);
            mixin.setScrollY(56 - (mixin.getMaxY() + mixin.getMinY()) / 2.0);
            mixin.setCentered(true);
        }

        graphics.enableScissor(x + 14,  y, x + 122, y + 109);
        graphics.enableScissor(x, y, x + viewWidth + 30, y + viewHeight);
        graphics.pose().translate((float)x, (float)y);

        int scrollX = (int) Math.floor(mixin.getScrollX());
        int scrollY = (int) Math.floor(mixin.getScrollY());

        for (AdvancementWidget widget : mixin.getWidgets().values()) {
            widget.extractConnectivity(graphics, scrollX, scrollY, true);
        }
        for (AdvancementWidget widget : mixin.getWidgets().values()) {
            widget.extractConnectivity(graphics, scrollX, scrollY, false);
        }
        for (AdvancementWidget widget : mixin.getWidgets().values()) {
            widget.extractRenderState(graphics, scrollX, scrollY);
        }

        mixin.getRoot().extractConnectivity(graphics, scrollX, scrollY, true);
        mixin.getRoot().extractConnectivity(graphics, scrollX, scrollY, false);
        mixin.getRoot().extractRenderState(graphics, scrollX, scrollY);

        graphics.disableScissor();
        graphics.disableScissor();
    }
}
