package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(TutorialModBlockLootSubProvider::new, LootContextParamSets.BLOCK)
                ),
                registries
        );
    }

    private static final class TutorialModBlockLootSubProvider extends BlockLootSubProvider {
        TutorialModBlockLootSubProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
            dropSelf(ModBlocks.ABYSSALNITE_BLOCK.block().get());
            dropSelf(ModBlocks.RAW_ABYSSALNITE_BLOCK.block().get());

            addOreDrop(ModBlocks.ABYSSALNITE_OVERWORLD_ORE.block().get(), ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);
            addOreDrop(ModBlocks.ABYSSALNITE_DEEPSLATE_ORE.block().get(), ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);
            addOreDrop(ModBlocks.ABYSSALNITE_NETHER_ORE.block().get(), ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);
            addOreDrop(ModBlocks.ABYSSALNITE_END_ORE.block().get(), ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);
        }

        @Override
        protected @NonNull Iterable<Block> getKnownBlocks() {
            return NeoForgeRegistryHelper.BLOCKS.getEntries()
                    .stream()
                    .map(entry -> (Block) entry.value())
                    .toList();
        }


        protected void addOreDrop(Block block, Item item, float min, float max) {
            this.add(block, b -> createOreDrop(block, item, min, max));
        }

        protected LootTable.Builder createOreDrop(Block block, Item item, float min, float max) {

            var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

            return createSilkTouchDispatchTable(
                    block,
                    applyExplosionDecay(
                            item,
                            LootItem.lootTableItem(item)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                    .apply(ApplyBonusCount.addOreBonusCount(
                                            enchantments.getOrThrow(Enchantments.FORTUNE)
                                    ))
                    )
            );
        }
    }
}