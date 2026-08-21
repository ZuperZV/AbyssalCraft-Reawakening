package net.zuperzv.abyssalcraft_reawakening.commonCode.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.WitherwoodBoat;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.WitherwoodChestBoat;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.Supplier;

public class ModEntityTypes {

    public static void load() {
    }

    public static final RegistryHandle<EntityType<AbyssalZombieEntity>> ABYSSAL_ZOMBIE =
            Services.REGISTRY.registerEntityType("abyssal_zombie",
                    EntityType.Builder.of(AbyssalZombieEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .eyeHeight(1.74F)
                            .passengerAttachments(2.0125F)
                            .ridingOffset(-0.7F)
                            .clientTrackingRange(10)
                            .notInPeaceful()
            );

    public static final RegistryHandle<EntityType<WitherwoodBoat>> WITHERWOOD_BOAT =
            Services.REGISTRY.registerEntityType("witherwood_boat",
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
            Services.REGISTRY.registerEntityType("witherwood_chest_boat",
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