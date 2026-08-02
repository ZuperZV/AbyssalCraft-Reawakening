package net.zuperzv.abyssalcraft_reawakening;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zuperzv.abyssalcraft_reawakening.datagen.*;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class AbyssalCraftDatagen {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModItemTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModBlockTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModModelProvider(packOutput));
        generator.addProvider(true, new ModEnglishLanguageProvider(packOutput));
        generator.addProvider(true, new ModLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new ModEquipmentAssetProvider(packOutput));

        generator.addProvider(true, new ModWorldgenProvider(packOutput, lookupProvider));
    }
}