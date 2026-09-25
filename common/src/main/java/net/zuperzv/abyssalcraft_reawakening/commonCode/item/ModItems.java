package net.zuperzv.abyssalcraft_reawakening.commonCode.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CodexTierData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.CoraliumGemItem;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.GatewayKeyItem;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.NecronomiconItem;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.StaffOfRendingItem;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemRegistry;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.ModDimensions;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;
import org.jspecify.annotations.NonNull;

public class ModItems {

    private ModItems() {}

    public static void load() {
        DataItemRegistry.getInstance().registerItems();
    }

    private static RegistryHandle<Item> simpleItem(String name) {
        return Services.REGISTRY.registerItem(name, Item::new);
    }

    // Misc

    public static final RegistryHandle<Item> SHOGGOTH_PROJECTILE =
            simpleItem("shoggoth_projectile");

    public static final RegistryHandle<Item> OBLIVION_CATALYST =
            simpleItem("oblivion_catalyst");

    public static final RegistryHandle<Item> STAFF_OF_THE_GATEKEEPER =
            simpleItem("staff_of_the_gatekeeper");

    public static final RegistryHandle<Item> POWERSTONE_TRACKER =
            simpleItem("powerstone_tracker");

    public static final RegistryHandle<Item> EYE_OF_THE_ABYSS =
            Services.REGISTRY.registerItem("eye_of_the_abyss",
                    properties -> new Item(properties.stacksTo(1)));

    public static final RegistryHandle<Item> DREADLANDS_INFUSED_GATEWAY_KEY =
            Services.REGISTRY.registerItem("dreadlands_infused_gateway_key",
                    properties -> new Item(properties.stacksTo(1)));

    public static final RegistryHandle<Item> CARBON_CLUSTER =
            simpleItem("carbon_cluster");

    public static final RegistryHandle<Item> DENSE_CARBON_CLUSTER =
            simpleItem("dense_carbon_cluster");

    public static final RegistryHandle<Item> NITRE =
            simpleItem("nitre");

    public static final RegistryHandle<Item> SULFUR_CHUNK =
            simpleItem("sulfur_chunk");

    public static final RegistryHandle<Item> OMOTHOL_FORGED_GATEWAY_KEY =
            Services.REGISTRY.registerItem("omothol_forged_gateway_key",
                    properties -> new Item(properties.stacksTo(1)));

    public static final RegistryHandle<Item> LIFE_CRYSTAL =
            simpleItem("life_crystal");

    // Necronomicon

    public static final RegistryHandle<Item> NECRONOMICON =
            Services.REGISTRY.registerItem("necronomicon",
                    properties -> new NecronomiconItem(
                            properties.stacksTo(1)
                                    .component(
                                            ModDataComponentTypes.CODEX_TIER.get(),
                                            new CodexTierData(1)
                                    )
                                    .component(
                                            DataComponents.DYED_COLOR,
                                            new DyedItemColor(0x643732)
                                    )
                    ));

    // Crystal Bags

    public static final RegistryHandle<Item> SMALL_CRYSTAL_BAG =
            simpleItem("small_crystal_bag");

    public static final RegistryHandle<Item> MEDIUM_CRYSTAL_BAG =
            simpleItem("medium_crystal_bag");

    public static final RegistryHandle<Item> LARGE_CRYSTAL_BAG =
            simpleItem("large_crystal_bag");

    public static final RegistryHandle<Item> HUGE_CRYSTAL_BAG =
            simpleItem("huge_crystal_bag");


    // Shadow

    public static final RegistryHandle<Item> SHADOW_FRAGMENT =
            simpleItem("shadow_fragment");

    public static final RegistryHandle<Item> SHADOW_SHARD =
            simpleItem("shadow_shard");

    public static final RegistryHandle<Item> SHADOW_GEM =
            simpleItem("shadow_gem");

    public static final RegistryHandle<Item> OBLIVION_SHARD =
            simpleItem("oblivion_shard");


    // Shoggoth Flesh

    public static final RegistryHandle<Item> OVERWORLD_SHOGGOTH_FLESH =
            simpleItem("overworld_shoggoth_flesh");

    public static final RegistryHandle<Item> ABYSSAL_SHOGGOTH_FLESH =
            simpleItem("abyssal_shoggoth_flesh");

    public static final RegistryHandle<Item> DREADED_SHOGGOTH_FLESH =
            simpleItem("dreaded_shoggoth_flesh");

    public static final RegistryHandle<Item> OMOTHOL_SHOGGOTH_FLESH =
            simpleItem("omothol_shoggoth_flesh");

    public static final RegistryHandle<Item> SHADOW_SHOGGOTH_FLESH =
            simpleItem("shadow_shoggoth_flesh");


    // Staff Of Rending

