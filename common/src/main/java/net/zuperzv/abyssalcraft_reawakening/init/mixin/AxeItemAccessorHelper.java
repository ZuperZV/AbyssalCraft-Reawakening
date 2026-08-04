package net.zuperzv.abyssalcraft_reawakening.init.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class AxeItemAccessorHelper {

    private static final AxeItemAccessor ACCESSOR =
            (AxeItemAccessor) (Object) AxeItem.class;

    public static Map<Block, Block> getStrippables() {
        return ACCESSOR.abyssalcraft$getStrippables();
    }

    public static void setStrippables(Map<Block, Block> map) {
        ACCESSOR.abyssalcraft$setStrippables(map);
    }
}