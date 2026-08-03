package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlockTags;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(ModBlockTags.ABYSSALCRAFT_ORES)
                .add(ModBlocks.ABYSSALNITE_ORE.block().get())
                .add(ModBlocks.ABYSSALNITE_OVERWORLD_ORE.block().get())
                .add(ModBlocks.ABYSSALNITE_DEEPSLATE_ORE.block().get())
                .add(ModBlocks.ABYSSALNITE_NETHER_ORE.block().get())
                .add(ModBlocks.ABYSSALNITE_END_ORE.block().get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ABYSSALNITE_BLOCK.block().get())
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(Tags.Blocks.ORES)
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(ModBlockTags.ABYSSAL_WAISTLAND_SURFACES)
                .add(ModBlocks.ABYSSAL_STONE.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.WASTITE.block().get())
                .add(Blocks.MUD);

        tag(ModBlockTags.WASTITE_SPIKE_REPLACEABLE)
                .add(ModBlocks.WASTITE.block().get())
                .addTag(ModBlockTags.ABYSSAL_WAISTLAND_SURFACES);

        tag(ModBlockTags.ABYSSAL_STONE_ORE_REPLACEABLES)
                .add(ModBlocks.ABYSSAL_STONE.block().get());

        tag(ModBlockTags.ABYSSAL_DEEPSLATE_ORE_REPLACEABLES)
                .add(ModBlocks.ABYSSAL_STONE.block().get());


        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ABYSSALNITE_BLOCK.block().get())
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(ModBlockTags.NEEDS_ABYSSALNITE_TOOL)
                .add(ModBlocks.REFINED_CORALIUM_BLOCK.block().get());

        tag(ModBlockTags.NEEDS_REFINED_CORALIUM_TOOL)
                .add(ModBlocks.DREADIUM_BLOCK.block().get());

        tag(ModBlockTags.NEEDS_DREADIUM_TOOL)
                .add(ModBlocks.ETHAXIUM_BLOCK.block().get());

        tag(ModBlockTags.NEEDS_ETHAXIUM_TOOL)
                .add(ModBlocks.ETHAXIUM_ORE.block().get());

        /*tag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(ModBlockTags.NEEDS_ABYSSALNITE_TOOL)
                .addTag(ModBlockTags.NEEDS_REFINED_CORALIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_DREADIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_ETHAXIUM_TOOL);
         */

        tag(ModBlockTags.INCORRECT_FOR_ABYSSALNITE_TOOL)
                .addTag(ModBlockTags.NEEDS_REFINED_CORALIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_DREADIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_ETHAXIUM_TOOL);

        tag(ModBlockTags.INCORRECT_FOR_REFINED_CORALIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_DREADIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_ETHAXIUM_TOOL);

        tag(ModBlockTags.INCORRECT_FOR_DREADIUM_TOOL)
                .addTag(ModBlockTags.NEEDS_ETHAXIUM_TOOL);

        tag(ModBlockTags.INCORRECT_FOR_ETHAXIUM_TOOL);
    }
}