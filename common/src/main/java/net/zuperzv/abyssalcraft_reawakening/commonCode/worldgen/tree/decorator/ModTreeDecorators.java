package net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.tree.decorator;

import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public class ModTreeDecorators {

    private ModTreeDecorators() {}

    public static void load() {}

    public static final RegistryHandle<TreeDecoratorType<TinyRootDecorator>> TINY_ROOT =
            Services.REGISTRY.registerTreeDecoratorType(
                    "tiny_root",
                    TinyRootDecorator.CODEC
            );
}