package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class CrystallizationDamageMixin {
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float abyssalcraft$reduceDamageWhileCrystallizing(float damage) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.CRYSTALLIZATION.get()))) {
            return damage * (2.0F / 3.0F);
        }
        return damage;
    }
}
