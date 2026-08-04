package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;


public final class ModWoodTypes {

    private ModWoodTypes() {}


    public static final WoodType WITHERWOOD = new WoodType(
            "witherwood",

            ModBlockSetTypes.WITHERWOOD,

            SoundType.WOOD,
            SoundType.HANGING_SIGN,

            SoundEvents.FENCE_GATE_CLOSE,
            SoundEvents.FENCE_GATE_OPEN
    );


    public static void load() {}
}