package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerPotionFluid;
import net.zuperzv.abyssalcraft_reawakening.services.types.IEssenceBoilerPlatformHooks;

public final class NeoForgeEssenceBoilerPlatformHooks implements IEssenceBoilerPlatformHooks {

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

        ItemAccess access = ItemAccess.forPlayerInteraction(player, hand);
        ResourceHandler<FluidResource> handler =
                access.getCapability(Capabilities.Fluid.ITEM);

        if (handler == null || handler.size() <= 0) {
            return false;
        }

        int freeSpace = boiler.getFluidTankCapacity() - boiler.getFluidTankAmount();
        if (freeSpace <= 0) {
            return false;
        }

        FluidResource resource = handler.getResource(0);
        long available = handler.getAmountAsLong(0);

        if (available <= 0 || resource == null || resource.isEmpty()) {
            return false;
        }

        Fluid fluid = resource.typeHolder().value();
        if (fluid == null) {
            return false;
        }

        int amount = Math.min(
                freeSpace,
                (int) Math.min(available, Integer.MAX_VALUE)
        );

        EssenceBoilerFluid candidate = new EssenceBoilerFluid(
                fluid,
                amount,
                null
        );

        if (EssenceBoilerPotionFluid.isPotionFluid(candidate)) {
            return false;
        }

        if (!boiler.getFluidTank().isEmpty()
                && !boiler.getFluidTank().isSame(candidate.fluid())) {
            return false;
        }

        FluidResource extractionResource = FluidResource.of(fluid);

        try (Transaction transaction = Transaction.openRoot()) {
            long extracted = handler.extract(
                    extractionResource,
                    amount,
                    transaction
            );

            if (extracted <= 0) {
                return false;
            }

            int accepted = boiler.fillFluidTank(
                    candidate.withAmount((int) extracted)
            );

            if (accepted != extracted) {
                return false;
            }

            transaction.commit();
            return true;
        }
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

        ItemAccess access = ItemAccess.forPlayerInteraction(player, hand);
        ResourceHandler<FluidResource> handler =
                access.getCapability(Capabilities.Fluid.ITEM);

        if (handler == null || handler.size() <= 0) {
            return false;
        }

        FluidResource resource = FluidResource.of(tankFluid.fluid());

        try (Transaction transaction = Transaction.openRoot()) {
            long inserted = handler.insert(
                    resource,
                    tankFluid.amount(),
                    transaction
            );

            if (inserted <= 0) {
                return false;
            }

            boiler.drainFluidTank((int) Math.min(inserted, Integer.MAX_VALUE));
            transaction.commit();
            return true;
        }
    }
}
