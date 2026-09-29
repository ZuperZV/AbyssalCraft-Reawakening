package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public final class EssenceBoilerPotionFluid {
    private static Fluid potionFluid = Fluids.EMPTY;
    private static int amountPerPotion;

    private EssenceBoilerPotionFluid() {
    }

    public static void configure(Fluid fluid, int amountPerPotion) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            throw new IllegalArgumentException("Potion fluid must be non-empty");
        }
        if (amountPerPotion <= 0) {
            throw new IllegalArgumentException("amountPerPotion must be > 0");
        }

        EssenceBoilerPotionFluid.potionFluid = fluid;
        EssenceBoilerPotionFluid.amountPerPotion = amountPerPotion;
    }

    public static boolean isConfigured() {
        return potionFluid != Fluids.EMPTY && amountPerPotion > 0;
    }

    public static Fluid fluid() {
        return potionFluid;
    }

    public static boolean isPotionFluid(@Nullable EssenceBoilerFluid fluid) {
        return fluid != null
                && !fluid.isEmpty()
                && isConfigured()
                && fluid.fluid() == potionFluid;
    }

    public static int amountPerPotion() {
        if (!isConfigured()) {
            throw new IllegalStateException("EssenceBoilerPotionFluid has not been configured");
        }
        return amountPerPotion;
    }

    public static EssenceBoilerFluid fromPotion(PotionContents contents) {
        if (!isConfigured()) {
            throw new IllegalStateException("Configure EssenceBoilerPotionFluid first");
        }
        if (contents == null || contents == PotionContents.EMPTY) {
            return EssenceBoilerFluid.EMPTY;
        }

        return new EssenceBoilerFluid(
                potionFluid,
                amountPerPotion,
                contents
        );
    }

    public static PotionContents getPotionContents(@Nullable EssenceBoilerFluid fluid) {
        if (!isPotionFluid(fluid)) {
            return PotionContents.EMPTY;
        }

        return fluid.potionContents() == null
                ? PotionContents.EMPTY
                : fluid.potionContents();
    }
}
