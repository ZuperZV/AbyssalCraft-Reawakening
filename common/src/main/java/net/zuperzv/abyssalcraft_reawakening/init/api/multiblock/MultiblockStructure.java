package net.zuperzv.abyssalcraft_reawakening.init.api.multiblock;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MultiblockStructure {

    private static final Map<
            Identifier,
            MultiblockStructure
            > CACHE =
            new HashMap<>();

    private final Identifier id;
    private final List<BlockEntry> blocks;
    private final Vec3iSize size;

    private final Map<
            Integer,
            List<BlockEntry>
            > layers;

    private final Map<
            Block,
            Integer
            > totalCounts;

    private MultiblockStructure(
            Identifier id,
            Vec3iSize size,
            List<BlockEntry> blocks
    ) {
        this.id = id;
        this.size = size;
        this.blocks =
                List.copyOf(blocks);

        this.layers =
                new HashMap<>();

        this.totalCounts =
                new HashMap<>();

        for (
                BlockEntry entry
                : blocks
        ) {
            layers.computeIfAbsent(
                    entry.pos().getY(),
                    ignored ->
                            new ArrayList<>()
            ).add(entry);

            totalCounts.merge(
                    entry.state().getBlock(),
                    1,
                    Integer::sum
            );
        }

        for (
                List<BlockEntry> layer
                : layers.values()
        ) {
            layer.sort(
                    Comparator
                            .comparingInt(
                                    (BlockEntry entry) ->
                                            entry.pos().getZ()
                            )
                            .thenComparingInt(
                                    entry ->
                                            entry.pos().getX()
                            )
            );
        }
    }

    public static MultiblockStructure load(
            Identifier id
    ) {
        MultiblockStructure cached =
                CACHE.get(id);

        if (cached != null) {
            return cached;
        }

        MultiblockStructure structure =
                loadInternal(id);

        if (structure != null) {
            CACHE.put(
                    id,
                    structure
            );
        }

        return structure;
    }

    private static MultiblockStructure loadInternal(
            Identifier id
    ) {
        Minecraft mc =
                Minecraft.getInstance();

        if (mc.getResourceManager() == null) {
            return null;
        }

        Identifier resourceId =
                Constants.id(
                        "structure/" +
                                id.getPath() +
                                ".nbt"
                );

        Optional<Resource> optionalResource =
                mc.getResourceManager()
                        .getResource(resourceId);

        if (optionalResource.isEmpty()) {
            System.out.println(
                    "Could not find multiblock structure: " +
                            resourceId
            );

            return null;
        }

        try (
                var input =
                        optionalResource
                                .get()
                                .open()
        ) {
            CompoundTag root =
                    NbtIo.readCompressed(
                            input,
                            NbtAccounter.unlimitedHeap()
                    );

            ListTag sizeTag =
                    root.getListOrEmpty(
                            "size"
                    );

            if (sizeTag.size() < 3) {
                throw new IllegalStateException(
                        "Structure has invalid size tag: " +
                                id
                );
            }

            int sizeX =
                    getInt(
                            sizeTag,
                            0
                    );

            int sizeY =
                    getInt(
                            sizeTag,
                            1
                    );

            int sizeZ =
                    getInt(
                            sizeTag,
                            2
                    );

            Vec3iSize size =
                    new Vec3iSize(
                            sizeX,
                            sizeY,
                            sizeZ
                    );

            if (mc.level == null) {
                throw new IllegalStateException(
                        "No level loaded"
                );
            }

            RegistryAccess registryAccess =
                    mc.level.registryAccess();

            HolderLookup.RegistryLookup<Block> blockLookup =
                    registryAccess.lookupOrThrow(
                            Registries.BLOCK
                    );

            ListTag palette =
                    root.getListOrEmpty(
                            "palette"
                    );

            ListTag blocksTag =
                    root.getListOrEmpty(
                            "blocks"
                    );

            List<BlockState> paletteStates =
                    new ArrayList<>(
                            palette.size()
                    );

            for (
                    int i = 0;
                    i < palette.size();
                    i++
            ) {
                CompoundTag stateTag =
                        getCompound(
                                palette,
                                i
                        );

                BlockState state =
                        NbtUtils.readBlockState(
                                blockLookup,
                                stateTag
                        );

                paletteStates.add(state);
            }

            List<BlockEntry> blocks =
                    new ArrayList<>();

            for (
                    int i = 0;
                    i < blocksTag.size();
                    i++
            ) {
                CompoundTag blockTag =
                        getCompound(
                                blocksTag,
                                i
                        );

                int paletteIndex =
                        getCompoundInt(
                                blockTag,
                                "state",
                                -1
                        );

                if (
                        paletteIndex < 0
                                ||
                                paletteIndex >=
                                        paletteStates.size()
                ) {
                    continue;
                }

                ListTag posTag =
                        blockTag.getListOrEmpty(
                                "pos"
                        );

                if (posTag.size() < 3) {
                    continue;
                }

                int posX =
                        getInt(
                                posTag,
                                0
                        );

                int posY =
                        getInt(
                                posTag,
                                1
                        );

                int posZ =
                        getInt(
                                posTag,
                                2
                        );

                BlockPos pos =
                        new BlockPos(
                                posX,
                                posY,
                                posZ
                        );

                BlockState state =
                        paletteStates.get(
                                paletteIndex
                        );

                if (state.isAir()) {
                    continue;
                }

                blocks.add(
                        new BlockEntry(
                                pos,
                                state
                        )
                );
            }

            return new MultiblockStructure(
                    id,
                    size,
                    blocks
            );

        } catch (
                IOException |
                RuntimeException exception
        ) {
            System.out.println(
                    "Failed to load multiblock structure: " +
                            id +
                            " exception: " +
                            exception
            );

            return null;
        }
    }

    private static int getInt(
            ListTag tag,
            int index
    ) {
        return tag
                .getInt(index)
                .orElse(0);
    }

    private static CompoundTag getCompound(
            ListTag tag,
            int index
    ) {
        return tag
                .getCompound(index)
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        "Expected CompoundTag at index " +
                                                index
                                )
                );
    }

    private static int getCompoundInt(
            CompoundTag tag,
            String name,
            int fallback
    ) {
        return tag
                .getInt(name)
                .orElse(fallback);
    }

    public Identifier id() {
        return id;
    }

    public Vec3iSize size() {
        return size;
    }

    public int height() {
        return size.y();
    }

    public List<BlockEntry> blocks() {
        return blocks;
    }

    public List<BlockEntry> getLayer(
            int layer
    ) {
        return layers.getOrDefault(
                layer,
                List.of()
        );
    }

    public Map<Block, Integer> getCountsForLayer(
            int layer
    ) {
        Map<Block, Integer> result =
                new HashMap<>();

        for (
                BlockEntry entry
                : getLayer(layer)
        ) {
            result.merge(
                    entry.state().getBlock(),
                    1,
                    Integer::sum
            );
        }

        return result;
    }

    public Map<Block, Integer> getTotalCounts() {
        return Collections.unmodifiableMap(
                totalCounts
        );
    }

    public record BlockEntry(
            BlockPos pos,
            BlockState state
    ) {
    }

    public record Vec3iSize(
            int x,
            int y,
            int z
    ) {
    }
}