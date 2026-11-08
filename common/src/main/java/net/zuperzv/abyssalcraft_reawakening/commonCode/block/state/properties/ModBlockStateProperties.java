package net.zuperzv.abyssalcraft_reawakening.commonCode.block.state.properties;

import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Custom block state properties used by the mod.
 */
public final class ModBlockStateProperties {
    private ModBlockStateProperties() {}

    /**
     * Boolean property indicating whether a block is filled with Coralium fluid.
     * Works similarly to WATERLOGGED but for the Coralium fluid.
     */
    public static final BooleanProperty CORALIUM_LOGGED = BooleanProperty.create("coralium_logged");
}
