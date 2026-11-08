package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.helper.CoraliumLoggedBlockHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StateDefinition.Builder.class)
abstract class StateDefinitionBuilderMixin<O, S extends StateHolder<O, S>> {
    @Shadow
    @Final
    private O owner;

    @Shadow
    public abstract StateDefinition.Builder<O, S> add(Property<?>... properties);

    @Inject(method = "create", at = @At("HEAD"))
    private void abyssalcraft$addCoraliumLoggedProperty(CallbackInfoReturnable<?> cir) {
        if (owner instanceof Block block && block instanceof SimpleWaterloggedBlock) {
            add(CoraliumLoggedBlockHelper.CORALIUM_LOGGED);
        }
    }
}
