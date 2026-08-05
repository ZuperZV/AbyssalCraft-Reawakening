package net.zuperzv.abyssalcraft_reawakening.init.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {

    @Redirect(
            method = "openWorldCheckWorldStemCompatibility",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;askForBackup(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;ZLjava/lang/Runnable;Ljava/lang/Runnable;)V"
            )
    )
    private void skipExperimentalBackupWarning(
            WorldOpenFlows instance,
            LevelStorageSource.LevelStorageAccess worldAccess,
            boolean oldCustomized,
            Runnable proceedCallback,
            Runnable cancelCallback
    ) {
        // Hvis den kommer herfra pga. experimental worldgen:
        // skip backup dialogen
        if (!oldCustomized) {
            proceedCallback.run();
            return;
        }

        // Behold vanilla behavior for customized worlds
        ((WorldOpenFlowsAccessor) instance).invokeAskForBackup(
                worldAccess,
                oldCustomized,
                proceedCallback,
                cancelCallback
        );
    }
}