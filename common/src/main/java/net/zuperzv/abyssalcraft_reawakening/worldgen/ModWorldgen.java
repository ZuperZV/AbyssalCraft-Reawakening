package net.zuperzv.abyssalcraft_reawakening.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class ModWorldgen {
    private ModWorldgen() {}
    public static void load() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_OVERWORLD_ORE = configuredFeatureKey("abyssalnite_overworld_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_NETHER_ORE = configuredFeatureKey("abyssalnite_nether_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_END_ORE = configuredFeatureKey("abyssalnite_end_ore");

    public static final ResourceKey<PlacedFeature> ABYSSALNITE_OVERWORLD_ORE_PLACED = placedFeatureKey("abyssalnite_overworld_ore");
    public static final ResourceKey<PlacedFeature> ABYSSALNITE_NETHER_ORE_PLACED = placedFeatureKey("abyssalnite_nether_ore");
    public static final ResourceKey<PlacedFeature> ABYSSALNITE_END_ORE_PLACED = placedFeatureKey("abyssalnite_end_ore");

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.id(name));
    }

    private static ResourceKey<PlacedFeature> placedFeatureKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Constants.id(name));
    }
}