package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class WitherwoodChestBoat extends ChestBoat {
    public WitherwoodChestBoat(
            EntityType<? extends ChestBoat> type,
            Level level,
            Supplier<Item> dropItem
    ) {
        super(type, level, dropItem);
    }
}