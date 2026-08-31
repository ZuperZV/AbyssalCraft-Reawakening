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

    public static final DataItemType DIRT = register(
            new DataItemType(
                    id("dirt"),
                    id("textures/entity/dirt_armadillo.png"),
                    id("textures/entity/wolf/dirt_armor.png"),
                    true
            )
    );

    public static final DataItemType COBBLESTONE = register(
            new DataItemType(
                    id("cobblestone"),
                    id("textures/entity/cobblestone_armadillo.png"),
                    id("textures/entity/wolf/cobblestone_armor.png"),
                    true
            )
    );

    public static final DataItemType FLINT = register(
            new DataItemType(
                    id("flint"),
                    id("textures/entity/flint_armadillo.png"),
                    id("textures/entity/wolf/flint_armor.png"),
                    true
            )
    );

    public static final DataItemType STONE = register(
            new DataItemType(
                    id("stone"),
                    id("textures/entity/stone_armadillo.png"),
                    id("textures/entity/wolf/stone_armor.png"),
                    true
            )
    );

    public static final DataItemType DEEPSLATE = register(
            new DataItemType(
                    id("deepslate"),
                    id("textures/entity/deepslate_armadillo.png"),
                    id("textures/entity/wolf/deepslate_armor.png"),
                    true
            )
    );

    public static final DataItemType SAND = register(
            new DataItemType(
                    id("sand"),
                    id("textures/entity/sand_armadillo.png"),
                    id("textures/entity/wolf/sand_armor.png"),
                    true
            )
    );

    public static final DataItemType CLAY = register(
            new DataItemType(
                    id("clay"),
                    id("textures/entity/clay_armadillo.png"),
                    id("textures/entity/wolf/clay_armor.png"),
                    true
            )
    );

    public static final DataItemType HONEY = register(
            new DataItemType(
                    id("honey"),
                    id("textures/entity/honey_armadillo.png"),
                    id("textures/entity/wolf/honey_armor.png"),
                    true
            )
    );

    public static final DataItemType AMETHYST = register(
            new DataItemType(
                    id("amethyst"),
                    id("textures/entity/amethyst_armadillo.png"),
                    id("textures/entity/wolf/amethyst_armor.png"),
                    true
            )
    );

    public static final DataItemType SLIME = register(
            new DataItemType(
                    id("slime"),
                    id("textures/entity/slime_armadillo.png"),
                    id("textures/entity/wolf/slime_armor.png"),
                    true
            )
    );

    public static final DataItemType DYE = register(
            new DataItemType(
                    id("dye"),
                    id("textures/entity/dye_armadillo.png"),
                    id("textures/entity/wolf/dye_armor.png"),
                    true
            )
    );

    public static final DataItemType NETHERRACK = register(
            new DataItemType(
                    id("netherrack"),
                    id("textures/entity/netherrack_armadillo.png"),
                    id("textures/entity/wolf/netherrack_armor.png"),
                    true
            )
    );

    public static final DataItemType COAL = register(
            new DataItemType(
                    id("coal"),
                    id("textures/entity/coal_armadillo.png"),
                    id("textures/entity/wolf/coal_armor.png"),
                    true
            )
    );

    public static final DataItemType IRON = register(
            new DataItemType(
                    id("iron"),
                    id("textures/entity/iron_armadillo.png"),
                    id("textures/entity/wolf/iron_armor.png"),
                    true
            )
    );

    public static final DataItemType DIAMOND = register(
            new DataItemType(
                    id("diamond"),
                    id("textures/entity/diamond_armadillo.png"),
                    id("textures/entity/wolf/diamond_armor.png"),
                    true
            )
    );

    public static final DataItemType QUARTZ = register(
            new DataItemType(
                    id("quartz"),
                    id("textures/entity/quartz_armadillo.png"),
                    id("textures/entity/wolf/quartz_armor.png"),
                    true
            )
    );

    public static final DataItemType CHROMIUM = register(
            new DataItemType(
                    id("chromium"),
                    id("textures/entity/chromium_armadillo.png"),
                    id("textures/entity/wolf/chromium_armor.png"),
                    true
            )
    );

    public static final DataItemType EMERALD = register(
            new DataItemType(
                    id("emerald"),
                    id("textures/entity/emerald_armadillo.png"),
                    id("textures/entity/wolf/emerald_armor.png"),
                    true
            )
    );

    public static final DataItemType GOLD = register(
            new DataItemType(
                    id("gold"),
                    id("textures/entity/gold_armadillo.png"),
                    id("textures/entity/wolf/gold_armor.png"),
                    true
            )
    );

    public static final DataItemType LAPIS = register(
            new DataItemType(
                    id("lapis"),
                    id("textures/entity/lapis_armadillo.png"),
                    id("textures/entity/wolf/lapis_armor.png"),
                    true
            )
    );

    public static final DataItemType REDSTONE = register(
            new DataItemType(
                    id("redstone"),
                    id("textures/entity/redstone_armadillo.png"),
                    id("textures/entity/wolf/redstone_armor.png"),
                    true
            )
    );

    public static final DataItemType NETHERITE = register(
            new DataItemType(
                    id("netherite"),
                    id("textures/entity/netherite_armadillo.png"),
                    id("textures/entity/wolf/netherite_armor.png"),
                    true
            )
    );

    public static final DataItemType COPPER = register(
            new DataItemType(
                    id("copper"),
                    id("textures/entity/copper_armadillo.png"),
                    id("textures/entity/wolf/copper_armor.png"),
                    true
            )
    );

    public static final DataItemType MAGMA = register(
            new DataItemType(
                    id("magma"),
                    id("textures/entity/magma_armadillo.png"),
                    id("textures/entity/wolf/magma_armor.png"),
                    true
            )
    );

    public static final DataItemType OBSIDIAN = register(
            new DataItemType(
                    id("obsidian"),
                    id("textures/entity/obsidian_armadillo.png"),
                    id("textures/entity/wolf/obsidian_armor.png"),
                    true
            )
    );

    public static final DataItemType PRISMARINE = register(
            new DataItemType(
                    id("prismarine"),
                    id("textures/entity/prismarine_armadillo.png"),
                    id("textures/entity/wolf/prismarine_armor.png"),
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