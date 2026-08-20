package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import org.jspecify.annotations.Nullable;

public class TransmutationItemBase extends Item {
    public TransmutationItemBase(Properties properties) {
        super(properties);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int maxDamage = stack.getMaxDamage();
        float healthPercentage = Math.max(0.0F, ((float)maxDamage - (float)stack.getDamageValue()) / (float)maxDamage);
        return Mth.hsvToRgb(healthPercentage * 0.63F, 1.0F, 1.0F);
    }

    public static @Nullable ItemStack getCraftingRemainderPearl(ItemInstance instance) {
        int damage = instance.getOrDefault(DataComponents.DAMAGE, 0);
        int maxDamage = instance.getOrDefault(DataComponents.MAX_DAMAGE, 10);

        if (damage >= maxDamage - 1) {
            return ModItems.CORALIUM_PEARL.get().getDefaultInstance();
        }

        ItemStack remainder = new ItemStack(instance.typeHolder().value());
        remainder.set(DataComponents.DAMAGE, damage + 1);

        return remainder;
    }
}