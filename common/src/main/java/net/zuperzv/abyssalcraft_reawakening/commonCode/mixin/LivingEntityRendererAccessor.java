package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {

    @Invoker("addLayer")
    boolean abyssalcraft$addLayer(RenderLayer<?, ?> layer);

    @Accessor("layers")
    List<RenderLayer<?, ?>> abyssalcraft$getLayers();
}
