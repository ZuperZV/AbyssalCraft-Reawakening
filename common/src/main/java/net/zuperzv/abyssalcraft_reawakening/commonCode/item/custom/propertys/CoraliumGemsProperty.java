package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.propertys;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import org.jspecify.annotations.Nullable;

public record CoraliumGemsProperty() implements RangeSelectItemModelProperty {

    public static final CoraliumGemsProperty INSTANCE =
            new CoraliumGemsProperty();

    public static final MapCodec<CoraliumGemsProperty> MAP_CODEC =
            MapCodec.unit(INSTANCE);

    @Override
    public float get(
            ItemStack stack,
            @Nullable ClientLevel level,
            @Nullable ItemOwner owner,
            int seed
    ) {
        CoraliumGemsData data =
                stack.get(ModDataComponentTypes.CORALIUM_GEMS.get());

        if (data == null) {
            return 0.0F;
        }

        return data.getCorealiumGems();
    }

    @Override
    public MapCodec<CoraliumGemsProperty> type() {
        return MAP_CODEC;
    }
}