package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class ModFluidTags {
    private ModFluidTags() {
    }

    public static final TagKey<Fluid> POTION = create("potion");
    public static final TagKey<Fluid> SULFURIC_ARCANUM = create("sulfuric_arcanum");
    public static final TagKey<Fluid> CORALIUM = create("coralium");
    public static final TagKey<Fluid> WATER_LIKE = create("water_like");

    private static TagKey<Fluid> create(String name) {
        return TagKey.create(Registries.FLUID, Constants.id(name));
    }
}
