package net.zuperzv.abyssalcraft_reawakening.init.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.zuperzv.abyssalcraft_reawakening.init.data.EnergyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record EnergyData(List<EnergyEntry> energy) {

    public static final Codec<EnergyData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    EnergyEntry.CODEC.listOf()
                            .fieldOf("energy")
                            .forGetter(EnergyData::energy)
            ).apply(instance, EnergyData::new)
    );

    public EnergyData {
        if (energy.size() != 4)
            throw new IllegalArgumentException("Must contain exactly 4 entries.");

        Set<EnergyType> types = energy.stream()
                .map(EnergyEntry::getType)
                .collect(Collectors.toSet());

        if (types.size() != 4)
            throw new IllegalArgumentException("Each energy type must appear exactly once.");

        energy = List.copyOf(energy);
    }

    public EnergyEntry get(EnergyType type) {
        return energy.stream()
                .filter(e -> e.getType() == type)
                .findFirst()
                .orElseThrow();
    }

    public int getAmount(EnergyType type) {
        return get(type).getAmount();
    }

    public EnergyData withAmount(EnergyType type, int amount) {
        List<EnergyEntry> newEnergy = energy.stream()
                .map(entry ->
                        entry.getType() == type
                                ? new EnergyEntry(type, amount)
                                : entry
                )
                .toList();

        return new EnergyData(newEnergy);
    }

    public static EnergyData createEmpty() {
        return new EnergyData(List.of(
                new EnergyEntry(EnergyType.DREAD, 0),
                new EnergyEntry(EnergyType.OMOTHOL, 0),
                new EnergyEntry(EnergyType.CORALIUM, 0),
                new EnergyEntry(EnergyType.SHADOW, 0)
        ));
    }
}