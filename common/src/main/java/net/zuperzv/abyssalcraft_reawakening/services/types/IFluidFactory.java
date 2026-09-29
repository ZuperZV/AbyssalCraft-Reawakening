package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.world.level.material.FlowingFluid;

public interface IFluidFactory {

    FlowingFluid createSource(FluidDefinition definition);

    FlowingFluid createFlowing(FluidDefinition definition);
}