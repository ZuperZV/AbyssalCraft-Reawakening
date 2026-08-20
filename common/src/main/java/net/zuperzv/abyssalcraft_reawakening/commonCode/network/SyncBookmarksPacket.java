package net.zuperzv.abyssalcraft_reawakening.commonCode.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.CodexBookmarksData;

import java.util.ArrayList;

public record SyncBookmarksPacket(ArrayList<String> bookmarks) implements CustomPacketPayload {

    public static final Type<SyncBookmarksPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "sync_bookmarks"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBookmarksPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeInt(pkt.bookmarks.size());
                        for (String s : pkt.bookmarks) buf.writeUtf(s);
                    },
                    buf -> {
                        int size = buf.readInt();
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < size; i++) list.add(buf.readUtf());
                        return new SyncBookmarksPacket(list);
                    }
            );

    @Override
    public Type<SyncBookmarksPacket> type() {
        return TYPE;
    }

    public static void handle(SyncBookmarksPacket msg, net.minecraft.client.Minecraft minecraft) {

        minecraft.execute(() -> {
            CodexBookmarksData.syncClientBookmarks(msg.bookmarks());
        });
    }
}