    public static final RegistryHandle<Item> STAFF_OF_RENDING =
            Services.REGISTRY.registerItem("staff_of_rending",
                    properties -> new StaffOfRendingItem(
                            properties.stacksTo(1),
                            30,
                            1000
                    ));

    public static final RegistryHandle<Item> ABYSSAL_WASTELAND_STAFF_OF_RENDING =
            Services.REGISTRY.registerItem("abyssal_wasteland_staff_of_rending",
                    properties -> new StaffOfRendingItem(
                            properties.stacksTo(1),
                            50,
                            3000
                    ));

    public static final RegistryHandle<Item> DREADLANDS_STAFF_OF_RENDING =
            Services.REGISTRY.registerItem("dreadlands_staff_of_rending",
                    properties -> new StaffOfRendingItem(
                            properties.stacksTo(1),
                            70,
                            5000
                    ));

    public static final RegistryHandle<Item> OMOTHOL_STAFF_OF_RENDING =
            Services.REGISTRY.registerItem("omothol_staff_of_rending",
                    properties -> new StaffOfRendingItem(
                            properties.stacksTo(1),
                            90,
                            7000
                    ));


    // Essence

    public static final RegistryHandle<Item> ABYSSAL_WASTELAND_ESSENCE =
            simpleItem("abyssal_wasteland_essence");

    public static final RegistryHandle<Item> DREADLANDS_ESSENCE =
            simpleItem("dreadlands_essence");

    public static final RegistryHandle<Item> OMOTHOL_ESSENCE =
            simpleItem("omothol_essence");


    // Skin Of

    public static final RegistryHandle<Item> SKIN_OF_THE_ABYSSAL_WASTELAND =
            simpleItem("skin_of_the_abyssal_wasteland");

    public static final RegistryHandle<Item> SKIN_OF_THE_DREADLANDS =
            simpleItem("skin_of_the_dreadlands");

    public static final RegistryHandle<Item> SKIN_OF_THE_OMOTHOL =
            simpleItem("skin_of_the_omothol");


    // Flesh

