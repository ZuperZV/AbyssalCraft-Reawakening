package net.zuperzv.abyssalcraft_reawakening.commonCode.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModDataComponentTypes {
    private ModDataComponentTypes() {}

    public static void load() {}

    public static final RegistryHandle<DataComponentType<CodexTierData>> CODEX_TIER =
            Services.REGISTRY.registerDataComponent(
                    "codex_tier",
                    b -> b.persistent(CodexTierData.CODEC)
            );

    public static final RegistryHandle<DataComponentType<PotentialEnergyData>> POTENTIAL_ENERGY =
            Services.REGISTRY.registerDataComponent(
                    "potential_energy",
                    b -> b.persistent(PotentialEnergyData.CODEC)
            );

    public static final RegistryHandle<DataComponentType<EnergyData>> ENERGY =
            Services.REGISTRY.registerDataComponent(
                    "energy",
                    b -> b.persistent(EnergyData.CODEC)
            );

    public static final RegistryHandle<DataComponentType<StaffTargetData>> STAFF_TARGET =
            Services.REGISTRY.registerDataComponent(
                    "staff_target",
                    b -> b.persistent(StaffTargetData.CODEC)
            );

    public static final RegistryHandle<DataComponentType<Boolean>> GRAYSCALE =
            Services.REGISTRY.registerDataComponent(
                    "grayscale",
                    b -> b.persistent(Codec.BOOL)
            );
}