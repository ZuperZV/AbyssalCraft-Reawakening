package net.zuperzv.abyssalcraft_reawakening.init.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.zuperzv.abyssalcraft_reawakening.Constants;

import java.util.EnumMap;

public final class ModArmorMaterials {
    private ModArmorMaterials() {}

    public static final ResourceKey<EquipmentAsset> ABYSSALNITE_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("abyssalnite"));
    public static final ResourceKey<EquipmentAsset> REFINED_CORALIUM_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("refined_coralium"));
    public static final ResourceKey<EquipmentAsset> PLATED_CORALIUM_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("plated_coralium"));
    public static final ResourceKey<EquipmentAsset> OF_THE_DEPTHS_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("of_the_depths"));
    public static final ResourceKey<EquipmentAsset> DREADIUM_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("dreadium"));
    public static final ResourceKey<EquipmentAsset> DREADIUM_SAMURAI_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("dreadium_samurai"));
    public static final ResourceKey<EquipmentAsset> ETHAXIUM_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Constants.id("ethaxium"));

    public static final ArmorMaterial ABYSSALNITE_ARMOR_MATERIAL = new ArmorMaterial(
            37,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 3);
                map.put(ArmorType.LEGGINGS, 6);
                map.put(ArmorType.CHESTPLATE, 8);
                map.put(ArmorType.HELMET, 3);
                map.put(ArmorType.BODY, 6);
            }),
            13,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.0F,
            0.1F,
            ModItemTags.ABYSSALNITE_MATERIALS,
            ABYSSALNITE_ASSET
    );

    public static final ArmorMaterial REFINED_CORALIUM_ARMOR_MATERIAL = new ArmorMaterial(
            42,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 4);
                map.put(ArmorType.LEGGINGS, 7);
                map.put(ArmorType.CHESTPLATE, 9);
                map.put(ArmorType.HELMET, 4);
                map.put(ArmorType.BODY, 7);
            }),
            16,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.1F,
            0.2F,
            ModItemTags.REFINED_CORALIUM_MATERIALS,
            REFINED_CORALIUM_ASSET
    );

    public static final ArmorMaterial PLATED_CORALIUM_ARMOR_MATERIAL = new ArmorMaterial(
            55,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 5);
                map.put(ArmorType.LEGGINGS, 8);
                map.put(ArmorType.CHESTPLATE, 10);
                map.put(ArmorType.HELMET, 5);
                map.put(ArmorType.BODY, 8);
            }),
            18,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.3F,
            0.4F,
            ModItemTags.REFINED_CORALIUM_MATERIALS,
            PLATED_CORALIUM_ASSET
    );

    public static final ArmorMaterial OF_THE_DEPTHS_ARMOR_MATERIAL = new ArmorMaterial(
            61,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 6);
                map.put(ArmorType.LEGGINGS, 9);
                map.put(ArmorType.CHESTPLATE, 11);
                map.put(ArmorType.HELMET, 6);
                map.put(ArmorType.BODY, 9);
            }),
            13,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.3F,
            0.1F,
            ModItemTags.REFINED_CORALIUM_MATERIALS,
            OF_THE_DEPTHS_ASSET
    );

    public static final ArmorMaterial DREADIUM_ARMOR_MATERIAL = new ArmorMaterial(
            68,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 7);
                map.put(ArmorType.LEGGINGS, 10);
                map.put(ArmorType.CHESTPLATE, 12);
                map.put(ArmorType.HELMET, 7);
                map.put(ArmorType.BODY, 10);
            }),
            17,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.4F,
            0.3F,
            ModItemTags.DREADIUM_MATERIALS,
            DREADIUM_ASSET
    );

    public static final ArmorMaterial DREADIUM_SAMURAI_ARMOR_MATERIAL = new ArmorMaterial(
            53,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 8);
                map.put(ArmorType.LEGGINGS, 11);
                map.put(ArmorType.CHESTPLATE, 13);
                map.put(ArmorType.HELMET, 8);
                map.put(ArmorType.BODY, 11);
            }),
            13,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.5F,
            0.4F,
            ModItemTags.DREADIUM_MATERIALS,
            DREADIUM_SAMURAI_ASSET
    );

    public static final ArmorMaterial ETHAXIUM_ARMOR_MATERIAL = new ArmorMaterial(
            73,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 9);
                map.put(ArmorType.LEGGINGS, 12);
                map.put(ArmorType.CHESTPLATE, 15);
                map.put(ArmorType.HELMET, 9);
                map.put(ArmorType.BODY, 12);
            }),
            20,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            1.6F,
            0.5F,
            ModItemTags.ETHAXIUM_MATERIALS,
            ETHAXIUM_ASSET
    );
}
