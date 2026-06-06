package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.zuperzv.abyssalcraft_reawakening.init.custom.NecronomiconScreen;

@Environment(EnvType.CLIENT)
public class ModResources implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register screens here - MenuScreens registration would go here
        // MenuScreens.register(/*MenuType*/, NecronomiconScreen::new);
        Constants.LOG.info("AbyssalCraft Reawakening client initialization complete");
    }
}