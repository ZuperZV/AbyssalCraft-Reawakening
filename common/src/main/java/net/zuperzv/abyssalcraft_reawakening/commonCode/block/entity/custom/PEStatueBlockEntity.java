package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
/*
public class PEStatueBlockEntity extends BlockEntity {

    public static final int MAX_PE = 4000;
    public static final int BASE_GENERATION = 1;

    private int peStored = 0;

    /**
     * 0 = inactive
     * 1 = duration
     * 2 = power
     * 3 = range
     */
/*
    private int amplifier = 0;

    public PEStatueBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                ModBlockEntities.PE_STATUE_BE.get(),
                pos,
                state
        );
    }

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            PEStatueBlockEntity statue
    ) {
        if (level.isClientSide()) {
            return;
        }

        statue.generatePE();
    }

    private void generatePE() {

        if (peStored >= MAX_PE) {
            return;
        }

        int generation = getGenerationRate();

        peStored = Math.min(
                MAX_PE,
                peStored + generation
        );

        setChanged();
    }

    public int getGenerationRate() {
        return switch (amplifier) {
            case 1 -> BASE_GENERATION + 1;
            case 2 -> BASE_GENERATION + 2;
            case 3 -> BASE_GENERATION + 3;
            default -> BASE_GENERATION;
        };
    }

    public int getPE() {
        return peStored;
    }

    public int getCapacity() {
        return MAX_PE;
    }

    public int extractPE(int amount) {

        int extracted = Math.min(
                amount,
                peStored
        );

        peStored -= extracted;

        if (extracted > 0) {
            setChanged();
        }

        return extracted;
    }

    public int insertPE(int amount) {

        int inserted = Math.min(
                amount,
                MAX_PE - peStored
        );

        peStored += inserted;

        if (inserted > 0) {
            setChanged();
        }

        return inserted;
    }

    public boolean isFull() {
        return peStored >= MAX_PE;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(int amplifier) {

        this.amplifier = Math.max(
                0,
                Math.min(3, amplifier)
        );

        updateAmplifierState();

        setChanged();
    }

    private void updateAmplifierState() {

        if (level == null) {
            return;
        }

        BlockState state = getBlockState();

        if (!(state.getBlock() instanceof PEStatueBlock)) {
            return;
        }

        BlockState newState = state.setValue(
                PEStatueBlock.AMPLIFIER,
                amplifier
        );

        if (!newState.equals(state)) {
            level.setBlock(
                    worldPosition,
                    newState,
                    3
            );
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("PE", peStored);
        output.putInt("Amplifier", amplifier);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        peStored = input.getIntOr("PE", 0);
        amplifier = input.getIntOr("Amplifier", 0);
    }
}
 */