package net.zuperzv.abyssalcraft_reawakening.commonCode.effect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;

import java.util.List;

public final class ModPotionBrewing {
    private static final List<Item> GEM_INGREDIENTS = List.of(
            ModItems.CORALIUM_GEM.get(),
            ModItems.TRANSMUTATION_GEM.get(),
            ModItems.SHADOW_GEM.get()
    );

    private ModPotionBrewing() {
    }

    public static void register(PotionBrewing.Builder builder) {
        var crystallization = BuiltInRegistries.POTION.wrapAsHolder(ModPotions.CRYSTALLIZATION.get());
        var longCrystallization = BuiltInRegistries.POTION.wrapAsHolder(ModPotions.LONG_CRYSTALLIZATION.get());

        for (Item gem : GEM_INGREDIENTS) {
            builder.addMix(Potions.AWKWARD, gem, crystallization);
        }
        builder.addMix(crystallization, Items.REDSTONE, longCrystallization);
    }
}
