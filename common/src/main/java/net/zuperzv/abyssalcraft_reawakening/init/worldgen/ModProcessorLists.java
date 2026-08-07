package net.zuperzv.abyssalcraft_reawakening.init.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public class ModProcessorLists {

    public static final ResourceKey<StructureProcessorList> GRAVITY_FOSSIL_PROCESSORS_GROUND =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("abyssalcraft_reawakening", "gravity_fossil_processors"));
    public static final ResourceKey<StructureProcessorList> GRAVITY_FOSSIL_PROCESSORS_UP =
            ResourceKey.create(Registries.PROCESSOR_LIST, Identifier.fromNamespaceAndPath("abyssalcraft_reawakening", "gravity_fossil_processors_up"));

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {

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