package net.zuperzv.abyssalcraft_reawakening.commonCode.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.alchemy.PotionContents;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PotionFluidBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.GlowingLiquidBlock;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.jetbrains.annotations.Nullable;

public class PotionLiquidBlock extends GlowingLiquidBlock implements EntityBlock {
    public PotionLiquidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotionFluidBlockEntity(pos, state);
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide()
                || !(level.getBlockEntity(pos) instanceof PotionFluidBlockEntity target)
                || target.getPotionContents() != PotionContents.EMPTY) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.getBlockState(neighbor).is(ModFluids.POTION_BLOCK.get())
                    && level.getBlockEntity(neighbor) instanceof PotionFluidBlockEntity source
                    && source.getPotionContents() != PotionContents.EMPTY) {
                target.setPotionContents(source.getPotionContents());
                return;
            }
        }
    }

    @Override
    public ItemStack pickupBlock(
            @Nullable LivingEntity user,
            LevelAccessor level,
            BlockPos pos,
            BlockState state
    ) {
        if (state.getValue(LEVEL) != 0) {
            return ItemStack.EMPTY;
        }

        PotionContents potionContents = level.getBlockEntity(pos)
                instanceof PotionFluidBlockEntity potionBlockEntity
                ? potionBlockEntity.getPotionContents()
                : PotionContents.EMPTY;
        ItemStack bucket = super.pickupBlock(user, level, pos, state);
        if (!bucket.isEmpty() && potionContents != PotionContents.EMPTY) {
            bucket.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS, potionContents);
        }
        return bucket;
    }
}
