package net.zuperzv.abyssalcraft_reawakening.init.worldgen.gravity_fossils;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.ModProcessorLists;

import javax.annotation.processing.Processor;
import java.util.List;

public class FossilRegistry {
    private static final ResourceKey<StructureProcessorList> EMPTY = createKey("empty");

    public static final FossilGenerator VANILA_FOSSIL =
            new FossilGenerator(
                    "vanila_fossil",

                    RarityFilter.onAverageOnceEvery(130),

                    List.of(
                            Constants.idWithDefaultNamespace("fossil/skull_1"),
                            Constants.idWithDefaultNamespace("fossil/skull_2"),
                            Constants.idWithDefaultNamespace("fossil/skull_3"),
                            Constants.idWithDefaultNamespace("fossil/skull_4"),
                            Constants.idWithDefaultNamespace("fossil/spine_1"),
                            Constants.idWithDefaultNamespace("fossil/spine_2"),
                            Constants.idWithDefaultNamespace("fossil/spine_3"),
                            Constants.idWithDefaultNamespace("fossil/spine_4")
                    ),

                    List.of(
                            Constants.idWithDefaultNamespace("fossil/skull_1"),
                            Constants.idWithDefaultNamespace("fossil/skull_2"),
                            Constants.idWithDefaultNamespace("fossil/skull_3"),
                            Constants.idWithDefaultNamespace("fossil/skull_4"),
                            Constants.idWithDefaultNamespace("fossil/spine_1"),
                            Constants.idWithDefaultNamespace("fossil/spine_2"),
                            Constants.idWithDefaultNamespace("fossil/spine_3"),
                            Constants.idWithDefaultNamespace("fossil/spine_4")
                    ),

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS,

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS
            );

    public static final List<FossilGenerator> ALL =
            List.of(
                    VANILA_FOSSIL
            );

    private static ResourceKey<StructureProcessorList> createKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.withDefaultNamespace(name));
    }
}