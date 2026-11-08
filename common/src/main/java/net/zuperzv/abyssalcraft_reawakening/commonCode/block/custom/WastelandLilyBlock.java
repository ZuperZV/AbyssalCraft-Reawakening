package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlockTags;

public class WastelandLilyBlock extends LilyPadBlock implements BonemealableBlock {
    private SimpleParticleType particleTypeForEmiting;
    private float rarityFloat;
    private boolean useSupportsAbyssVegetation = false;

    public WastelandLilyBlock(SimpleParticleType particleType, float rarity, BlockBehaviour.Properties properties) {
        super(properties);
        particleTypeForEmiting = particleType;
        rarityFloat = rarity;
    }

    public WastelandLilyBlock(SimpleParticleType particleType, float rarity, boolean canSurviveInAbyss, BlockBehaviour.Properties properties) {
        super(properties);
        particleTypeForEmiting = particleType;
        rarityFloat = rarity;
        useSupportsAbyssVegetation = canSurviveInAbyss;
    }

    @Override
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
        BonemealableBlock.findSpreadableNeighbourPos(level, pos, state).ifPresent((blockPos) -> level.setBlockAndUpdate(blockPos, this.defaultBlockState()));
    }

    @Override
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
