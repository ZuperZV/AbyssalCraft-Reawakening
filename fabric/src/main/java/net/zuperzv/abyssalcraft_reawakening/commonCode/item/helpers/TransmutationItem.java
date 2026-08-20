package net.zuperzv.abyssalcraft_reawakening.commonCode.item.helpers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.TransmutationItemBase;
import org.jspecify.annotations.Nullable;

public class TransmutationItem extends TransmutationItemBase {

    public TransmutationItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        ItemStack remainder = TransmutationItemBase.getCraftingRemainderPearl(stack);

        return remainder != null ? new ItemStackTemplate(remainder.getItem(), remainder.getComponentsPatch()) : null;
    }
}