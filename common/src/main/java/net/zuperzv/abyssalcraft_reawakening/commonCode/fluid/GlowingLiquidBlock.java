package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;

public class GlowingLiquidBlock extends LiquidBlock {

    public GlowingLiquidBlock(
            FlowingFluid fluid,
            BlockBehaviour.Properties properties
    ) {
        super(fluid, properties);
    }
}