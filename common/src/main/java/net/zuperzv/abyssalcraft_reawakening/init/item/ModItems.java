package net.zuperzv.abyssalcraft_reawakening.init.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorType;
import net.zuperzv.abyssalcraft_reawakening.init.component.CodexTierData;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.NecronomiconItem;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModItems {
    private ModItems() {}

    public static void load() {}

    public static final RegistryHandle<Item> NECRONOMICON = Services.REGISTRY.registerItem("necronomicon",
            properties -> new NecronomiconItem(properties.stacksTo(1)
                    .component(ModDataComponentTypes.CODEX_TIER.get(), new CodexTierData(1))
                    .component(DataComponents.DYED_COLOR, new DyedItemColor(0x643732))));


    public static final RegistryHandle<Item> ABYSSALNITE_INGOT = Services.REGISTRY.registerItem("abyssalnite_ingot",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> RAW_ABYSSALNITE = Services.REGISTRY.registerItem("raw_abyssalnite",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> ABYSSALNITE_NUGGET = Services.REGISTRY.registerItem("abyssalnite_nugget",
            properties -> new Item(properties));


    public static final RegistryHandle<Item> ABYSSALNITE_SWORD = Services.REGISTRY.registerItem("abyssalnite_sword",
            properties -> new Item(properties.sword(ModToolMaterials.ABYSSALNITE, 3.0f, -2.4F)));

    public static final RegistryHandle<Item> ABYSSALNITE_PICKAXE = Services.REGISTRY.registerItem("abyssalnite_pickaxe",
            properties -> new Item(properties.pickaxe(ModToolMaterials.ABYSSALNITE, 1.0F, -2.8F)));

    public static final RegistryHandle<Item> ABYSSALNITE_AXE = Services.REGISTRY.registerItem("abyssalnite_axe",
            properties -> new AxeItem(ModToolMaterials.ABYSSALNITE, 5.0F, -3.0F, properties));

    public static final RegistryHandle<Item> ABYSSALNITE_SHOVEL = Services.REGISTRY.registerItem("abyssalnite_shovel",
            properties -> new ShovelItem(ModToolMaterials.ABYSSALNITE, 1.5F, -3.0F, properties));

    public static final RegistryHandle<Item> ABYSSALNITE_HOE = Services.REGISTRY.registerItem("abyssalnite_hoe",
            properties -> new HoeItem(ModToolMaterials.ABYSSALNITE, -3.0F, 0.0F, properties));

    public static final RegistryHandle<Item> ABYSSALNITE_SPEAR = Services.REGISTRY.registerItem("abyssalnite_spear",
            properties -> new Item(properties.spear(ModToolMaterials.ABYSSALNITE, 1.20F, 1.25F, 0.70F, 3.2F, 14.0F, 2.8F, 12.0F, 5.5F, 10.5F)));


    public static final RegistryHandle<Item> ABYSSALNITE_HELMET = Services.REGISTRY.registerItem("abyssalnite_helmet",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL, ArmorType.HELMET)));

    public static final RegistryHandle<Item> ABYSSALNITE_CHESTPLATE = Services.REGISTRY.registerItem("abyssalnite_chestplate",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL, ArmorType.CHESTPLATE)));

    public static final RegistryHandle<Item> ABYSSALNITE_LEGGINGS = Services.REGISTRY.registerItem("abyssalnite_leggings",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL, ArmorType.LEGGINGS)));

    public static final RegistryHandle<Item> ABYSSALNITE_BOOTS = Services.REGISTRY.registerItem("abyssalnite_boots",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL, ArmorType.BOOTS)));
}
