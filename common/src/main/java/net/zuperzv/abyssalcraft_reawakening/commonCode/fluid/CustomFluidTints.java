package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PotionFluidBlockEntity;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import java.util.Map;

/** Colors shared by custom-fluid rendering and immersion particles. */
public final class CustomFluidTints {
    private static final Map<Fluid, Integer> COLORS = Map.of(
            ModFluids.SOURCE_POTION.get(), PotionFluidColor.WORLD_COLOR,
            ModFluids.FLOWING_POTION.get(), PotionFluidColor.WORLD_COLOR,
            ModFluids.SOURCE_SULFURIC_ARCANUM.get(), 0xFFDDD8A6,
            ModFluids.FLOWING_SULFURIC_ARCANUM.get(), 0xFFDDD8A6
    );

    private CustomFluidTints() {
    }

    public static Integer getColor(Fluid fluid) {
        return COLORS.get(fluid);
    }

    public static Integer getColor(Fluid fluid, BlockAndTintGetter level, BlockPos pos) {
        Integer fallback = COLORS.get(fluid);
        if (fallback == null) {
            return null;
        }

        if ((fluid == ModFluids.SOURCE_POTION.get()
                || fluid == ModFluids.FLOWING_POTION.get())
                && level.getBlockEntity(pos) instanceof PotionFluidBlockEntity potionBlockEntity
                && potionBlockEntity.getPotionContents() != net.minecraft.world.item.alchemy.PotionContents.EMPTY) {
            return 0xFF000000 | potionBlockEntity.getPotionContents().getColor();
        }
        return fallback;
    }

    public static int getColorForParticles(Fluid fluid, BlockAndTintGetter level, BlockPos pos) {
        Integer color = getColor(fluid, level, pos);
        if (color == null
                || (fluid != ModFluids.SOURCE_POTION.get()
                && fluid != ModFluids.FLOWING_POTION.get())) {
            return color == null ? 0xFFFFFFFF : color;
        }

        record SearchNode(BlockPos pos, int distance) {}
        ArrayDeque<SearchNode> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(new SearchNode(pos, 0));
        visited.add(pos);

        while (!queue.isEmpty()) {
            SearchNode node = queue.removeFirst();
            if (level.getBlockEntity(node.pos()) instanceof PotionFluidBlockEntity blockEntity) {
                PotionContents contents = blockEntity.getPotionContents();
                if (contents != PotionContents.EMPTY) {
                    return 0xFF000000 | contents.getColor();
                }
            }
            if (node.distance() >= 8) {
                continue;
            }

            for (net.minecraft.core.Direction direction : net.minecraft.core.Direction.values()) {
                BlockPos neighbor = node.pos().relative(direction);
                if (!visited.add(neighbor)) {
                    continue;
                }
                Fluid neighborFluid = level.getFluidState(neighbor).getType();
                if (neighborFluid == ModFluids.SOURCE_POTION.get()
                        || neighborFluid == ModFluids.FLOWING_POTION.get()) {
                    queue.addLast(new SearchNode(neighbor, node.distance() + 1));
                }
            }
        }
        return color;
    }

    public static Map<Fluid, Integer> all() {
        return COLORS;
    }
}
