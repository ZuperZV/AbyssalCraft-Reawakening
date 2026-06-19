package net.zuperzv.abyssalcraft_reawakening;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.init.data.DyedColorTintSource;
import net.zuperzv.abyssalcraft_reawakening.init.network.SetBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.services.FabricServerHelper;
import net.zuperzv.abyssalcraft_reawakening.worldgen.FabricModWorldgen;

public class FabricAbyssalCraft implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        PayloadTypeRegistry.serverboundPlay().register(SetBookmarksPacket.TYPE, SetBookmarksPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncBookmarksPacket.TYPE, SyncBookmarksPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                SetBookmarksPacket.TYPE,
                (packet, context) -> context.server().execute(() -> SetBookmarksPacket.handle(packet, context.player()))
        );

        ServerLifecycleEvents.SERVER_STARTED.register(FabricServerHelper::onServerStarted);

        ItemTintSources.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "dyed_color_tint"),
                DyedColorTintSource.MAP_CODEC
        );

        FabricModWorldgen.load();
    }
}
