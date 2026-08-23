package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record MonsterRoomFeatureConfiguration(
        List<Identifier> structures,
        int maxEmptyCorners
) implements FeatureConfiguration {

    public static final Codec<MonsterRoomFeatureConfiguration> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.listOf()
                            .fieldOf("structures")
                            .forGetter(MonsterRoomFeatureConfiguration::structures),

                    Codec.INT
                            .optionalFieldOf("max_empty_corners", 5)
                            .forGetter(MonsterRoomFeatureConfiguration::maxEmptyCorners)

            ).apply(
                    instance,
                    MonsterRoomFeatureConfiguration::new
            ));
}