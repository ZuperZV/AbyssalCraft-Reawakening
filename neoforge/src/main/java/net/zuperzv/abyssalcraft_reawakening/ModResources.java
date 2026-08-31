package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.renderer.*;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.CodexDataLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.DyedColorTintSource;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.DataItemJsonLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.screen.NecronomiconScreen;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

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
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "dyed_color_tint"),
                DyedColorTintSource.MAP_CODEC
        );

        event.register(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "dyed_color_tint"),
                DyedColorTintSource.MAP_CODEC
        );
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.STONE_RITUAL_ALTAR_BE.get(), StoneRitualAltarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STONE_RITUAL_PEDESTAL_BE.get(), StoneRitualPedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SHELF_BE.get(), WitherwoodShelfBlockEntityRender::new);

        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), context -> new StandingSignRenderer(context));
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), context -> new HangingSignRenderer(context));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                StoneRitualAltarBlockEntityRenderer.MAGIC_AURA_LAYER,
                StoneRitualAltarBlockEntityRenderer::createMagicAuraLayer
        );
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        CodexDataLoader.load();
        DataItemJsonLoader.load();
        Services.I_MULTIBLOCK_INPUT.register();
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
                            DataItemJsonLoader.loadFromResourceManager(manager);

                        }, backgroundExecutor).thenCompose(barrier::wait);
                    }
                }
        );
    }
}