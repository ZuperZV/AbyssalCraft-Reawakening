package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {
    void sendToServer(CustomPacketPayload packet);
    void sendToPlayer(CustomPacketPayload packet, ServerPlayer player);
}