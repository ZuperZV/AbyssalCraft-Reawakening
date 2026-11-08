package net.zuperzv.abyssalcraft_reawakening.commonCode.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum CoralliumGeyserState implements StringRepresentable {
    DRY("dry"),
    WET("wet"),
    DORMANT("dormant"),
    ERUPTING("erupting"),
    CONTINUOUS("continuous");

    private final String name;

    private CoralliumGeyserState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
