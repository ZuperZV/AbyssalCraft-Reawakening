package net.zuperzv.abyssalcraft_reawakening.init.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class EnergyData {

    private int amount;

    public EnergyData(int amount) {
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increaseAmount(int amount) {
        this.amount += amount;
    }

    public int getAmount() {
        return amount;
    }

    public void decrease(int amount) {
        this.amount = Math.max(0, this.amount - amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof EnergyData ex) {
            return this.amount == ex.amount;
        }
        return false;
    }

    public static final Codec<EnergyData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("amount").forGetter(EnergyData::getAmount)
            ).apply(instance, EnergyData::new)
    );
}