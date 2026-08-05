package net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;

public class ModHangingSignBlockEntity extends SignBlockEntity {
    public ModHangingSignBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.MOD_HANGING_SIGN.get(), worldPosition, blockState);
    }
    private static final int MAX_TEXT_LINE_WIDTH = 60;
    private static final int TEXT_LINE_HEIGHT = 9;

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.MOD_HANGING_SIGN.get();
    }

    @Override
    public int getMaxTextLineWidth() {
        return MAX_TEXT_LINE_WIDTH;
    }

    @Override
    public int getTextLineHeight() {
        return TEXT_LINE_HEIGHT;
    }

    @Override
    public SoundEvent getSignInteractionFailedSoundEvent() {
        return SoundEvents.WAXED_HANGING_SIGN_INTERACT_FAIL;
    }
}
