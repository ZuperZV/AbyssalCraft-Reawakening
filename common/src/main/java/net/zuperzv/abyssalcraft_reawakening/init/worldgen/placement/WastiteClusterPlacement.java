package net.zuperzv.abyssalcraft_reawakening.init.worldgen.placement;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;

import java.util.stream.Stream;

public class WastiteClusterPlacement extends PlacementModifier {

    public static final MapCodec<WastiteClusterPlacement> CODEC =
            MapCodec.unit(WastiteClusterPlacement::new);

    private static final int RADIUS = 32;

    @Override
    public Stream<BlockPos> getPositions(
            PlacementContext context,
            RandomSource random,
            BlockPos pos
    ) {
        int distance = findNearestWastiteDistance(context.getLevel(), pos);

        float chance;

        if (distance <= 4) {
            chance = 0.90F;
        } else if (distance <= 8) {
            chance = 0.65F;
        } else if (distance <= 16) {
            chance = 0.35F;
        } else if (distance <= 32) {
            chance = 0.10F;
        } else {
            chance = 0.01F;
        }

        if (random.nextFloat() <= chance) {
            return Stream.of(pos);
        }

        return Stream.empty();
    }

    private static int findNearestWastiteDistance(
            LevelSimulatedReader level,
            BlockPos origin
    ) {
        int closest = RADIUS + 1;

        MutableBlockPos checkPos = new MutableBlockPos();

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {

                if (x * x + z * z > RADIUS * RADIUS) {
                    continue;
                }

                checkPos.set(
                        origin.getX() + x,
                        origin.getY(),
                        origin.getZ() + z
                );

                if (level.isStateAtPosition(checkPos, state -> state.is(ModBlocks.WASTITE.block().get()))) {
                    int distance = (int) Math.sqrt(
                            x * x + z * z
                    );

                    closest = Math.min(closest, distance);
                }
            }
        }

        return closest;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacementModifierTypes.WASTITE_CLUSTER.get();
    }
}