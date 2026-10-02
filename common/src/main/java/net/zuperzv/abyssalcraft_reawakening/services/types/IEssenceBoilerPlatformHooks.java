package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
public interface IEssenceBoilerPlatformHooks {
    boolean tryEmptyFluidContainer(
            IFluidTankAccess tank,
            Player player,
            InteractionHand hand
    );

    boolean tryFillFluidContainer(
            IFluidTankAccess tank,
            Player player,
            InteractionHand hand
    );
}
