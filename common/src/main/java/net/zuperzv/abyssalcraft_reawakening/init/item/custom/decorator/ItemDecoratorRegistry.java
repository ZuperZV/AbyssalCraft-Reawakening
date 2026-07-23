package net.zuperzv.abyssalcraft_reawakening.init.item.custom.decorator;

import java.util.ArrayList;
import java.util.List;

public class ItemDecoratorRegistry {

    private static final List<ItemDecorator> DECORATORS = new ArrayList<>();

    public static void register(ItemDecorator decorator) {
        DECORATORS.add(decorator);
    }

    public static List<ItemDecorator> getDecorators() {
        return DECORATORS;
    }
}