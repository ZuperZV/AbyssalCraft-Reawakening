package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class TinyRootDecorator extends TreeDecorator {

    public static final MapCodec<TinyRootDecorator> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            BlockStateProvider.CODEC.fieldOf("provider")
                                    .forGetter(root -> root.provider),

                            Codec.FLOAT.fieldOf("chance")
                                    .forGetter(root -> root.chance)

                    ).apply(instance, TinyRootDecorator::new)
            );

    private final BlockStateProvider provider;
    private final float chance;

    public TinyRootDecorator(BlockStateProvider provider, float chance) {
        this.provider = provider;
        this.chance = chance;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.TINY_ROOT.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();

        BlockPos base = context.logs()
                .stream()
                .min(BlockPos::compareTo)
                .orElse(context.logs().getFirst());

        for (Direction direction : Direction.Plane.HORIZONTAL) {

            if (random.nextFloat() > chance)
                continue;

            BlockPos rootPos = base.relative(direction);

            placeRoot(context, random, rootPos);

            /*
             * 35% chance for a second root block
             */

            if (random.nextFloat() < 0.35F) {

                BlockPos secondRoot =
                        rootPos.above();

                placeRoot(context, random, secondRoot);
            }

            /*
             * 25% chance for a root down
             */

            if (random.nextFloat() < 0.25F ) {

                BlockPos belowRoot =
                        rootPos.relative(direction).below();

                placeRoot(context, random, belowRoot, false);
            }

            /*
             * Spawn root downward if air
             */

            BlockPos belowRoot = rootPos.below();

            if (context.level()
                    .getBlockState(belowRoot)
                    .isAir()) {

                placeRoot(context, random, belowRoot);
            }
        }
    }

    private void placeRoot(
            Context context,
            RandomSource random,
            BlockPos pos
    ) {
        placeRoot(context, random, pos, true);
    }


    private void placeRoot(
            Context context,
            RandomSource random,
            BlockPos pos,
            boolean needsAir
    ) {
        BlockState current = context.level().getBlockState(pos);

        if (needsAir) {
            if (!current.isAir()) {
                return;
            }
        } else {
            if (current.is(BlockTags.FEATURES_CANNOT_REPLACE)) {
                return;
            }
        }

        BlockState state =
                provider.getState(context.level(), random, pos);

        context.level().setBlock(pos, state, 19);
    }
}