package net.zuperzv.abyssalcraft_reawakening;


import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.JEIPlugin;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.NecronomiconClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.StaffClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.StaffTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.commonCode.network.SetBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.commonCode.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.types.IAttributeRegistryHelper;
import net.zuperzv.abyssalcraft_reawakening.services.types.ISpawnPlacementHelper;
import net.zuperzv.abyssalcraft_reawakening.services.util.ModWoodTypes;

import static net.zuperzv.abyssalcraft_reawakening.Constants.MOD_ID;

@Mod(MOD_ID)
public class NeoForgeAbyssalCraft {

    public NeoForgeAbyssalCraft(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        eventBus.addListener(this::registerPayloadHandlers);
        eventBus.addListener(this::onRegisterSpawnPlacements);
        eventBus.addListener(this::onEntityAttributeCreation);

        NeoForgeRegistryHelper.register(eventBus);
    }

    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID);

        registrar.playToServer(
                SetBookmarksPacket.TYPE,
                SetBookmarksPacket.STREAM_CODEC,
                (packet, context) -> context.enqueueWork(() -> SetBookmarksPacket.handle(packet, (net.minecraft.server.level.ServerPlayer) context.player()))
        );

        registrar.playToClient(
                SyncBookmarksPacket.TYPE,
                SyncBookmarksPacket.STREAM_CODEC,
                (packet, context) -> SyncBookmarksPacket.handle(packet, net.minecraft.client.Minecraft.getInstance())
        );
    }

    private void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        Services.SPAWN_PLACEMENTS.applySpawnPlacements(new ISpawnPlacementHelper.SpawnPlacementsRegistrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate) {
                event.register(entityType, spawnPlacementType, heightmap, predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
            }
        });
    }

    private void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        Services.ATTRIBUTES.applyEntityAttributeRegistrations(new IAttributeRegistryHelper.EntityAttributeRegistrar() {
            @Override
            public <T extends LivingEntity> void register(EntityType<T> entityType, AttributeSupplier.Builder builder) {
                event.put(entityType, builder.build());
            }
        });
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public class ClientInit {

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            Services.CLIENT_REGISTRY.applyEntityRendererRegistrations(event::registerEntityRenderer);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            Services.CLIENT_REGISTRY.applyModelLayerRegistrations(event::registerLayerDefinition);
        }

        @SubscribeEvent
        public static void onRecipeReceived(RecipesReceivedEvent event) {
            JEIPlugin.syncedRecipes = event.getRecipeMap();
        }

        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            event.sendRecipes(
                    ModRecipes.ASTRAL_ALTAR.type().get()
            );
        }

        @SubscribeEvent
        public static void registerTooltipFactories(
                RegisterClientTooltipComponentFactoriesEvent event
        ) {
            event.register(
                    StaffTooltipComponent.class,
                    StaffClientTooltip::new
            );

            event.register(
                    NecronomiconTooltipComponent.class,
                    NecronomiconClientTooltip::new
            );
        }

        @SubscribeEvent
        public static void init(FMLClientSetupEvent event) {
            Sheets.addWoodType(ModWoodTypes.WITHERWOOD);

            //ItemDecoratorRegistry.register(
            //        new StaffOfRenderingOverlay(Identifier.fromNamespaceAndPath(MOD_ID, "textures/item/oblivion_shard.png")));
        }
    }
}
