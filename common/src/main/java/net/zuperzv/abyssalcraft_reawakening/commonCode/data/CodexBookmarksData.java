package net.zuperzv.abyssalcraft_reawakening.commonCode.data;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class CodexBookmarksData {

    public static List<String> getBookmarks(Player player) {
        if (player == null) return List.of();

        if (player instanceof ServerPlayer serverPlayer && serverPlayer instanceof IModPlayerData serverData) {
            return new ArrayList<>(serverData.abyssalCraftGetBookmarks());
        }

        return new ArrayList<>(ClientPlayerData.INSTANCE.abyssalCraftGetBookmarks());
    }

    public static void addBookmark(Player player, String entryId) {
        if (player instanceof IModPlayerData data) {
            ArrayList<String> bookmarks = data.abyssalCraftGetBookmarks();
            if (!bookmarks.contains(entryId) && bookmarks.size() < 24) {
                bookmarks.add(entryId);
                data.abyssalCraftSetBookmarks(bookmarks);
            }
        } else {
            ArrayList<String> bookmarks = ClientPlayerData.INSTANCE.abyssalCraftGetBookmarks();
            if (!bookmarks.contains(entryId) && bookmarks.size() < 24) {
                bookmarks.add(entryId);
            }
        }
    }

    public static void removeBookmark(Player player, String entryId) {
        if (player instanceof IModPlayerData data) {
            ArrayList<String> bookmarks = data.abyssalCraftGetBookmarks();
            bookmarks.remove(entryId);
            data.abyssalCraftSetBookmarks(bookmarks);
        } else {
            ArrayList<String> bookmarks = ClientPlayerData.INSTANCE.abyssalCraftGetBookmarks();
            bookmarks.remove(entryId);
        }
    }

    public static void syncClientBookmarks(List<String> bookmarks) {
        ClientPlayerData.INSTANCE.abyssalCraftSetBookmarks(new ArrayList<>(bookmarks));
    }
}