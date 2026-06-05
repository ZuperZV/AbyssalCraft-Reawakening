package net.zuperzv.abyssalcraft_reawakening.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.Objects;

public final class ModCreativeTabs {
    private ModCreativeTabs() {}

    public static void load() {}

    public static final RegistryHandle<CreativeModeTab> ABYSSALCRAFT_TAB =
            Services.REGISTRY.registerCreativeTab("abyssalcraft_tab", () -> new ItemStack(ModItems.ABYSSALNITE_INGOT.get()),
                    output -> BuiltInRegistries.ITEM.stream()
                            .filter(item -> Objects.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace(), Constants.MOD_ID))
                            .forEachOrdered(output::accept));
}