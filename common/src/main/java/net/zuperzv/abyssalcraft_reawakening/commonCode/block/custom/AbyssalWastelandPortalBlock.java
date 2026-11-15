package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.ModDimensions;
import org.jspecify.annotations.Nullable;

public class AbyssalWastelandPortalBlock extends BasePortalBlock{
    public AbyssalWastelandPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        ResourceKey<Level> newDimension = currentLevel.dimension() == ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY ? Level.OVERWORLD : ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY;
        ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
        if (newLevel == null) {
            return null;
        } else {
            boolean toNether = newLevel.dimension() == ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY;
            WorldBorder newWorldBorder = newLevel.getWorldBorder();
            double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
            BlockPos approximateExitPos = newWorldBorder.clampToBounds(entity.getX() * teleportationScale, entity.getY(), entity.getZ() * teleportationScale);
            return this.getExitPortal(newLevel, entity, portalEntryPos, approximateExitPos, toNether, newWorldBorder);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isSpawningMonsters()
                && level.environmentAttributes().getValue(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, pos)
                && random.nextInt(2000) < level.getDifficulty().getId()
                && level.anyPlayerCloseEnoughForSpawning(pos)) {
            while (level.getBlockState(pos).is(this)) {
                pos = pos.below();
            }

            if (level.getBlockState(pos).isValidSpawn(level, pos, ModEntityTypes.ABYSSAL_ZOMBIE.get())) {
                Entity entity = ModEntityTypes.ABYSSAL_ZOMBIE.get().spawn(level, pos.above(), EntitySpawnReason.STRUCTURE);
                if (entity != null) {
                    entity.setPortalCooldown();
                    Entity vehicle = entity.getVehicle();
                    if (vehicle != null) {
                        vehicle.setPortalCooldown();
                    }
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        for (int i = 0; i < 4; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            double xa = (random.nextFloat() - 0.5) * 0.5;
            double ya = (random.nextFloat() - 0.5) * 0.5;
            double za = (random.nextFloat() - 0.5) * 0.5;
            int flip = random.nextInt(2) * 2 - 1;
            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                x = pos.getX() + 0.5 + 0.25 * flip;
                xa = random.nextFloat() * 2.0F * flip;
            } else {
                z = pos.getZ() + 0.5 + 0.25 * flip;
                za = random.nextFloat() * 2.0F * flip;
            }

            level.addParticle(ParticleTypes.ASH, x, y, z, xa, ya, za);
        }
    }
}
