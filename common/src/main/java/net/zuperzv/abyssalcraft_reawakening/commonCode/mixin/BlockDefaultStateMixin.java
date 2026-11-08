package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper.CoraliumLoggedBlockHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Block.class)
abstract class BlockDefaultStateMixin {
    @ModifyVariable(method = "registerDefaultState", at = @At("HEAD"), argsOnly = true)
    private BlockState abyssalcraft$defaultCoraliumLoggedFalse(BlockState state) {
        if (state.hasProperty(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)
                && state.getValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED)) {
            return state.setValue(CoraliumLoggedBlockHelper.CORALIUM_LOGGED, false);
        }
        return state;
    }
}
