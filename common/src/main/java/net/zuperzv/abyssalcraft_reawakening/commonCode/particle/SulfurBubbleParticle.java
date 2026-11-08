package net.zuperzv.abyssalcraft_reawakening.commonCode.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

public class SulfurBubbleParticle extends SingleQuadParticle {
    private static final float SIZE_START = 0.15F;
    private static final float SIZE_END = 1.0F;
    private static final float UPWARDS_SPEED = 0.005F;
    private static final float HORIZONTAL_WIGGLING = 0.0015F;
    
    private final double yStart;
    private final double yEnd;
    private final float sizeStart;
    private double yPrev;
    private float spiralPhase;
    private final float spiralSpeed;

    public SulfurBubbleParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xa,
            double za,
            SpriteSet sprites,
            RandomSource random
    ) {
        super(level, x, y, z, sprites.get(random));
        this.gravity = -0.005F;
        this.friction = 1.85F;
        this.setSize(SIZE_START, SIZE_START);
        this.xd = xa * 0.025 + (this.random.nextFloat() * 2.0F - 1.0F) * 0.0025F;
        this.zd = za * 0.025 + (this.random.nextFloat() * 2.0F - 1.0F) * 0.0025F;
        this.sizeStart = SIZE_START + 0.1F * this.random.nextFloat();
        this.quadSize = this.sizeStart;
        this.lifetime = Integer.MAX_VALUE;
        this.yStart = this.yo;
        this.yEnd = this.yo + 4.0F - 1.0F;
        this.yPrev = y;
        this.spiralPhase = random.nextFloat() * Mth.TWO_PI;
        this.spiralSpeed = 0.0005F + random.nextFloat() * 0.0005F;
        
        // Sulfur farve - gul/orange med grønlig tone
        this.rCol = 0.8F + random.nextFloat() * 0.2F;
        this.gCol = 0.6F + random.nextFloat() * 0.3F;
        this.bCol = 0.1F + random.nextFloat() * 0.1F;
        this.alpha = 0.8F;
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).isSourceOfType(Fluids.WATER)) {
            this.remove();
        }

        if (!this.removed && this.y >= this.yEnd) {
            this.remove();
        }

        if (!this.removed && this.y <= this.yPrev) {
            this.remove();
        }

        // Twist: spiral bevægning i stedet for tilfældig wiggling
        this.spiralPhase += this.spiralSpeed;
        float spiralRadius = 0.0019F;
        this.xd += Mth.cos(this.spiralPhase) * spiralRadius;
        this.zd += Mth.sin(this.spiralPhase) * spiralRadius;
        
        // Tilføj lidt random wiggling for naturlig bevægelse
        this.xd += randomHorizontalWiggling();
        this.zd += randomHorizontalWiggling();
        
        this.move(this.xd, 0.0F, this.zd);
        
        float travelProgress = (float)((this.y - this.yStart) / (this.yEnd - this.yStart));
        this.quadSize = this.sizeStart + travelProgress * (SIZE_END - this.sizeStart);
        
        // Twist: farveforandring over tid - fra gul til orange/rød
        float colorShift = Mth.clamp(travelProgress * 1.5F, 0.0F, 1.0F);
        this.rCol = Mth.lerp(colorShift, 0.9F, 1.0F);
        this.gCol = Mth.lerp(colorShift, 0.7F, 0.3F);
        this.bCol = Mth.lerp(colorShift, 0.15F, 0.05F);
        
        // Fade ud nær toppen
        if (travelProgress > 0.7F) {
            this.alpha = Mth.lerp((travelProgress - 0.7F) / 0.3F, 0.8F, 0.0F);
        }
        
        this.yPrev = this.y;
    }

    private double randomHorizontalWiggling() {
        return (this.random.nextFloat() * HORIZONTAL_WIGGLING * (this.random.nextBoolean() ? 1 : -1)) * 0.5F;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            SulfurBubbleParticle particle = new SulfurBubbleParticle(level, x, y, z, xAux, yAux, this.sprite, random);
            if (particle == null || particle.removed) {
                return null;
            }
            return particle;
        }
    }
}
