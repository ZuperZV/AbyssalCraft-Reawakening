package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class ModLootTableProvider extends LootTableProvider {
    private static final Set<Block> generatedLoot = new java.util.HashSet<>();

    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)
                ),
                registries
        );
    }

    private static final class ModBlockLootSubProvider extends BlockLootSubProvider {

        protected ModBlockLootSubProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {

            dropSelf(ModBlocks.ABYSSALNITE_BLOCK.block().get());
            dropSelf(ModBlocks.RAW_ABYSSALNITE_BLOCK.block().get());

            addOreDrop(ModBlocks.ABYSSALNITE_OVERWORLD_ORE.block().get(),
                    ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);

            addOreDrop(ModBlocks.ABYSSALNITE_DEEPSLATE_ORE.block().get(),
                    ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);

            addOreDrop(ModBlocks.ABYSSALNITE_NETHER_ORE.block().get(),
                    ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);

            addOreDrop(ModBlocks.ABYSSALNITE_END_ORE.block().get(),
                    ModItems.RAW_ABYSSALNITE.get(), 1.0F, 3.0F);

            noDrop(ModBlocks.ABYSSAL_WASTELAND_PORTAL_BLOCK.block().get());

            addDoorDrop(ModBlocks.WITHERWOOD_DOOR.block().get());
            addSlabDrop(ModBlocks.WITHERWOOD_SLAB.block().get());

            addSingleItemTable(ModBlocks.WITHERWOOD_SIGN.get(), ModItems.WITHERWOOD_SIGN.get());
            addSingleItemTable(ModBlocks.WITHERWOOD_WALL_SIGN.get(), ModItems.WITHERWOOD_SIGN.get());
            addSingleItemTable(ModBlocks.WITHERWOOD_HANGING_SIGN.get(), ModItems.WITHERWOOD_HANGING_SIGN.get());
            addSingleItemTable(ModBlocks.WITHERWOOD_WALL_HANGING_SIGN.get(), ModItems.WITHERWOOD_HANGING_SIGN.get());

            addLeafLitterDrops(
                    ModBlocks.WITHERWOOD_LEAF_LITTER.block().get(),
                    ModBlocks.WITHERWOOD_SAPLING.block().get()
            );

            addLeavesDrops(ModBlocks.WITHERWOOD_LEAVES.block().get(), ModBlocks.WITHERWOOD_SAPLING.block().get());

            for (Block block : getKnownBlocks()) {

                if (!generatedLoot.contains(block)) {
                    dropSelf(block);
                }
            }
        }

        private static final float[] LEAF_LITTER_SAPLING_CHANCES = new float[]{
                0.30F, // Fortune 0
                0.35F, // Fortune I
                0.40F, // Fortune II
                0.45F, // Fortune III
                0.50F  // Fortune IV+
        };

        protected void addLeafLitterDrops(Block litter, Block sapling) {
            generatedLoot.add(litter);

            HolderLookup.RegistryLookup<Enchantment> enchantments =
                    this.registries.lookupOrThrow(Registries.ENCHANTMENT);

            this.add(litter, block ->
                    this.createSilkTouchOrShearsDispatchTable(
                            block,
                            LootItem.lootTableItem(sapling)
                                    .when(
                                            BonusLevelTableCondition.bonusLevelFlatChance(
                                                    enchantments.getOrThrow(Enchantments.FORTUNE),
                                                    LEAF_LITTER_SAPLING_CHANCES
                                            )
                                    )
                                    .otherwise(createSegmentedLootEntry(block))
                    )
            );
        }

        protected LootPoolEntryContainer.Builder<?> createSegmentedLootEntry(Block block) {
            if (!(block instanceof SegmentableBlock segmentableBlock)) {
                return LootItem.lootTableItem(block);
            }

            return LootItem.lootTableItem(block).apply(
                    IntStream.rangeClosed(1, 4).boxed().toList(),
                    count -> SetItemCountFunction.setCount(ConstantValue.exactly(count))
                            .when(
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(
                                                    StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(
                                                                    segmentableBlock.getSegmentAmountProperty(),
                                                                    count
                                                            )
                                            )
                            )
            );
        }

        protected void addLeavesDrops(Block block, Block item) {
            generatedLoot.add(block);
            this.add(block, blockToMine ->
                    this.createLeavesDrops(blockToMine, item, NORMAL_LEAVES_SAPLING_CHANCES));
        }

        protected void addSingleItemTable(Block block, Item item) {
            generatedLoot.add(block);
            this.add(block, blockToMine ->
                    createSingleItemTable(item));
        }

        protected void addOreDrop(Block block, Item item, float min, float max) {
            this.add(block, b -> createOreDrop(block, item, min, max));
        }

        protected void noDrop(Block block) {
            generatedLoot.add(block);
            this.add(block, noDrop());
        }

        protected void addDoorDrop(Block block) {
            generatedLoot.add(block);
            this.add(block, x$0 -> this.createDoorTable(x$0));
        }

        protected void addSlabDrop(Block block) {
            generatedLoot.add(block);
            this.add(block, x$0 -> this.createSlabItemTable(x$0));
        }

        protected LootTable.Builder createOreDrop(Block block, Item item, float min, float max) {
            generatedLoot.add(block);

            var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

            return createSilkTouchDispatchTable(
                    block,
                    applyExplosionDecay(
                            item,
                            LootItem.lootTableItem(item)
                                    .apply(SetItemCountFunction.setCount(
                                            UniformGenerator.between(min, max)
                                    ))
                                    .apply(ApplyBonusCount.addOreBonusCount(
                                            enchantments.getOrThrow(Enchantments.FORTUNE)
                                    ))
                    )
            );
        }

        @Override
        protected @NonNull Iterable<Block> getKnownBlocks() {
            return NeoForgeRegistryHelper.BLOCKS.getEntries()
                    .stream()
                    .map(entry -> (Block) entry.value())
                    .toList();
        }

        @Override
        protected void dropSelf(Block block) {
            generatedLoot.add(block);
            super.dropSelf(block);
        }
    }
}