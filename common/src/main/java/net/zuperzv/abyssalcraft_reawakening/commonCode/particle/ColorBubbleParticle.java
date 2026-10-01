package net.zuperzv.abyssalcraft_reawakening.commonCode.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;

public class ColorBubbleParticle extends SimpleAnimatedParticle {

    protected ColorBubbleParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xd,
            double yd,
            double zd,
            ColorBubbleData data,
            SpriteSet sprites
    ) {
        super(level, x, y, z, sprites, -0.004F);

        this.rCol = data.red();
        this.gCol = data.green();
        this.bCol = data.blue();

        this.alpha = 1F;

        this.lifetime = 20;

        this.gravity = -0.004F;

        this.scale(0.6F);

        this.setParticleSpeed(xd, yd, zd);
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
                    this.sprites
            );
        }
    }
}