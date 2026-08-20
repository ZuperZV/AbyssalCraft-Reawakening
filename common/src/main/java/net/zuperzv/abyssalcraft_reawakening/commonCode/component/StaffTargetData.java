package net.zuperzv.abyssalcraft_reawakening.commonCode.component;

import com.mojang.serialization.Codec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class StaffTargetData {

    private static final int MAX_TARGETS = 50;

    private static final Codec<UUID> UUID_STRING_CODEC = Codec.STRING.xmap(
            UUID::fromString,
            UUID::toString
    );

    private final Map<UUID, Integer> targets;

    public StaffTargetData(Map<UUID, Integer> targets) {
        this.targets = new LinkedHashMap<>(targets);

        while (this.targets.size() > MAX_TARGETS) {
            UUID oldest = this.targets.keySet().iterator().next();
            this.targets.remove(oldest);
        }
    }

    public int getRepeatCount(UUID uuid) {
        return targets.getOrDefault(uuid, 0);
    }

    public StaffTargetData increase(UUID uuid) {
        LinkedHashMap<UUID, Integer> newTargets = new LinkedHashMap<>(targets);

        int count = newTargets.getOrDefault(uuid, 0);

        newTargets.remove(uuid);
        newTargets.put(uuid, count + 1);

        return new StaffTargetData(newTargets);
    }

    private Map<UUID, Integer> targetsForCodec() {
        return targets;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaffTargetData other)) return false;

        return targets.equals(other.targets);
    }

    @Override
    public int hashCode() {
        return targets.hashCode();
    }

    public static final Codec<StaffTargetData> CODEC =
            Codec.unboundedMap(
                    UUID_STRING_CODEC,
                    Codec.INT
            ).xmap(
                    StaffTargetData::new,
                    StaffTargetData::targetsForCodec
            );
}