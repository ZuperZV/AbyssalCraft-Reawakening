package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.zuperzv.abyssalcraft_reawakening.services.types.ISpawnPlacementHelper;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeSpawnPlacementHelper implements ISpawnPlacementHelper {

    private final List<SpawnPlacementEntry<?>> spawnPlacements = new ArrayList<>();

    @Override
    public <T extends Mob> void registerSpawnPlacement(
            RegistryHandle<EntityType<T>> entityType,
            SpawnPlacementType spawnPlacementType,
            Heightmap.Types heightmap,
            SpawnPlacements.SpawnPredicate<T> predicate
    ) {
        this.spawnPlacements.add(
                new SpawnPlacementEntry<>(
                        entityType,
                        spawnPlacementType,
                        heightmap,
                        predicate
                )
        );
    }

    @Override
    public void applySpawnPlacements(SpawnPlacementsRegistrar registrar) {
        for (SpawnPlacementEntry<?> spawnPlacement : this.spawnPlacements) {
            spawnPlacement.register(registrar);
        }
    }

    private record SpawnPlacementEntry<T extends Mob>(
            RegistryHandle<EntityType<T>> entityType,
            SpawnPlacementType spawnPlacementType,
            Heightmap.Types heightmap,
            SpawnPlacements.SpawnPredicate<T> spawnPredicate
    ) {

        private void register(SpawnPlacementsRegistrar registrar) {
            registrar.register(
                    this.entityType.get(),
                    this.spawnPlacementType,
                    this.heightmap,
                    this.spawnPredicate
            );
        }
    }
}