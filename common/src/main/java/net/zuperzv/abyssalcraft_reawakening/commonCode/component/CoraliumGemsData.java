package net.zuperzv.abyssalcraft_reawakening.commonCode.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class CoraliumGemsData {

    private int corealiumGems;

    public CoraliumGemsData(int corealiumGems) {
        this.corealiumGems = corealiumGems;
    }

    public int getCorealiumGems() {
        return corealiumGems;
    }

    public void setCorealiumGems(int corealiumGems) {
        this.corealiumGems = corealiumGems;
    }

    public void increase(int amount) {
        this.corealiumGems = Math.min(9, this.corealiumGems + amount);
    }

    public static CoraliumGemsData createEmpty() {
        return new CoraliumGemsData(0);
    }

    public void decrease(int amount) {
        this.corealiumGems = Math.max(0, this.corealiumGems - amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.corealiumGems);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof CoraliumGemsData ex) {
            return this.corealiumGems == ex.corealiumGems;
        }
        return false;
    }

    public static final Codec<CoraliumGemsData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("corealium_gems").forGetter(CoraliumGemsData::getCorealiumGems)
            ).apply(instance, CoraliumGemsData::new)
    );
}