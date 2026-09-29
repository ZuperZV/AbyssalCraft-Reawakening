package net.zuperzv.abyssalcraft_reawakening.services;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerPotionFluid;
import net.zuperzv.abyssalcraft_reawakening.services.types.IEssenceBoilerPlatformHooks;

public final class FabricEssenceBoilerPlatformHooks implements IEssenceBoilerPlatformHooks {

    @Override
    public boolean tryEmptyFluidContainer(
            EssenceBoilerBlockEntity boiler,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return false;
        }

        Storage<FluidVariant> storage = FluidStorage.ITEM.find(stack, null);
        if (storage == null) {
            return false;
        }

        int freeSpace = boiler.getFluidTankCapacity() - boiler.getFluidTankAmount();
        if (freeSpace <= 0) {
            return false;
        }

        for (StorageView<FluidVariant> view : storage) {
            if (view.isResourceBlank() || view.getAmount() <= 0) {
                continue;
            }

            FluidVariant variant = view.getResource();
            EssenceBoilerFluid candidate = new EssenceBoilerFluid(
                    variant.getFluid(),
                    freeSpace,
                    null
            );

            if (EssenceBoilerPotionFluid.isPotionFluid(candidate)) {
                continue;
            }

            if (!boiler.getFluidTank().isEmpty()
                    && !boiler.getFluidTank().isSame(candidate)) {
                continue;
            }

            long amountToMove = Math.min(view.getAmount(), freeSpace);

            try (Transaction transaction = Transaction.openOuter()) {
                long extracted = storage.extract(
                        variant,
                        amountToMove,
                        transaction
                );

                if (extracted <= 0) {
                    continue;
                }

                int accepted = boiler.fillFluidTank(
                        candidate.withAmount((int) extracted)
                );

                if (accepted != extracted) {
                    continue;
                }

                transaction.commit();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean tryFillFluidContainer(
            EssenceBoilerBlockEntity boiler,
            Player player,
            InteractionHand hand
    ) {
        EssenceBoilerFluid tankFluid = boiler.getFluidTank();
        if (tankFluid.isEmpty()
                || EssenceBoilerPotionFluid.isPotionFluid(tankFluid)) {
            return false;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return false;
        }

        Storage<FluidVariant> storage = FluidStorage.ITEM.find(stack, null);
        if (storage == null) {
            return false;
        }

        FluidVariant variant = FluidVariant.of(tankFluid.fluid());

        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = storage.insert(
                    variant,
                    tankFluid.amount(),
                    transaction
            );

            if (inserted <= 0) {
                return false;
            }

            boiler.drainFluidTank((int) inserted);
            transaction.commit();
            return true;
        }
    }
}
