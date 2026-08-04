package net.zuperzv.abyssalcraft_reawakening.init.block.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class ModBlockSetTypes {

    private ModBlockSetTypes() {}

    public static final BlockSetType WITHERWOOD = new BlockSetType(
            "witherwood",

            true,
            true,
            true,

            BlockSetType.PressurePlateSensitivity.EVERYTHING,

            SoundType.WOOD,

            SoundEvents.WOODEN_DOOR_CLOSE,
            SoundEvents.WOODEN_DOOR_OPEN,

            SoundEvents.WOODEN_TRAPDOOR_CLOSE,
            SoundEvents.WOODEN_TRAPDOOR_OPEN,

            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,

            SoundEvents.WOODEN_BUTTON_CLICK_OFF,
            SoundEvents.WOODEN_BUTTON_CLICK_ON
    );

    public static void load() {}
}