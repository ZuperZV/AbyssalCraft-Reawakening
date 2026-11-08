package net.zuperzv.abyssalcraft_reawakening.commonCode.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

public class NoxiousGasParticle extends BaseAshSmokeParticle {
    private final float fadeOutStartingPoint;

    public NoxiousGasParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xa, ya, za, scale, sprites, 0.3F, 5, -0.02F, true);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.lifetime = (int)((double)6.0F / ((double)this.random.nextFloat() * (double)0.5F + (double)0.5F) * (double)scale);
        this.fadeOutStartingPoint = (float)this.lifetime / 2.0F;
    }

    public void tick() {
        super.tick();
        if ((float)this.age > this.fadeOutStartingPoint) {
            float framesSinceFadeOutStart = (float)this.age - this.fadeOutStartingPoint;
            this.setAlpha(((float)this.lifetime - framesSinceFadeOutStart) / (float)this.lifetime);
        }

    }

    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new NoxiousGasParticle(level, x, y, z, xAux, yAux, zAux, 3.0F, this.sprites);
        }
    }
}
