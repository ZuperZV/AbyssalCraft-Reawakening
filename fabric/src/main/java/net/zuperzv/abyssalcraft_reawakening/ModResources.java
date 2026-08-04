package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.JEIPlugin;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer.ModStandingSignRenderer;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer.StoneRitualAltarBlockEntityRenderer;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer.StoneRitualPedestalBlockEntityRenderer;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer.WitherwoodShelfBlockEntityRender;
import net.zuperzv.abyssalcraft_reawakening.init.data.CodexDataLoader;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.StaffClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.StaffTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.init.screen.NecronomiconScreen;

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


        BlockEntityRendererRegistry.register(
                ModBlockEntities.STONE_RITUAL_ALTAR_BE.get(),
                StoneRitualAltarBlockEntityRenderer::new
        );
        BlockEntityRendererRegistry.register(
                ModBlockEntities.STONE_RITUAL_PEDESTAL_BE.get(),
                StoneRitualPedestalBlockEntityRenderer::new
        );
        BlockEntityRendererRegistry.register(
                ModBlockEntities.MOD_SHELF_BE.get(),
                WitherwoodShelfBlockEntityRender::new
        );
        BlockEntityRendererRegistry.register(
                ModBlockEntities.MOD_SIGN_BE.get(),
                ModStandingSignRenderer::new
        );


        ModelLayerRegistry.registerModelLayer(
                StoneRitualAltarBlockEntityRenderer.MAGIC_AURA_LAYER,
                StoneRitualAltarBlockEntityRenderer::createMagicAuraLayer
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

        ClientTooltipComponentCallback.EVENT.register(component -> {

            if (component instanceof StaffTooltipComponent staff) {
                return new StaffClientTooltip(staff);
            }

            if (component instanceof NecronomiconTooltipComponent necronomicon) {
                return new NecronomiconClientTooltip(necronomicon);
            }

            return null;
        });

        //ItemDecoratorRegistry.register(
        //        new StaffOfRenderingOverlay(Identifier.fromNamespaceAndPath(MOD_ID, "textures/item/oblivion_shard.png")));

        //JEI

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            client.execute(() -> {
                if (client.level != null) {
                    JEIPlugin.syncedRecipes =
                            (RecipeMap) client.level.recipeAccess().getSynchronizedRecipes();

                    System.out.println(
                            "Synced recipes: " + JEIPlugin.syncedRecipes.values().size()
                    );
                }
            });
        });
    }
}
