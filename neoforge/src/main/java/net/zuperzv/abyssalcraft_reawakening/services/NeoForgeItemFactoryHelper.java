package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.helpers.TransmutationItem;
import net.zuperzv.abyssalcraft_reawakening.services.types.IItemFactory;

public class NeoForgeItemFactoryHelper implements IItemFactory {

    @Override
    public Item createTransmutationGem(Item.Properties properties) {
        return new TransmutationItem(properties);
    }
}