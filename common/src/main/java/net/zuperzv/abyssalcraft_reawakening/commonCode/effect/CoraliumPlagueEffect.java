package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CoraliumPlagueEffect extends BasicEffect {

    public CoraliumPlagueEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public CoraliumPlagueEffect(MobEffectCategory category, int color, Identifier shader) {
        super(category, color, shader);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        DamageSource damageSource = level.damageSources().magic();

        entity.hurtServer(level, damageSource, 2.0F);

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        int interval = 40 >> amplifier;
        return interval > 0 && tickCount % interval == 0;
    }
}