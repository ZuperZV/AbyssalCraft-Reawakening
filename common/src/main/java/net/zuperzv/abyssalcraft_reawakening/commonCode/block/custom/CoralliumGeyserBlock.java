package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.ColorBubbleData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.CoralliumGeyserBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.state.properties.CoralliumGeyserState;
import org.jspecify.annotations.Nullable;

public class CoralliumGeyserBlock extends BaseEntityBlock {
    public static final int ALLOWED_WATER_BLOCKS_ABOVE = 4;
    public static final MapCodec<CoralliumGeyserBlock> CODEC = simpleCodec(CoralliumGeyserBlock::new);
    public static final EnumProperty<CoralliumGeyserState> STATE = EnumProperty.create("state", CoralliumGeyserState.class);

    public CoralliumGeyserBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(STATE, CoralliumGeyserState.DRY));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{STATE});
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoralliumGeyserBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return validBlockState(state, level, pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return validBlockState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    private static BlockState validBlockState(BlockState state, LevelReader level, BlockPos pos) {
        if (!level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)) {
            return state.setValue(STATE, CoralliumGeyserState.DRY);
        } else {
            BlockState belowState = level.getBlockState(pos.below());
            if (belowState.is(Blocks.OBSIDIAN) && isSourceIfFluid(belowState)) {
                return state.setValue(STATE, CoralliumGeyserState.CONTINUOUS);
            } else if (belowState.is(Blocks.MAGMA_BLOCK) && isSourceIfFluid(belowState)) {
                boolean isGeyser = state.getValue(STATE) == CoralliumGeyserState.ERUPTING || state.getValue(STATE) == CoralliumGeyserState.DORMANT;
                if (!isGeyser) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof CoralliumGeyserBlockEntity geyserEntity) {
                        geyserEntity.resetCountdown();
                    }
                }
                return state.getValue(STATE) == CoralliumGeyserState.ERUPTING ? state : state.setValue(STATE, CoralliumGeyserState.DORMANT);
            } else {
                return state.setValue(STATE, CoralliumGeyserState.WET);
            }
        }
    }

    private static boolean isSourceIfFluid(BlockState belowState) {
        FluidState fluidState = belowState.getFluidState();
        return fluidState.isEmpty() || fluidState.isSource();
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (state.getValue(STATE) == CoralliumGeyserState.ERUPTING || state.getValue(STATE) == CoralliumGeyserState.CONTINUOUS) {
            level.blockEvent(pos, this, 0, 0);
            level.playSound((Entity) null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, Context.of(state));
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(STATE) != CoralliumGeyserState.DRY && level.getFluidState(pos.above()).isSourceOfType(ModFluids.SOURCE_CORALIUM.get())) {
            spawnSulfurBubbleParticlesAt(level, random, (double) pos.getX() + 0.5, (double) (pos.getY() + 1), (double) pos.getZ() + 0.5);
            spawnSulfurBubbleParticlesAt(level, random, (double) pos.getX() + 0.5, (double) (pos.getY() + 1), (double) pos.getZ() + 0.5);
            if (random.nextInt(10) == 0) {
                level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
        }
    }

    private static void spawnSulfurBubbleParticlesAt(Level level, RandomSource random, double x, double y, double z) {
        level.addAlwaysVisibleParticle(
                new ColorBubbleData(0.9F, 0.7F, 0.15F),
                x + random.nextFloat(),
                y + random.nextFloat(),
                z + random.nextFloat(),
                0.0F,
                0.0F,
                0.0F
        );
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CoralliumGeyserBlockEntity entity) {
            entity.eruptionTick = level.getGameTime();
        }
        return true;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (type != ModBlockEntities.CORALIUM_GEYSER_BE.get()) {
            return null;
        }
        boolean client = level.isClientSide();
        CoralliumGeyserState geyserState = blockState.getValue(STATE);
        switch (geyserState) {
            case DRY:
                return null;
            case WET:
                return client ? (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.CLIENT_croasius_gas_TICKER
                        : (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.SERVER_NAUSEA_EFFECT_TICKER;
            case DORMANT:
                return client ? (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.CLIENT_croasius_gas_TICKER
                        : (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.SERVER_DORMANT_TICKER;
            case ERUPTING:
                return client ? (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.CLIENT_ERUPTING_TICKER
                        : (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.SERVER_ERUPTING_TICKER;
            case CONTINUOUS:
                return client ? (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.CLIENT_CONTINUOUS_TICKER
                        : (BlockEntityTicker<T>) CoralliumGeyserBlockEntity.SERVER_CONTINUOUS_TICKER;
            default:
                return null;
        }
    }
}
