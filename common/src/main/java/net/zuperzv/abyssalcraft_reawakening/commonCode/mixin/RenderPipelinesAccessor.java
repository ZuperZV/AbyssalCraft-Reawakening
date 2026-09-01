package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {

    @Accessor("ENTITY_SNIPPET")
    static RenderPipeline.Snippet abyssalcraft$getEntitySnippet() {
        throw new AssertionError();
    }

    @Invoker("register")
    static RenderPipeline abyssalcraft$register(RenderPipeline pipeline) {
        throw new AssertionError();
    }
}