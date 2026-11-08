package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;

import java.util.Optional;

public class GloomMossBlock extends Block implements BonemealableBlock {
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public GloomMossBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return hasSpreadableNeighbourPos(level, pos, state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // Spread moss to a neighbor block
        findSpreadableNeighbourPos(level, pos, state)
            .ifPresent(blockPos -> {
                level.setBlockAndUpdate(blockPos, this.defaultBlockState());
                // Place vegetation on top of the new moss block
                Optional<Holder.Reference<PlacedFeature>> vegetationFeature =
                        level.registryAccess()
                                .lookupOrThrow(Registries.PLACED_FEATURE)
                                .get(ModWorldgen.GLOOM_MOSS_PATCH_BONEMEAL_PLACED);

                vegetationFeature.ifPresent(holder ->
                        holder.value().place(
                                level,
                                level.getChunkSource().getGenerator(),
                                random,
                                blockPos
                        )
                );
            });
    }

    private static boolean hasSpreadableNeighbourPos(LevelReader level, BlockPos pos, BlockState state) {
        return findSpreadableNeighbourPos(level, pos, state).isPresent();
    }

    private static Optional<BlockPos> findSpreadableNeighbourPos(LevelReader level, BlockPos pos, BlockState state) {
        RandomSource random = RandomSource.create();
        int maxX = random.nextInt(2, 4);
        int maxZ = random.nextInt(2, 4);

        for (int x = -maxX; x <= maxX; x++) {
            for (int z = -maxZ; z <= maxZ; z++) {
                if (Math.abs(x) == maxX && Math.abs(z) == maxZ) continue;
                if (random.nextFloat() < 0.25f && (Math.abs(x) == maxX || Math.abs(z) == maxZ)) continue;

                for (int y = -1; y <= 5; y++) {
                    BlockPos targetPos = pos.offset(x, y, z);
                    BlockState targetState = level.getBlockState(targetPos);
                    BlockState aboveState = level.getBlockState(targetPos.above());

                    if (aboveState.isAir() && canConvert(targetState)) {
                        return Optional.of(targetPos);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static boolean canConvert(BlockState state) {
        return state.is(BlockTags.DIRT)
            || state.is(Blocks.GRASS_BLOCK)
            || state.is(Blocks.COARSE_DIRT)
            || state.is(Blocks.PODZOL)
            || state.is(Blocks.ROOTED_DIRT)
            || state.is(Blocks.MUD)
            || state.is(Blocks.MANGROVE_ROOTS)
            || state.is(Blocks.STONE)
            || state.is(Blocks.ANDESITE)
            || state.is(Blocks.DIORITE)
            || state.is(Blocks.GRANITE)
            || state.is(Blocks.DEEPSLATE)
            || state.is(Blocks.TUFF)
            || state.is(Blocks.MYCELIUM)
            || state.is(ModBlockTags.ABYSSALWASTELAND_SURFACES);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction == Direction.DOWN && !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(32) == 0) {
            findSpreadableNeighbourPos(level, pos, state)
                .ifPresent(blockPos -> level.setBlockAndUpdate(blockPos, this.defaultBlockState()));
        }
    }
}
