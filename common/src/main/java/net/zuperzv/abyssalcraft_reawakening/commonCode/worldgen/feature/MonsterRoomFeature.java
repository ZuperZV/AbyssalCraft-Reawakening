package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.feature;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MonsterRoomFeature extends Feature<MonsterRoomFeatureConfiguration> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float MIN_SOLID_RATIO = 0.4F;
    private static final float MAX_EXISTING_AIR_RATIO = 0.6F;

    private static final int MIN_CAVE_OPENING = 5;

    public MonsterRoomFeature(
            com.mojang.serialization.Codec<MonsterRoomFeatureConfiguration> codec
    ) {
        super(codec);
    }

    @Override
    public boolean place(
            FeaturePlaceContext<MonsterRoomFeatureConfiguration> context
    ) {
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        MonsterRoomFeatureConfiguration config = context.config();

        //get structure
        if (config.structures().isEmpty()) {
            LOGGER.error("MonsterRoomFeature has no structures configured!");
            return false;
        }

        Identifier structureId = Util.getRandom(
                config.structures().toArray(Identifier[]::new),
                random
        );

        StructureTemplateManager structureManager =
                level.getLevel()
                        .getServer()
                        .getStructureManager();

        StructureTemplate structure =
                structureManager.getOrCreate(structureId);

        //rotation
        Rotation rotation = Rotation.getRandom(random);

        Vec3i size = structure.getSize(rotation);

        if (size.getX() <= 0
                || size.getY() <= 0
                || size.getZ() <= 0) {

            LOGGER.warn(
                    "Monster room structure {} has invalid size {}",
                    structureId,
                    size
            );

            return false;
        }

        //X pos
        BlockPos lowCorner = origin.offset(
                -size.getX() / 2,
                0,
                -size.getZ() / 2
        );

        //Y pos
        int lowestSurfaceY = origin.getY();

        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {

                int height = level.getHeight(
                        net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR_WG,
                        lowCorner.getX() + x,
                        lowCorner.getZ() + z
                );

                lowestSurfaceY = Math.min(
                        lowestSurfaceY,
                        height
                );
            }
        }

        int targetY = Math.max(
                lowestSurfaceY
                        - 15
                        - random.nextInt(10),
                level.getMinY() + 10
        );

        BlockPos targetPos =
                structure.getZeroPositionWithTransform(
                        lowCorner.atY(targetY),
                        Mirror.NONE,
                        rotation
                );

        //bound
        ChunkPos chunkPos =
                ChunkPos.containing(origin);

        BoundingBox generationBounds =
                new BoundingBox(
                        chunkPos.getMinBlockX() - 16,
                        level.getMinY(),
                        chunkPos.getMinBlockZ() - 16,

                        chunkPos.getMaxBlockX() + 16,
                        level.getMaxY(),
                        chunkPos.getMaxBlockZ() + 16
                );

        StructurePlaceSettings settings =
                new StructurePlaceSettings()
                        .setRotation(rotation)
                        .setBoundingBox(generationBounds)
                        .setRandom(random);

        BoundingBox structureBounds =
                structure.getBoundingBox(
                        settings,
                        targetPos
                );

        if (structureBounds.minY() < level.getMinY()
                || structureBounds.maxY() > level.getMaxY()) {

            return false;
        }

        AreaStats areaStats =
                scanStructureArea(
                        level,
                        structureBounds
                );

        if (areaStats.airRatio() > MAX_EXISTING_AIR_RATIO) {
            return false;
        }

        if (areaStats.solidRatio() < MIN_SOLID_RATIO) {
            return false;
        }

        CaveConnection connection =
                findCaveConnection(
                        level,
                        structureBounds
                );

        if (connection == null) {
            return false;
        }

        if (connection.openingSize() < MIN_CAVE_OPENING) {
            return false;
        }

        carveStructure(
                level,
                structureBounds,
                connection
        );

        boolean placed =
                structure.placeInWorld(
                        level,
                        targetPos,
                        targetPos,
                        settings,
                        random,
                        260
                );

        if (!placed) {
            return false;
        }

        openCaveConnection(
                level,
                structureBounds,
                connection
        );

        //get spawner
        BlockPos spawnerPos =
                findSpawner(
                        level,
                        structureBounds
                );

        if (spawnerPos != null) {

            BlockEntity blockEntity =
                    level.getBlockEntity(spawnerPos);

            if (blockEntity instanceof SpawnerBlockEntity spawner) {

                spawner.setEntityId(
                        randomEntityId(random),
                        random
                );

            } else {

                LOGGER.warn(
                        "Expected spawner block entity at {}, but found {}",
                        spawnerPos,
                        blockEntity
                );
            }

        } else {

            LOGGER.warn(
                    "Monster room {} was placed without a spawner!",
                    structureId
            );
        }

        return true;
    }

    private static AreaStats scanStructureArea(
            WorldGenLevel level,
            BoundingBox bounds
    ) {
        int solid = 0;
        int air = 0;
        int total = 0;

        BlockPos.MutableBlockPos pos =
                new BlockPos.MutableBlockPos();

        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
                for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {

                    pos.set(x, y, z);

                    BlockState state =
                            level.getBlockState(pos);

                    total++;

                    if (state.isAir()) {
                        air++;
                    } else if (state.isSolid()) {
                        solid++;
                    }
                }
            }
        }

        if (total == 0) {
            return new AreaStats(0.0F, 0.0F);
        }

        return new AreaStats(
                (float) solid / total,
                (float) air / total
        );
    }

    //Cave Connection
    private static CaveConnection findCaveConnection(
            WorldGenLevel level,
            BoundingBox bounds
    ) {
        CaveConnection result;

        result = findConnectionOnSide(
                level,
                bounds,
                Direction.NORTH
        );

        if (result != null) {
            return result;
        }

        result = findConnectionOnSide(
                level,
                bounds,
                Direction.SOUTH
        );

        if (result != null) {
            return result;
        }

        result = findConnectionOnSide(
                level,
                bounds,
                Direction.WEST
        );

        if (result != null) {
            return result;
        }

        return findConnectionOnSide(
                level,
                bounds,
                Direction.EAST
        );
    }

    private static CaveConnection findConnectionOnSide(
            WorldGenLevel level,
            BoundingBox bounds,
            Direction side
    ) {
        List<BlockPos> candidates =
                new ArrayList<>();

        if (side == Direction.NORTH
                || side == Direction.SOUTH) {

            int z =
                    side == Direction.NORTH
                            ? bounds.minZ() - 1
                            : bounds.maxZ() + 1;

            for (int x = bounds.minX() + 1;
                 x < bounds.maxX();
                 x++) {

                for (int y = bounds.minY() + 1;
                     y < bounds.maxY() - 1;
                     y++) {

                    BlockPos cavePos =
                            new BlockPos(x, y, z);

                    if (!isCaveAir(level, cavePos)) {
                        continue;
                    }

                    if (!isCaveAir(
                            level,
                            cavePos.above()
                    )) {
                        continue;
                    }

                    candidates.add(cavePos);
                }
            }

        } else {

            int x =
                    side == Direction.WEST
                            ? bounds.minX() - 1
                            : bounds.maxX() + 1;

            for (int z = bounds.minZ() + 1;
                 z < bounds.maxZ();
                 z++) {

                for (int y = bounds.minY() + 1;
                     y < bounds.maxY() - 1;
                     y++) {

                    BlockPos cavePos =
                            new BlockPos(x, y, z);

                    if (!isCaveAir(level, cavePos)) {
                        continue;
                    }

                    if (!isCaveAir(
                            level,
                            cavePos.above()
                    )) {
                        continue;
                    }

                    candidates.add(cavePos);
                }
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        List<BlockPos> best =
                findLargestOpening(
                        level,
                        candidates,
                        side
                );

        if (best.size() < MIN_CAVE_OPENING) {
            return null;
        }

        if (best.size() > 6) {
            best = new ArrayList<>(
                    best.subList(0, 6)
            );
        }

        return new CaveConnection(
                side,
                best
        );
    }

    private static List<BlockPos> findLargestOpening(
            WorldGenLevel level,
            List<BlockPos> candidates,
            Direction side
    ) {
        List<BlockPos> best =
                new ArrayList<>();

        List<BlockPos> remaining =
                new ArrayList<>(candidates);

        while (!remaining.isEmpty()) {

            BlockPos start =
                    remaining.remove(0);

            List<BlockPos> group =
                    new ArrayList<>();

            List<BlockPos> queue =
                    new ArrayList<>();

            queue.add(start);

            while (!queue.isEmpty()) {

                BlockPos current =
                        queue.remove(0);

                if (!group.contains(current)) {
                    group.add(current);
                }

                for (Direction direction
                        : connectionDirections(side)) {

                    BlockPos next =
                            current.relative(direction);

                    if (remaining.remove(next)) {
                        queue.add(next);
                    }
                }
            }

            if (group.size() > best.size()) {
                best = group;
            }
        }

        return best;
    }

    private static Direction[] connectionDirections(
            Direction side
    ) {
        if (side == Direction.NORTH
                || side == Direction.SOUTH) {

            return new Direction[]{
                    Direction.EAST,
                    Direction.WEST,
                    Direction.UP,
                    Direction.DOWN
            };
        }

        return new Direction[]{
                Direction.NORTH,
                Direction.SOUTH,
                Direction.UP,
                Direction.DOWN
        };
    }

    private static boolean isCaveAir(
            WorldGenLevel level,
            BlockPos pos
    ) {
        BlockState state =
                level.getBlockState(pos);

        return state.isAir()
                || state.is(Blocks.CAVE_AIR)
                || state.is(Blocks.VOID_AIR)
                || state.is(Blocks.WATER)
                || state.is(Blocks.LAVA);
    }

    private static void carveStructure(
            WorldGenLevel level,
            BoundingBox bounds,
            CaveConnection connection
    ) {
        BlockPos.MutableBlockPos pos =
                new BlockPos.MutableBlockPos();

        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
                for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {

                    pos.set(x, y, z);

                    if (level.getBlockEntity(pos) != null) {
                        continue;
                    }

                    if (isBoundary(bounds, x, y, z)) {
                        continue;
                    }

                    BlockState state =
                            level.getBlockState(pos);

                    if (state.isAir()) {
                        continue;
                    }

                    if (state.isSolid()) {
                        level.setBlock(
                                pos,
                                Blocks.CAVE_AIR.defaultBlockState(),
                                2
                        );
                    }
                }
            }
        }
    }

    private static void openCaveConnection(
            WorldGenLevel level,
            BoundingBox bounds,
            CaveConnection connection
    ) {
        for (BlockPos cavePos : connection.opening()) {

            BlockPos wallPos =
                    cavePos.relative(connection.side().getOpposite());

            if (bounds.isInside(wallPos)) {

                level.setBlock(
                        wallPos,
                        Blocks.CAVE_AIR.defaultBlockState(),
                        2
                );
            }

            BlockPos innerPos =
                    wallPos.relative(connection.side());

            if (bounds.isInside(innerPos)) {

                level.setBlock(
                        innerPos,
                        Blocks.CAVE_AIR.defaultBlockState(),
                        2
                );
            }

            BlockPos above =
                    wallPos.above();

            if (bounds.isInside(above)) {

                level.setBlock(
                        above,
                        Blocks.CAVE_AIR.defaultBlockState(),
                        2
                );
            }
        }
    }

    private static boolean isBoundary(
            BoundingBox bounds,
            int x,
            int y,
            int z
    ) {
        return x == bounds.minX()
                || x == bounds.maxX()
                || y == bounds.minY()
                || y == bounds.maxY()
                || z == bounds.minZ()
                || z == bounds.maxZ();
    }

    private static BlockPos findSpawner(
            WorldGenLevel level,
            BoundingBox bounds
    ) {
        BlockPos.MutableBlockPos pos =
                new BlockPos.MutableBlockPos();

        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
                for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {

                    pos.set(x, y, z);

                    if (level.getBlockState(pos)
                            .is(Blocks.SPAWNER)) {

                        return pos.immutable();
                    }
                }
            }
        }

        return null;
    }

    private static EntityType<?> randomEntityId(
            RandomSource random
    ) {
        return switch (random.nextInt(5)) {
            case 0, 1 ->
                    ModEntityTypes.ABYSSAL_ZOMBIE.get();

            case 2 ->
                    ModEntityTypes.GROUNDLING.get();

            case 3 ->
                    EntityType.SPIDER;

            case 4 ->
                    EntityType.ZOMBIE;

            default ->
                    ModEntityTypes.ABYSSAL_ZOMBIE.get();
        };
    }

    private record AreaStats(
            float solidRatio,
            float airRatio
    ) {
    }

    private record CaveConnection(
            Direction side,
            List<BlockPos> opening
    ) {
        int openingSize() {
            return opening.size();
        }
    }
}