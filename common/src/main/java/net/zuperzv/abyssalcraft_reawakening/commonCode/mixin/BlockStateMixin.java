package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FluidState;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper.CoraliumLoggedBlockHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.CoraliumPlagueHelper;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
abstract class BlockStateMixin {
    @Inject(method = "entityInside", at = @At("HEAD"))
    private void abyssalcraft$applyCoraliumPlagueToLoggedBlocks(Level level, BlockPos pos, Entity entity,
            InsideBlockEffectApplier effectApplier, boolean isPrecise, CallbackInfo ci) {
        BlockState state = (BlockState) (Object) this;
        if (CoraliumLoggedBlockHelper.isCoraliumLogged(state)
                && (!state.hasProperty(BlockStateProperties.WATERLOGGED)
                || !state.getValue(BlockStateProperties.WATERLOGGED))) {
            CoraliumPlagueHelper.applyTo(entity, level);
        }
    }

    @Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
    private void abyssalcraft$coraliumFluidState(CallbackInfoReturnable<FluidState> cir) {
        BlockState state = (BlockState) (Object) this;
        if (CoraliumLoggedBlockHelper.isCoraliumLogged(state)
                && (!state.hasProperty(BlockStateProperties.WATERLOGGED)
                || !state.getValue(BlockStateProperties.WATERLOGGED))
                && BuiltInRegistries.BLOCK.getOptional(ModFluids.CORALIUM_BLOCK.id()).isPresent()) {
            cir.setReturnValue(ModFluids.CORALIUM_BLOCK.get().defaultBlockState().getFluidState());
        }
    }
}
