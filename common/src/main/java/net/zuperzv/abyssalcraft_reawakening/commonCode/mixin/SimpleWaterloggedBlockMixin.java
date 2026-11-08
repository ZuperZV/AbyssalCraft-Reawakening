package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper.CoraliumLoggedBlockHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin that allows Coralium fluid to be placed in waterloggable blocks (fences, walls, etc.),
 * making them act as if Coralium were water for the purposes of waterlogging.
 */
@Mixin(SimpleWaterloggedBlock.class)
public interface SimpleWaterloggedBlockMixin {

    @Inject(method = "canPlaceLiquid(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/Fluid;)Z",
            at = @At("HEAD"), cancellable = true)
    default void abyssalcraft$allowCoraliumPlacement(
            @Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (CoraliumLoggedBlockHelper.isCoralium(type)) {
            cir.setReturnValue(CoraliumLoggedBlockHelper.canBeCoraliumLogged(state));
        } else if (type.is(FluidTags.WATER)
                && CoraliumLoggedBlockHelper.isCoraliumLogged(state)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "placeLiquid(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("HEAD"), cancellable = true)
    default void abyssalcraft$placeCoralium(
            LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (CoraliumLoggedBlockHelper.isCoralium(fluidState)) {
            if (!CoraliumLoggedBlockHelper.canBeCoraliumLogged(state)) {
                cir.setReturnValue(false);
                return;
            }
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }
            cir.setReturnValue(true);
        } else if (fluidState.is(FluidTags.WATER)
                && CoraliumLoggedBlockHelper.isCoraliumLogged(state)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "pickupBlock(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true)
    default void abyssalcraft$pickupCoralium(
            @Nullable LivingEntity entity, LevelAccessor level, BlockPos pos, BlockState state,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (state.hasProperty(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)
                && state.getValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)) {
            level.setBlock(pos, state.setValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED, false), 3);
            cir.setReturnValue(new ItemStack(ModFluids.CORALIUM_BUCKET.get()));
        }
    }
}
