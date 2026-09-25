package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.resources.Identifier;

public interface IShaderRenderer {

    void enableShader(
            Identifier shaderId,
            int ticks
    );

    void enableDarkShader(
            int ticks
    );
}