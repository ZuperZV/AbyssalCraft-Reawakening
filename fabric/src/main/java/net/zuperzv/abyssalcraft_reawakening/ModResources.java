package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.zuperzv.abyssalcraft_reawakening.init.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.data.CodexDataLoader;
import net.zuperzv.abyssalcraft_reawakening.init.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Environment(EnvType.CLIENT)
public class ModResources implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModContainer container = FabricLoader.getInstance()
                .getModContainer(Constants.MOD_ID)
                .orElseThrow();

        ResourceManagerHelper.registerBuiltinResourcePack(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalcraft_programmer_art"),
                container,
                ResourcePackActivationType.NORMAL
        );

        MenuScreens.register(ModMenuTypes.NECRONOMICON_MENU.get(), NecronomiconScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncBookmarksPacket.TYPE,
                (packet, context) -> SyncBookmarksPacket.handle(packet, context.client())
        );



        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            CodexDataLoader.load();
        });

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new IdentifiableResourceReloadListener() {

                    @Override
                    public CompletableFuture<Void> reload(
                            SharedState currentReload,
                            Executor taskExecutor,
                            PreparationBarrier preparationBarrier,
                            Executor reloadExecutor
                    ) {
                        return CompletableFuture.runAsync(() -> {
                            ResourceManager manager = currentReload.resourceManager();

                            CodexDataLoader.loadFromResourceManager(manager);

                        }, taskExecutor).thenCompose(preparationBarrier::wait);
                    }

                    @Override
                    public Identifier getFabricId() {
                        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "codex_data");
                    }
                });
    }
}
