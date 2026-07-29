package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zuperzv.abyssalcraft_reawakening.services.types.INetworkHelper;

public class NeoForgeNetworkHelper implements INetworkHelper {

    @Override
    public void sendToServer(CustomPacketPayload packet) {
        ClientPacketDistributor.sendToServer(packet);
    }

    @Override
    public void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}