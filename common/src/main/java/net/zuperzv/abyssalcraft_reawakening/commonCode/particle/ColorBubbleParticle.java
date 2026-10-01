package net.zuperzv.abyssalcraft_reawakening.commonCode.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ColorBubbleParticle extends SimpleAnimatedParticle {
    private final float driftPhase;

    protected ColorBubbleParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xd,
            double yd,
            double zd,
            ColorBubbleData data,
            SpriteSet sprites,
            float driftPhase
    ) {
        super(level, x, y, z, sprites, -0.004F);

        this.rCol = data.red();
        this.gCol = data.green();
        this.bCol = data.blue();

        this.alpha = 1F;

        this.lifetime = 45;

        this.gravity = -0.0002F;
        this.driftPhase = driftPhase;

        this.scale(0.6F);

        this.setParticleSpeed(xd, yd, zd);
    }

    @Override
    public void tick() {
        super.tick();
        if (age >= lifetime) {
            return;
        }

        float lifeProgress = (float) age / lifetime;
        float fadeProgress = Mth.clamp((lifeProgress - 0.65F) / 0.35F, 0.0F, 1.0F);
        fadeProgress = fadeProgress * fadeProgress * (3.0F - 2.0F * fadeProgress);
        alpha = 1.0F - fadeProgress;

        float drift = (age + driftPhase) * 0.12F;
        xd += Mth.sin(drift) * 0.00002F;
        zd += Mth.cos(drift) * 0.00002F;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<ColorBubbleData> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(
                ColorBubbleData data,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xd,
                double yd,
                double zd,
                RandomSource random
        ) {
            return new ColorBubbleParticle(
                    level,
                    x,
                    y,
                    z,
                    xd,
                    yd,
                    zd,
                    data,
                    this.sprites,
                    random.nextFloat() * Mth.TWO_PI
            );
        }
    }
}