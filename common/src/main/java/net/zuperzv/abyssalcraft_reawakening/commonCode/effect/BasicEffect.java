package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BasicEffect extends MobEffect {

    private final Identifier shader;

    public BasicEffect(MobEffectCategory category, int color) {
        this(category, color, null);
    }

    public BasicEffect(MobEffectCategory category, int color, Identifier shader) {
        super(category, color);
        this.shader = shader;
    }

    public Identifier getShader() {
        return shader;
    }

    public boolean hasShader() {
        return shader != null;
    }
}