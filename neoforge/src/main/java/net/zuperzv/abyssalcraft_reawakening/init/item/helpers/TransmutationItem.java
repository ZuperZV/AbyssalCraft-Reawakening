package net.zuperzv.abyssalcraft_reawakening.init.item.helpers;

import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.zuperzv.abyssalcraft_reawakening.init.item.custom.TransmutationItemBase;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class TransmutationItem extends TransmutationItemBase {

    public TransmutationItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        ItemStack stack = TransmutationItemBase.getCraftingRemainderPearl(instance);

        return stack != null ? new ItemStackTemplate(stack.getItem(), stack.getComponentsPatch()) : null;
    }
}