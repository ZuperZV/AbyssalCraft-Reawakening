package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {

    @Accessor("STRIPPABLES")
    Map<Block, Block> abyssalcraft$getStrippables();

    @Accessor("STRIPPABLES")
    @Mutable
    void abyssalcraft$setStrippables(Map<Block, Block> map);
}