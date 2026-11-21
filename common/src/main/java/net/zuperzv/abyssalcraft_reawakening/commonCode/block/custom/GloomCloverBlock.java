package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;

public class GloomCloverBlock extends VegetationBlock implements BonemealableBlock {

    public static final MapCodec<GloomCloverBlock> CODEC = simpleCodec(GloomCloverBlock::new);
    public static final IntegerProperty FACING = IntegerProperty.create("facing", 0, 3);
    public static final BooleanProperty FOUR_LEAF = BooleanProperty.create("four_leaf");

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 3, 14);

    public GloomCloverBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, 0)
                .setValue(FOUR_LEAF, false));
    }

    @Override
    protected MapCodec<? extends VegetationBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FOUR_LEAF);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState ground = level.getBlockState(below);
        return ground.is(ModBlockTags.SWAMP_SOIL) || ground.is(net.minecraft.tags.BlockTags.DIRT)
                || ground.is(net.minecraft.tags.BlockTags.SAND) || ground.is(net.minecraft.tags.BlockTags.MUD)
                || ground.is(net.minecraft.tags.BlockTags.GRASS_BLOCKS);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(FOUR_LEAF) && random.nextInt(25) == 0) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            level.addParticle((SimpleParticleType)ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0.02, 0);
        } else if (!state.getValue(FOUR_LEAF) && random.nextInt(40) == 0) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            double y = pos.getY() + 0.3;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            level.addParticle((SimpleParticleType)ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BonemealableBlock.findSpreadableNeighbourPos(level, pos, state)
                .ifPresent(blockPos -> {
                    boolean isFourLeaf = random.nextInt(50) == 0; // 2% chance
                    BlockState newState = this.defaultBlockState()
                            .setValue(FACING, random.nextInt(4))
                            .setValue(FOUR_LEAF, isFourLeaf);
                    level.setBlockAndUpdate(blockPos, newState);
                });
    }
}
