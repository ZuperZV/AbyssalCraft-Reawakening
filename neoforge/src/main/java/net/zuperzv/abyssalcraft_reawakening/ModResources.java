package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModResources {

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {

        event.addPackFinders(
                Constants.id("abyssalcraft_programmer_art"),
                PackType.CLIENT_RESOURCES,
                Component.literal("AbyssalCraft Programmer Art"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.NECRONOMICON_MENU.get(), NecronomiconScreen::new);
    }
}