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
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerPotionFluid;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidTankAccess;
import org.jetbrains.annotations.Nullable;

public class BoilerTipBlockEntity extends BlockEntity {
    private static final int TRANSFER_AMOUNT = 5;
    private static final int MAX_TIPS = 1;

    private final EssenceBoilerFluid[] visualFluids = {
            EssenceBoilerFluid.EMPTY, EssenceBoilerFluid.EMPTY,
            EssenceBoilerFluid.EMPTY, EssenceBoilerFluid.EMPTY
    };
    private final boolean[] extracting = new boolean[MAX_TIPS];

    public BoilerTipBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOILER_TIP_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BoilerTipBlockEntity tip) {
        if (level.isClientSide()) {
            return;
        }

        EssenceBoilerBlockEntity source = getSourceBoiler(level, pos, state);
        if (source == null) {
            tip.clearConnections();
            return;
        }

        BlockPos targetPos = tip.findBoilerBelow(level, pos);
        if (targetPos == null
                || !(level.getBlockEntity(targetPos) instanceof IFluidTankAccess target)) {
            tip.clearConnections();
            return;
        }

        int count = 1;
        boolean redstonePowered = level.hasNeighborSignal(pos);
        int activeLane = -1;
        for (int lane = 0; lane < Math.min(count, MAX_TIPS); lane++) {
            if (tip.extracting[lane]) {
                activeLane = lane;
                break;
            }
        }
        if (activeLane < 0 && redstonePowered && count > 0) {
            activeLane = 0;
        }
        for (int lane = 0; lane < MAX_TIPS; lane++) {
            if (lane >= count || lane != activeLane) {
                tip.setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
                continue;
            }

            EssenceBoilerFluid sourceFluid = source.getTank();
            if (sourceFluid.isEmpty()) {
                tip.setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
                continue;
            }

            EssenceBoilerFluid transfer = sourceFluid.withAmount(
                    Math.min(TRANSFER_AMOUNT, sourceFluid.amount())
            );
            int accepted = EssenceBoilerPotionFluid.isPotionFluid(transfer)
                    && target instanceof EssenceBoilerBlockEntity boiler
                    ? boiler.fillPotionFluidTank(transfer)
                    : target.fillFluidTank(transfer);

            if (accepted <= 0) {
                tip.setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
                continue;
            }

            tip.setVisualFluid(lane, source.drainFluidTank(accepted));
        }
    }

    @Nullable
    private static EssenceBoilerBlockEntity getSourceBoiler(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING);
        BlockPos sourcePos = pos.relative(facing.getOpposite());
        if (!level.getBlockState(sourcePos).is(ModBlocks.ESSENCE_BOILER.block().get())) {
            return null;
        }
        return level.getBlockEntity(sourcePos) instanceof EssenceBoilerBlockEntity boiler
                ? boiler
                : null;
    }

    @Nullable
    public BlockPos findBoilerBelow(Level level, BlockPos pos) {
        for (BlockPos checkPos = pos.below();
             checkPos.getY() > level.getMinY();
             checkPos = checkPos.below()) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.is(ModBlocks.ESSENCE_BOILER.block().get())
                    || checkState.is(ModBlocks.CRYSTAL_GROWTH_CHAMBER.block().get())) {
                return checkPos;
            }
            if (!checkState.isAir()) {
                return null;
            }
        }
        return null;
    }

    public EssenceBoilerFluid getVisualFluid(int lane) {
        return visualFluids[lane];
    }

    public void setVisualFluid(int lane, EssenceBoilerFluid fluid) {
        EssenceBoilerFluid updated = fluid == null ? EssenceBoilerFluid.EMPTY : fluid;
        if (visualFluids[lane].equals(updated)) {
            return;
        }

        visualFluids[lane] = updated;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean isExtracting(int lane) {
        return extracting[lane];
    }

    public void setExtracting(int lane, boolean active) {
        if (extracting[lane] == active) {
            return;
        }
        if (active) {
            for (int otherLane = 0; otherLane < MAX_TIPS; otherLane++) {
                if (otherLane != lane) {
                    extracting[otherLane] = false;
                    setVisualFluid(otherLane, EssenceBoilerFluid.EMPTY);
                }
            }
        }
        extracting[lane] = active;
        if (!active) {
            setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
        }
        setChanged();
        sync();
    }

    public void clearConnections() {
        boolean changed = false;
        for (int lane = 0; lane < MAX_TIPS; lane++) {
            changed |= extracting[lane];
            extracting[lane] = false;
            setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
        }
        if (changed) {
            setChanged();
            sync();
        }
    }

    private void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        for (int lane = 0; lane < MAX_TIPS; lane++) {
            output.putBoolean("extractFluid" + lane, extracting[lane]);
            if (!visualFluids[lane].isEmpty()) {
                output.store("visualFluid" + lane, EssenceBoilerFluid.CODEC.codec(), visualFluids[lane]);
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        for (int lane = 0; lane < MAX_TIPS; lane++) {
            extracting[lane] = input.getBooleanOr("extractFluid" + lane, false);
            visualFluids[lane] = input.read("visualFluid" + lane, EssenceBoilerFluid.CODEC.codec())
                    .orElse(EssenceBoilerFluid.EMPTY);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
