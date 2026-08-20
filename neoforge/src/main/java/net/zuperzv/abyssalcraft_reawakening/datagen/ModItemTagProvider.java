package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItemTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(ItemTags.CAULDRON_CAN_REMOVE_DYE)
                .add(ModItems.NECRONOMICON.get());

        tag(ItemTags.DYES)
                .add(ModItems.NECRONOMICON.get());



        tag(ItemTags.BEACON_PAYMENT_ITEMS)
                .add(ModItems.ABYSSALNITE_INGOT.get());

        tag(ModItemTags.ABYSSALNITE_MATERIALS)
                .add(ModItems.ABYSSALNITE_INGOT.get());

        tag(ModItemTags.REFINED_CORALIUM_MATERIALS)
                .add(ModItems.ABYSSALNITE_INGOT.get());

        tag(ModItemTags.DREADIUM_MATERIALS)
                .add(ModItems.ABYSSALNITE_INGOT.get());

        tag(ModItemTags.ETHAXIUM_MATERIALS)
                .add(ModItems.ABYSSALNITE_INGOT.get());



        tag(ItemTags.SWORDS)
                .add(ModItems.ABYSSALNITE_SWORD.get());

        tag(ItemTags.PICKAXES)
                .add(ModItems.ABYSSALNITE_PICKAXE.get());

        tag(ItemTags.AXES)
                .add(ModItems.ABYSSALNITE_AXE.get());

        tag(ItemTags.SHOVELS)
        .add(ModItems.ABYSSALNITE_SHOVEL.get());

        tag(ItemTags.HOES)
        .add(ModItems.ABYSSALNITE_HOE.get());

        tag(ItemTags.SPEARS)
        .add(ModItems.ABYSSALNITE_SPEAR.get());



        tag(ItemTags.HEAD_ARMOR)
        .add(ModItems.ABYSSALNITE_HELMET.get());

        tag(ItemTags.CHEST_ARMOR)
        .add(ModItems.ABYSSALNITE_CHESTPLATE.get());

        tag(ItemTags.LEG_ARMOR)
        .add(ModItems.ABYSSALNITE_LEGGINGS.get());

        tag(ItemTags.FOOT_ARMOR)
        .add(ModItems.ABYSSALNITE_BOOTS.get());


        tag(ItemTags.PLANKS)
                .add(ModBlocks.WITHERWOOD_PLANKS.item().get());

        
        tag(ItemTags.WOODEN_DOORS)
                .add(ModBlocks.WITHERWOOD_DOOR.item().get());
        
        tag(ItemTags.WOODEN_STAIRS)
                .add(ModBlocks.WITHERWOOD_STAIRS.item().get());

        tag(ItemTags.WOODEN_SLABS)
                .add(ModBlocks.WITHERWOOD_SLAB.item().get());

        tag(ItemTags.WOODEN_FENCES)
                .add(ModBlocks.WITHERWOOD_FENCE.item().get());

        tag(ItemTags.FENCE_GATES)
                .add(ModBlocks.WITHERWOOD_FENCE_GATE.item().get());

        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.WITHERWOOD_PRESSURE_PLATE.item().get());

        /*
        tag(ItemTags.SAPLINGS)
                .add(
                        ModBlocks.WITHERWOOD_SAPLING.item().get());
                );
         */
        tag(ItemTags.LEAVES)
                .add(ModBlocks.WITHERWOOD_LEAVES.item().get());

        tag(ItemTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.WITHERWOOD_TRAPDOOR.item().get());

        tag(ModItemTags.WITHERWOOD_LOGS)
                .add(ModBlocks.WITHERWOOD_LOG.item().get())
                .add(ModBlocks.WITHERWOOD_WOOD.item().get())
                .add(ModBlocks.STRIPPED_WITHERWOOD_LOG.item().get())
                .add(ModBlocks.STRIPPED_WITHERWOOD_WOOD.item().get());

        tag(ItemTags.LOGS_THAT_BURN)
                .addTags(ModItemTags.WITHERWOOD_LOGS);
    }
}