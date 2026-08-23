package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.feature.MonsterRoomFeature;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.feature.MonsterRoomFeatureConfiguration;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public final class ModFeatures {
    private ModFeatures() {}

    public static void load() {
    }

    public static final RegistryHandle<MonsterRoomFeature> MONSTER_ROOM =
            Services.REGISTRY.registerFeature(
                    "monster_room",
                    new MonsterRoomFeature(
                            MonsterRoomFeatureConfiguration.CODEC
                    )
            );

}