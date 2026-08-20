package net.zuperzv.abyssalcraft_reawakening.commonCode.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record DyedColorTintSource(int defaultColor) implements ItemTintSource {

    public static final MapCodec<DyedColorTintSource> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default")
                                    .forGetter(DyedColorTintSource::defaultColor)
                    ).apply(instance, DyedColorTintSource::new)
            );

    public DyedColorTintSource() {
        this(0x643732);
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        var dye = stack.get(DataComponents.DYED_COLOR);
        return dye != null ? ARGB.opaque(dye.rgb()) : ARGB.opaque(defaultColor);
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}