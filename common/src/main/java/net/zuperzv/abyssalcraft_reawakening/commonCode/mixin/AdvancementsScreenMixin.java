package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(AdvancementsScreen.class)
public interface AdvancementsScreenMixin {

    @Invoker("extractInside")
    void callExtractInside(GuiGraphicsExtractor guiGraphics, int xo, int yo);

    @Accessor("isScrolling")
    boolean getIsScrolling();

    @Mutable
    @Accessor("isScrolling")
    void setIsScrolling(boolean value);

    @Accessor("tabs")
    Map<AdvancementHolder, AdvancementTab> getTabs();
}