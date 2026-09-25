package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemRegistry;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModEffects {
    private ModEffects() {}

    public static void load() {}

    public static final RegistryHandle<CoraliumPlagueEffect> CORALIUM_PLAGUE =
            Services.REGISTRY.registerMobEffect(
                    "coralium_plague",
                    () -> new CoraliumPlagueEffect(
                            MobEffectCategory.HARMFUL,
                            0x386231,
                            Constants.id("coralium_plague")
                    )
            );

    public static final RegistryHandle<BasicEffect> OMNIVISION =
            Services.REGISTRY.registerMobEffect(
                    "omnivision",
                    () -> new BasicEffect(
                            MobEffectCategory.BENEFICIAL,
                            0x557672
                    )
            );
}

