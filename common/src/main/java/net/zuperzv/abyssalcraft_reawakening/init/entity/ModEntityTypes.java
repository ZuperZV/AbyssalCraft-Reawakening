package net.zuperzv.abyssalcraft_reawakening.init.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.init.entity.custom.WitherwoodBoat;
import net.zuperzv.abyssalcraft_reawakening.init.entity.custom.WitherwoodChestBoat;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.Supplier;

public class ModEntityTypes {

    public static void load() {
    }

    public static final RegistryHandle<EntityType<WitherwoodBoat>> WITHERWOOD_BOAT =
            Services.REGISTRY.registerEntityType(
                    "witherwood_boat",
                    EntityType.Builder.of(
                                    boatFactory(() -> ModItems.WITHERWOOD_BOAT.get()),
                                    MobCategory.MISC
                            )
                            .noLootTable()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10)
            );

    public static final RegistryHandle<EntityType<WitherwoodChestBoat>> WITHERWOOD_CHEST_BOAT =
            Services.REGISTRY.registerEntityType(
                    "witherwood_chest_boat",
                    EntityType.Builder.of(
                                    chestBoatFactory(() -> ModItems.WITHERWOOD_CHEST_BOAT.get()),
                                    MobCategory.MISC
                            )
                            .noLootTable()
                            .sized(1.375F, 0.5625F)
                            .eyeHeight(0.5625F)
                            .clientTrackingRange(10)
            );

    private static EntityType.EntityFactory<WitherwoodBoat> boatFactory(
            Supplier<Item> boatItem
    ) {
        return (entityType, level) ->
                new WitherwoodBoat(entityType, level, boatItem);
    }

    private static EntityType.EntityFactory<WitherwoodChestBoat> chestBoatFactory(
            Supplier<Item> dropItem
    ) {
        return (entityType, level) ->
                new WitherwoodChestBoat(entityType, level, dropItem);
    }
}