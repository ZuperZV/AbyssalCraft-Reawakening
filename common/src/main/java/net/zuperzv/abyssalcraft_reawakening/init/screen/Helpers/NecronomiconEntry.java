package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class NecronomiconEntry {
    public List<NecronomiconPage> right_side;
    public String id;
    public List<String> search_items;
    public String title;
    public String title_key;
    public String type;
    public String icon;
    public List<String> related;

    private static final Gson GSON = new Gson();

    public static NecronomiconEntry fromJson(JsonObject json) {
        return GSON.fromJson(json, NecronomiconEntry.class);
    }
}

