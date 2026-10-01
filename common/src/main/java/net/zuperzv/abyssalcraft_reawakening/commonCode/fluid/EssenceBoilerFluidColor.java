package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public final class EssenceBoilerFluidColor {
    public static final int NO_TINT = 0xFFFFFFFF;

    private static final float POTION_TINT_BLEND = 0.5F;

    private EssenceBoilerFluidColor() {
    }

    public static int getTint(
            FluidModel fluidModel,
            EssenceBoilerFluid fluid,
            Level level,
            BlockPos pos
    ) {
        int fluidTint = NO_TINT;
        boolean hasFluidTint = false;

        if (fluidModel.tintSource() != null) {
            fluidTint = fluidModel.tintSource().colorInWorld(
                    fluid.fluid().defaultFluidState().createLegacyBlock(),
                    (BlockAndTintGetter) level,
                    pos
            );
            hasFluidTint = fluidTint != NO_TINT;
        }

        PotionContents potionContents = fluid.potionContents();
        boolean hasPotionTint = potionContents != null && potionContents != PotionContents.EMPTY;

        if (!hasPotionTint) {
            return hasFluidTint ? fluidTint : NO_TINT;
        }

        int potionTint = potionContents.getColor();
        if (!hasFluidTint) {
            return potionTint;
        }

        return interpolateColor(fluidTint, potionTint, POTION_TINT_BLEND);
    }

    private static int interpolateColor(int colorA, int colorB, float amount) {
        amount = Mth.clamp(amount, 0.0F, 1.0F);

        int red = Mth.lerpInt(amount, (colorA >> 16) & 0xFF, (colorB >> 16) & 0xFF);
        int green = Mth.lerpInt(amount, (colorA >> 8) & 0xFF, (colorB >> 8) & 0xFF);
        int blue = Mth.lerpInt(amount, colorA & 0xFF, colorB & 0xFF);

        return 0xFF000000 | (red << 16) | (green << 8) | blue;
    }
}
