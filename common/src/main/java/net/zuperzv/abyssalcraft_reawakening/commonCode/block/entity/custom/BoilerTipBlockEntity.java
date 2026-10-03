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
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom.BoilerTipBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerPotionFluid;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidTankAccess;
import org.jetbrains.annotations.Nullable;

public class BoilerTipBlockEntity extends BlockEntity {
    private static final int TRANSFER_AMOUNT = 5;
    private static final int MAX_TIPS = Direction.values().length;

    private final EssenceBoilerFluid[] visualFluids = {
            EssenceBoilerFluid.EMPTY, EssenceBoilerFluid.EMPTY,
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

        BlockPos targetPos = tip.findBoilerBelow(level, pos);
        if (targetPos == null) {
            tip.clearConnections();
            return;
        }

        BlockEntity targetEntity = level.getBlockEntity(targetPos);
        if (!(targetEntity instanceof IFluidTankAccess target)) {
            tip.clearConnections();
            return;
        }

        boolean redstonePowered = level.hasNeighborSignal(pos);
        for (Direction direction : Direction.values()) {
            int lane = direction.ordinal();
            if (!BoilerTipBlock.hasAttachment(state, direction)) {
                tip.clearConnection(direction);
                continue;
            }
            EssenceBoilerBlockEntity source = findSourceBoiler(level, pos, direction);
            if (source == null) {
                tip.clearConnection(direction);
                continue;
            }
            if (!tip.extracting[lane] && !redstonePowered) {
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
    private static EssenceBoilerBlockEntity findSourceBoiler(Level level, BlockPos pos, Direction direction) {
        BlockPos sourcePos = pos.relative(direction.getOpposite());
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

    public boolean isExtracting(Direction direction) {
        return extracting[direction.ordinal()];
    }

    public void setExtracting(Direction direction, boolean active) {
        int lane = direction.ordinal();
        if (extracting[lane] == active) {
            return;
        }
        extracting[lane] = active;
        if (!active) {
            setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
        }
        setChanged();
        sync();
    }

    public void clearConnections() {
        for (Direction direction : Direction.values()) {
            clearConnection(direction);
        }
    }

    public void clearConnection(Direction direction) {
        int lane = direction.ordinal();
        boolean changed = extracting[lane];
        extracting[lane] = false;
        boolean hadFluid = !visualFluids[lane].isEmpty();
        setVisualFluid(lane, EssenceBoilerFluid.EMPTY);
        if (changed && !hadFluid) {
            setChanged();
            sync();
        }
    }

    public void copyConnectionsTo(BoilerTipBlockEntity target, Direction excludedDirection) {
        for (Direction direction : Direction.values()) {
            if (direction == excludedDirection) {
                continue;
            }
            int lane = direction.ordinal();
            target.extracting[lane] = extracting[lane];
            target.visualFluids[lane] = visualFluids[lane];
        }
        target.setChanged();
        target.sync();
    }

    private void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        for (Direction direction : Direction.values()) {
            int lane = direction.ordinal();
            String key = direction.getName();
            output.putBoolean("extractFluid_" + key, extracting[lane]);
            if (!visualFluids[lane].isEmpty()) {
                output.store("visualFluid_" + key, EssenceBoilerFluid.CODEC.codec(), visualFluids[lane]);
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        for (Direction direction : Direction.values()) {
            int lane = direction.ordinal();
            String key = direction.getName();
            extracting[lane] = input.getBooleanOr("extractFluid_" + key, false);
            visualFluids[lane] = input.read("visualFluid_" + key, EssenceBoilerFluid.CODEC.codec())
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
