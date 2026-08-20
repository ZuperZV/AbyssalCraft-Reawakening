package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FireflyBushBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;

public class ModPlantBlock extends VegetationBlock implements BonemealableBlock {
    private static final double FIREFLY_CHANCE_PER_TICK = 0.7;
    private static final double FIREFLY_HORIZONTAL_RANGE = (double)10.0F;
    private static final double FIREFLY_VERTICAL_RANGE = (double)5.0F;
    private static final int FIREFLY_SPAWN_MAX_BRIGHTNESS_LEVEL = 13;
    private static final int FIREFLY_AMBIENT_SOUND_CHANCE_ONE_IN = 30;
    public static final MapCodec<FireflyBushBlock> CODEC = simpleCodec(FireflyBushBlock::new);

    private static final VoxelShape SHAPE = Block.column((double)8.0F, (double)0.0F, (double)13.0F);
    private SimpleParticleType particleTypeForEmiting = ParticleTypes.FIREFLY;
    private float rarityFloat = 0.7f;
    private Holder<MobEffect> collisionEffectForEntity;
    private boolean useSupportsAbyssVegetation = false;

    public ModPlantBlock(SimpleParticleType particleType, float rarity, BlockBehaviour.Properties properties) {
        super(properties);
        particleTypeForEmiting = particleType;
        rarityFloat = rarity;
    }

    public ModPlantBlock(SimpleParticleType particleType, float rarity, Holder<MobEffect> collisionEffect, BlockBehaviour.Properties properties) {
        super(properties);
        particleTypeForEmiting = particleType;
        rarityFloat = rarity;
        collisionEffectForEntity = collisionEffect;
    }
    public ModPlantBlock(SimpleParticleType particleType, float rarity,  boolean canSurviveInAbyss, BlockBehaviour.Properties properties) {
        super(properties);
        particleTypeForEmiting = particleType;
        rarityFloat = rarity;
        useSupportsAbyssVegetation = canSurviveInAbyss;
    }

    public ModPlantBlock(SimpleParticleType particleType, float rarity, Holder<MobEffect> collisionEffect,  boolean canSurviveInAbyss, BlockBehaviour.Properties properties) {
        super(properties);
        particleTypeForEmiting = particleType;
        rarityFloat = rarity;
        collisionEffectForEntity = collisionEffect;
        useSupportsAbyssVegetation = canSurviveInAbyss;
    }

    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE.move(state.getOffset(pos));
    }

    protected MapCodec<? extends FireflyBushBlock> codec() {
        return CODEC;
    }


    protected void entityInside(final BlockState state, final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier effectApplier, final boolean isPrecise) {
        if (entity instanceof LivingEntity && !entity.is(EntityType.FOX) && !entity.is(EntityType.BEE)) {
            entity.makeStuckInBlock(state, new Vec3((double)0.8F, (double)0.75F, (double)0.8F));
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                Vec3 movement = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
                if (movement.horizontalDistanceSqr() > (double)0.0F) {
                    double xs = Math.abs(movement.x());
                    double zs = Math.abs(movement.z());
                    if (xs >= (double)0.003F || zs >= (double)0.003F) {
                        if (collisionEffectForEntity == MobEffects.INSTANT_DAMAGE) {
                            entity.hurtServer(serverLevel, level.damageSources().sweetBerryBush(), 1.0F); //TODO make damageSource
                        } else if (collisionEffectForEntity != null) {

                            ((LivingEntity) entity).addEffect(
                                    new MobEffectInstance(
                                            collisionEffectForEntity,
                                            100,
                                            0,
                                            false,
                                            true,
                                            true
                                    )
                            );

                        }
                    }
                }
            }
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(30) == 0 && (Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, pos) && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos) <= pos.getY()) {
            level.playLocalSound(pos, SoundEvents.FIREFLY_BUSH_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
        }

        if (level.getMaxLocalRawBrightness(pos) <= 13 && random.nextDouble() <= rarityFloat) {
            double fireflyX = (double)pos.getX() + random.nextDouble() * (double)10.0F - (double)5.0F;
            double fireflyY = (double)pos.getY() + random.nextDouble() * (double)5.0F;
            double fireflyZ = (double)pos.getZ() + random.nextDouble() * (double)10.0F - (double)5.0F;
            level.addParticle(particleTypeForEmiting, fireflyX, fireflyY, fireflyZ, (double)0.0F, (double)0.0F, (double)0.0F);
        }
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, state);
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BonemealableBlock.findSpreadableNeighbourPos(level, pos, state).ifPresent((blockPos) -> level.setBlockAndUpdate(blockPos, this.defaultBlockState()));
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        if (useSupportsAbyssVegetation) {
            return state.is(ModBlockTags.SUPPORTS_ABYSS_VEGETATION);
        } else {
            return state.is(BlockTags.SUPPORTS_VEGETATION);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return this.mayPlaceOn(level.getBlockState(below), level, below);
    }
}
