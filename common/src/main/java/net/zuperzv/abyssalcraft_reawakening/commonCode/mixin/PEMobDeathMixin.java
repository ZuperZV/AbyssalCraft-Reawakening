package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PEMachineBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PEMobDeathMixin {
    @Inject(method = "die", at = @At("HEAD"))
    private void abyssalcraft$collectSacrificialEnergy(DamageSource damageSource, CallbackInfo callback) {
        LivingEntity mob = (LivingEntity) (Object) this;
        if (mob instanceof Player) return;
        Level level = mob.level();
        if (level instanceof ServerLevel serverLevel) {
            PEMachineBlockEntity.onMobDeath(serverLevel, mob);
        }
    }
}
