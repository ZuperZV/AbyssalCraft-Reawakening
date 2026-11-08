package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class CoraliumPlagueHelper {
    private CoraliumPlagueHelper() {}

    public static void applyTo(Entity entity, Level level) {
        if (!level.isClientSide() && entity instanceof LivingEntity living) {
            Holder<MobEffect> plague = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.CORALIUM_PLAGUE.get());
            if (!living.hasEffect(plague)) {
                living.addEffect(new MobEffectInstance(plague, 200), null);
            }
        }
    }
}
