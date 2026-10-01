package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluidTypes;
import net.zuperzv.abyssalcraft_reawakening.services.types.FluidDefinition;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidFactory;

public final class NeoForgeFluidFactory implements IFluidFactory {

    @Override
    public FlowingFluid createSource(FluidDefinition definition) {
        return new BaseFlowingFluid.Source(
                createProperties(definition)
        );
    }

    @Override
    public FlowingFluid createFlowing(FluidDefinition definition) {
        return new BaseFlowingFluid.Flowing(
                createProperties(definition)
        );
    }

    private BaseFlowingFluid.Properties createProperties(
            FluidDefinition definition
    ) {
        return new BaseFlowingFluid.Properties(
                () -> getFluidType(definition.fluidTypeName()),
                definition.source(),
                definition.flowing()
        )
                .slopeFindDistance(
                        definition.slopeFindDistance()
                )
                .levelDecreasePerBlock(
                        definition.levelDecreasePerBlock()
                )
                .tickRate(
                        definition.tickRate()
                )
                .explosionResistance(
                        definition.explosionResistance()
                )
                .block(
                        definition.block()
                )
                .bucket(
                        definition.bucket()
                );
    }

    private FluidType getFluidType(String name) {
        return switch (name) {
            case "noctilume" ->
                    createFluidType(ModFluidTypes.NOCTILUME);

            case "potion" ->
                    createFluidType(ModFluidTypes.POTION);

            case "sulfuric_arcanum" ->
                    createFluidType(ModFluidTypes.SULFURIC_ARCANUM);

            default ->
                    throw new IllegalArgumentException(
                            "Unknown fluid type: " + name
                    );
        };
    }

    private FluidType createFluidType(
            ModFluidTypes.FluidTypeDefinition definition
    ) {
        return new FluidType(
                FluidType.Properties.create()
                        .density(definition.density())
                        .temperature(definition.temperature())
                        .viscosity(definition.viscosity())
                        .fallDistanceModifier(
                                definition.fallDistanceModifier()
                        )
        );
    }
}