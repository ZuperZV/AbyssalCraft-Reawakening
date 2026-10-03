package net.zuperzv.abyssalcraft_reawakening.commonCode.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.DataItemJsonLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemRegistry;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemType;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public final class ModDataItem { //TODO make this work

    private ModDataItem() {
    }

    public static final List<DataItemType> SCUTE_TYPES = new ArrayList<>();
    public static final List<DataItemType> MOD_SCUTE_TYPES = new ArrayList<>();
    public static final List<CrystalType> CRYSTAL_TYPES = List.of(
            new CrystalType("iron", 0xD5D5D5),
            new CrystalType("gold", 0xFFD84D),
            new CrystalType("sulfur", 0xE8E34A),
            new CrystalType("carbon", 0x454545),
            new CrystalType("oxygen", 0x8FD7FF),
            new CrystalType("hydrogen", 0xE8F6FF),
            new CrystalType("nitrogen", 0xA8C8FF),
            new CrystalType("phosphorus", 0xFF9B52),
            new CrystalType("potassium", 0xD9A7FF),
            new CrystalType("nitrate", 0xE7E7D9),
            new CrystalType("methane", 0xB4E4D2),
            new CrystalType("redstone", 0xE53935),
            new CrystalType("abyssalnite", 0x897cac),
            new CrystalType("coralium", 0x6e9082),
            new CrystalType("dreadium", 0xc34145),
            new CrystalType("blaze", 0xFF6A22),
            new CrystalType("silicon", 0xB8C7C9),
            new CrystalType("magnesium", 0xE5E5F0),
            new CrystalType("aluminium", 0xC8D6E5),
            new CrystalType("silica", 0xD9F2E6),
            new CrystalType("alumina", 0xD6C7F2),
            new CrystalType("magnesia", 0xCFE8E1),
            new CrystalType("zinc", 0xA7D4D1),
            new CrystalType("calcium", 0xF2E6CC),
            new CrystalType("beryllium", 0x9DE3A0),
            new CrystalType("beryl", 0x70D5B2)
    );

    //TEST
    public static final DataItemType DIRT = register(
            new DataItemType(
                    id("dirt"),
                    id("textures/entity/dirt_armadillo.png"),
                    id("textures/entity/wolf/dirt_armor.png"),
                    true
            )
    );

    private static Identifier id(String path) {
        return Constants.id(path);
    }

    private static DataItemType register(
            DataItemType type
    ) {
        type.setEssenceItem(
                () -> new Item(new Item.Properties()), //TODO noget som ArmadilloScuteItem(type)
                true
        );

        SCUTE_TYPES.add(type);

        return type;
    }

    public static DataItemType registerWithRequiredMod(
            boolean modLoaded,
            DataItemType type
    ) {
        SCUTE_TYPES.add(type);
        MOD_SCUTE_TYPES.add(type);

        type.setEssenceItem(
                () -> new Item(new Item.Properties()),
                true
        );

        if (type.isEnabled()) {
            type.setEnabled(modLoaded);
        }

        return type;
    }

    public static void registerAll(DataItemRegistry registry) {
        DataItemJsonLoader.load();

        System.out.println("getLoadedItems: " + DataItemJsonLoader.getLoadedItems());
        for (DataItemType scute : DataItemJsonLoader.getLoadedItems()) {
            System.out.println("registered: " + scute.getDisplayName());
            registry.register(scute);
            SCUTE_TYPES.add(scute);
        }

        SCUTE_TYPES.forEach(registry::register);
    }

    public static void registerCrystalBlocks() {
        ModBlocks.registerCrystalBlocks(CRYSTAL_TYPES);
    }

    public record CrystalType(String name, int color) {
        public Identifier productId(String product) {
            String path = switch (product) {
                case "crystal" -> name + "_crystal";
                case "shard" -> name + "_crystal_shard";
                case "fragment" -> name + "_crystal_fragment";
                case "dust" -> name + "_crystal_dust";
                default -> throw new IllegalArgumentException("Unknown crystal product: " + product);
            };
            return Constants.id(path);
        }
    }
}