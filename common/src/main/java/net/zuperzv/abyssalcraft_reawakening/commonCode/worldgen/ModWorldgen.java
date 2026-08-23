package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public final class ModWorldgen {
    private ModWorldgen() {}
    public static void load() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> CORALIUM_TENDRILS = configuredFeatureKey("coralium_tendrils");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMINOUS_THISTLE = configuredFeatureKey("luminous_thistle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTELANDS_THORN = configuredFeatureKey("wastelands_thorn");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WITHERWOOD_TREE = configuredFeatureKey("witherwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAVES_WITHERWOOD_TREE = configuredFeatureKey("leaves_witherwood_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTITE_CLUSTER_BLOCK = configuredFeatureKey("wastite_cluster_block");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTITE_CLUSTER = configuredFeatureKey("wastite_cluster");

    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTITE_SPIKE_PATCH = configuredFeatureKey("wastite_spike_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WASTITE_SPIKE_COLUMN = configuredFeatureKey("wastite_spike_column");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_STONE_SPIKE_PATCH = configuredFeatureKey("abyssal_stone_spike_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_STONE_SPIKE_COLUMN = configuredFeatureKey("abyssal_stone_spike_column");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_STONE_FOREST_ROCK = configuredFeatureKey("abyssal_stone_forest_rock");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_MUD_DISK = configuredFeatureKey("abyssal_mud_disk");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_SAND_DISK = configuredFeatureKey("abyssal_sand_disk");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FUSED_ABYSSAL_SAND_DISK = configuredFeatureKey("fused_abyssal_sand_disk");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSALNITE_ORE = configuredFeatureKey("abyssalnite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORALIUM_ORE = configuredFeatureKey("coralium_ore");


    public static final ResourceKey<PlacedFeature> CORALIUM_TENDRILS_PLACED = placedFeatureKey("coralium_tendrils");
    public static final ResourceKey<PlacedFeature> LUMINOUS_THISTLE_PLACED = placedFeatureKey("luminous_thistle");
    public static final ResourceKey<PlacedFeature> WASTELANDS_THORN_PLACED = placedFeatureKey("wastelands_thorn");

    public static final ResourceKey<PlacedFeature> ABYSSAL_DRY_GRASS_PLACED = placedFeatureKey("abyssal_dry_grass_placed");
    public static final ResourceKey<PlacedFeature> ABYSSAL_DEAD_BUSH_PLACED = placedFeatureKey("abyssal_dead_bush_placed");

    public static final ResourceKey<PlacedFeature> WITHERWOOD_TREE_PLACED = placedFeatureKey("witherwood_tree");
    public static final ResourceKey<PlacedFeature> FOREST_WITHERWOOD_TREE_PLACED = placedFeatureKey("forest_witherwood_tree");

    public static final ResourceKey<PlacedFeature> WASTITE_CLUSTER_BLOCK_PLACED = placedFeatureKey("wastite_cluster_block");
    public static final ResourceKey<PlacedFeature> WASTITE_CLUSTER_PLACED = placedFeatureKey("wastite_cluster_column");

    public static final ResourceKey<PlacedFeature> WASTITE_SPIKE_COLUMN_PLACED = placedFeatureKey("wastite_spike_column");
    public static final ResourceKey<PlacedFeature> WASTITE_SPIKE_PLACED = placedFeatureKey("wastite_spike");

    public static final ResourceKey<PlacedFeature> ABYSSAL_STONE_SPIKE_COLUMN_PLACED = placedFeatureKey("abyssal_stone_spike_column");
    public static final ResourceKey<PlacedFeature> ABYSSAL_STONE_SPIKE_PLACED = placedFeatureKey("abyssal_stone_spike");

    public static final ResourceKey<PlacedFeature> ABYSSAL_STONE_FOREST_ROCK_PLACED = placedFeatureKey("abyssal_stone_forest_rock");

    public static final ResourceKey<PlacedFeature> ABYSSAL_MUD_DISK_PLACED = placedFeatureKey("abyssal_mud_disk");
    public static final ResourceKey<PlacedFeature> ABYSSAL_SAND_DISK_PLACED = placedFeatureKey("abyssal_sand_disk");
    public static final ResourceKey<PlacedFeature> FUSED_ABYSSAL_SAND_DISK_PLACED = placedFeatureKey("fused_abyssal_sand_disk");

    public static final ResourceKey<PlacedFeature> RARE_ABYSSAL_MUD_DISK_PLACED = placedFeatureKey("rare_abyssal_mud_disk");

    public static final ResourceKey<PlacedFeature> ABYSSALNITE_ORE_PLACED = placedFeatureKey("abyssalnite_ore");
    public static final ResourceKey<PlacedFeature> CORALIUM_ORE_PLACED = placedFeatureKey("coralium_ore");

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.id(name));
    }

    private static ResourceKey<PlacedFeature> placedFeatureKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Constants.id(name));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> fossil(String name) {
        return configuredFeatureKey(name + "_fossil");
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> fossilDisk(String name) {
        return configuredFeatureKey(name + "_disk");
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> fossilCleanup(String name){
        return configuredFeatureKey(name + "/disk_cleanup");
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> fossilMarker(String name){
        return configuredFeatureKey(name + "/marker");
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> fossilMarkerCleanup(String name){
        return configuredFeatureKey(name + "/marker_cleanup");
    }

    public static ResourceKey<PlacedFeature> fossilPlaced(String name) {
        return placedFeatureKey(name + "/fossil");
    }

    public static ResourceKey<PlacedFeature> fossilDiskPlaced(String name) {
        return placedFeatureKey(name + "/disk");
    }

    public static ResourceKey<PlacedFeature> fossilCleanupPlaced(String name) {
        return placedFeatureKey(name + "/disk_cleanup");
    }

    public static ResourceKey<PlacedFeature> fossilMarkerPlaced(String name) {
        return placedFeatureKey(name + "/marker");
    }

    public static ResourceKey<PlacedFeature> fossilMarkerCleanupPlaced(String name) {
        return placedFeatureKey(name + "/marker_cleanup");
    }

    //LOOC os si tahT
}