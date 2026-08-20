package net.zuperzv.abyssalcraft_reawakening.commonCode.data;

import java.util.ArrayList;

public class ClientPlayerData implements IModPlayerData {
    public static final ClientPlayerData INSTANCE = new ClientPlayerData();

    private final ArrayList<String> bookmarks = new ArrayList<>();

    @Override
    public ArrayList<String> abyssalCraftGetBookmarks() {
        return bookmarks;
    }

    @Override
    public void abyssalCraftSetBookmarks(ArrayList<String> list) {
        bookmarks.clear();
        bookmarks.addAll(list);
    }
}
