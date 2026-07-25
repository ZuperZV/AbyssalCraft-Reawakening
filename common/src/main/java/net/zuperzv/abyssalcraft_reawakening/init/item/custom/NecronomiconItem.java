package net.zuperzv.abyssalcraft_reawakening.init.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.component.EnergyEntry;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.data.EnergyType;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconMenu;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class NecronomiconItem extends Item {

    public NecronomiconItem(Properties properties) {
        super(properties);
    }

    /*
    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);

        System.out.println("Potential Energy: " + itemStack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get()).getPotentialEnergy());
    }
     */

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        initializeEnergy(stack);

        if (player.isShiftKeyDown()) {
            increaseEnergy(stack, 10);

            return level.isClientSide()
                    ? InteractionResult.SUCCESS
                    : InteractionResult.CONSUME;
        }

        if (!level.isClientSide()) {
            player.openMenu(this.getMenuProvider(stack));
        }

        return level.isClientSide()
                ? InteractionResult.SUCCESS
                : InteractionResult.CONSUME;
    }

    private MenuProvider getMenuProvider(ItemStack itemStack) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return itemStack.getHoverName();
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new NecronomiconMenu(i, inventory);
            }
        };
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        initializeEnergy(stack);

        PotentialEnergyData pe =
                stack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());

        System.out.println("Potential Energy getTooltipImage: " + pe.getPotentialEnergy());

        return Optional.of(
                new NecronomiconTooltipComponent(
                        pe,
                        stack
                )
        );
    }

    private void increaseEnergy(ItemStack stack, int amount) {
        PotentialEnergyData energyData =
                stack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());

        if (energyData == null) {
            energyData = PotentialEnergyData.createEmpty();
            stack.set(ModDataComponentTypes.POTENTIAL_ENERGY.get(), energyData);
        }

        int max = getMaxPotentialEnergy(stack);

        int newAmount = Math.min(
                energyData.getPotentialEnergy() + amount,
                max
        );

        energyData.setPotentialEnergy(newAmount);

        stack.set(
                ModDataComponentTypes.POTENTIAL_ENERGY.get(),
                energyData
        );
    }

    public static int getMaxPotentialEnergy(ItemStack stack) {
        var tier = stack.get(
                ModDataComponentTypes.CODEX_TIER.get()
        );

        if (tier == null) {
            return 500;
        }

        return getMaxPotentialEnergy(tier.getTier());
    }

    public static int getMaxPotentialEnergy(int tier) {
        return switch (tier) {
            case 1 -> 500;
            case 2 -> 10_000;
            case 3 -> 20_000;
            case 4 -> 40_000;
            case 5 -> 100_000;
            default -> 500;
        };
    }

    private void initializeEnergy(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.POTENTIAL_ENERGY.get())) {
            stack.set(
                    ModDataComponentTypes.POTENTIAL_ENERGY.get(),
                    new PotentialEnergyData(0)
            );
        }
    }
}