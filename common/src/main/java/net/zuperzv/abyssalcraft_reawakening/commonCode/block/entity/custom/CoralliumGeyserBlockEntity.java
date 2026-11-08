package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.zuperzv.abyssalcraft_reawakening.commonCode.particle.ColorBubbleData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.CoralliumGeyserBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.state.properties.CoralliumGeyserState;
import org.jspecify.annotations.Nullable;

public class CoralliumGeyserBlockEntity extends BlockEntity {
    private static final int EFFECT_APPLICATION_FREQUENCY_TICKS = 10;
    private static final int EFFECT_DURATION_IN_TICKS = 80;
    public static final float EFFECT_RANGE = 3.0F;
    private static final Predicate<Entity> EFFECT_PREDICATE;
    public static final int PARTICLE_FREQUENCY_TICKS = 20;
    public static final int SOUND_FREQUENCY_TICKS = 40;
    public int waitingCountdown = -1;
    public long eruptionTick = -1L;
    public static final long GEYSER_SALT = -904011478L;

    // Server tickers
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> SERVER_NAUSEA_EFFECT_TICKER;
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> SERVER_DORMANT_TICKER;
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> SERVER_ERUPTING_TICKER;
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> SERVER_CONTINUOUS_TICKER;

    // Client tickers
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> CLIENT_croasius_gas_TICKER;
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> CLIENT_ERUPTING_TICKER;
    public static BlockEntityTicker<CoralliumGeyserBlockEntity> CLIENT_CONTINUOUS_TICKER;

