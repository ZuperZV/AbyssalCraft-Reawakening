package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public interface ISpawnPlacementHelper {

    <T extends Mob> void registerSpawnPlacement(
            RegistryHandle<EntityType<T>> entityType,
            SpawnPlacementType spawnPlacementType,
            Heightmap.Types heightmap,
            SpawnPlacements.SpawnPredicate<T> predicate
    );

    void applySpawnPlacements(SpawnPlacementsRegistrar registrar);

    interface SpawnPlacementsRegistrar {

        <T extends Mob> void register(
                EntityType<T> entityType,
                SpawnPlacementType spawnPlacementType,
                Heightmap.Types heightmap,
                SpawnPlacements.SpawnPredicate<T> predicate
        );
    }
}