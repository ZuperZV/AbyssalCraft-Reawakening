package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.gravityFossils;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

public record FossilGenerator(
        String name,
        RarityFilter rarityFilter,
        List<Identifier> fossils,
        List<Identifier> overlays,
        ResourceKey<StructureProcessorList> fossilProcessor,
        ResourceKey<StructureProcessorList> overlayProcessor
) {
}