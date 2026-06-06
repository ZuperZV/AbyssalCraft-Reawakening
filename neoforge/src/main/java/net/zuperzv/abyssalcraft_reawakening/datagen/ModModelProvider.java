package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.block.Block;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.ModArmorMaterials;
import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

import java.util.HashSet;
import java.util.Set;


public class ModModelProvider extends ModelProvider {
    private final Set<Item> generatedItems = new HashSet<>();
    private final Set<Block> generatedBlocks = new HashSet<>();


    public ModModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        generateCubeBlock(blockModels, ModBlocks.ABYSSALNITE_BLOCK.block().get());

        for (Item item : getModItems()) {
            if (!(item instanceof BlockItem) && !generatedItems.contains(item)) {
                generateItem(itemModels, item);
            }
        }

        for (Block block : getModBlocks()) {
            if (!generatedBlocks.contains(block)) {
                generateCubeBlock(blockModels, block);
            }
        }
    }

    private void generateItem(ItemModelGenerators itemModels, Item item) {
        generatedItems.add(item);

        if (item.getDescriptionId().toLowerCase().contains("spear")) {
            itemModels.generateSpear(item);
            return;
        } else if (isTool(item)) {
            itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
            return;
        } else if (generateTrimmableItem(itemModels, item, false)) {
            return;
        }

        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private void generateTrimmableItem(ItemModelGenerators itemModels, Item item, ResourceKey<EquipmentAsset> equipmentAssetId, Identifier slotTrimPrefix, boolean hasDyedLayer) {
        generatedItems.add(item);
        itemModels.generateTrimmableItem(item, equipmentAssetId, slotTrimPrefix, hasDyedLayer);
    }

    private boolean generateTrimmableItem(ItemModelGenerators itemModels, Item item, boolean hasDyedLayer) {
        ResourceKey<EquipmentAsset> equipmentAssetId = getEquipmentAssetForItem(item);
        if (equipmentAssetId == null) return false;

        if (item.getDescriptionId().toLowerCase().contains("helmet")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_HELMET, hasDyedLayer);
            return true;
        } else if  (item.getDescriptionId().toLowerCase().contains("chestplate")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, hasDyedLayer);
            return true;
        } else if  (item.getDescriptionId().toLowerCase().contains("leggings")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, hasDyedLayer);
            return true;
        } else if  (item.getDescriptionId().toLowerCase().contains("boots")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_BOOTS, hasDyedLayer);
            return true;
        }
        return false;
    }

    private ResourceKey<EquipmentAsset> getEquipmentAssetForItem(Item item) {
        String id = item.getDescriptionId().toLowerCase();

        if (id.contains("abyssalnite")) {
            return ModArmorMaterials.ABYSSALNITE_ASSET;
        } else if (id.contains("refined_coralium")) {
            return ModArmorMaterials.REFINED_CORALIUM_ASSET;
        } else if (id.contains("plated_coralium")) {
            return ModArmorMaterials.PLATED_CORALIUM_ASSET;
        } else if (id.contains("of_the_depths")) {
            return ModArmorMaterials.OF_THE_DEPTHS_ASSET;
        } else if (id.contains("dreadium_samurai")) {
            return ModArmorMaterials.DREADIUM_SAMURAI_ASSET;
        } else if (id.contains("dreadium")) {
            return ModArmorMaterials.DREADIUM_ASSET;
        } else if (id.contains("ethaxium")) {
            return ModArmorMaterials.ETHAXIUM_ASSET;
        }
        return null;
    }

    private boolean isTool(Item item) {
        return item.getDescriptionId().toLowerCase().contains("sword")
                || item.getDescriptionId().toLowerCase().contains("pickaxe")
                || item instanceof AxeItem
                || item instanceof ShovelItem
                || item instanceof HoeItem;
    }

    private void generateCubeBlock(BlockModelGenerators blockModels, Block block) {
        generatedBlocks.add(block);
        blockModels.createTrivialCube(block);
    }

    private Iterable<Block> getModBlocks() {
        return NeoForgeRegistryHelper.BLOCKS.getEntries()
                .stream()
                .map(entry -> (Block) entry.value())
                .toList();
    }

    private Iterable<Item> getModItems() {
        return NeoForgeRegistryHelper.ITEMS.getEntries()
                .stream()
                .map(entry -> (Item) entry.value())
                .toList();
    }
}