package net.zuperzv.abyssalcraft_reawakening.commonCode.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModParticleTypes {
    private ModParticleTypes() {}
    public static void load() {}

    public static final RegistryHandle<ParticleType<ColorBubbleData>> COLOR_BUBBLE =
            Services.REGISTRY.registerParticleType("color_bubble", ColorBubbleType::new);

    public static final RegistryHandle<SimpleParticleType> CROASIUS_GAS =
            Services.REGISTRY.registerSimpleParticleType("croasius_gas", true);
}