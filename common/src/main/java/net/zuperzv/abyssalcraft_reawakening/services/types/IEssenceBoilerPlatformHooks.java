package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;

public interface IEssenceBoilerPlatformHooks {
    boolean tryEmptyFluidContainer(
            EssenceBoilerBlockEntity boiler,
            Player player,
            InteractionHand hand
    );

    boolean tryFillFluidContainer(
            EssenceBoilerBlockEntity boiler,
            Player player,
            InteractionHand hand
    );
}
