package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.placement;

import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ModPlacementModifierTypes {

    public static RegistryHandle<PlacementModifierType<NoFluidBelowPlacement>> NO_FLUID_BELOW;
    public static RegistryHandle<PlacementModifierType<WastiteClusterPlacement>> WASTITE_CLUSTER;

    public static void load() {
        NO_FLUID_BELOW =
                Services.REGISTRY.registerPlacementModifierType(
                        "no_fluid_below",
                        NoFluidBelowPlacement.CODEC
                );

        WASTITE_CLUSTER =
                Services.REGISTRY.registerPlacementModifierType(
                        "wastite_cluster",
                        WastiteClusterPlacement.CODEC
                );
    }

    private ModPlacementModifierTypes() {}
}