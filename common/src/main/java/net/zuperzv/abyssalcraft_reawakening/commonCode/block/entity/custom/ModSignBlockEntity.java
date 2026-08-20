package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;

public class ModSignBlockEntity extends SignBlockEntity {

    public ModSignBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.MOD_SIGN.get(), worldPosition, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.MOD_SIGN.get();
    }
}
