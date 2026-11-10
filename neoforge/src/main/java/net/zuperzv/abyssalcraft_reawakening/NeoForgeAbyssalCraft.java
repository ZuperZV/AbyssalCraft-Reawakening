package net.zuperzv.abyssalcraft_reawakening;


import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EntityType;
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
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.JEIPlugin;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.NecronomiconClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.StaffClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.tooltip.StaffTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.commonCode.client.AbyssalniteArmorRenderLayer;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.LivingEntityRendererAccessor;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.ModPotionBrewing;
import net.zuperzv.abyssalcraft_reawakening.commonCode.network.SetBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.commonCode.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.ColorBubbleParticle;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.ModParticleTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.SulfurBubbleParticle;
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
        NeoForge.EVENT_BUS.addListener(this::registerBrewingRecipes);

        NeoForgeRegistryHelper.register(eventBus);
    }

    private void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        ModPotionBrewing.register(event.getBuilder());
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
        public static void addArmorLayers(EntityRenderersEvent.AddLayers event) {
            for (EntityType<?> entityType : event.getEntityTypes()) {
                Object renderer = event.getRenderer(entityType);
                if (renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer) {
                    if (livingRenderer.getModel() instanceof HumanoidModel<?>) {
                        ((LivingEntityRendererAccessor) livingRenderer)
                                .abyssalcraft$addLayer(AbyssalniteArmorRenderLayer.create(livingRenderer));
                    }
                }
            }

            for (var skin : event.getSkins()) {
                var playerRenderer = event.getPlayerRenderer(skin);
                if (playerRenderer.getModel() instanceof HumanoidModel<?>) {
                    ((LivingEntityRendererAccessor) playerRenderer)
                            .abyssalcraft$addLayer(AbyssalniteArmorRenderLayer.create(playerRenderer));
                }
                var mannequinRenderer = event.getMannequinRenderer(skin);
                if (mannequinRenderer.getModel() instanceof HumanoidModel<?>) {
                    ((LivingEntityRendererAccessor) mannequinRenderer)
                            .abyssalcraft$addLayer(AbyssalniteArmorRenderLayer.create(mannequinRenderer));
                }
            }
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(
                    ModParticleTypes.COLOR_BUBBLE.get(),
                    ColorBubbleParticle.Provider::new
            );
            event.registerSpriteSet(
                    ModParticleTypes.CROASIUS_GAS.get(),
                    SulfurBubbleParticle.Provider::new
            );
        }

        @SubscribeEvent
        public static void onRecipeReceived(RecipesReceivedEvent event) {
            JEIPlugin.syncedRecipes = event.getRecipeMap();
        }

        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            event.sendRecipes(
                    ModRecipes.RITUAL_ALTAR.type().get()
            );
            event.sendRecipes(
                    ModRecipes.ESSENCE_BOILER.type().get()
            );
            event.sendRecipes(
                    ModRecipes.CRYSTAL_GROWTH.type().get()
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
