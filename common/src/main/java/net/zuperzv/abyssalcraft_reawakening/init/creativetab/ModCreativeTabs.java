package net.zuperzv.abyssalcraft_reawakening.init.creativetab;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public final class ModCreativeTabs {

    private ModCreativeTabs() {}

    public static void load() {}

    public static final RegistryHandle<CreativeModeTab> ABYSSALCRAFT_TAB =
            Services.REGISTRY.registerCreativeTab(
                    "abyssalcraft_tab",

                    () -> new ItemStack(
                            ModItems.ABYSSALNITE_INGOT.get()
                    ),

                    output -> ModCreativeTabItemSorter
                            .getOrderedItems()
                            .forEach(output::accept)
            );
}