    public static final RegistryHandle<Item> CORALIUM_PLAGUED_FLESH =
            Services.REGISTRY.registerItem("coralium_plagued_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.CORALIUM_PLAGUED_FLESH)
                    ));

    public static final RegistryHandle<Item> GHOUL_FLESH =
            Services.REGISTRY.registerItem("ghoul_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.GHOUL_FLESH)
                    ));

    public static final RegistryHandle<Item> ABYSSAL_GHOUL_FLESH =
            Services.REGISTRY.registerItem("abyssal_ghoul_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.ABYSSAL_GHOUL_FLESH)
                    ));

    public static final RegistryHandle<Item> DREADED_GHOUL_FLESH =
            Services.REGISTRY.registerItem("dreaded_ghoul_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.DREADED_GHOUL_FLESH)
                    ));

    public static final RegistryHandle<Item> OMOTHOL_GHOUL_FLESH =
            Services.REGISTRY.registerItem("omothol_ghoul_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.OMOTHOL_GHOUL_FLESH)
                    ));

    public static final RegistryHandle<Item> SHADOW_GHOUL_FLESH =
            Services.REGISTRY.registerItem("shadow_ghoul_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.SHADOW_GHOUL_FLESH)
                    ));

    public static final RegistryHandle<Item> ANTI_GHOUL_FLESH =
            Services.REGISTRY.registerItem("anti_ghoul_flesh",
                    properties -> new Item(
                            properties.food(ModFoods.ANTI_GHOUL_FLESH)
                    ));


    // Abyssalnite

    public static final RegistryHandle<Item> GATEWAY_KEY =
            Services.REGISTRY.registerItem("gateway_key",
                    properties -> new GatewayKeyItem(
                            Level.OVERWORLD,
                            ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY,
                            properties.stacksTo(1)
                    ));

    public static final RegistryHandle<Item> ABYSSAL_ZOMBIE_SPAWN_EGG =
            Services.REGISTRY.registerItem("abyssal_zombie_spawn_egg",
                    properties -> new SpawnEggItem(
                            properties.spawnEgg(ModEntityTypes.ABYSSAL_ZOMBIE.get())
                    ));

    public static final RegistryHandle<Item> GROUNDLING_SPAWN_EGG =
            Services.REGISTRY.registerItem("groundling_spawn_egg",
                    properties -> new SpawnEggItem(
                            properties.spawnEgg(ModEntityTypes.GROUNDLING.get())
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_INGOT =
            simpleItem("abyssalnite_ingot");

    public static final RegistryHandle<Item> RAW_ABYSSALNITE =
            simpleItem("raw_abyssalnite");

    public static final RegistryHandle<Item> ABYSSALNITE_NUGGET =
            simpleItem("abyssalnite_nugget");


    public static final RegistryHandle<Item> ABYSSALNITE_SWORD =
            Services.REGISTRY.registerItem("abyssalnite_sword",
                    properties -> new Item(
                            properties.sword(
                                    ModToolMaterials.ABYSSALNITE,
                                    3.0f,
                                    -2.4F
                            )
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_PICKAXE =
            Services.REGISTRY.registerItem("abyssalnite_pickaxe",
                    properties -> new Item(
                            properties.pickaxe(
                                    ModToolMaterials.ABYSSALNITE,
                                    1.0F,
                                    -2.8F
                            )
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_AXE =
            Services.REGISTRY.registerItem("abyssalnite_axe",
                    properties -> new AxeItem(
                            ModToolMaterials.ABYSSALNITE,
                            5.0F,
                            -3.0F,
                            properties
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_SHOVEL =
            Services.REGISTRY.registerItem("abyssalnite_shovel",
                    properties -> new ShovelItem(
                            ModToolMaterials.ABYSSALNITE,
                            1.5F,
                            -3.0F,
                            properties
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_HOE =
            Services.REGISTRY.registerItem("abyssalnite_hoe",
                    properties -> new HoeItem(
                            ModToolMaterials.ABYSSALNITE,
                            -3.0F,
                            0.0F,
                            properties
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_SPEAR =
            Services.REGISTRY.registerItem("abyssalnite_spear",
                    properties -> new Item(
                            properties.spear(
                                    ModToolMaterials.ABYSSALNITE,
                                    1.20F,
                                    1.25F,
                                    0.70F,
                                    3.2F,
                                    14.0F,
                                    2.8F,
                                    12.0F,
                                    5.5F,
                                    10.5F
                            )
                    ));


    public static final RegistryHandle<Item> ABYSSALNITE_HELMET =
            Services.REGISTRY.registerItem("abyssalnite_helmet",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL,
                                    ArmorType.HELMET
                            )
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_CHESTPLATE =
            Services.REGISTRY.registerItem("abyssalnite_chestplate",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL,
                                    ArmorType.CHESTPLATE
                            )
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_LEGGINGS =
            Services.REGISTRY.registerItem("abyssalnite_leggings",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL,
                                    ArmorType.LEGGINGS
                            )
                    ));

    public static final RegistryHandle<Item> ABYSSALNITE_BOOTS =
            Services.REGISTRY.registerItem("abyssalnite_boots",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.ABYSSALNITE_ARMOR_MATERIAL,
                                    ArmorType.BOOTS
                            )
                    ));


    // Witherwood

    public static final RegistryHandle<Item> WITHERWOOD_SIGN =
            Services.REGISTRY.registerItem("witherwood_sign",
                    properties -> new SignItem(
                            ModBlocks.WITHERWOOD_SIGN.get(),
                            ModBlocks.WITHERWOOD_WALL_SIGN.get(),
                            properties.stacksTo(16)
                    ));

    public static final RegistryHandle<Item> WITHERWOOD_HANGING_SIGN =
            Services.REGISTRY.registerItem("witherwood_hanging_sign",
                    properties -> new HangingSignItem(
                            ModBlocks.WITHERWOOD_HANGING_SIGN.get(),
                            ModBlocks.WITHERWOOD_WALL_HANGING_SIGN.get(),
                            properties.stacksTo(16)
                    ));

    public static final RegistryHandle<Item> WITHERWOOD_BOAT =
            Services.REGISTRY.registerItem("witherwood_boat",
                    properties -> new BoatItem(
                            ModEntityTypes.WITHERWOOD_BOAT.get(),
                            properties.stacksTo(1)
                    ));

    public static final RegistryHandle<Item> WITHERWOOD_CHEST_BOAT =
            Services.REGISTRY.registerItem("witherwood_chest_boat",
                    properties -> new BoatItem(
                            ModEntityTypes.WITHERWOOD_CHEST_BOAT.get(),
                            properties.stacksTo(1)
                    ));


    // ========================================================================
    // Coralium
    // ========================================================================

    public static final RegistryHandle<Item> CORALIUM_GEM =
            Services.REGISTRY.registerItem("coralium_gem",
                    properties -> new CoraliumGemItem(properties));

    public static final RegistryHandle<Item> CHUNK_OF_CORALIUM =
            simpleItem("chunk_of_coralium");

    public static final RegistryHandle<Item> CORALIUM_PLATE =
            simpleItem("coralium_plate");

    public static final RegistryHandle<Item> CORALIUM_PEARL =
            simpleItem("coralium_pearl");

    public static final RegistryHandle<Item> TRANSMUTATION_GEM =
            Services.REGISTRY.registerItem("transmutation_gem",
                    properties -> Services.ITEM_FACTORY.createTransmutationGem(
                            properties
                                    .durability(10)
                                    .stacksTo(1)
                    ));

    public static final RegistryHandle<Item> CORALIUM_INGOT =
            simpleItem("coralium_ingot");

    public static final RegistryHandle<Item> RAW_CORALIUM =
            simpleItem("raw_coralium");

    public static final RegistryHandle<Item> CORALIUM_NUGGET =
            simpleItem("coralium_nugget");


    public static final RegistryHandle<Item> CORALIUM_SWORD =
            Services.REGISTRY.registerItem(
                    "coralium" + getSword(),
                    properties -> new Item(
                            properties.sword(
                                    ModToolMaterials.REFINED_CORALIUM,
                                    3.0f,
                                    -2.4F
                            )
                    ));

    private static @NonNull String getSword() {
        return "_sword";
    }

    public static final RegistryHandle<Item> CORALIUM_PICKAXE =
            Services.REGISTRY.registerItem("coralium_pickaxe",
                    properties -> new Item(
                            properties.pickaxe(
                                    ModToolMaterials.REFINED_CORALIUM,
                                    1.0F,
                                    -2.8F
                            )
                    ));

    public static final RegistryHandle<Item> CORALIUM_AXE =
            Services.REGISTRY.registerItem("coralium_axe",
                    properties -> new AxeItem(
                            ModToolMaterials.REFINED_CORALIUM,
                            5.0F,
                            -3.0F,
                            properties
                    ));

    public static final RegistryHandle<Item> CORALIUM_SHOVEL =
            Services.REGISTRY.registerItem("coralium_shovel",
                    properties -> new ShovelItem(
                            ModToolMaterials.REFINED_CORALIUM,
                            1.5F,
                            -3.0F,
                            properties
                    ));

    public static final RegistryHandle<Item> CORALIUM_HOE =
            Services.REGISTRY.registerItem("coralium_hoe",
                    properties -> new HoeItem(
                            ModToolMaterials.REFINED_CORALIUM,
                            -3.0F,
                            0.0F,
                            properties
                    ));

    public static final RegistryHandle<Item> CORALIUM_SPEAR =
            Services.REGISTRY.registerItem("coralium_spear",
                    properties -> new Item(
                            properties.spear(
                                    ModToolMaterials.REFINED_CORALIUM,
                                    1.20F,
                                    1.25F,
                                    0.70F,
                                    3.2F,
                                    14.0F,
                                    2.8F,
                                    12.0F,
                                    5.5F,
                                    10.5F
                            )
                    ));


    public static final RegistryHandle<Item> CORALIUM_HELMET =
            Services.REGISTRY.registerItem("coralium_helmet",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.REFINED_CORALIUM_ARMOR_MATERIAL,
                                    ArmorType.HELMET
                            )
                    ));

    public static final RegistryHandle<Item> CORALIUM_CHESTPLATE =
            Services.REGISTRY.registerItem("coralium_chestplate",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.REFINED_CORALIUM_ARMOR_MATERIAL,
                                    ArmorType.CHESTPLATE
                            )
                    ));

    public static final RegistryHandle<Item> CORALIUM_LEGGINGS =
            Services.REGISTRY.registerItem("coralium_leggings",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.REFINED_CORALIUM_ARMOR_MATERIAL,
                                    ArmorType.LEGGINGS
                            )
                    ));

    public static final RegistryHandle<Item> CORALIUM_BOOTS =
            Services.REGISTRY.registerItem("coralium_boots",
                    properties -> new Item(
                            properties.humanoidArmor(
                                    ModArmorMaterials.REFINED_CORALIUM_ARMOR_MATERIAL,
                                    ArmorType.BOOTS
                            )
                    ));


    // Plated Coralium

    public static final RegistryHandle<Item> PLATED_CORALIUM_HELMET =
            simpleItem("plated_coralium_helmet");

    public static final RegistryHandle<Item> PLATED_CORALIUM_CHESTPLATE =
            simpleItem("plated_coralium_chestplate");

    public static final RegistryHandle<Item> PLATED_CORALIUM_LEGGINGS =
            simpleItem("plated_coralium_leggings");

    public static final RegistryHandle<Item> PLATED_CORALIUM_BOOTS =
            simpleItem("plated_coralium_boots");


    // Dread

    public static final RegistryHandle<Item> DREAD_FRAGMENT =
            simpleItem("dread_fragment");

    public static final RegistryHandle<Item> DREADED_SHARD_OF_ABYSSALNITE =
            simpleItem("dreaded_shard_of_abyssalnite");

    public static final RegistryHandle<Item> DREADIUM_INGOT =
            simpleItem("dreadium_ingot");

    public static final RegistryHandle<Item> DREADIUM_NUGGET =
            simpleItem("dreadium_nugget");

    public static final RegistryHandle<Item> DREAD_CLOTH =
            simpleItem("dread_cloth");

    public static final RegistryHandle<Item> DREADIUM_PLATE =
            simpleItem("dreadium_plate");

    public static final RegistryHandle<Item> DREADIUM_KATANA_BLADE =
            simpleItem("dreadium_katana_blade");

    public static final RegistryHandle<Item> DREADIUM_KATANA_HILT =
            simpleItem("dreadium_katana_hilt");

    public static final RegistryHandle<Item> DREADIUM_KATANA =
            simpleItem("dreadium_katana");

    public static final RegistryHandle<Item> DREAD_PLAGUED_GATEWAY_KEY =
            Services.REGISTRY.registerItem("dread_plagued_gateway_key",
                    properties -> new Item(properties.stacksTo(1)));

    public static final RegistryHandle<Item> CHARCOAL =
            simpleItem("charcoal");


    // Dread Tools

    public static final RegistryHandle<Item> DREADIUM_SWORD =
            simpleItem("dreadium_sword");

    public static final RegistryHandle<Item> DREADIUM_PICKAXE =
            simpleItem("dreadium_pickaxe");

    public static final RegistryHandle<Item> DREADIUM_AXE =
            simpleItem("dreadium_axe");

    public static final RegistryHandle<Item> DREADIUM_SHOVEL =
            simpleItem("dreadium_shovel");

    public static final RegistryHandle<Item> DREADIUM_HOE =
            simpleItem("dreadium_hoe");


    // Dread Armor

    public static final RegistryHandle<Item> DREADIUM_HELMET =
            simpleItem("dreadium_helmet");

    public static final RegistryHandle<Item> DREADIUM_CHESTPLATE =
            simpleItem("dreadium_chestplate");

    public static final RegistryHandle<Item> DREADIUM_LEGGINGS =
            simpleItem("dreadium_leggings");

    public static final RegistryHandle<Item> DREADIUM_BOOTS =
            simpleItem("dreadium_boots");


    // Samurai Armor

    public static final RegistryHandle<Item> DREADIUM_SAMURAI_HELMET =
            simpleItem("dreadium_samurai_helmet");

    public static final RegistryHandle<Item> DREADIUM_SAMURAI_CHESTPLATE =
            simpleItem("dreadium_samurai_chestplate");

    public static final RegistryHandle<Item> DREADIUM_SAMURAI_LEGGINGS =
            simpleItem("dreadium_samurai_leggings");

    public static final RegistryHandle<Item> DREADIUM_SAMURAI_BOOTS =
            simpleItem("dreadium_samurai_boots");


    // Ethaxium

    public static final RegistryHandle<Item> ETHAXIUM_INGOT =
            simpleItem("ethaxium_ingot");

    public static final RegistryHandle<Item> ETHAXIUM_NUGGET =
            simpleItem("ethaxium_nugget");

    public static final RegistryHandle<Item> ELDRITCH_SCALE =
            simpleItem("eldritch_scale");


    public static final RegistryHandle<Item> ETHAXIUM_PICKAXE =
            simpleItem("ethaxium_pickaxe");

    public static final RegistryHandle<Item> ETHAXIUM_AXE =
            simpleItem("ethaxium_axe");

    public static final RegistryHandle<Item> ETHAXIUM_SHOVEL =
            simpleItem("ethaxium_shovel");

    public static final RegistryHandle<Item> ETHAXIUM_SWORD =
            simpleItem("ethaxium_sword");

    public static final RegistryHandle<Item> ETHAXIUM_HOE =
            simpleItem("ethaxium_hoe");


    public static final RegistryHandle<Item> ETHAXIUM_HELMET =
            simpleItem("ethaxium_helmet");

    public static final RegistryHandle<Item> ETHAXIUM_CHESTPLATE =
            simpleItem("ethaxium_chestplate");

    public static final RegistryHandle<Item> ETHAXIUM_LEGGINGS =
            simpleItem("ethaxium_leggings");

    public static final RegistryHandle<Item> ETHAXIUM_BOOTS =
            simpleItem("ethaxium_boots");


    // Anti Items

    public static final RegistryHandle<Item> ANTI_BEEF =
            simpleItem("anti_beef");

    public static final RegistryHandle<Item> ANTI_CHICKEN =
            simpleItem("anti_chicken");

    public static final RegistryHandle<Item> ANTI_PORK =
            simpleItem("anti_pork");

    public static final RegistryHandle<Item> ROTTEN_ANTI_FLESH =
            simpleItem("rotten_anti_flesh");

    public static final RegistryHandle<Item> ANTI_BONE =
            simpleItem("anti_bone");

    public static final RegistryHandle<Item> ANTI_SPIDER_EYE =
            simpleItem("anti_spider_eye");

    public static final RegistryHandle<Item> ANTI_PLAGUED_FLESH =
            simpleItem("anti_plagued_flesh");


    // Crystals

    public static final RegistryHandle<Item> CRYSTAL_IRON =
            simpleItem("iron_crystal");

    public static final RegistryHandle<Item> CRYSTAL_GOLD =
            simpleItem("gold_crystal");

    public static final RegistryHandle<Item> CRYSTAL_SULFUR =
            simpleItem("sulfur_crystal");

    public static final RegistryHandle<Item> CRYSTAL_CARBON =
            simpleItem("carbon_crystal");

    public static final RegistryHandle<Item> CRYSTAL_OXYGEN =
            simpleItem("oxygen_crystal");

    public static final RegistryHandle<Item> CRYSTAL_HYDROGEN =
            simpleItem("hydrogen_crystal");

    public static final RegistryHandle<Item> CRYSTAL_NITROGEN =
            simpleItem("nitrogen_crystal");

    public static final RegistryHandle<Item> CRYSTAL_PHOSPHORUS =
            simpleItem("phosphorus_crystal");

    public static final RegistryHandle<Item> CRYSTAL_POTASSIUM =
            simpleItem("potassium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_NITRATE =
            simpleItem("nitrate_crystal");

    public static final RegistryHandle<Item> CRYSTAL_METHANE =
            simpleItem("methane_crystal");

    public static final RegistryHandle<Item> CRYSTAL_REDSTONE =
            simpleItem("redstone_crystal");

    public static final RegistryHandle<Item> CRYSTAL_ABYSSALNITE =
            simpleItem("abyssalnite_crystal");

    public static final RegistryHandle<Item> CRYSTAL_CORALIUM =
            simpleItem("coralium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_DREADIUM =
            simpleItem("dreadium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_BLAZE =
            simpleItem("blaze_crystal");

    public static final RegistryHandle<Item> CRYSTAL_SILICON =
            simpleItem("silicon_crystal");

    public static final RegistryHandle<Item> CRYSTAL_MAGNESIUM =
            simpleItem("magnesium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_ALUMINIUM =
            simpleItem("aluminium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_SILICA =
            simpleItem("silica_crystal");

    public static final RegistryHandle<Item> CRYSTAL_ALUMINA =
            simpleItem("alumina_crystal");

    public static final RegistryHandle<Item> CRYSTAL_MAGNESIA =
            simpleItem("magnesia_crystal");

    public static final RegistryHandle<Item> CRYSTAL_ZINC =
            simpleItem("zinc_crystal");

    public static final RegistryHandle<Item> CRYSTAL_CALCIUM =
            simpleItem("calcium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_BERYLLIUM =
            simpleItem("beryllium_crystal");

    public static final RegistryHandle<Item> CRYSTAL_BERYL =
            simpleItem("beryl_crystal");


    // Crystal Shards

    public static final RegistryHandle<Item> CRYSTAL_SHARD_IRON =
            simpleItem("iron_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_GOLD =
            simpleItem("gold_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_SULFUR =
            simpleItem("sulfur_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_CARBON =
            simpleItem("carbon_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_OXYGEN =
            simpleItem("oxygen_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_HYDROGEN =
            simpleItem("hydrogen_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_NITROGEN =
            simpleItem("nitrogen_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_PHOSPHORUS =
            simpleItem("phosphorus_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_POTASSIUM =
            simpleItem("potassium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_NITRATE =
            simpleItem("nitrate_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_METHANE =
            simpleItem("methane_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_REDSTONE =
            simpleItem("redstone_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_ABYSSALNITE =
            simpleItem("abyssalnite_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_CORALIUM =
            simpleItem("coralium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_DREADIUM =
            simpleItem("dreadium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_BLAZE =
            simpleItem("blaze_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_SILICON =
            simpleItem("silicon_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_MAGNESIUM =
            simpleItem("magnesium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_ALUMINIUM =
            simpleItem("aluminium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_SILICA =
            simpleItem("silica_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_ALUMINA =
            simpleItem("alumina_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_MAGNESIA =
            simpleItem("magnesia_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_ZINC =
            simpleItem("zinc_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_CALCIUM =
            simpleItem("calcium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_BERYLLIUM =
            simpleItem("beryllium_crystal_shard");

    public static final RegistryHandle<Item> CRYSTAL_SHARD_BERYL =
            simpleItem("beryl_crystal_shard");


    // Crystal Fragments

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_IRON =
            simpleItem("iron_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_GOLD =
            simpleItem("gold_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_SULFUR =
            simpleItem("sulfur_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_CARBON =
            simpleItem("carbon_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_OXYGEN =
            simpleItem("oxygen_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_HYDROGEN =
            simpleItem("hydrogen_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_NITROGEN =
            simpleItem("nitrogen_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_PHOSPHORUS =
            simpleItem("phosphorus_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_POTASSIUM =
            simpleItem("potassium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_NITRATE =
            simpleItem("nitrate_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_METHANE =
            simpleItem("methane_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_REDSTONE =
            simpleItem("redstone_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_ABYSSALNITE =
            simpleItem("abyssalnite_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_CORALIUM =
            simpleItem("coralium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_DREADIUM =
            simpleItem("dreadium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_BLAZE =
            simpleItem("blaze_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_SILICON =
            simpleItem("silicon_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_MAGNESIUM =
            simpleItem("magnesium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_ALUMINIUM =
            simpleItem("aluminium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_SILICA =
            simpleItem("silica_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_ALUMINA =
            simpleItem("alumina_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_MAGNESIA =
            simpleItem("magnesia_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_ZINC =
            simpleItem("zinc_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_CALCIUM =
            simpleItem("calcium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_BERYLLIUM =
            simpleItem("beryllium_crystal_fragment");

    public static final RegistryHandle<Item> CRYSTAL_FRAGMENT_BERYL =
            simpleItem("beryl_crystal_fragment");


    // Tools / Weapons

    public static final RegistryHandle<Item> CORALIUM_LONGBOW =
            simpleItem("coralium_longbow");

    public static final RegistryHandle<Item> SOUL_HARVESTING_BLADE =
            simpleItem("soul_harvesting_blade");


    // Food

    public static final RegistryHandle<Item> GENERIC_MEAT =
            simpleItem("generic_meat");

    public static final RegistryHandle<Item> COOKED_GENERIC_MEAT =
            simpleItem("cooked_generic_meat");


    // Coins

    public static final RegistryHandle<Item> COIN =
            simpleItem("coin");

    public static final RegistryHandle<Item> TOKEN_OF_JZAHAR =
            simpleItem("token_of_jzahar");


    // Charms

    public static final RegistryHandle<Item> RITUAL_CHARM =
            simpleItem("ritual_charm");

    public static final RegistryHandle<Item> RANGE_RITUAL_CHARM =
            simpleItem("range_ritual_charm");

    public static final RegistryHandle<Item> DURATION_RITUAL_CHARM =
            simpleItem("duration_ritual_charm");

    public static final RegistryHandle<Item> POWER_RITUAL_CHARM =
            simpleItem("power_ritual_charm");


    public static final RegistryHandle<Item> CTHULHU_CHARM =
            simpleItem("cthulhu_charm");

    public static final RegistryHandle<Item> RANGE_CTHULHU_CHARM =
            simpleItem("range_cthulhu_charm");

    public static final RegistryHandle<Item> DURATION_CTHULHU_CHARM =
            simpleItem("duration_cthulhu_charm");

    public static final RegistryHandle<Item> POWER_CTHULHU_CHARM =
            simpleItem("power_cthulhu_charm");


    public static final RegistryHandle<Item> HASTUR_CHARM =
            simpleItem("hastur_charm");

    public static final RegistryHandle<Item> RANGE_HASTUR_CHARM =
            simpleItem("range_hastur_charm");

    public static final RegistryHandle<Item> DURATION_HASTUR_CHARM =
            simpleItem("duration_hastur_charm");

    public static final RegistryHandle<Item> POWER_HASTUR_CHARM =
            simpleItem("power_hastur_charm");


    public static final RegistryHandle<Item> JZAHAR_CHARM =
            simpleItem("jzahar_charm");

    public static final RegistryHandle<Item> RANGE_JZAHAR_CHARM =
            simpleItem("range_jzahar_charm");

    public static final RegistryHandle<Item> DURATION_JZAHAR_CHARM =
            simpleItem("duration_jzahar_charm");

    public static final RegistryHandle<Item> POWER_JZAHAR_CHARM =
            simpleItem("power_jzahar_charm");


    public static final RegistryHandle<Item> AZATHOTH_CHARM =
            simpleItem("azathoth_charm");

    public static final RegistryHandle<Item> RANGE_AZATHOTH_CHARM =
            simpleItem("range_azathoth_charm");

    public static final RegistryHandle<Item> DURATION_AZATHOTH_CHARM =
            simpleItem("duration_azathoth_charm");

    public static final RegistryHandle<Item> POWER_AZATHOTH_CHARM =
            simpleItem("power_azathoth_charm");


    public static final RegistryHandle<Item> NYARLATHOTEP_CHARM =
            simpleItem("nyarlathotep_charm");

    public static final RegistryHandle<Item> RANGE_NYARLATHOTEP_CHARM =
            simpleItem("range_nyarlathotep_charm");

    public static final RegistryHandle<Item> DURATION_NYARLATHOTEP_CHARM =
            simpleItem("duration_nyarlathotep_charm");

    public static final RegistryHandle<Item> POWER_NYARLATHOTEP_CHARM =
            simpleItem("power_nyarlathotep_charm");


    public static final RegistryHandle<Item> YOG_SOTHOTH_CHARM =
            simpleItem("yog_sothoth_charm");

    public static final RegistryHandle<Item> RANGE_YOG_SOTHOTH_CHARM =
            simpleItem("range_yog_sothoth_charm");

    public static final RegistryHandle<Item> DURATION_YOG_SOTHOTH_CHARM =
            simpleItem("duration_yog_sothoth_charm");

    public static final RegistryHandle<Item> POWER_YOG_SOTHOTH_CHARM =
            simpleItem("power_yog_sothoth_charm");


    public static final RegistryHandle<Item> SHUB_NIGGURATH_CHARM =
            simpleItem("shub_niggurath_charm");

    public static final RegistryHandle<Item> RANGE_SHUB_NIGGURATH_CHARM =
            simpleItem("range_shub_niggurath_charm");

    public static final RegistryHandle<Item> DURATION_SHUB_NIGGURATH_CHARM =
            simpleItem("duration_shub_niggurath_charm");

    public static final RegistryHandle<Item> POWER_SHUB_NIGGURATH_CHARM =
            simpleItem("power_shub_niggurath_charm");


    // Misc Items

    public static final RegistryHandle<Item> ESSENCE_OF_THE_GATEKEEPER =
            simpleItem("essence_of_the_gatekeeper");

    public static final RegistryHandle<Item> INTERDIMENSIONAL_CAGE =
            simpleItem("interdimensional_cage");

    public static final RegistryHandle<Item> STONE_TABLET =
            simpleItem("stone_tablet");

    public static final RegistryHandle<Item> SPIRIT_TABLET =
            simpleItem("spirit_tablet");

    public static final RegistryHandle<Item> SPIRIT_TABLET_SHARD =
            simpleItem("spirit_tablet_shard");

    public static final RegistryHandle<Item> SILVER_KEY =
            Services.REGISTRY.registerItem("silver_key",
                    properties -> new Item(properties.stacksTo(1)));

    public static final RegistryHandle<Item> BOOK_OF_MANY_FACES =
            simpleItem("book_of_many_faces");

    public static final RegistryHandle<Item> LOST_PAGE =
            simpleItem("lost_page");

    public static final RegistryHandle<Item> SCRIPTURES_OF_OMNISCIENCE =
            simpleItem("scriptures_of_omniscience");

    public static final RegistryHandle<Item> SEALING_KEY =
            Services.REGISTRY.registerItem("sealing_key",
                    properties -> new Item(properties.stacksTo(1)));


    // Scrolls

    public static final RegistryHandle<Item> BASIC_SCROLL =
            simpleItem("basic_scroll");

    public static final RegistryHandle<Item> LESSER_SCROLL =
            simpleItem("lesser_scroll");

    public static final RegistryHandle<Item> MODERATE_SCROLL =
            simpleItem("moderate_scroll");

    public static final RegistryHandle<Item> GREATER_SCROLL =
            simpleItem("greater_scroll");

    public static final RegistryHandle<Item> ANTIMATTER_SCROLL =
            simpleItem("antimatter_scroll");

    public static final RegistryHandle<Item> OBLIVION_SCROLL =
            simpleItem("oblivion_scroll");


    // Antidotes

    public static final RegistryHandle<Item> CORALIUM_PLAGUE_ANTIDOTE =
            simpleItem("coralium_plague_antidote");

    public static final RegistryHandle<Item> DREAD_PLAGUE_ANTIDOTE =
            simpleItem("dread_plague_antidote");


    // Doors

    public static final RegistryHandle<Item> DARKLANDS_OAK_DOOR =
            simpleItem("darklands_oak_door");

    public static final RegistryHandle<Item> DREADLANDS_DOOR =
            simpleItem("dreadlands_door");


    // Depths Armor

    public static final RegistryHandle<Item> DEPTHS_HELMET =
            simpleItem("depths_helmet");

    public static final RegistryHandle<Item> DEPTHS_CHESTPLATE =
            simpleItem("depths_chestplate");

    public static final RegistryHandle<Item> DEPTHS_LEGGINGS =
            simpleItem("depths_leggings");

    public static final RegistryHandle<Item> DEPTHS_BOOTS =
            simpleItem("depths_boots");


    // Rings

    public static final RegistryHandle<Item> RING =
            simpleItem("ring");

    public static final RegistryHandle<Item> RING_OVERWORLD =
            simpleItem("ring_overworld");

    public static final RegistryHandle<Item> RING_ABYSSAL_WASTELAND =
            simpleItem("ring_abyssal_wasteland");

    public static final RegistryHandle<Item> RING_DREADLANDS =
            simpleItem("ring_dreadlands");

    public static final RegistryHandle<Item> RING_OMOTHOL =
            simpleItem("ring_omothol");


    // Rendering / Recipe

    public static final RegistryHandle<Item> POTENTIAL_ENERGY =
            simpleItem("potential_energy");

    public static final RegistryHandle<Item> RECIPE_ITEM =
            simpleItem("recipe_item");
}