package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;

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
        }

        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
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