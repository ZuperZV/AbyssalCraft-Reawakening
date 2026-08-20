package net.zuperzv.abyssalcraft_reawakening.commonCode.screen.Helpers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class NecronomiconCategory {

    public final String id;
    public final String title;
    public final String iconId;
    public final ItemStack icon;
    public final List<NecronomiconEntry> entries;
    public final List<Integer> tiers;
    public final int order;

    public NecronomiconCategory(String id, ItemStack icon, List<NecronomiconEntry> entries, List<Integer> tiers) {
        this(id, id, inferIconId(icon), icon, entries, tiers, Integer.MAX_VALUE);
    }

    public NecronomiconCategory(String id, String title, String iconId, ItemStack icon, List<NecronomiconEntry> entries, List<Integer> tiers, int order) {
        this.id = id;
        this.title = title;
        this.iconId = iconId;
        this.icon = icon;
        this.entries = entries;
        this.tiers = tiers;
        this.order = order;
    }

    public NecronomiconCategory(String id, String title, String iconId, List<NecronomiconEntry> entries, List<Integer> tiers, int order) {
        this.id = id;
        this.title = title;
        this.iconId = iconId;
        this.icon = ItemStack.EMPTY;
        this.entries = entries;
        this.tiers = tiers;
        this.order = order;
    }

    public String getDisplayTitle() {
        if (title != null && !title.isBlank()) {
            return title;
        }
        return id;
    }

    private static String inferIconId(ItemStack icon) {
        if (icon == null || icon.isEmpty()) {
            return "minecraft:book";
        }

        Identifier key = BuiltInRegistries.ITEM.getKey(icon.getItem());
        return key != null ? key.toString() : "minecraft:book";
    }
}
