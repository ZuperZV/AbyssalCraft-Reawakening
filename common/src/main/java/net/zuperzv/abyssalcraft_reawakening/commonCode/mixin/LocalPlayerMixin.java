package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.ModHangingSignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(
            method = "openTextEdit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void abyssalcraft$customSignScreen(
            SignBlockEntity sign,
            boolean isFrontText,
            CallbackInfo ci
    ) {

        if (sign instanceof ModHangingSignBlockEntity modSign) {

            Minecraft minecraft = Minecraft.getInstance();

            minecraft.setScreen(
                    new HangingSignEditScreen(
                            modSign,
                            isFrontText,
                            minecraft.isTextFilteringEnabled()
                    )
            );

            ci.cancel();
        }
    }
}