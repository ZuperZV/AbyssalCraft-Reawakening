package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.helpers;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

import java.util.ArrayList;
import java.util.List;

public class ModWorldgenPredicates {

    public static BlockPredicate nearCustomWaterPredicate(Block customWaterBlock, int blocksAway) {
        List<BlockPredicate> waterChecks = new ArrayList<>();

        for (int x = -blocksAway; x <= blocksAway; x++) {
            for (int y = -blocksAway; y <= blocksAway; y++) {
                for (int z = -blocksAway; z <= blocksAway; z++) {

                    if (x == 0 && y == 0 && z == 0)
                        continue;

                    waterChecks.add(
                            BlockPredicate.matchesBlocks(
                                    new Vec3i(x,y,z),
                                    customWaterBlock
                            )
                    );
                }
            }
        }

        return BlockPredicate.anyOf(
                waterChecks.toArray(new BlockPredicate[0])
        );
    }
}