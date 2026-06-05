package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
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
        }

        @Override
        protected @NonNull Iterable<Block> getKnownBlocks() {
            return NeoForgeRegistryHelper.BLOCKS.getEntries()
                    .stream()
                    .map(entry -> (Block) entry.value())
                    .toList();
        }
    }
}