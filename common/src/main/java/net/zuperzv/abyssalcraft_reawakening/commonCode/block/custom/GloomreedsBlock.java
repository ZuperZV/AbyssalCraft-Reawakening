package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.jspecify.annotations.Nullable;

public class GloomreedsBlock extends VegetationBlock implements BonemealableBlock, SimpleWaterloggedBlock {

    public static final MapCodec<GloomreedsBlock> CODEC = simpleCodec(GloomreedsBlock::new);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    public static final int MAX_AGE = 3;
    public static final BooleanProperty CORALIUMLOGGED = BooleanProperty.create("coraliumlogged");
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    private static final VoxelShape BASE_SHAPE = Block.box(4, 0, 4, 12, 16, 12);
    private static final VoxelShape TOP_SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public GloomreedsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(BlockStateProperties.WATERLOGGED, false).setValue(CORALIUMLOGGED, false).setValue(TOP, true));
    }

    @Override
    protected MapCodec<? extends VegetationBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, BlockStateProperties.WATERLOGGED, CORALIUMLOGGED, TOP);
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean isMoving) {
        if (neighborBlock == this || fromPos.above().equals(pos)) {
            boolean isTop = !level.getBlockState(pos.above()).is(this);
            if (state.getValue(TOP) != isTop) {
                level.setBlock(pos, state.setValue(TOP, isTop), 3);
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return isTopSegment(state, level, pos) ? TOP_SHAPE : BASE_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return isTopSegment(state, level, pos) ? TOP_SHAPE : BASE_SHAPE;
    }

    private boolean isTopSegment(BlockState state, BlockGetter level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        return !above.is(this);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState below = level.getBlockState(pos.below());
        FluidState fluidState = level.getFluidState(pos);

        boolean isWaterlogged = fluidState.is(FluidTags.WATER);
        boolean isCoraliumlogged = fluidState.getType() == ModFluids.SOURCE_CORALIUM.get();

        boolean canPlace = false;
        if (isWaterlogged || isCoraliumlogged) {
            canPlace = true;
        } else if (below.is(this)) {
            canPlace = below.getValue(BlockStateProperties.WATERLOGGED) || below.getValue(CORALIUMLOGGED);
        }

        if (!canPlace) {
            return null;
        }

        BlockState state = this.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, isWaterlogged)
                .setValue(CORALIUMLOGGED, isCoraliumlogged)
                .setValue(TOP, true);

        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        if (canSurvive(state, level, pos)) {
            super.setPlacedBy(level, pos, state, placer, stack);

            BlockState below = level.getBlockState(pos.below());
            if (below.is(this) && below.getValue(TOP)) {
                level.setBlock(pos.below(), below.setValue(TOP, false), 3);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos attachedToPos = pos.relative(Direction.DOWN);
        BlockState attachedToState = level.getBlockState(attachedToPos);
        return !this.canAttachTo(attachedToState)
                ? false
                : attachedToState.isFaceSturdy(level, attachedToPos, Direction.UP)
                || attachedToState.is(this);
    }

    protected boolean canAttachTo(BlockState state) {
        return true;
    }


    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (entity instanceof LivingEntity && state.getValue(AGE) == 0) {
            Vec3 slowSpeed = new Vec3(0.15, 0.05F, 0.15);
            entity.makeStuckInBlock(state, slowSpeed);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age == MAX_AGE && random.nextInt(20) == 0) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.5 - 0.25;
            level.addParticle((SimpleParticleType)ParticleTypes.SWEEP_ATTACK, x, y, z, 0, 0.02, 0);
        }
        if (age == 0 && random.nextInt(30) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble() * 0.5;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle((SimpleParticleType)ParticleTypes.BUBBLE, x, y, z, 0, 0.01, 0);
        }
    }

    private void grow(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);

        if (age < MAX_AGE) {
            BlockState newState = state.setValue(AGE, age + 1);
            level.setBlock(pos, newState, 3);
        } else {
            BlockPos abovePos = pos.above();
            BlockState above = level.getBlockState(abovePos);
            FluidState aboveFluid = level.getFluidState(abovePos);
            // Allow growth into air or into water/coralium positions
            if (above.isAir() || aboveFluid.is(FluidTags.WATER) || aboveFluid.getType() == ModFluids.SOURCE_CORALIUM.get()) {
                BlockState newState = this.defaultBlockState()
                        .setValue(AGE, 0)
                        .setValue(TOP, true);
                // Set fluid state based on the fluid at the new position, or inherit from current block
                if (aboveFluid.getType() == ModFluids.SOURCE_CORALIUM.get()) {
                    newState = newState.setValue(CORALIUMLOGGED, true);
                } else if (aboveFluid.is(FluidTags.WATER)) {
                    newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
                } else if (state.getValue(CORALIUMLOGGED)) {
                    newState = newState.setValue(CORALIUMLOGGED, true);
                } else if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                    newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
                }
                level.setBlock(abovePos, newState, 3);

                level.setBlock(pos, state.setValue(TOP, false), 3);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE) {
            return true;
        }
        BlockState above = level.getBlockState(pos.above());
        return above.isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(level, random, pos, state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE) {
            if (random.nextInt(40) == 0) {
                grow(level, random, pos, state);
            }
        } else {
            if (random.nextInt(60) == 0) {
                BlockState above = level.getBlockState(pos.above());
                if (above.isAir()) {
                    grow(level, random, pos, state);
                }
            }
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            return Fluids.WATER.defaultFluidState();
        }
        if (state.getValue(CORALIUMLOGGED)) {
            return ModFluids.SOURCE_CORALIUM.get().defaultFluidState();
        }
        return super.getFluidState(state);
    }

    public boolean canGrow(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    protected net.minecraft.world.item.Item getPickItem(BlockState state) {
        return net.minecraft.world.item.Items.AIR;
    }
}