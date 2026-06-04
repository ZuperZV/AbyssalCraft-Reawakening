package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.ModItems;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.ABYSSALNITE_INGOT.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(ModBlocks.ABYSSALNITE_BLOCK.block().get());
    }
}