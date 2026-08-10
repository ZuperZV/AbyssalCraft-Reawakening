package net.zuperzv.abyssalcraft_reawakening.init.worldgen.gravity_fossils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.worldgen.ModProcessorLists;

import java.util.List;

public class FossilRegistry {
    private static final ResourceKey<StructureProcessorList> EMPTY = createKey("empty");

    public static final FossilGenerator CHAIN =
            new FossilGenerator(
                    "chain",

                    RarityFilter.onAverageOnceEvery(60),

                    List.of(
                            Constants.id("chain/chain_1"),
                            Constants.id("chain/chain_2"),
                            Constants.id("chain/chain_3"),
                            Constants.id("chain/chain_4"),
                            Constants.id("chain/chain_5")
                    ),

                    List.of(
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty")
                    ),

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_UP,

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_UP
            );

    public static final FossilGenerator FALLEN_CHAIN =
            new FossilGenerator(
                    "fallen_chain",

                    RarityFilter.onAverageOnceEvery(80),

                    List.of(
                            Constants.id("fallen_chain/fallen_chain_1"),
                            Constants.id("fallen_chain/fallen_chain_2"),
                            Constants.id("fallen_chain/fallen_chain_3"),
                            Constants.id("fallen_chain/fallen_chain_4"),
                            Constants.id("fallen_chain/fallen_chain_5"),
                            Constants.id("fallen_chain/fallen_chain_6"),
                            Constants.id("fallen_chain/fallen_chain_7")
                    ),

                    List.of(
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty"),
                            Constants.id("empty")
                    ),

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE_LOW,

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE_LOW
            );

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
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty")
                    ),

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_GROUND,

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_GROUND
            );

    public static final FossilGenerator IN_GROUND =
            new FossilGenerator(
                    "in_ground",

                    RarityFilter.onAverageOnceEvery(130),

                    List.of(
                            Constants.idWithDefaultNamespace("in_ground/in_ground_1"),
                            Constants.idWithDefaultNamespace("in_ground/in_ground_2"),
                            Constants.idWithDefaultNamespace("in_ground/in_ground_3"),
                            Constants.idWithDefaultNamespace("in_ground/in_ground_4"),
                            Constants.idWithDefaultNamespace("in_ground/in_ground_5"),
                            Constants.idWithDefaultNamespace("in_ground/in_ground_6"),
                            Constants.idWithDefaultNamespace("in_ground/in_ground_7")
                    ),

                    List.of(
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty"),
                            Constants.idWithDefaultNamespace("empty")
                    ),

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE,

                    ModProcessorLists.GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE
            );

    public static final List<FossilGenerator> ALL =
            List.of(
                    CHAIN,
                    FALLEN_CHAIN,
                    VANILA_FOSSIL,
                    IN_GROUND
            );

    private static ResourceKey<StructureProcessorList> createKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.withDefaultNamespace(name));
    }
}