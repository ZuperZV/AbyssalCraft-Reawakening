package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DreadvineMossBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 2);

    public DreadvineMossBlock(BlockBehaviour.Properties properties) {
        super(properties.noOcclusion().noCollision());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState north = level.getBlockState(pos.north());
        BlockState south = level.getBlockState(pos.south());
        BlockState west = level.getBlockState(pos.west());
        BlockState east = level.getBlockState(pos.east());
        BlockState below = level.getBlockState(pos.below());

        return (north.is(net.minecraft.tags.BlockTags.LOGS) || north.is(net.minecraft.tags.BlockTags.LEAVES)
                || north.is(net.minecraft.tags.BlockTags.WALLS) || north.is(net.minecraft.tags.BlockTags.MOSS_BLOCKS))
                || (south.is(net.minecraft.tags.BlockTags.LOGS) || south.is(net.minecraft.tags.BlockTags.LEAVES)
                || south.is(net.minecraft.tags.BlockTags.WALLS) || south.is(net.minecraft.tags.BlockTags.MOSS_BLOCKS))
                || (west.is(net.minecraft.tags.BlockTags.LOGS) || west.is(net.minecraft.tags.BlockTags.LEAVES)
                || west.is(net.minecraft.tags.BlockTags.WALLS) || west.is(net.minecraft.tags.BlockTags.MOSS_BLOCKS))
                || (east.is(net.minecraft.tags.BlockTags.LOGS) || east.is(net.minecraft.tags.BlockTags.LEAVES)
                || east.is(net.minecraft.tags.BlockTags.WALLS) || east.is(net.minecraft.tags.BlockTags.MOSS_BLOCKS))
                || (below.is(net.minecraft.tags.BlockTags.LOGS) || below.is(net.minecraft.tags.BlockTags.LEAVES)
                || below.is(net.minecraft.tags.BlockTags.WALLS) || below.is(net.minecraft.tags.BlockTags.MOSS_BLOCKS));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(50) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle((SimpleParticleType)ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, 0, 0, 0);
        }
    }
}
