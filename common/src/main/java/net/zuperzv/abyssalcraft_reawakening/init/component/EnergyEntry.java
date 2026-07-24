package net.zuperzv.abyssalcraft_reawakening.init.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.zuperzv.abyssalcraft_reawakening.init.data.EnergyType;

public class EnergyEntry {

    public static final Codec<EnergyEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    EnergyType.CODEC.fieldOf("type").forGetter(EnergyEntry::getType),
                    Codec.INT.fieldOf("amount").forGetter(EnergyEntry::getAmount)
            ).apply(instance, EnergyEntry::new)
    );

    private final EnergyType type;
    private int amount;

    public EnergyEntry(EnergyType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public EnergyType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increase(int amount) {
        this.amount += amount;
    }

    public void decrease(int amount) {
        this.amount = Math.max(0, this.amount - amount);
    }
}