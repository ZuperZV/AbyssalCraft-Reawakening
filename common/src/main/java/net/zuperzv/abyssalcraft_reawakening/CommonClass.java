package net.zuperzv.abyssalcraft_reawakening;

import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class CommonClass {
    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {

            Constants.LOG.info("Hello to " + Constants.MOD_ID);
        }

        ModItems.load();
        ModBlocks.load();
    }
}