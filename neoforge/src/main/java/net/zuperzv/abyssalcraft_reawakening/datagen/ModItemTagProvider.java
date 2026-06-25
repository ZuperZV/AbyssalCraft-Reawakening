package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItemTags;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
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
    }
}