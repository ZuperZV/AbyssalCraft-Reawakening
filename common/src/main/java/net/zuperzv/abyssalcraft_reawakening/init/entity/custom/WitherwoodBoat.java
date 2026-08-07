package net.zuperzv.abyssalcraft_reawakening.init.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;

import java.util.function.Supplier;

public class WitherwoodBoat extends Boat {
    public WitherwoodBoat(
            EntityType<? extends Boat> type,
            Level level,
            Supplier<Item> dropItem
    ) {
        super(type, level, dropItem);
    }
}