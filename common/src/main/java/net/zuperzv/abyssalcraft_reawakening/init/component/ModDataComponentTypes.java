package net.zuperzv.abyssalcraft_reawakening.init.component;

import net.minecraft.core.component.DataComponentType;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    private ModDataComponentTypes() {}

    public static void load() {}

    public static final RegistryHandle<DataComponentType<CodexTierData>> CODEX_TIER =
            Services.REGISTRY.registerDataComponent(
                    "codex_tier",
                    b -> b.persistent(CodexTierData.CODEC)
            );
}