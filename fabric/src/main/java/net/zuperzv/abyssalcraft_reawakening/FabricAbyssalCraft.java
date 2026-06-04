package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ModInitializer;

public class FabricAbyssalCraft implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
    }
}
