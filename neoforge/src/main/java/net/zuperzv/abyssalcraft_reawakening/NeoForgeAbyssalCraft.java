package net.zuperzv.abyssalcraft_reawakening;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zuperzv.abyssalcraft_reawakening.init.network.SetBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.network.SyncBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Mod(Constants.MOD_ID)
public class NeoForgeAbyssalCraft {

    public NeoForgeAbyssalCraft(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        eventBus.addListener(AbyssalCraftDatagen::onGatherClientData);
        eventBus.addListener(this::registerPayloadHandlers);

        NeoForgeRegistryHelper.register(eventBus);
    }

    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);

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
}
