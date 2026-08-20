package net.zuperzv.abyssalcraft_reawakening.commonCode.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoods {

    public static final FoodProperties GHOUL_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable GHOUL_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.HUNGER, 600, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NAUSEA, 600)))
                    .build();

    // Anti Ghoul Flesh
    public static final FoodProperties ANTI_GHOUL_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable ANTI_GHOUL_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.SATURATION, 600, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.REGENERATION, 600)))
                    .build();

    // Abyssal Ghoul Flesh
    public static final FoodProperties ABYSSAL_GHOUL_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable ABYSSAL_GHOUL_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.HUNGER, 600, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NAUSEA, 600)))
                    // Tilføj Coralium Plague her når effekten findes.
                    //.onConsume(new ApplyStatusEffectsConsumeEffect(
                    //        new MobEffectInstance(ModEffects.CORALIUM_PLAGUE, 600)))
                    .build();

    // Coralium Plagued Flesh
    public static final FoodProperties CORALIUM_PLAGUED_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable CORALIUM_PLAGUED_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.HUNGER, 300, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NAUSEA, 300)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.INFESTED, 300)))
                    // Tilføj Coralium Plague her når effekten findes.
                    //.onConsume(new ApplyStatusEffectsConsumeEffect(
                    //        new MobEffectInstance(ModEffects.CORALIUM_PLAGUE, 300)))
                    .build();

    // Dreaded Ghoul Flesh
    public static final FoodProperties DREADED_GHOUL_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable DREADED_GHOUL_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.HUNGER, 600, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NAUSEA, 600)))
                    // Tilføj Dread Plague her.
                    //.onConsume(new ApplyStatusEffectsConsumeEffect(
                    //        new MobEffectInstance(ModEffects.DREAD_PLAGUE, 600)))
                    .build();

    // Omothol Ghoul Flesh (standard version)
    public static final FoodProperties OMOTHOL_GHOUL_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable OMOTHOL_GHOUL_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.WEAKNESS, 100)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.HUNGER, 400, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NAUSEA, 300)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.BLINDNESS, 40)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NIGHT_VISION, 40)))
                    .build();

    // Shadow Ghoul Flesh
    public static final FoodProperties SHADOW_GHOUL_FLESH =
            new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationModifier(0.3F)
                    .build();

    public static final Consumable SHADOW_GHOUL_FLESH_CONSUMABLE =
            Consumables.defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.HUNGER, 600, 1)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NAUSEA, 300)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.DARKNESS, 600)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.BLINDNESS, 600)))
                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.NIGHT_VISION, 600)))
                    .build();
}