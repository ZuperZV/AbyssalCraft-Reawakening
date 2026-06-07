package net.zuperzv.abyssalcraft_reawakening.init.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.data.IModPlayerData;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

import java.util.ArrayList;

public record SetBookmarksPacket(String entryId, boolean isSetter) implements CustomPacketPayload {

    public static final Type<SetBookmarksPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "set_bookmark"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetBookmarksPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeUtf(pkt.entryId);
                        buf.writeBoolean(pkt.isSetter);
                    },
                    buf -> new SetBookmarksPacket(buf.readUtf(), buf.readBoolean())
            );

    @Override
    public Type<SetBookmarksPacket> type() {
        return TYPE;
    }

    public static void handle(SetBookmarksPacket msg, ServerPlayer player) {

        if (player instanceof IModPlayerData data) {

            ArrayList<String> bookmarks = data.abyssalCraftGetBookmarks();

            if (msg.isSetter()) {
                if (!bookmarks.contains(msg.entryId()) && bookmarks.size() < 24) {
                    bookmarks.add(msg.entryId());
                }
            } else {
                bookmarks.remove(msg.entryId());
            }

            Services.NETWORK.sendToPlayer(new SyncBookmarksPacket(bookmarks), player);
        }
    }
}