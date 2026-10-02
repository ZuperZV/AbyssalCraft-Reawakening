package net.zuperzv.abyssalcraft_reawakening.commonCode.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.DataItemJsonLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemRegistry;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemType;

import java.util.ArrayList;
import java.util.List;

public final class ModDataItem { //TODO make this work

    private ModDataItem() {
    }

    public static final List<DataItemType> SCUTE_TYPES = new ArrayList<>();
    public static final List<DataItemType> MOD_SCUTE_TYPES = new ArrayList<>();

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
}