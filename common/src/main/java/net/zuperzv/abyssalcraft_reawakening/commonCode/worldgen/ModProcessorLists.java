package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.resources.ResourceKey;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;

import java.util.List;

public class ModProcessorLists {

    public static final ResourceKey<StructureProcessorList> GRAVITY_FOSSIL_PROCESSORS_GROUND =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("abyssalcraft_reawakening", "gravity_fossil_processors"));
    public static final ResourceKey<StructureProcessorList> GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE_LOW =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("abyssalcraft_reawakening", "gravity_fossil_processors_ground_abyssal_stone_to_cobblestone_low"));
    public static final ResourceKey<StructureProcessorList> GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("abyssalcraft_reawakening", "gravity_fossil_processors_ground_abyssal_stone_to_cobblestone"));
    public static final ResourceKey<StructureProcessorList> GRAVITY_FOSSIL_PROCESSORS_UP =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("abyssalcraft_reawakening", "gravity_fossil_processors_up"));

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        ProcessorRule ABYSSAL_STONE_TO_COBBLESTONE =
                new ProcessorRule(
                        new RandomBlockMatchTest(
                                ModBlocks.ABYSSAL_STONE.block().get(),
                                0.20F ),
                        AlwaysTrueTest.INSTANCE,
                        ModBlocks.ABYSSAL_COBBLESTONE.block().get().defaultBlockState()
                );

        context.register(
                GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE_LOW,
                new StructureProcessorList(
                        List.of(
                                new GravityProcessor(
                                        Heightmap.Types.WORLD_SURFACE,
                                        -11
                                ),
                                new RuleProcessor(
                                        List.of(
                                                ABYSSAL_STONE_TO_COBBLESTONE
                                        )
                                )
                        )
                )
        );

        context.register(
                GRAVITY_FOSSIL_PROCESSORS_GROUND_ABYSSAL_STONE_TO_COBBLESTONE,
                new StructureProcessorList(
                        List.of(
                                new GravityProcessor(
                                        Heightmap.Types.WORLD_SURFACE,
                                        -9
                                ),
                                new RuleProcessor(
                                        List.of(
                                                ABYSSAL_STONE_TO_COBBLESTONE
                                        )
                                )
                        )
                )
        );

        context.register(
                GRAVITY_FOSSIL_PROCESSORS_GROUND,
                new StructureProcessorList(
                        List.of(
                                new GravityProcessor(
                                        Heightmap.Types.WORLD_SURFACE,
                                        -9
                                )
                        )
                )
        );

        context.register(
                GRAVITY_FOSSIL_PROCESSORS_UP,
                new StructureProcessorList(
                        List.of(
                                new GravityProcessor(
                                        Heightmap.Types.WORLD_SURFACE,
                                        75
                                )
                        )
                )
        );
    }
}