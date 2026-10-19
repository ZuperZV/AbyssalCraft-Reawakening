package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** Matches the in-world pattern against the editable Place of Power structure NBT. */
public final class PlaceOfPowerStructure {
    private static final Identifier STRUCTURE_ID = Identifier.fromNamespaceAndPath(
            "abyssalcraft_reawakening", "places_of_power/basic");

    private PlaceOfPowerStructure() {}

    public static boolean matches(Level level, BlockPos center) {
        if (!(level instanceof ServerLevel serverLevel)) return false;
        StructureTemplateManager manager = serverLevel.getStructureManager();
        StructureTemplate template = manager.get(STRUCTURE_ID).orElse(null);
        if (template == null) return false;

        var size = template.getSize();
        BlockPos origin = center.offset(-(size.getX() / 2), 0, -(size.getZ() / 2));
        List<StructureTemplate.StructureBlockInfo> blocks = getBlocks(template);
        if (blocks.isEmpty()) return false;

        for (StructureTemplate.StructureBlockInfo expected : blocks) {
            BlockPos worldPos = origin.offset(expected.pos());
            var actual = level.getBlockState(worldPos);
            if (expected.state().is(ModBlocks.CTHULHU_STATUE.block().get())) {
                if (!(actual.getBlock() instanceof PEMachineBlock statue)
                        || statue.getKind() != PEMachineBlock.Kind.STATUE) return false;
            } else if (!actual.is(expected.state().getBlock())) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsStatue(Level level, BlockPos statuePos) {
        if (!(level.getBlockState(statuePos).getBlock() instanceof PEMachineBlock statue)
                || statue.getKind() != PEMachineBlock.Kind.STATUE
                || !(level instanceof ServerLevel serverLevel)) return false;

        StructureTemplate template = serverLevel.getStructureManager().get(STRUCTURE_ID).orElse(null);
        if (template == null) return false;
        var size = template.getSize();
        for (int x : new int[]{0, size.getX() - 1}) {
            for (int z : new int[]{0, size.getZ() - 1}) {
                BlockPos origin = statuePos.offset(-x, -2, -z);
                BlockPos center = origin.offset(size.getX() / 2, 0, size.getZ() / 2);
                if (matches(level, center)) return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static List<StructureTemplate.StructureBlockInfo> getBlocks(StructureTemplate structure) {
        try {
            Field palettesField = StructureTemplate.class.getDeclaredField("palettes");
            palettesField.setAccessible(true);
            List<StructureTemplate.Palette> palettes = (List<StructureTemplate.Palette>) palettesField.get(structure);
            List<StructureTemplate.StructureBlockInfo> blocks = new ArrayList<>();
            for (StructureTemplate.Palette palette : palettes) blocks.addAll(palette.blocks());
            return blocks;
        } catch (ReflectiveOperationException exception) {
            return List.of();
        }
    }
}
