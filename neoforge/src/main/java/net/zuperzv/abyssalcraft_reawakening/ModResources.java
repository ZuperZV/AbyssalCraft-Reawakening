package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.zuperzv.abyssalcraft_reawakening.init.data.CodexDataLoader;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        CodexDataLoader.load();
    }

    @SubscribeEvent
    public static void onAddServerReloadListeners(AddClientReloadListenersEvent event) {

        event.addListener(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "data"),

                new PreparableReloadListener() {

                    @Override
                    public CompletableFuture<Void> reload(
                            SharedState sharedState,
                            Executor backgroundExecutor,
                            PreparationBarrier barrier,
                            Executor gameExecutor
                    ) {
                        return CompletableFuture.runAsync(() -> {
                            ResourceManager manager = sharedState.resourceManager();

                            CodexDataLoader.loadFromResourceManager(manager);

                        }, backgroundExecutor).thenCompose(barrier::wait);
                    }
                }
        );
    }
}