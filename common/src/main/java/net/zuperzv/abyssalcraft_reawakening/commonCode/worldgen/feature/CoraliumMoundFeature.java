package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;

public class CoraliumMoundFeature extends Feature<NoneFeatureConfiguration> {

    public static final CoraliumMoundFeature INSTANCE = new CoraliumMoundFeature();

    // Block layers from top to bottom (lazy-loaded to avoid registry access during class init)
    private BlockState scarletShale;
    private BlockState grimestone;
    private BlockState sitarite;

    private void ensureLoaded() {
        if (scarletShale == null) {
            scarletShale = ModBlocks.SCARLET_SHALE.block().get().defaultBlockState();
            grimestone = ModBlocks.GRIMESTONE.block().get().defaultBlockState();
            sitarite = ModBlocks.SITARITE.block().get().defaultBlockState();
        }
    }

    public CoraliumMoundFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        ensureLoaded();

        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        var random = context.random();

        // Random size: 3-5 radius
        int radius = 3 + random.nextInt(3);
        // Random height: 4-7 layers
        int height = 4 + random.nextInt(4);

        // Find the surface Y by checking upward from origin
        int surfaceY = origin.getY();
        for (int y = origin.getY(); y < origin.getY() + 20; y++) {
            BlockState state = level.getBlockState(new BlockPos(origin.getX(), y, origin.getZ()));
            if (!state.isAir() && !state.is(Blocks.WATER) && !state.is(ModFluids.CORALIUM_BLOCK.get())) {
                surfaceY = y + 1;
                break;
            }
        }

        // Place the mound
        for (int y = 0; y < height; y++) {
            int currentY = surfaceY + y;
            // Radius decreases as we go up (cone-like shape)
            int layerRadius = radius - (y * radius / height);
            if (layerRadius < 1) layerRadius = 1;

            // Determine which block type based on Y position
            BlockState mainBlock;
            if (y == 0) mainBlock = scarletShale;
            else if (y == 1) mainBlock = grimestone;
            else mainBlock = sitarite;

            for (int dx = -layerRadius; dx <= layerRadius; dx++) {
                for (int dz = -layerRadius; dz <= layerRadius; dz++) {
                    // Check distance for circular shape
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > layerRadius) continue;

                    BlockPos pos = new BlockPos(origin.getX() + dx, currentY, origin.getZ() + dz);

                    // Only place if current block is air or replaceable
                    BlockState existing = level.getBlockState(pos);
                    if (!existing.isAir() && !existing.is(BlockTags.REPLACEABLE)) continue;

                    // 10% chance to use the block from below (mixing layers)
                    BlockState blockToPlace = mainBlock;
                    if (random.nextDouble() < 0.1) {
                        if (y == 0) blockToPlace = grimestone;
                        else if (y == 1) blockToPlace = sitarite;
                    }

                    // 10% chance for Corallium Geyser if there's fluid above
                    if (random.nextDouble() < 0.1) {
                        BlockPos above = pos.above();
                        BlockState aboveState = level.getBlockState(above);
                        if (aboveState.is(ModFluids.CORALIUM_BLOCK.get())) {
                            blockToPlace = ModBlocks.CORALIUM_GEYSER.block().get().defaultBlockState();
                        }
                    }

                    level.setBlock(pos, blockToPlace, 3);
                }
            }
        }

        // Place Corallium fluid in the center
        int fluidHeight = Math.max(1, height / 3);
        for (int y = 0; y < fluidHeight; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos pos = new BlockPos(
                            origin.getX() + dx,
                            surfaceY + y,
                            origin.getZ() + dz
                    );
                    BlockState existing = level.getBlockState(pos);
                    if (existing.isAir()) {
                        level.setBlock(pos, ModFluids.CORALIUM_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }

        return true;
    }
}
