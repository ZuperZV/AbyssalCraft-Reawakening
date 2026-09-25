package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.shaders.DarkShaderRenderer;
import net.zuperzv.abyssalcraft_reawakening.commonCode.shaders.ModShaderRenderer;
import net.zuperzv.abyssalcraft_reawakening.services.types.IShaderRenderer;

@EventBusSubscriber(
        modid = Constants.MOD_ID,
        value = Dist.CLIENT
)
public class NeoForgeShaderRenderer implements IShaderRenderer {

    public static final NeoForgeShaderRenderer INSTANCE =
            new NeoForgeShaderRenderer();

    public NeoForgeShaderRenderer() {
    }

    @SubscribeEvent
    public static void renderLevel(
            RenderLevelStageEvent.AfterLevel event
    ) {
        DarkShaderRenderer.render();
        ModShaderRenderer.render();
    }

    @Override
    public void enableShader(
            Identifier shaderId,
            int ticks
    ) {
        ModShaderRenderer.enableShader(
                shaderId,
                ticks
        );
    }

    @Override
    public void enableDarkShader(
            int ticks
    ) {
        DarkShaderRenderer.enableForTicks(ticks);
    }
}