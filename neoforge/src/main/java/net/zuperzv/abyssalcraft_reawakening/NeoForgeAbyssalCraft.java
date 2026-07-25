package net.zuperzv.abyssalcraft_reawakening;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.JEIPlugin;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.NecronomiconTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.StaffClientTooltip;
import net.zuperzv.abyssalcraft_reawakening.init.data.tooltip.StaffTooltipComponent;
import net.zuperzv.abyssalcraft_reawakening.init.network.SetBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

import static net.zuperzv.abyssalcraft_reawakening.Constants.MOD_ID;

@Mod(MOD_ID)
public class NeoForgeAbyssalCraft {

    public NeoForgeAbyssalCraft(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        eventBus.addListener(AbyssalCraftDatagen::onGatherClientData);
        eventBus.addListener(this::registerPayloadHandlers);

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

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public class ClientInit {

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

        //@SubscribeEvent
        //public static void init(FMLClientSetupEvent event) {
            //ItemDecoratorRegistry.register(
            //        new StaffOfRenderingOverlay(Identifier.fromNamespaceAndPath(MOD_ID, "textures/item/oblivion_shard.png")));
        //}
    }
}
