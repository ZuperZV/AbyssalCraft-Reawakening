package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
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
                .add(ModBlocks.ABYSSALNITE_GRIMESTONE_ORE.block().get())
                .add(ModBlocks.CORALIUM_INFUSED_ORE.block().get())
                .add(ModBlocks.CORALIUM_INFUSED_DEEPSLATE_ORE.block().get())
                .add(ModBlocks.CORALIUM_INFUSED_ABYSSAL_ORE.block().get())
                .add(ModBlocks.CORALIUM_INFUSED_GRIMESTONE_ORE.block().get())
                .add(ModBlocks.CORALIUM_ORE.block().get())
                .add(ModBlocks.CORALIUM_DEEPSLATE_ORE.block().get())
                .add(ModBlocks.CORALIUM_ABYSSAL_ORE.block().get())
                .add(ModBlocks.CORALIUM_GRIMESTONE_ORE.block().get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.RAW_ABYSSALNITE_BLOCK.block().get())
                .add(ModBlocks.ABYSSALNITE_BLOCK.block().get())
                .add(ModBlocks.RAW_CORALIUM_BLOCK.block().get())
                .add(ModBlocks.CORALIUM_BLOCK.block().get())
                .add(ModBlocks.ABYSSAL_STONE.block().get())
                .add(ModBlocks.ABYSSAL_COBBLESTONE.block().get())
                .add(ModBlocks.CORALIUM_STONE.block().get())
                .add(ModBlocks.CORALIUM_COBBLESTONE.block().get())
                .add(ModBlocks.CORALIUM_BRICKS.block().get())
                .add(ModBlocks.GRIMESTONE.block().get())
                .add(ModBlocks.MONOLITH_STONE.block().get())
                .add(ModBlocks.MONOLITH_STONE_PILLAR.block().get())
                .add(ModBlocks.CTHULHU_STATUE.block().get())
                .add(ModBlocks.HASTUR_STATUE.block().get())
                .add(ModBlocks.JZAHAR_STATUE.block().get())
                .add(ModBlocks.YOG_SOTHOTH_STATUE.block().get())
                .add(ModBlocks.AZATHOTH_STATUE.block().get())
                .add(ModBlocks.NYARLATHOTEP_STATUE.block().get())
                .add(ModBlocks.SHUB_NIGGURATH_STATUE.block().get())
                .add(ModBlocks.ENERGY_PEDESTAL.block().get())
                .add(ModBlocks.ENERGY_COLLECTOR.block().get())
                .add(ModBlocks.ENERGY_RELAY.block().get())
                .add(ModBlocks.ENERGY_CONTAINER.block().get())
                .add(ModBlocks.SACRIFICIAL_ALTAR.block().get())
                .add(ModBlocks.ENERGY_DEPOSITIONER.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.WASTITE.block().get())
                .add(ModBlocks.STARITE.block().get())
                .add(ModBlocks.DREADIUM_STONE.block().get())
                .add(ModBlocks.DREADIUM_COBBLESTONE.block().get())
                .add(ModBlocks.OMOTHOL_STONE.block().get())
                .add(ModBlocks.OMOTHOL_COBBLESTONE.block().get())
                // Darkstone blocks
                .add(ModBlocks.DARK_STONE.block().get())
                .add(ModBlocks.DARK_COBBLESTONE.block().get())
                .add(ModBlocks.DARK_STONE_BRICKS.block().get())
                .add(ModBlocks.CHISELED_DARK_STONE_BRICKS.block().get())
                .add(ModBlocks.CRACKED_DARK_STONE_BRICKS.block().get())
                .add(ModBlocks.DARK_STONE_BUTTON.block().get())
                .add(ModBlocks.DARK_STONE_PRESSURE_PLATE.block().get())
                .add(ModBlocks.DARK_STONE_STAIRS.block().get())
                .add(ModBlocks.DARK_STONE_SLAB.block().get())
                .add(ModBlocks.DARK_STONE_WALL.block().get())
                .add(ModBlocks.DARK_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.DARK_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.DARK_COBBLESTONE_WALL.block().get())
                .add(ModBlocks.DARK_STONE_BRICKS_STAIRS.block().get())
                .add(ModBlocks.DARK_STONE_BRICKS_SLAB.block().get())
                .add(ModBlocks.DARK_STONE_BRICKS_WALL.block().get())
                // Abyssalnite Stone blocks
                .add(ModBlocks.ABYSSALNITE_STONE.block().get())
                .add(ModBlocks.ABYSSALNITE_STONE_STAIRS.block().get())
                .add(ModBlocks.ABYSSALNITE_STONE_SLAB.block().get())
                .add(ModBlocks.ABYSSALNITE_STONE_WALL.block().get())
                .add(ModBlocks.ABYSSALNITE_COBBLESTONE.block().get())
                .add(ModBlocks.ABYSSALNITE_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.ABYSSALNITE_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.ABYSSALNITE_COBBLESTONE_WALL.block().get())
                .add(ModBlocks.ABYSSALNITE_BRICKS.block().get())
                .add(ModBlocks.ABYSSALNITE_BRICKS_STAIRS.block().get())
                .add(ModBlocks.ABYSSALNITE_BRICKS_SLAB.block().get())
                .add(ModBlocks.ABYSSALNITE_BRICKS_WALL.block().get())
                .add(ModBlocks.CHISELED_ABYSSALNITE_BRICKS.block().get())
                .add(ModBlocks.CRACKED_ABYSSALNITE_BRICKS.block().get())
                .add(ModBlocks.ABYSSALNITE_BUTTON.block().get())
                .add(ModBlocks.ABYSSALNITE_PRESSURE_PLATE.block().get())
                // Ethaxium Stone blocks
                .add(ModBlocks.ETHAXIUM_STONE.block().get())
                .add(ModBlocks.ETHAXIUM_STONE_STAIRS.block().get())
                .add(ModBlocks.ETHAXIUM_STONE_SLAB.block().get())
                .add(ModBlocks.ETHAXIUM_STONE_WALL.block().get())
                .add(ModBlocks.ETHAXIUM_COBBLESTONE.block().get())
                .add(ModBlocks.ETHAXIUM_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.ETHAXIUM_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.ETHAXIUM_COBBLESTONE_WALL.block().get())
                .add(ModBlocks.ETHAXIUM_BRICKS.block().get())
                .add(ModBlocks.ETHAXIUM_BRICKS_STAIRS.block().get())
                .add(ModBlocks.ETHAXIUM_BRICKS_SLAB.block().get())
                .add(ModBlocks.ETHAXIUM_BRICKS_WALL.block().get())
                .add(ModBlocks.CHISELED_ETHAXIUM_BRICKS.block().get())
                .add(ModBlocks.CRACKED_ETHAXIUM_BRICKS.block().get())
                .add(ModBlocks.ETHAXIUM_BUTTON.block().get())
                .add(ModBlocks.ETHAXIUM_PRESSURE_PLATE.block().get())
                // Abyssal Stone building blocks
                .add(ModBlocks.ABYSSAL_STONE_BRICKS_STAIRS.block().get())
                .add(ModBlocks.ABYSSAL_STONE_BRICKS_SLAB.block().get())
                .add(ModBlocks.ABYSSAL_STONE_BRICKS_FENCE.block().get())
                .add(ModBlocks.CHISELED_ABYSSAL_STONE_BRICKS.block().get())
                .add(ModBlocks.CRACKED_ABYSSAL_STONE_BRICKS.block().get())
                .add(ModBlocks.ABYSSAL_STONE_BUTTON.block().get())
                .add(ModBlocks.ABYSSAL_STONE_PRESSURE_PLATE.block().get())
                .add(ModBlocks.ABYSSAL_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.ABYSSAL_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.ABYSSAL_COBBLESTONE_WALL.block().get())
                // Coralium building blocks
                .add(ModBlocks.CORALIUM_STONE_STAIRS.block().get())
                .add(ModBlocks.CORALIUM_STONE_SLAB.block().get())
                .add(ModBlocks.CORALIUM_STONE_WALL.block().get())
                .add(ModBlocks.CORALIUM_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.CORALIUM_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.CORALIUM_COBBLESTONE_WALL.block().get())
                .add(ModBlocks.CORALIUM_BRICKS_STAIRS.block().get())
                .add(ModBlocks.CORALIUM_BRICKS_SLAB.block().get())
                .add(ModBlocks.CORALIUM_BRICKS_WALL.block().get())
                .add(ModBlocks.CHISELED_CORALIUM_BRICKS.block().get())
                .add(ModBlocks.CRACKED_CORALIUM_BRICKS.block().get())
                .add(ModBlocks.CORALIUM_BUTTON.block().get())
                .add(ModBlocks.CORALIUM_PRESSURE_PLATE.block().get())
                // Dreadium building blocks
                .add(ModBlocks.DREADIUM_STONE_STAIRS.block().get())
                .add(ModBlocks.DREADIUM_STONE_SLAB.block().get())
                .add(ModBlocks.DREADIUM_STONE_WALL.block().get())
                .add(ModBlocks.DREADIUM_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.DREADIUM_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.DREADIUM_COBBLESTONE_WALL.block().get())
                .add(ModBlocks.DREADIUM_BRICKS.block().get())
                .add(ModBlocks.DREADIUM_BRICKS_STAIRS.block().get())
                .add(ModBlocks.DREADIUM_BRICKS_SLAB.block().get())
                .add(ModBlocks.DREADIUM_BRICKS_WALL.block().get())
                .add(ModBlocks.CHISELED_DREADIUM_BRICKS.block().get())
                .add(ModBlocks.CRACKED_DREADIUM_BRICKS.block().get())
                .add(ModBlocks.DREADIUM_BUTTON.block().get())
                .add(ModBlocks.DREADIUM_PRESSURE_PLATE.block().get())
                // Omothol building blocks
                .add(ModBlocks.OMOTHOL_STONE_STAIRS.block().get())
                .add(ModBlocks.OMOTHOL_STONE_SLAB.block().get())
                .add(ModBlocks.OMOTHOL_STONE_WALL.block().get())
                .add(ModBlocks.OMOTHOL_COBBLESTONE_STAIRS.block().get())
                .add(ModBlocks.OMOTHOL_COBBLESTONE_SLAB.block().get())
                .add(ModBlocks.OMOTHOL_COBBLESTONE_WALL.block().get())
                .add(ModBlocks.OMOTHOL_BRICKS.block().get())
                .add(ModBlocks.OMOTHOL_BRICKS_STAIRS.block().get())
                .add(ModBlocks.OMOTHOL_BRICKS_SLAB.block().get())
                .add(ModBlocks.OMOTHOL_BRICKS_WALL.block().get())
                .add(ModBlocks.CHISELED_OMOTHOL_BRICKS.block().get())
                .add(ModBlocks.CRACKED_OMOTHOL_BRICKS.block().get())
                .add(ModBlocks.OMOTHOL_BUTTON.block().get())
                .add(ModBlocks.OMOTHOL_PRESSURE_PLATE.block().get())
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(Tags.Blocks.ORES)
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(ModBlockTags.ABYSSALWASTELAND_SURFACES)
                .add(ModBlocks.ABYSSAL_STONE.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get())
                .add(ModBlocks.WASTITE.block().get())
                .add(ModBlocks.SCARLET_SHALE.block().get())
                .add(ModBlocks.AZURE_WASTE_STONE.block().get())
                .add(Blocks.MUD);

        tag(ModBlockTags.ABYSSALWASTELAND_CARVER_REPLACEABLES)
                .add(ModBlocks.ABYSSAL_STONE.block().get())
                .add(ModBlocks.GRIMESTONE.block().get())
                .add(ModBlocks.SCARLET_SHALE.block().get())
                .add(ModBlocks.AZURE_WASTE_STONE.block().get())
                .add(ModBlocks.WASTITE.block().get())
                .add(Blocks.MUD);

        tag(BlockTags.MOSS_REPLACEABLE)
                .addTags(ModBlockTags.ABYSSALWASTELAND_SURFACES);

        tag(BlockTags.SUPPORTS_DRY_VEGETATION)
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get());

        tag(ModBlockTags.WASTITE_SPIKE_REPLACEABLE)
                .add(ModBlocks.WASTITE.block().get())
                .addTag(ModBlockTags.ABYSSALWASTELAND_SURFACES);

        tag(ModBlockTags.ABYSSAL_STONE_ORE_REPLACEABLES)
                .add(ModBlocks.ABYSSAL_STONE.block().get());

        tag(ModBlockTags.GRIMSTONE_ORE_REPLACEABLES)
                .add(ModBlocks.GRIMESTONE.block().get());


        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ABYSSALNITE_BLOCK.block().get())
                .add(ModBlocks.RAW_ABYSSALNITE_BLOCK.block().get())
                .addTag(ModBlockTags.ABYSSALCRAFT_ORES);

        tag(ModBlockTags.NEEDS_ABYSSALNITE_TOOL)
                .add(ModBlocks.CORALIUM_BLOCK.block().get())
                .add(ModBlocks.RAW_CORALIUM_BLOCK.block().get());

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
                        ModBlocks.ABYSSAL_STONE_BRICKS_STAIRS.block().get(),
                        ModBlocks.ABYSSAL_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.CORALIUM_STONE_STAIRS.block().get(),
                        ModBlocks.CORALIUM_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.CORALIUM_BRICKS_STAIRS.block().get(),
                        ModBlocks.DREADIUM_STONE_STAIRS.block().get(),
                        ModBlocks.DREADIUM_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.DREADIUM_BRICKS_STAIRS.block().get(),
                        ModBlocks.OMOTHOL_STONE_STAIRS.block().get(),
                        ModBlocks.OMOTHOL_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.OMOTHOL_BRICKS_STAIRS.block().get(),
                        // Darkstone stairs
                        ModBlocks.DARK_STONE_STAIRS.block().get(),
                        ModBlocks.DARK_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.DARK_STONE_BRICKS_STAIRS.block().get(),
                        // Abyssalnite Stone stairs
                        ModBlocks.ABYSSALNITE_STONE_STAIRS.block().get(),
                        ModBlocks.ABYSSALNITE_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.ABYSSALNITE_BRICKS_STAIRS.block().get(),
                        // Ethaxium Stone stairs
                        ModBlocks.ETHAXIUM_STONE_STAIRS.block().get(),
                        ModBlocks.ETHAXIUM_COBBLESTONE_STAIRS.block().get(),
                        ModBlocks.ETHAXIUM_BRICKS_STAIRS.block().get()
                );
        this.tag(BlockTags.WOODEN_SLABS)
                .add(
                        ModBlocks.WITHERWOOD_SLAB.block().get()
                );
        this.tag(BlockTags.SLABS)
                .add(
                        ModBlocks.ABYSSAL_STONE_BRICKS_SLAB.block().get(),
                        ModBlocks.ABYSSAL_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.CORALIUM_STONE_SLAB.block().get(),
                        ModBlocks.CORALIUM_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.CORALIUM_BRICKS_SLAB.block().get(),
                        ModBlocks.DREADIUM_STONE_SLAB.block().get(),
                        ModBlocks.DREADIUM_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.DREADIUM_BRICKS_SLAB.block().get(),
                        ModBlocks.OMOTHOL_STONE_SLAB.block().get(),
                        ModBlocks.OMOTHOL_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.OMOTHOL_BRICKS_SLAB.block().get(),
                        // Darkstone slabs
                        ModBlocks.DARK_STONE_SLAB.block().get(),
                        ModBlocks.DARK_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.DARK_STONE_BRICKS_SLAB.block().get(),
                        // Abyssalnite Stone slabs
                        ModBlocks.ABYSSALNITE_STONE_SLAB.block().get(),
                        ModBlocks.ABYSSALNITE_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.ABYSSALNITE_BRICKS_SLAB.block().get(),
                        // Ethaxium Stone slabs
                        ModBlocks.ETHAXIUM_STONE_SLAB.block().get(),
                        ModBlocks.ETHAXIUM_COBBLESTONE_SLAB.block().get(),
                        ModBlocks.ETHAXIUM_BRICKS_SLAB.block().get()
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
        this.tag(BlockTags.STONE_PRESSURE_PLATES)
                .add(
                        ModBlocks.ABYSSAL_STONE_PRESSURE_PLATE.block().get(),
                        ModBlocks.DARK_STONE_PRESSURE_PLATE.block().get(),
                        ModBlocks.CORALIUM_PRESSURE_PLATE.block().get(),
                        ModBlocks.DREADIUM_PRESSURE_PLATE.block().get(),
                        ModBlocks.OMOTHOL_PRESSURE_PLATE.block().get(),
                        ModBlocks.ABYSSALNITE_PRESSURE_PLATE.block().get(),
                        ModBlocks.ETHAXIUM_PRESSURE_PLATE.block().get()
                );
        this.tag(BlockTags.STONE_BUTTONS)
                .add(
                        ModBlocks.ABYSSAL_STONE_BUTTON.block().get(),
                        ModBlocks.DARK_STONE_BUTTON.block().get(),
                        ModBlocks.CORALIUM_BUTTON.block().get(),
                        ModBlocks.DREADIUM_BUTTON.block().get(),
                        ModBlocks.OMOTHOL_BUTTON.block().get(),
                        ModBlocks.ABYSSALNITE_BUTTON.block().get(),
                        ModBlocks.ETHAXIUM_BUTTON.block().get()
                );
        this.tag(BlockTags.WALLS)
                .add(
                        ModBlocks.ABYSSAL_COBBLESTONE_WALL.block().get(),
                        ModBlocks.CORALIUM_STONE_WALL.block().get(),
                        ModBlocks.CORALIUM_COBBLESTONE_WALL.block().get(),
                        ModBlocks.CORALIUM_BRICKS_WALL.block().get(),
                        ModBlocks.DREADIUM_STONE_WALL.block().get(),
                        ModBlocks.DREADIUM_COBBLESTONE_WALL.block().get(),
                        ModBlocks.DREADIUM_BRICKS_WALL.block().get(),
                        ModBlocks.OMOTHOL_STONE_WALL.block().get(),
                        ModBlocks.OMOTHOL_COBBLESTONE_WALL.block().get(),
                        ModBlocks.OMOTHOL_BRICKS_WALL.block().get(),
                        // Darkstone walls
                        ModBlocks.DARK_STONE_WALL.block().get(),
                        ModBlocks.DARK_COBBLESTONE_WALL.block().get(),
                        ModBlocks.DARK_STONE_BRICKS_WALL.block().get(),
                        // Abyssalnite Stone walls
                        ModBlocks.ABYSSALNITE_STONE_WALL.block().get(),
                        ModBlocks.ABYSSALNITE_COBBLESTONE_WALL.block().get(),
                        ModBlocks.ABYSSALNITE_BRICKS_WALL.block().get(),
                        // Ethaxium Stone walls
                        ModBlocks.ETHAXIUM_STONE_WALL.block().get(),
                        ModBlocks.ETHAXIUM_COBBLESTONE_WALL.block().get(),
                        ModBlocks.ETHAXIUM_BRICKS_WALL.block().get()
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
                .add(ModBlocks.GLOOM_MOSS.block().get())
                .add(ModBlocks.FUSED_ABYSSAL_SAND.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get());

        this.tag(ModBlockTags.SWAMP_SOIL)
                .add(ModBlocks.SINKING_MIRE.block().get())
                .add(ModBlocks.CORRUPTED_SOIL.block().get())
                .add(ModBlocks.ABYSSAL_SAND.block().get());
    }
}
