package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;

import java.awt.*;
import java.util.function.Consumer;

public class CoraliumGemItem extends Item {

    public static final int MAX_GEMS = 9;

    public CoraliumGemItem(Properties properties) {
        super(properties);
    }

    public static int getGems(ItemStack stack) {
        CoraliumGemsData data =
                stack.get(ModDataComponentTypes.CORALIUM_GEMS.get());

        if (data == null) {
            return 1;
        }

        return Math.min(data.getCorealiumGems(), MAX_GEMS);
    }

    public static void setGems(ItemStack stack, int amount) {
        amount = Math.max(1, Math.min(amount, MAX_GEMS));

        stack.set(
                ModDataComponentTypes.CORALIUM_GEMS.get(),
                new CoraliumGemsData(amount)
        );
    }

    public static int addGems(ItemStack stack, int amount) {
        if (amount <= 0) {
            return 1;
        }

        int current = getGems(stack);
        int newAmount = Math.min(current + amount, MAX_GEMS);

        setGems(stack, newAmount);

        return newAmount - current;
    }

    public static int removeGems(ItemStack stack, int amount) {
        if (amount <= 0) {
            return 1;
        }

        int current = getGems(stack);
        int newAmount = Math.max(current - amount, 1);

        setGems(stack, newAmount);

        return current - newAmount;
    }

    private static void initializeGems(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.CORALIUM_GEMS.get())) {
            setGems(stack, 1);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);

        initializeGems(itemStack);

        int gems = getGems(itemStack);

        builder.accept(
                Component.translatable(
                        "tooltip.abyssalcraft_reawakening.gems",
                        gems,
                        MAX_GEMS
                ).withColor(Color.CYAN.getRGB())
                        .append(Component.literal(": "))
                        .append(Component.literal(gems + "/" + MAX_GEMS).withStyle(Style.EMPTY.withColor(Color.lightGray.getRGB())))
        );
    }

    @Override
    public Component getName(ItemStack itemStack) {
        initializeGems(itemStack);

        if (itemStack.getComponents().get(ModDataComponentTypes.CORALIUM_GEMS.get()).getCorealiumGems() > 1) {
            Component name = (Component) itemStack.getComponents()
                    .getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);

            return name.copy().append(
                    Component.literal(
                            " "
                    ).append(
                    Component.translatable(
                            "name.abyssalcraft_reawakening.cluster"
                    )
            ));
        }
        return super.getName(itemStack);
    }
}