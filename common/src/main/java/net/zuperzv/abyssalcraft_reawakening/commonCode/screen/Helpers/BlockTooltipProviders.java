package net.zuperzv.abyssalcraft_reawakening.commonCode.screen.Helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.world.SimpleContainer;
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
                                    Component.translatable("tooltip.abyssalcraft_reawakening.fragment", stored.getHoverName())
                                            .withStyle(ChatFormatting.GRAY)
                            );
                            addNextFluidRequirement(growth, tooltip);
                        } else {
                            tooltip.add(
                                    Component.translatable("tooltip.abyssalcraft_reawakening.place_crystal_fragment")
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
                                    Component.translatable("tooltip.abyssalcraft_reawakening.potion", potionContents.getName("item.minecraft.potion.effect."))
                                            .withStyle(ChatFormatting.GRAY)
                            );
                        } else {
                            Component fluidName = getFluidName(fluid.fluid());
                            tooltip.add(
                                    Component.translatable("tooltip.abyssalcraft_reawakening.fluid", fluidName)
                                            .withStyle(ChatFormatting.GRAY)
                            );
                        }

                        tooltip.add(
                                Component.translatable("tooltip.abyssalcraft_reawakening.amount", fluid.amount(), tank.getFluidTankCapacity())
                                        .withStyle(ChatFormatting.GRAY)
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
                                Component.translatable("tooltip.abyssalcraft_reawakening.progress", formatSeconds(progress), formatSeconds(maxProgress))
                                        .withStyle(ChatFormatting.GRAY)
                        );
                    }
                }
        );
    }

    private static void addNextFluidRequirement(
            CrystalGrowthBlockEntity growth,
            List<Component> tooltip
    ) {
        EssenceBoilerFluid required = growth.getNextRequiredFluid();
        if (required.isEmpty()) {
            return;
        }
        int minimumAmount = required.amount();
        EssenceBoilerFluid tank = growth.getFluidTank();
        if (tank.isSame(required) && tank.amount() >= minimumAmount) {
            return;
        }

        Component fluidName = required.hasPotionContents()
                ? Component.translatable(
                        "tooltip.abyssalcraft_reawakening.potion",
                        required.potionContents().getName("item.minecraft.potion.effect."))
                : getFluidName(required.fluid());
        var requirement = minimumAmount <= 1
                ? Component.translatable("tooltip.abyssalcraft_reawakening.growth_requires_fluid_any_amount", fluidName)
                : Component.translatable(
                        "tooltip.abyssalcraft_reawakening.growth_requires_fluid",
                        minimumAmount,
                        fluidName);
        tooltip.add(requirement.withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    private static Component getFluidName(net.minecraft.world.level.material.Fluid fluid) {
        String blockKey = BuiltInRegistries.FLUID.getKey(fluid).toLanguageKey("block");
        if (Language.getInstance().has(blockKey)) {
            return Component.translatable(blockKey);
        }

        String fluidPath = BuiltInRegistries.FLUID.getKey(fluid).getPath();
        if (fluidPath.startsWith("source_")) {
            fluidPath = fluidPath.substring("source_".length());
        }
        String path = fluidPath.replace('_', ' ');
        StringBuilder name = new StringBuilder();
        for (String word : path.split(" ")) {
            if (!word.isEmpty()) {
                if (!name.isEmpty()) name.append(' ');
                name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
        }
        return Component.literal(name.toString());
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
