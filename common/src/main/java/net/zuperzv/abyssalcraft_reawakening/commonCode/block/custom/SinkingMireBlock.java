package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Sinking Mire - a custom swamp mud block that slowly sinks entities standing on it.
 * Inspired by real peat bogs. Entities gradually sink half a block when stationary.
 * Releases bubble particles as entities sink deeper.
 */
public class SinkingMireBlock extends Block implements BonemealableBlock {

    public static final IntegerProperty SINK = IntegerProperty.create("sink", 0, 4);

    private static final VoxelShape[] SHAPES = {
            Block.box(0, 0, 0, 16, 16, 16),  // sink=0 - full block
            Block.box(0, 0, 0, 16, 14, 16),  // sink=1
            Block.box(0, 0, 0, 16, 12, 16),  // sink=2
            Block.box(0, 0, 0, 16, 10, 16),  // sink=3
            Block.box(0, 0, 0, 16, 8, 16)    // sink=4 - half block
    };

    public SinkingMireBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SINK, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SINK);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(SINK)];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(SINK)];
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (entity instanceof LivingEntity && !level.isClientSide()) {
            // Slow movement on sinking mire
            Vec3 slowSpeed = new Vec3(0.2, 0.05F, 0.2);
            entity.makeStuckInBlock(state, slowSpeed);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Gradually increase sink level when entity is standing on it
        if (state.getValue(SINK) < 4) {
            BlockPos above = pos.above();
            AABB area = new AABB(above);
            boolean hasEntity = false;
            for (Entity entity : level.getEntities(null, area)) {
                if (entity instanceof LivingEntity) {
                    hasEntity = true;
                    break;
                }
            }
            if (hasEntity) {
                level.setBlock(pos, state.setValue(SINK, state.getValue(SINK) + 1), 3);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(SINK) > 0 && random.nextInt(20) == 0) {
            // Bubbles rising from sinking mud
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle((SimpleParticleType)ParticleTypes.BUBBLE_POP, x, y, z, 0, 0.02, 0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(SINK) > 0;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // Bonemeal restores sinking mire to full height
        if (state.getValue(SINK) > 0) {
            level.setBlock(pos, state.setValue(SINK, 0), 3);
        }
    }
}
