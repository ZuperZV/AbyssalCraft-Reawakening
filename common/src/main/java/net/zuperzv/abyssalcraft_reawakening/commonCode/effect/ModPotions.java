package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public final class ModPotions {
    private ModPotions() {
    }

    public static void load() {
    }

    public static final RegistryHandle<Potion> CRYSTALLIZATION = Services.REGISTRY.registerPotion(
            "crystallization",
            () -> new Potion(
                    "crystallization",
                    new MobEffectInstance(
                            BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.CRYSTALLIZATION.get()),
                            3600
                    )
            )
    );

    public static final RegistryHandle<Potion> LONG_CRYSTALLIZATION = Services.REGISTRY.registerPotion(
            "long_crystallization",
            () -> new Potion(
                    "long_crystallization",
                    new MobEffectInstance(
                            BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.CRYSTALLIZATION.get()),
                            9600
                    )
            )
    );
}
