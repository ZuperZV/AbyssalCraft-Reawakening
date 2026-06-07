package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.zuperzv.abyssalcraft_reawakening.services.FabricServerHelper;
import net.zuperzv.abyssalcraft_reawakening.worldgen.FabricModWorldgen;

public class FabricAbyssalCraft implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        ServerLifecycleEvents.SERVER_STARTED.register(FabricServerHelper::onServerStarted);

        FabricModWorldgen.load();
    }
}
