package net.zuperzv.abyssalcraft_reawakening.commonCode.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class PotentialEnergyData {

    private int potentialEnergy;

    public PotentialEnergyData(int potentialEnergy) {
        this.potentialEnergy = potentialEnergy;
    }

    public int getPotentialEnergy() {
        return potentialEnergy;
    }

    public void setPotentialEnergy(int potentialEnergy) {
        this.potentialEnergy = potentialEnergy;
    }

    public void increase(int amount) {
        this.potentialEnergy += amount;
    }

    public static PotentialEnergyData createEmpty() {
        return new PotentialEnergyData(0);
    }

    public void decrease(int amount) {
        this.potentialEnergy = Math.max(0, this.potentialEnergy - amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.potentialEnergy);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof PotentialEnergyData ex) {
            return this.potentialEnergy == ex.potentialEnergy;
        }
        return false;
    }

    public static final Codec<PotentialEnergyData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("potential_energy").forGetter(PotentialEnergyData::getPotentialEnergy)
            ).apply(instance, PotentialEnergyData::new)
    );
}