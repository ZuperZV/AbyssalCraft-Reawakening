package net.zuperzv.abyssalcraft_reawakening.commonCode.screen.Helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.CrystalProductBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.CrystalGrowthBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.BlockTooltipDataLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidTankAccess;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class BlockTooltipProviders {

    private BlockTooltipProviders() {
    }

    private static final Map<
            String,
            BlockTooltipProvider
            > PROVIDERS = new LinkedHashMap<>();

    public static void register(
            String id,
            BlockTooltipProvider provider
    ) {
        PROVIDERS.put(id, provider);
    }

    public static BlockTooltipProvider get(String id) {
        return PROVIDERS.get(id);
    }

    public static void registerDefaults() {

        register(
                "growth_inputs",
                (context, tooltip) -> {
                    if (context.blockEntity() instanceof CrystalGrowthBlockEntity growth) {
                        ItemStack stored = growth.getItem(CrystalGrowthBlockEntity.SLOT_ITEM);
                        if (isCrystalFragment(stored)) {
                            tooltip.add(
                                    Component.literal("Fragment: ")
                                            .append(stored.getHoverName())
                                            .withStyle(ChatFormatting.GRAY)
                            );

                            if (true) {
                                tooltip.add(
                                        Component.literal("Fragment: ")
                                                .append(stored.getHoverName())
                                                .withStyle(ChatFormatting.GRAY)
                                );
                            }
                        } else {
                            tooltip.add(
                                    Component.literal("Place a crystal fragment")
                                            .withStyle(ChatFormatting.LIGHT_PURPLE)
                            );
                        }
                    }
                }
        );

        register(
                "fluid_tank",
                (context, tooltip) -> {

                    if (!(context.blockEntity() instanceof IFluidTankAccess tank)) {
                        return;
                    }

                    EssenceBoilerFluid fluid = tank.getFluidTank();

                    if (!fluid.isEmpty()) {
                        if (fluid.hasPotionContents()) {
                            PotionContents potionContents = fluid.potionContents();
                            tooltip.add(
                                    Component.literal("Potion: ")
                                            .append(potionContents.getName("item.minecraft.potion.effect."))
                                            .withStyle(ChatFormatting.GRAY)
                            );
                        } else {
                            String fluidName = BuiltInRegistries.FLUID.getKey(fluid.fluid())
                                    .getPath()
                                    .replace('_', ' ');
                            fluidName = Character.toUpperCase(fluidName.charAt(0))
                                    + fluidName.substring(1);
                            tooltip.add(
                                    Component.literal(
                                            "Fluid: "
                                                    + fluidName
                                    ).withStyle(ChatFormatting.GRAY)
                            );
                        }

                        tooltip.add(
                                Component.literal(
                                        "Amount: "
                                                + fluid.amount()
                                                + " / "
                                                + tank.getFluidTankCapacity()
                                                + " mB"
                                ).withStyle(ChatFormatting.GRAY)
                        );
                    }
                }
        );

        register(
                "progress",
                (context, tooltip) -> {

                    int progress;
                    int maxProgress;
                    if (context.blockEntity() instanceof EssenceBoilerBlockEntity boiler) {
                        progress = boiler.progress;
                        maxProgress = boiler.maxProgress;
                    } else if (context.blockEntity() instanceof CrystalGrowthBlockEntity growth) {
                        progress = growth.progress;
                        maxProgress = growth.maxProgress;
                    } else {
                        return;
                    }
                    if (progress > 0) {
                        tooltip.add(
                                Component.literal(
                                        "Progress: "
                                                + formatSeconds(progress)
                                                + " s / "
                                                + formatSeconds(maxProgress)
                                                + " s"
                                ).withStyle(ChatFormatting.GRAY)
                        );
                    }
                }
        );
    }

    private static String formatSeconds(int ticks) {
        return String.format(Locale.ROOT, "%.1f", ticks / 20.0);
    }

    public static boolean isCrystalFragment(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof CrystalProductBlock product
                && "fragment".equals(product.getProduct());
    }

    public static void applyProviders(
            BlockTooltipDataLoader.BlockTooltipDefinition definition,
            BlockTooltipContext context
    ) {
        for (BlockTooltipDataLoader.TooltipProviderDefinition providerDefinition
                : definition.providers()) {

            BlockTooltipProvider provider =
                    PROVIDERS.get(providerDefinition.id());

            if (provider == null) {
                System.err.println(
                        "[BlockTooltip] Unknown provider: "
                                + providerDefinition.id()
                );
                continue;
            }

            provider.apply(
                    context,
                    context.tooltip()
            );
        }
    }

    @FunctionalInterface
    public interface BlockTooltipProvider {

        void apply(
                BlockTooltipContext context,
                List<Component> tooltip
        );
    }

    public record BlockTooltipContext(
            net.minecraft.world.level.Level level,
            net.minecraft.core.BlockPos pos,
            net.minecraft.world.level.block.state.BlockState state,
            net.minecraft.world.item.ItemStack stack,
            BlockEntity blockEntity,
            List<Component> tooltip
    ) {
    }
}