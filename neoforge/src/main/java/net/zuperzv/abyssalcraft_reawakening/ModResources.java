package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
//import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.JEIPlugin;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer.StoneRitualAltarBlockEntityRenderer;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer.StoneRitualPedestalBlockEntityRenderer;
import net.zuperzv.abyssalcraft_reawakening.init.data.CodexDataLoader;
import net.zuperzv.abyssalcraft_reawakening.init.data.DyedColorTintSource;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.decorator.ItemDecoratorRegistry;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.decorator.StaffOfRenderingOverlay;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.ModRecipes;
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
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.STONE_RITUAL_ALTAR_BE.get(), StoneRitualAltarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STONE_RITUAL_PEDESTAL_BE.get(), StoneRitualPedestalBlockEntityRenderer::new);
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

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(
                ModRecipes.ASTRAL_ALTAR.type().get()
        );
    }
}