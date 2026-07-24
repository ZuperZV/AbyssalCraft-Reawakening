package net.zuperzv.abyssalcraft_reawakening.init.data;

import com.mojang.serialization.Codec;

public enum EnergyType {
    CORALIUM,
    DREAD,
    OMOTHOL,
    SHADOW;

    public static final Codec<EnergyType> CODEC =
            Codec.STRING.xmap(
                    EnergyType::valueOf,
                    EnergyType::name
            );
}