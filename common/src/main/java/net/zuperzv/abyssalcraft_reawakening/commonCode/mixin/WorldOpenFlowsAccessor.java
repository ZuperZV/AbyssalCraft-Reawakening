package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldOpenFlows.class)
public interface WorldOpenFlowsAccessor {

    @Invoker("askForBackup")
    void invokeAskForBackup(
            LevelStorageSource.LevelStorageAccess levelAccess,
            boolean oldCustomized,
            Runnable proceedCallback,
            Runnable cancelCallback
    );
}