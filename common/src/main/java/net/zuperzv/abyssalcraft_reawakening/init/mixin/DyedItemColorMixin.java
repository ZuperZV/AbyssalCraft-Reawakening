package net.zuperzv.abyssalcraft_reawakening.init.mixin;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(DyedItemColor.class)
public class DyedItemColorMixin {

    @Inject(
            method = "addToTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    private void abyssalcraft$NoDyedTextOnStaffTooltip(
            Item.TooltipContext context,
            Consumer<Component> consumer,
            TooltipFlag flag,
            DataComponentGetter components,
            CallbackInfo ci
    ) {
        if (components.get(ModDataComponentTypes.ENERGY.get()) != null
                || components.get(ModDataComponentTypes.CODEX_TIER.get()) != null) {
            ci.cancel();
        }
    }
}