package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public record FluidDefinition(
        String fluidTypeName,

        int slopeFindDistance,
        int levelDecreasePerBlock,
        int tickRate,
        float explosionResistance,

        Supplier<? extends FlowingFluid> source,
        Supplier<? extends FlowingFluid> flowing,

        Supplier<? extends LiquidBlock> block,
        Supplier<? extends Item> bucket
) {
}