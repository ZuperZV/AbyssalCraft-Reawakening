package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
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
                .add(ModBlocks.ABYSSALNITE_ORE.block().get())
                .add(ModBlocks.ABYSSAL_STONE.block().get())
                .add(ModBlocks.ABYSSAL_COBBLESTONE.block().get())
                .add(ModBlocks.CORALIUM_STONE.block().get())
                .add(ModBlocks.CORALIUM_COBBLESTONE.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.WASTITE.block().get())
                .add(ModBlocks.STARITE.block().get())
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(Tags.Blocks.ORES)
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(ModBlockTags.ABYSSAL_WAISTLAND_SURFACES)
                .add(ModBlocks.ABYSSAL_STONE.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get())
                .add(ModBlocks.WASTITE.block().get())
                .add(Blocks.MUD);

        tag(BlockTags.SUPPORTS_DRY_VEGETATION)
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get());

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

        this.tag(BlockTags.WOODEN_DOORS)
                .add(
                        ModBlocks.WITHERWOOD_DOOR.block().get()
                );
        this.tag(BlockTags.WOODEN_STAIRS)
                .add(
                        ModBlocks.WITHERWOOD_STAIRS.block().get()
                );
        this.tag(BlockTags.STAIRS)
                .add(
                        ModBlocks.ABYSSAL_STONE_BRICKS_STAIRS.block().get()
                );
        this.tag(BlockTags.WOODEN_SLABS)
                .add(
                        ModBlocks.WITHERWOOD_SLAB.block().get()
                );
        this.tag(BlockTags.SLABS)
                .add(
                        ModBlocks.ABYSSAL_STONE_BRICKS_SLAB.block().get()
                );
        this.tag(BlockTags.FENCES)
                .add(
                        ModBlocks.ABYSSAL_STONE_BRICKS_FENCE.block().get()
                );
        this.tag(BlockTags.WOODEN_FENCES)
                .add(
                        ModBlocks.WITHERWOOD_FENCE.block().get()
                );
        this.tag(BlockTags.FENCE_GATES)
                .add(
                        ModBlocks.WITHERWOOD_FENCE_GATE.block().get()
                );
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(
                        ModBlocks.WITHERWOOD_PRESSURE_PLATE.block().get()
                );
        this.tag(BlockTags.WOODEN_SHELVES)
                .add(
                        ModBlocks.WITHERWOOD_SHELF.block().get()
                );
        /*
        this.tag(BlockTags.SAPLINGS)
                .add(
                        ModBlocks.WITHERWOOD_SAPLING.block().get()
                );
         */
        this.tag(BlockTags.LEAVES)
                .add(
                        ModBlocks.WITHERWOOD_LEAVES.block().get()
                );

        this.tag(BlockTags.WOODEN_TRAPDOORS)
                .add(
                        ModBlocks.WITHERWOOD_TRAPDOOR.block().get()
                );

        this.tag(ModBlockTags.WITHERWOOD_LOGS)
                .add(ModBlocks.WITHERWOOD_LOG.block().get())
                .add(ModBlocks.WITHERWOOD_WOOD.block().get())
                .add(ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get())
                .add(ModBlocks.STRIPPED_WITHERWOOD_WOOD.block().get());


        this.tag(BlockTags.LOGS_THAT_BURN)
                .addTags(ModBlockTags.WITHERWOOD_LOGS);

        this.tag(ModBlockTags.RED_WOOL_REPLACEABLE)
                .add(Blocks.RED_WOOL);

        this.tag(ModBlockTags.SUPPORTS_ABYSS_VEGETATION)
                .addTags(BlockTags.SUPPORTS_VEGETATION)
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get());
    }
}