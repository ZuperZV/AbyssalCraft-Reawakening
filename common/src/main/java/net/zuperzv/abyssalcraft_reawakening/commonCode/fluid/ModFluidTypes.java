package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import org.joml.Vector3f;

public final class ModFluidTypes {

    private ModFluidTypes() {
    }

    public static final BaseFluidType NOCTILUME_FLUID_TYPE =
            new BaseFluidType(
                    108f / 255f,
                    168f / 255f,
                    212f / 255f
            );

    public static final BaseFluidType POTION_FLUID_TYPE =
            new BaseFluidType(
                    108f / 255f,
                    168f / 255f,
                    212f / 255f
            );

    public static final FluidTypeDefinition NOCTILUME =
            new FluidTypeDefinition(
                    "noctilume",
                    600,
                    1500,
                    3000,
                    0.5F,
                    NOCTILUME_FLUID_TYPE.fogColor()
            );

    public static final FluidTypeDefinition POTION =
            new FluidTypeDefinition(
                    "potion",
                    15,
                    1000,
                    1000,
                    1.0F,
                    POTION_FLUID_TYPE.fogColor()
            );

    public static final BaseFluidType SULFURIC_ARCANUM_FLUID_TYPE =
            new BaseFluidType(
                    207f / 255f,
                    193f / 255f,
                    128f / 255f
            );

    public static final FluidTypeDefinition SULFURIC_ARCANUM =
            new FluidTypeDefinition(
                    "sulfuric_arcanum",
                    350,
                    1200,
                    2500,
                    1.0F,
                    SULFURIC_ARCANUM_FLUID_TYPE.fogColor()
            );

    public record FluidTypeDefinition(
            String name,
            int temperature,
            int density,
            int viscosity,
            float fallDistanceModifier,
            Vector3f fogColor
    ) {
        public FluidTypeDefinition {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Fluid type name must not be empty");
            }

            if (temperature < 0) {
                throw new IllegalArgumentException("Temperature must be >= 0");
            }

            if (viscosity < 0) {
                throw new IllegalArgumentException("Viscosity must be >= 0");
            }

            if (fallDistanceModifier < 0.0F) {
                throw new IllegalArgumentException(
                        "Fall distance modifier must be >= 0"
                );
            }

            if (fogColor == null) {
                throw new IllegalArgumentException("Fog color must not be null");
            }

            fogColor = new Vector3f(fogColor);
        }

        @Override
        public Vector3f fogColor() {
            return new Vector3f(fogColor);
        }
    }
}