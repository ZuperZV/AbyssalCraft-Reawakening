package net.zuperzv.abyssalcraft_reawakening.services;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.commonCode.shaders.DarkShaderRenderer;
import net.zuperzv.abyssalcraft_reawakening.commonCode.shaders.ModShaderRenderer;
import net.zuperzv.abyssalcraft_reawakening.services.types.IShaderRenderer;

public class FabricShaderRenderer implements IShaderRenderer {

    public static final FabricShaderRenderer INSTANCE =
            new FabricShaderRenderer();

    public FabricShaderRenderer() {
    }

    public static void register() {

        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(
                context -> {

                    DarkShaderRenderer.render();
                    ModShaderRenderer.render();
                }
        );
    }

    @Override
    public void enableShader(Identifier shaderId, int ticks) {
        ModShaderRenderer.enableShader(
                shaderId,
                ticks
        );
    }

    @Override
    public void enableDarkShader(int ticks) {
        DarkShaderRenderer.enableForTicks(ticks);
    }
}