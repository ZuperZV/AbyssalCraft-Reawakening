package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;

@Environment(EnvType.CLIENT)
public class ModResources implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        MenuScreens.register(ModMenuTypes.NECRONOMICON_MENU.get(), NecronomiconScreen::new);
    }
}