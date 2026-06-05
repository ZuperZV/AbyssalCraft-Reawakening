package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModResources {

    @net.neoforged.bus.api.SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {

        event.addPackFinders(
                net.minecraft.resources.Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalcraft_programmer_art"),
                net.minecraft.server.packs.PackType.CLIENT_RESOURCES,
                net.minecraft.network.chat.Component.literal("AbyssalCraft Programmer Art"),
                PackSource.BUILT_IN,
                false,
                net.minecraft.server.packs.repository.Pack.Position.TOP
        );
    }
}

