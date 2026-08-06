package net.zuperzv.abyssalcraft_reawakening.init.worldgen.placement;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class NoFluidBelowPlacement extends PlacementFilter {

    public static final MapCodec<NoFluidBelowPlacement> CODEC =
            MapCodec.unit(new NoFluidBelowPlacement());

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacementModifierTypes.NO_FLUID_BELOW.get();
    }

    @Override
    protected boolean shouldPlace(
            PlacementContext context,
            RandomSource random,
            BlockPos pos
    ) {

        var level = context.getLevel();

        BlockPos checkPos = new BlockPos(pos.getX() + 8, pos.getY(), pos.getZ() + 8);

        BlockPos.MutableBlockPos check = checkPos.mutable();

        for (int i = 1; i <= 254; i++) {

            check.setY(checkPos.getY() - i);

            var state = level.getBlockState(check);
            var fluid = level.getFluidState(check);

            if (fluid.is(net.minecraft.tags.FluidTags.WATER)
                    || fluid.is(net.minecraft.tags.FluidTags.LAVA)) {
                return false;
            }

            if (state.isAir()
                    || state.is(net.minecraft.world.level.block.Blocks.LIGHT)
                    || state.is(net.minecraft.world.level.block.Blocks.JUNGLE_LEAVES)
                    ) {

                continue;
            }

            System.out.println("return true at: " + checkPos);
            return true;
        }

        return false;
    }
}