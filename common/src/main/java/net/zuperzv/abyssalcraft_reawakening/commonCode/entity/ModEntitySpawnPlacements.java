package net.zuperzv.abyssalcraft_reawakening.commonCode.entity;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.GroundlingEntity;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

public class ModEntitySpawnPlacements {

    private ModEntitySpawnPlacements() {}

    public static void load() {
        Services.SPAWN_PLACEMENTS.registerSpawnPlacement(
                ModEntityTypes.ABYSSAL_ZOMBIE,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AbyssalZombieEntity::canSpawn
        );

        Services.SPAWN_PLACEMENTS.registerSpawnPlacement(
                ModEntityTypes.GROUNDLING,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GroundlingEntity::canSpawn
        );
    }
}