package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class PotionFluidBlockEntity extends BlockEntity {
    private PotionContents potionContents = PotionContents.EMPTY;

    public PotionFluidBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_FLUID_BE.get(), pos, state);
    }

    public PotionContents getPotionContents() {
        return potionContents;
    }

    public void setPotionContents(PotionContents potionContents) {
        PotionContents contents = potionContents == null
                ? PotionContents.EMPTY
                : potionContents;
        if (this.potionContents.equals(contents)) {
            return;
        }
        this.potionContents = contents;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            if (contents != PotionContents.EMPTY) {
                propagateToExistingFluid(level, contents);
            }
        }
    }

    private void setPropagatedContents(PotionContents contents) {
        if (!potionContents.equals(contents)) {
            potionContents = contents;
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    private void propagateToExistingFluid(Level level, PotionContents contents) {
        record SearchNode(BlockPos pos, int distance) {}
        ArrayDeque<SearchNode> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(new SearchNode(worldPosition, 0));
        visited.add(worldPosition);

        while (!queue.isEmpty()) {
            SearchNode node = queue.removeFirst();
            if (node.distance() >= 8) {
                continue;
            }

            for (Direction direction : Direction.values()) {
                BlockPos next = node.pos().relative(direction);
                if (!visited.add(next) || !level.hasChunkAt(next)) {
                    continue;
                }

                Fluid fluid = level.getFluidState(next).getType();
                if (fluid != ModFluids.SOURCE_POTION.get()
                        && fluid != ModFluids.FLOWING_POTION.get()) {
                    continue;
                }

                if (!(level.getBlockEntity(next) instanceof PotionFluidBlockEntity adjacent)) {
                    continue;
                }

                PotionContents adjacentContents = adjacent.getPotionContents();
                if (adjacentContents != PotionContents.EMPTY
                        && !adjacentContents.equals(contents)) {
                    continue;
                }

                adjacent.setPropagatedContents(contents);
                queue.addLast(new SearchNode(next, node.distance() + 1));
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (potionContents != PotionContents.EMPTY) {
            output.store("PotionContents", PotionContents.CODEC, potionContents);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        potionContents = input.read("PotionContents", PotionContents.CODEC)
                .orElse(PotionContents.EMPTY);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
