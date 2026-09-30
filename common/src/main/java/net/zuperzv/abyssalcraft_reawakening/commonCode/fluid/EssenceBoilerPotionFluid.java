package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class EssenceBoilerPotionFluid {

    @Nullable
    private static Supplier<? extends Fluid> potionFluidSupplier;

    private static int amountPerPotion;

    private EssenceBoilerPotionFluid() {
    }

    public static void configure(
            Supplier<? extends Fluid> fluidSupplier,
            int amountPerPotion
    ) {
        if (fluidSupplier == null) {
            throw new IllegalArgumentException(
                    "Potion fluid supplier must not be null"
            );
        }

        if (amountPerPotion <= 0) {
            throw new IllegalArgumentException(
                    "amountPerPotion must be > 0"
            );
        }

        EssenceBoilerPotionFluid.potionFluidSupplier = fluidSupplier;
        EssenceBoilerPotionFluid.amountPerPotion = amountPerPotion;
    }

    public static boolean isConfigured() {
        return potionFluidSupplier != null
                && amountPerPotion > 0;
    }

    public static Fluid fluid() {
        if (potionFluidSupplier == null) {
            throw new IllegalStateException(
                    "EssenceBoilerPotionFluid has not been configured"
            );
        }

        Fluid fluid = potionFluidSupplier.get();

        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new IllegalStateException(
                    "EssenceBoilerPotionFluid resolved to an empty fluid"
            );
        }

        return fluid;
    }

    public static boolean isPotionFluid(
            @Nullable EssenceBoilerFluid fluid
    ) {
        return fluid != null
                && !fluid.isEmpty()
                && isConfigured()
                && fluid.fluid() == fluid();
    }

    public static int amountPerPotion() {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "EssenceBoilerPotionFluid has not been configured"
            );
        }

        return amountPerPotion;
    }

    public static EssenceBoilerFluid fromPotion(
            PotionContents contents
    ) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "EssenceBoilerPotionFluid has not been configured"
            );
        }

        if (contents == null || contents == PotionContents.EMPTY) {
            return EssenceBoilerFluid.EMPTY;
        }

        return new EssenceBoilerFluid(
                fluid(),
                amountPerPotion,
                contents
        );
    }

    public static PotionContents getPotionContents(
            @Nullable EssenceBoilerFluid fluid
    ) {
        if (!isPotionFluid(fluid)) {
            return PotionContents.EMPTY;
        }

        return fluid.potionContents() == null
                ? PotionContents.EMPTY
                : fluid.potionContents();
    }
}