package net.zuperzv.abyssalcraft_reawakening.services.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.WoodTypeAccessor;

public final class ModWoodTypes {

    private ModWoodTypes() {}

    public static final WoodType WITHERWOOD =
            WoodTypeAccessor.register(
                    new WoodType(
                            Constants.MOD_ID + ":witherwood",
                            ModBlockSetTypes.WITHERWOOD,
                            SoundType.WOOD,
                            SoundType.HANGING_SIGN,
                            SoundEvents.FENCE_GATE_CLOSE,
                            SoundEvents.FENCE_GATE_OPEN
                    )
            );

    public static void load() {}
}