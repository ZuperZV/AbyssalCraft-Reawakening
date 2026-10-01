package net.zuperzv.abyssalcraft_reawakening.commonCode.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.BlockTooltipDataLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.screen.Helpers.BlockTooltipProviders;

import java.util.ArrayList;
import java.util.List;

public final class BlockTooltipScreen {

    private static final int MAX_TEXT_WIDTH = 200;
    private static final int LINE_HEIGHT = 10;
    private static final int TITLE_SPACING = 2;

    private BlockTooltipScreen() {
    }

    public static void renderOverlay(GuiGraphicsExtractor graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null
                || !(minecraft.hitResult instanceof BlockHitResult blockHit)
                || minecraft.hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }

        var pos = blockHit.getBlockPos();
        BlockState state = minecraft.level.getBlockState(pos);
        BlockTooltipDataLoader.BlockTooltipDefinition definition =
                BlockTooltipDataLoader.get(state.getBlock());
        if (definition == null) {
            return;
        }

        ItemStack stack = state.getCloneItemStack(minecraft.level, pos, true);
        if (stack.isEmpty()) {
            stack = new ItemStack(state.getBlock());
        }

        List<Component> tooltip = new ArrayList<>(
                stack.getTooltipLines(
                        Item.TooltipContext.of(minecraft.level),
                        minecraft.player,
                        minecraft.options.advancedItemTooltips
                                ? net.minecraft.world.item.TooltipFlag.Default.ADVANCED
                                : net.minecraft.world.item.TooltipFlag.Default.NORMAL
                )
        );
        Component blockName = Component.translatable(
                "item."
                        + definition.block().getNamespace()
                        + "."
                        + definition.block().getPath()
        );
        if (tooltip.isEmpty()) {
            tooltip.add(blockName);
        } else {
            tooltip.set(0, blockName);
        }
        int baseTooltipLineCount = tooltip.size();

        BlockEntity blockEntity = minecraft.level.getBlockEntity(pos);
        BlockTooltipProviders.applyProviders(
                definition,
                new BlockTooltipProviders.BlockTooltipContext(
                        minecraft.level,
                        pos,
                        state,
                        stack,
                        blockEntity,
                        tooltip
                )
        );
        if (tooltip.size() <= baseTooltipLineCount) {
            return;
        }

        int maxTextWidth = Math.max(1, Math.min(MAX_TEXT_WIDTH, graphics.guiWidth() - 24));
        List<FormattedCharSequence> lines = new ArrayList<>();
        for (Component line : tooltip) {
            lines.addAll(minecraft.font.split(line, maxTextWidth));
        }
        if (lines.isEmpty()) {
            return;
        }

        int textWidth = lines.stream()
                .mapToInt(minecraft.font::width)
                .max()
                .orElse(0);
        int textHeight = lines.size() * LINE_HEIGHT
                + (lines.size() > 1 ? TITLE_SPACING : -2);
        int anchorX = graphics.guiWidth() / 2;
        int anchorY = graphics.guiHeight() / 2;
        var position = DefaultTooltipPositioner.INSTANCE.positionTooltip(
                graphics.guiWidth(),
                graphics.guiHeight(),
                anchorX,
                anchorY,
                textWidth,
                textHeight
        );

        graphics.nextStratum();
        TooltipRenderUtil.extractTooltipBackground(
                graphics,
                position.x(),
                position.y(),
                textWidth,
                textHeight,
                null
        );

        int y = position.y();
        for (int i = 0; i < lines.size(); i++) {
            graphics.text(minecraft.font, lines.get(i), position.x(), y, 0xFFFFFFFF);
            y += LINE_HEIGHT;
            if (i == 0 && lines.size() > 1) {
                y += TITLE_SPACING;
            }
        }
    }
}
