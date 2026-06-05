package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModResources implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Constants.LOG.info("AbyssalCraft Reawakening client initialization complete");
    }
}