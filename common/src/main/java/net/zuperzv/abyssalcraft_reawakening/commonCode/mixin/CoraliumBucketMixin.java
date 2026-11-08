package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
abstract class CoraliumBucketMixin {
    @Shadow
    @Final
    private Fluid content;

    @Shadow
    protected abstract void playEmptySound(LivingEntity entity, LevelAccessor level, BlockPos pos);

    @ModifyExpressionValue(
            method = "use",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/material/Fluids;WATER:Lnet/minecraft/world/level/material/FlowingFluid;"
            )
    )
    private FlowingFluid abyssalcraft$targetCoraliumContainers(FlowingFluid water) {
        return content == ModFluids.SOURCE_CORALIUM.get()
                ? ModFluids.SOURCE_CORALIUM.get()
                : water;
    }

    @Inject(method = "emptyContents", at = @At("HEAD"), cancellable = true)
    private void abyssalcraft$fillCoraliumContainers(
            LivingEntity entity, Level level, BlockPos pos,
            net.minecraft.world.phys.BlockHitResult hitResult,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (content != ModFluids.SOURCE_CORALIUM.get()) {
            return;
        }

        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LiquidBlockContainer container
                && container.canPlaceLiquid(entity, level, pos, state, content)
                && container.placeLiquid(level, pos, state, ModFluids.SOURCE_CORALIUM.get().defaultFluidState())) {
            playEmptySound(entity, level, pos);
            cir.setReturnValue(true);
        }
    }
}
