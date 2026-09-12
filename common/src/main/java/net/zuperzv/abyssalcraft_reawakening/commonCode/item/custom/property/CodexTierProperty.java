package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.property;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import org.jspecify.annotations.Nullable;

public record CodexTierProperty() implements RangeSelectItemModelProperty {

    public static final CodexTierProperty INSTANCE = new CodexTierProperty();

    public static final MapCodec<CodexTierProperty> MAP_CODEC =
            MapCodec.unit(INSTANCE);

    @Override
    public float get(
            ItemStack stack,
            @Nullable ClientLevel level,
            @Nullable ItemOwner owner,
            int seed
    ) {
        Integer tier = stack.get(ModDataComponentTypes.CODEX_TIER.get()).getTier();

        return tier == null ? 1.0F : tier.floatValue();
    }

    @Override
    public MapCodec<CodexTierProperty> type() {
        return MAP_CODEC;
    }
}