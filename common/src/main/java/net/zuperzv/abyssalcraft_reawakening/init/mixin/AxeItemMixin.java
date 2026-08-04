package net.zuperzv.abyssalcraft_reawakening.init.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.AxeItem;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    private static final Map<Identifier, Identifier> STRIPPABLES = Map.of(
            ModBlocks.WITHERWOOD_LOG.block().id(),
            ModBlocks.STRIPPED_WITHERWOOD_LOG.block().id(),

            ModBlocks.WITHERWOOD_WOOD.block().id(),
            ModBlocks.STRIPPED_WITHERWOOD_WOOD.block().id()
    );

    @Inject(
            method = "getStripped",
            at = @At("HEAD"),
            cancellable = true
    )
    private void abyssalcraft$getStripped(
            BlockState state,
            CallbackInfoReturnable<Optional<BlockState>> cir
    ) {

        Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        Identifier strippedId = STRIPPABLES.get(id);

        if (strippedId == null) {
            return;
        }

        Block stripped = BuiltInRegistries.BLOCK
                .get(strippedId)
                .orElseThrow()
                .value();

        cir.setReturnValue(Optional.of(
                stripped.defaultBlockState()
                        .setValue(
                                RotatedPillarBlock.AXIS,
                                state.getValue(RotatedPillarBlock.AXIS)
                        )
        ));
    }
}