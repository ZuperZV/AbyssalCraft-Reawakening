package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.services.types.IItemFactory;

/** Shared service entry point; the returned item class is supplied by the active loader. */
public final class CommonItemFactoryHelper implements IItemFactory {
    private static final String ITEM_IMPLEMENTATION =
            "net.zuperzv.abyssalcraft_reawakening.commonCode.item.helpers.TransmutationItem";

    @Override
    public Item createTransmutationGem(Item.Properties properties) {
        try {
            Class<?> implementation = Class.forName(ITEM_IMPLEMENTATION, true, getClass().getClassLoader());
            return (Item) implementation.getConstructor(Item.Properties.class).newInstance(properties);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not create the loader-specific TransmutationItem", exception);
        }
    }
}
