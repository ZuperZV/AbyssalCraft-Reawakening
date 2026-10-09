package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class CrystallizationEffect extends MobEffect {
    public CrystallizationEffect() {
        super(MobEffectCategory.HARMFUL, 0xA9E7E8);
        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                Constants.id("crystallization_movement_slowdown"),
                -0.4D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        addAttributeModifier(
                Attributes.JUMP_STRENGTH,
                Constants.id("crystallization_jump_reduction"),
                -0.4D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
