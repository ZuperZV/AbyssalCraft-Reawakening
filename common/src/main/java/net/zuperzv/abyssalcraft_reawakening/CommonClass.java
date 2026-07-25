package net.zuperzv.abyssalcraft_reawakening;

import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.ModCreativeTabs;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.init.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.worldgen.ModWorldgen;

public class CommonClass {
    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {

            Constants.LOG.info("Hello to " + Constants.MOD_ID);
        }

        ModItems.load();
        ModBlocks.load();
        ModDataComponentTypes.load();
        ModMenuTypes.load();
        ModCreativeTabs.load();
        ModWorldgen.load();
        ModBlockEntities.load();
        ModRecipes.load(Services.REGISTRY);
    }
}