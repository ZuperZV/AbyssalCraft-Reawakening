package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTypes.class)
public interface RenderTypesAccessor {

    @Accessor("END_PORTAL")
    static RenderType abyssalcraft$getEndPortal() {
        throw new AssertionError();
    }
}
