package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zuperzv.abyssalcraft_reawakening.init.item.helpers.TransmutationItem;
import net.zuperzv.abyssalcraft_reawakening.services.types.IItemFactory;
import net.zuperzv.abyssalcraft_reawakening.services.types.INetworkHelper;

public class NeoForgeItemFactoryHelper implements IItemFactory {

    @Override
    public Item createTransmutationGem(Item.Properties properties) {
        return new TransmutationItem(properties);
    }
}