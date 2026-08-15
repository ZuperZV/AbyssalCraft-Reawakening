package net.zuperzv.abyssalcraft_reawakening.services.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public final class StructureHelper {

    private StructureHelper() {
    }

    public static final Identifier STONE_ALTAR =
            Identifier.fromNamespaceAndPath(
                    "abyssalcraft_reawakening",
                    "stone_alter"
            );

    public static final Identifier STONE_ALTAR_DONE =
            Identifier.fromNamespaceAndPath(
                    "abyssalcraft_reawakening",
                    "stone_alter_done"
            );

    public static boolean replaceStructure(
            ServerLevel level,
            BlockPos origin,
            Identifier structureId
    ) {
        StructureTemplateManager manager = level.getStructureManager();

        Optional<StructureTemplate> optional = manager.get(structureId);

        if (optional.isEmpty()) {
            return false;
        }

        StructureTemplate template = optional.get();

        StructurePlaceSettings settings =
                new StructurePlaceSettings();

        return template.placeInWorld(
                level,
                origin,
                origin,
                settings,
                level.getRandom(),
                2
        );
    }

    public static boolean replaceStoneAltar(
            ServerLevel level,
            BlockPos origin
    ) {
        return replaceStructure(
                level,
                origin,
                STONE_ALTAR_DONE
        );
    }
}