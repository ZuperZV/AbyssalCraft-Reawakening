package net.zuperzv.abyssalcraft_reawakening.init.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class ModWorldgen {
    private ModWorldgen() {}
    public static void load() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL = configuredFeatureKey("abyssal_fossil");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_DISK = configuredFeatureKey("fossil_disk");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_DISK_REMOVE = configuredFeatureKey("fossil_disk_remove");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_DISK_AIR = configuredFeatureKey("fossil_disk_air");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_MARKER = configuredFeatureKey("fossil_marker");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_MARKER_REMOVE = configuredFeatureKey("fossil_marker_remove");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CORALIUM_TENDRILS = configuredFeatureKey("coralium_tendrils");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WITHERWOOD_TREE = configuredFeatureKey("witherwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAPLING_WITHERWOOD_TREE = configuredFeatureKey("sapling_witherwood_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTITE_SPIKE_PATCH = configuredFeatureKey("wastite_spike_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTITE_SPIKE_COLUMN = configuredFeatureKey("wastite_spike_column");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_MUD_DISK = configuredFeatureKey("abyssal_mud_disk");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_ORE = configuredFeatureKey("abyssalnite_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_OVERWORLD_ORE = configuredFeatureKey("abyssalnite_overworld_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_NETHER_ORE = configuredFeatureKey("abyssalnite_nether_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_END_ORE = configuredFeatureKey("abyssalnite_end_ore");



    public static final ResourceKey<PlacedFeature> FOSSIL_PLACED = placedFeatureKey("fossil");
    public static final ResourceKey<PlacedFeature> FOSSIL_DISK_PLACED = placedFeatureKey("fossil_disk");
    public static final ResourceKey<PlacedFeature> FOSSIL_DISK_REMOVE_PLACED = placedFeatureKey("fossil_disk_remove");
    public static final ResourceKey<PlacedFeature> FOSSIL_DISK_AIR_PLACED = placedFeatureKey("fossil_disk_air");
    public static final ResourceKey<PlacedFeature> FOSSIL_MARKER_PLACED = placedFeatureKey("fossil_marker");
    public static final ResourceKey<PlacedFeature> FOSSIL_MARKER_REMOVE_PLACED = placedFeatureKey("fossil_marker_remove");

    public static final ResourceKey<PlacedFeature> CORALIUM_TENDRILS_PLACED = placedFeatureKey("coralium_tendrils");

    public static final ResourceKey<PlacedFeature> WITHERWOOD_TREE_PLACED = placedFeatureKey("witherwood_tree");

    public static final ResourceKey<PlacedFeature> WASTITE_SPIKE_COLUMN_PLACED = placedFeatureKey("wastite_spike_column");
    public static final ResourceKey<PlacedFeature> WASTITE_SPIKE_PLACED = placedFeatureKey("wastite_spike");

    public static final ResourceKey<PlacedFeature> ABYSSAL_MUD_DISK_PLACED = placedFeatureKey("abyssal_mud_disk");
    public static final ResourceKey<PlacedFeature> ABYSSALNITE_ORE_PLACED = placedFeatureKey("abyssalnite_ore");

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