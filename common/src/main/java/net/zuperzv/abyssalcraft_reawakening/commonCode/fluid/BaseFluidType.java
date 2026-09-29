package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import org.joml.Vector3f;

public final class BaseFluidType {

    private final Vector3f fogColor;

    public BaseFluidType(
            float red,
            float green,
            float blue
    ) {
        this.fogColor = new Vector3f(
                red,
                green,
                blue
        );
    }

    public Vector3f fogColor() {
        return new Vector3f(fogColor);
    }

    public float red() {
        return fogColor.x;
    }

    public float green() {
        return fogColor.y;
    }

    public float blue() {
        return fogColor.z;
    }
}