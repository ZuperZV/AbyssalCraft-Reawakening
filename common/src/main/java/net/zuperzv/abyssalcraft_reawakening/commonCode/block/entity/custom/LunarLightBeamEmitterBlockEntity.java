package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.LunarLightBeamEmitterBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;

public class LunarLightBeamEmitterBlockEntity extends BlockEntity {
    public static final int MAX_BEAM_TICKS = 40;
    private static final int MAX_BEAM_LENGTH = 32;

    private int beamLength;
    private int beamTicksRemaining;
    private boolean beamBlocked;

    public LunarLightBeamEmitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LUNAR_LIGHT_BEAM_EMITTER_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LunarLightBeamEmitterBlockEntity emitter) {
        if (level.isClientSide()) return;

        Direction facing = state.getValue(LunarLightBeamEmitterBlock.FACING);
        BlockPos inputPos = pos.relative(facing.getOpposite());
        BlockEntity input = level.getBlockEntity(inputPos);
        if (input instanceof PEMachineBlockEntity source && source.getStoredPE() > 0) {
            BeamPath path = findBeamPath(level, pos, facing);
            int amountTransferred = 0;
            if (path.target() != null) {
                int accepted = path.target().insertPE(1);
                if (accepted > 0) {
                    amountTransferred = source.extractPE(accepted);
                }
            } else {
                amountTransferred = source.extractPE(1);
            }

            if (amountTransferred > 0) {
                boolean changed = emitter.beamLength != path.length()
                        || emitter.beamBlocked != path.blocked()
                        || emitter.beamTicksRemaining != MAX_BEAM_TICKS;
                emitter.beamLength = path.length();
                emitter.beamBlocked = path.blocked();
                emitter.beamTicksRemaining = MAX_BEAM_TICKS;
                if (changed) emitter.sync();
                return;
            }

            if (path.length() != emitter.beamLength) {
                emitter.beamLength = path.length();
                emitter.beamBlocked = path.blocked();
                emitter.sync();
            }
        }

        if (emitter.beamTicksRemaining > 0) {
            emitter.beamTicksRemaining--;
            emitter.sync();
            if (emitter.beamTicksRemaining == 0) {
                emitter.beamLength = 0;
            }
        }
    }

    private static BeamPath findBeamPath(Level level, BlockPos pos, Direction facing) {
        BlockPos.MutableBlockPos cursor = pos.mutable();
        int length = 0;
        PEMachineBlockEntity target = null;
        boolean blocked = false;
        for (int i = 1; i < MAX_BEAM_LENGTH; i++) {
            cursor.move(facing);
            length++;
            if (!level.getBlockState(cursor).isAir()) {
                blocked = true;
                BlockEntity blockEntity = level.getBlockEntity(cursor);
                if (blockEntity instanceof PEMachineBlockEntity machine) {
                    target = machine;
                }
                break;
            }
        }
        return new BeamPath(length, target, blocked);
    }

    private record BeamPath(int length, PEMachineBlockEntity target, boolean blocked) {}

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public int getBeamLength() {
        return beamLength;
    }

    public int getBeamTicksRemaining() {
        return beamTicksRemaining;
    }

    public boolean isBeamBlocked() {
        return beamBlocked;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("beam_length", beamLength);
        output.putInt("beam_ticks_remaining", beamTicksRemaining);
        output.putBoolean("beam_blocked", beamBlocked);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        beamLength = Math.max(0, Math.min(MAX_BEAM_LENGTH - 1, input.getIntOr("beam_length", 0)));
        beamTicksRemaining = Math.max(0, Math.min(MAX_BEAM_TICKS, input.getIntOr("beam_ticks_remaining", 0)));
        beamBlocked = input.getBooleanOr("beam_blocked", false);
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
