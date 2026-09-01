package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {

    @Invoker("create")
    static RenderType abyssalcraft$create(
            String name,
            RenderSetup setup
    ) {
        throw new AssertionError();
    }
}