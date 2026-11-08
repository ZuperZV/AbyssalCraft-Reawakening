package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper.CoraliumLoggedBlockHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
abstract class BlockItemMixin {
    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void abyssalcraft$coraliumLogPlacement(
            BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        if (state != null
                && CoraliumLoggedBlockHelper.canBeCoraliumLogged(state)
                && CoraliumLoggedBlockHelper.isCoralium(fluidState)) {
            cir.setReturnValue(state.setValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED, true));
            if (!context.getLevel().isClientSide()) {
                context.getLevel().scheduleTick(
                        context.getClickedPos(),
                        fluidState.getType(),
                        fluidState.getType().getTickDelay(context.getLevel())
                );
            }
        }
    }
}
