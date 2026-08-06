package net.zuperzv.abyssalcraft_reawakening.init.worldgen.placement;

import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ModPlacementModifierTypes {

    public static RegistryHandle<PlacementModifierType<NoFluidBelowPlacement>> NO_FLUID_BELOW;

    public static void load() {
        NO_FLUID_BELOW =
                Services.REGISTRY.registerPlacementModifierType(
                        "no_fluid_below",
                        NoFluidBelowPlacement.CODEC
                );
    }

    private ModPlacementModifierTypes() {}
}