    public CoralliumGeyserBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.CORALIUM_GEYSER_BE.get(), worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("countdown", this.waitingCountdown);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.getInt("countdown").ifPresent((value) -> this.waitingCountdown = value);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.eruptionTick == -1L) {
            this.eruptionTick = level.getGameTime();
        }
    }

    public void resetCountdown() {
        this.waitingCountdown = -1;
    }

    private static void applyNauseaEffect(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 80, 0, true, true));
    }

    private static List<LivingEntity> getNearbyLivingEntities(Level level, BlockPos pos) {
        AABB aabb = (new AABB(pos)).inflate((double) 2.5F, (double) 0.0F, (double) 2.5F);
        return level.getEntitiesOfClass(LivingEntity.class, aabb, EFFECT_PREDICATE);
    }

    public static RandomSource geyserPositional(ServerLevel level, BlockPos pos) {
        return (new XoroshiroRandomSource(level.getSeed() ^ GEYSER_SALT)).forkPositional().at(pos);
    }

    private static void spawnGeyserParticle(Level level, BlockPos sourcePos) {
        ColorBubbleData data = new ColorBubbleData(0.9F, 0.7F, 0.15F);
        level.addAlwaysVisibleParticle(
                data,
                (double) sourcePos.getX() + 0.5F,
                (double) sourcePos.getY(),
                (double) sourcePos.getZ() + 0.5F,
                0.0F,
                0.005F,
                0.0F
        );
    }

    private static void spawnNoxiousGasCloudParticle(Level level, Vec3 pos) {
        ColorBubbleData data = new ColorBubbleData(0.5F, 0.3F, 0.6F);
        level.addAlwaysVisibleParticle(
                data,
                pos.x,
                pos.y,
                pos.z,
                0.0F,
                0.0025F,
                0.0F
        );
    }

    private static int getUnobstructedBlockCount(Level level, BlockPos pos, int waterBlocks) {
        int geyserForceHeight = 6 * waterBlocks;
        CollisionContext context = CollisionContext.empty();

        for (int i = 0; i < geyserForceHeight; ++i) {
            BlockPos currentPos = pos.above(i);
            BlockState state = level.getBlockState(currentPos);
            if (!isGeyserPassableBlock(state, level, currentPos, context)) {
                return i;
            }
        }

        return geyserForceHeight;
    }

    private static boolean isGeyserPassableBlock(BlockState state, Level level, BlockPos pos, CollisionContext context) {
        return !state.isAir() && !state.is(Blocks.WATER) ? state.getCollisionShape(level, pos, context).isEmpty() : true;
    }

    private static @Nullable BlockPos findNoxiousGasSourceBlock(Level level, BlockPos origin) {
        int maxY = origin.getY() + 4 + 1;
        CollisionContext context = CollisionContext.empty();
        BlockPos.MutableBlockPos pos = origin.above(1).mutable();

        while (true) {
            if (pos.getY() <= maxY) {
                BlockState state = level.getBlockState(pos);
                boolean isWaterLogged = level.getFluidState(pos).isSourceOfType(Fluids.WATER);
                if (isWaterLogged && (state.is(Blocks.WATER) || isGeyserPassableBlock(state, level, pos, context))) {
                    pos.move(Direction.UP);
                    continue;
                }

                if (state.isAir() || isGeyserPassableBlock(state, level, pos, context)) {
                    return pos.immutable();
                }
            }

            return null;
        }
    }

    public static boolean canBeReachedByNoxiousGas(Level level, BlockPos sourceBlock, Vec3 pos) {
        BlockPos blockPos = BlockPos.containing(pos);
        CollisionContext context = CollisionContext.empty();
        if (!isGeyserPassableBlock(level.getBlockState(blockPos), level, blockPos, context)) {
            return false;
        } else if (pos.distanceToSqr(Vec3.atCenterOf(sourceBlock)) > (double) 9.0F) {
            return false;
        } else {
            Vec3 belowSource = Vec3.atCenterOf(sourceBlock.below());
            Vec3 belowPos = pos.with(Axis.Y, pos.y - (double) 1.0F);
            return isWater(level, belowPos) && haveLineOfSight(level, belowSource, belowPos);
        }
    }

    private static boolean isWater(Level level, Vec3 pos) {
        return level.getFluidState(BlockPos.containing(pos)).isSourceOfType(Fluids.WATER);
    }

    private static boolean haveLineOfSight(Level level, Vec3 a, Vec3 b) {
        HitResult hitResult = level.clip(new ClipContext(a, b, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
        return hitResult.getType() != Type.BLOCK;
    }

    private static void launchEntities(Level level, BlockPos pos) {
        BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
        if (sourceBlock != null) {
            int waterBlocks = sourceBlock.getY() - pos.getY() - 1;
            int geyserForceHeight = getUnobstructedBlockCount(level, pos.above(), waterBlocks);
            AABB aabb = (new AABB(pos.above())).expandTowards((double) 0.0F, (double) (geyserForceHeight - 1), (double) 0.0F);

            for (Entity entityToBeLaunched : level.getEntitiesOfClass(Entity.class, aabb, EFFECT_PREDICATE)) {
                Vec3 entityVelocity = entityToBeLaunched.getDeltaMovement();
                entityToBeLaunched.checkFallDistanceAccumulation();
                if (entityToBeLaunched.canSimulateMovement()) {
                    if (entityToBeLaunched instanceof Player player && player.getAbilities().flying) {
                        continue;
                    }

                    if (!entityToBeLaunched.isPassenger() && entityVelocity.y < (double) 0.3F + (double) waterBlocks * 0.1) {
                        entityToBeLaunched.addDeltaMovement(new Vec3((double) 0.0F, (double) 0.2F, (double) 0.0F));
                    }
                }
            }
        }
    }

    private static void doNauseaEffect(Level level, BlockPos pos) {
        BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
        if (sourceBlock != null) {
            for (LivingEntity entity : getNearbyLivingEntities(level, sourceBlock)) {
                if (canBeReachedByNoxiousGas(level, sourceBlock, entity.getEyePosition())) {
                    applyNauseaEffect(entity);
                }
            }
        }
    }

    private static void doCountdown(Level level, BlockPos pos, BlockState state, CoralliumGeyserBlockEntity geyser) {
        BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
        if (sourceBlock != null) {
            if (geyser.waitingCountdown <= 0) {
                int waterBlocks = sourceBlock.getY() - pos.getY() - 1;
                RandomSource random = geyserPositional((ServerLevel) level, pos);
                if (state.getValue(CoralliumGeyserBlock.STATE) == CoralliumGeyserState.DORMANT) {
                    geyser.waitingCountdown = 10 * (waterBlocks - 1) + random.nextIntBetweenInclusive(15, 30);
                } else {
                    random.nextInt();
                    geyser.waitingCountdown = waterBlocks - 1 + random.nextIntBetweenInclusive(1, 2);
                }
            }

            if (geyser.waitingCountdown > 0) {
                --geyser.waitingCountdown;
            }

            if (geyser.waitingCountdown == 0) {
                CoralliumGeyserState stateToSet = state.getValue(CoralliumGeyserBlock.STATE) == CoralliumGeyserState.DORMANT ? CoralliumGeyserState.ERUPTING : CoralliumGeyserState.DORMANT;
                level.setBlockAndUpdate(pos, (BlockState) state.setValue(CoralliumGeyserBlock.STATE, stateToSet));
                if (stateToSet == CoralliumGeyserState.DORMANT) {
                    level.gameEvent(GameEvent.BLOCK_DEACTIVATE, pos, Context.of(state));
                }
            }
        }
    }

    static {
        EFFECT_PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE);

        // WET state: server applies nausea
        SERVER_NAUSEA_EFFECT_TICKER = (level, pos, state, geyser) -> {
            if (level.getGameTime() % EFFECT_APPLICATION_FREQUENCY_TICKS == 0L) {
                doNauseaEffect(level, pos);
            }
        };

        // Client: spawns noxious gas cloud particles
        CLIENT_croasius_gas_TICKER = (level, pos, state, geyser) -> {
            if (level.getGameTime() % PARTICLE_FREQUENCY_TICKS == 0L) {
                BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
                if (sourceBlock != null) {
                    spawnNoxiousGasCloudParticle(level, Vec3.atCenterOf(sourceBlock));
                }
            }
        };

        // DORMANT: server does countdown + nausea
        SERVER_DORMANT_TICKER = (level, pos, state, geyser) -> {
            if (level.getGameTime() % 20L == 0L) {
                doCountdown(level, pos, state, geyser);
            }
            if (level.getGameTime() % EFFECT_APPLICATION_FREQUENCY_TICKS == 0L) {
                doNauseaEffect(level, pos);
            }
        };

        // ERUPTING server: launch entities + countdown back to dormant
        SERVER_ERUPTING_TICKER = (level, pos, state, geyser) -> {
            launchEntities(level, pos);
            if (level.getGameTime() % 20L == 0L) {
                doCountdown(level, pos, state, geyser);
            }
        };

        // ERUPTING client: particles + sound
        CLIENT_ERUPTING_TICKER = (level, pos, state, geyser) -> {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                long eruptionTime = level.getGameTime() - geyser.eruptionTick;
                if (eruptionTime % PARTICLE_FREQUENCY_TICKS == 0L) {
                    spawnGeyserParticle(level, sourceBlock);
                }
                if (eruptionTime % SOUND_FREQUENCY_TICKS == 0L) {
                    level.playLocalSound(
                            (double) sourceBlock.getX() + 0.5F,
                            (double) sourceBlock.getY() + 0.5F,
                            (double) sourceBlock.getZ() + 0.5F,
                            SoundEvents.FLINTANDSTEEL_USE,
                            SoundSource.BLOCKS, 1.0F, 1.0F, false
                    );
                }
            }
        };

        // CONTINUOUS server: always launch entities
        SERVER_CONTINUOUS_TICKER = (level, pos, state, geyser) -> {
            launchEntities(level, pos);
        };

        // CONTINUOUS client: particles + sound
        CLIENT_CONTINUOUS_TICKER = (level, pos, state, geyser) -> {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                long eruptionTime = level.getGameTime() - geyser.eruptionTick;
                if (eruptionTime % PARTICLE_FREQUENCY_TICKS == 0L) {
                    spawnGeyserParticle(level, sourceBlock);
                }
                if (eruptionTime % SOUND_FREQUENCY_TICKS == 0L) {
                    level.playLocalSound(
                            (double) sourceBlock.getX() + 0.5F,
                            (double) sourceBlock.getY() + 0.5F,
                            (double) sourceBlock.getZ() + 0.5F,
                            SoundEvents.FLINTANDSTEEL_USE,
                            SoundSource.BLOCKS, 1.0F, 1.0F, false
                    );
                }
            }
        };
    }
}
