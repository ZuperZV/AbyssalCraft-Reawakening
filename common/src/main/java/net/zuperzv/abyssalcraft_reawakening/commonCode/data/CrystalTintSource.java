package net.zuperzv.abyssalcraft_reawakening.commonCode.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record CrystalTintSource(int color) implements ItemTintSource {
    public static final MapCodec<CrystalTintSource> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color")
                            .forGetter(CrystalTintSource::color)
            ).apply(instance, CrystalTintSource::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        return ARGB.opaque(color);
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
