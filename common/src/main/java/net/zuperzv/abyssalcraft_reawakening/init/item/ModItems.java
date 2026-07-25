package net.zuperzv.abyssalcraft_reawakening.init.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorType;
import net.zuperzv.abyssalcraft_reawakening.init.component.CodexTierData;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.NecronomiconItem;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.StaffOfRendingItem;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModItems {
    private ModItems() {}

    public static void load() {}

    public static final RegistryHandle<Item> NECRONOMICON = Services.REGISTRY.registerItem("necronomicon",
            properties -> new NecronomiconItem(properties.stacksTo(1)
                    .component(ModDataComponentTypes.CODEX_TIER.get(), new CodexTierData(1))
                    .component(DataComponents.DYED_COLOR, new DyedItemColor(0x643732))));


    //Shadow items
    public static final RegistryHandle<Item> SHADOW_FRAGMENT = Services.REGISTRY.registerItem("shadow_fragment",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> SHADOW_SHARD = Services.REGISTRY.registerItem("shadow_shard",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> SHADOW_GEM = Services.REGISTRY.registerItem("shadow_gem",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> OBLIVION_SHARD = Services.REGISTRY.registerItem("oblivion_shard",
            properties -> new Item(properties));


    //Staff Of Rending
    public static final RegistryHandle<Item> STAFF_OF_RENDING = Services.REGISTRY.registerItem("staff_of_rending",
            properties -> new StaffOfRendingItem(properties.stacksTo(1), 30, 1000));

    public static final RegistryHandle<Item> ABYSSAL_WASTELAND_STAFF_OF_RENDING = Services.REGISTRY.registerItem("abyssal_wasteland_staff_of_rending",
            properties -> new StaffOfRendingItem(properties.stacksTo(1), 50, 3000));

    public static final RegistryHandle<Item> DREADLANDS_STAFF_OF_RENDING = Services.REGISTRY.registerItem("dreadlands_staff_of_rending",
            properties -> new StaffOfRendingItem(properties.stacksTo(1), 70, 5000));

    public static final RegistryHandle<Item> OMOTHOL_STAFF_OF_RENDING = Services.REGISTRY.registerItem("omothol_staff_of_rending",
            properties -> new StaffOfRendingItem(properties.stacksTo(1), 90, 7000));


    //Essence
    public static final RegistryHandle<Item> ABYSSAL_WASTELAND_ESSENCE = Services.REGISTRY.registerItem("abyssal_wasteland_essence",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> DREADLANDS_ESSENCE = Services.REGISTRY.registerItem("dreadlands_essence",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> OMOTHOL_ESSENCE = Services.REGISTRY.registerItem("omothol_essence",
            properties -> new Item(properties));


    //Skin Of
    public static final RegistryHandle<Item> SKIN_OF_THE_ABYSSAL_WASTELAND = Services.REGISTRY.registerItem("skin_of_the_abyssal_wasteland",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> SKIN_OF_THE_DREADLANDS = Services.REGISTRY.registerItem("skin_of_the_dreadlands",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> SKIN_OF_THE_OMOTHOL = Services.REGISTRY.registerItem("skin_of_the_omothol",
            properties -> new Item(properties));


    //Flesh
    public static final RegistryHandle<Item> CORALIUM_PLAGUED_FLESH = Services.REGISTRY.registerItem("coralium_plagued_flesh",
            properties -> new Item(properties.food(ModFoods.CORALIUM_PLAGUED_FLESH)));

        //Ghoul Flesh
    public static final RegistryHandle<Item> GHOUL_FLESH = Services.REGISTRY.registerItem("ghoul_flesh",
            properties -> new Item(properties.food(ModFoods.GHOUL_FLESH)));

    public static final RegistryHandle<Item> ABYSSAL_GHOUL_FLESH = Services.REGISTRY.registerItem("abyssal_ghoul_flesh",
            properties -> new Item(properties.food(ModFoods.ABYSSAL_GHOUL_FLESH)));

    public static final RegistryHandle<Item> DREADED_GHOUL_FLESH = Services.REGISTRY.registerItem("dreaded_ghoul_flesh",
            properties -> new Item(properties.food(ModFoods.DREADED_GHOUL_FLESH)));

    public static final RegistryHandle<Item> OMOTHOL_GHOUL_FLESH = Services.REGISTRY.registerItem("omothol_ghoul_flesh",
            properties -> new Item(properties.food(ModFoods.OMOTHOL_GHOUL_FLESH)));

    public static final RegistryHandle<Item> SHADOW_GHOUL_FLESH = Services.REGISTRY.registerItem("shadow_ghoul_flesh",
            properties -> new Item(properties.food(ModFoods.SHADOW_GHOUL_FLESH)));

    public static final RegistryHandle<Item> ANTI_GHOUL_FLESH = Services.REGISTRY.registerItem("anti_ghoul_flesh",
            properties -> new Item(properties.food(ModFoods.ANTI_GHOUL_FLESH)));


    //Abyssalnite
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


    //Coralium
    public static final RegistryHandle<Item> TRANSMUTATION_GEM = Services.REGISTRY.registerItem("transmutation_gem",
            properties -> new Item(properties));

    public static final RegistryHandle<Item> CORALIUM_PEARL = Services.REGISTRY.registerItem("coralium_pearl",
            properties -> new Item(properties));


    //Rendering Items
    public static final RegistryHandle<Item> POTENTIAL_ENERGY = Services.REGISTRY.registerItem("potential_energy",
            properties -> new Item(properties));
}
