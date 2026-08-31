package net.zuperzv.abyssalcraft_reawakening;

import net.zuperzv.abyssalcraft_reawakening.commonCode.creativetab.ModCreativeTabs;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader.DataItemJsonLoader;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityAttributes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.ModModelLayer;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.renderer.ModEntityRenderers;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModDataItem;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemRegistry;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.screen.ModMenuTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModFeatures;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.placement.ModPlacementModifierTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.tree.decorator.ModTreeDecorators;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.ModWorldgen;

public class CommonClass {
    public static void init() {

        Constants.LOG.info("Hello from Common commonCode on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {

            Constants.LOG.info("Hello to " + Constants.MOD_ID);
        }

        ModEntityTypes.load();
        ModEntityRenderers.load();
        ModModelLayer.load();
        ModEntityAttributes.load();

        DataItemJsonLoader.load();

        DataItemRegistry registry = DataItemRegistry.getInstance();
        ModDataItem.registerAll(registry);

        ModItems.load();
        ModBlocks.load();
        ModDataComponentTypes.load();
        ModMenuTypes.load();
        ModCreativeTabs.load();
        ModPlacementModifierTypes.load();
        ModTreeDecorators.load();
        ModWorldgen.load();
        ModFeatures.load();
        ModBlockEntities.load();
        ModRecipes.load(Services.REGISTRY);
    }
}