package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticlePositionAccessor {
    @Accessor("x")
    double abyssalcraft$getParticleX();

    @Accessor("y")
    double abyssalcraft$getParticleY();

    @Accessor("z")
    double abyssalcraft$getParticleZ();
}
