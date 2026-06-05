package net.zuperzv.abyssalcraft_reawakening;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zuperzv.abyssalcraft_reawakening.datagen.*;

public class AbyssalCraftDatagen {
    private AbyssalCraftDatagen() {}

    public static void onGatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModItemTagProvider::new);
        event.createProvider(ModBlockTagProvider::new);
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModEnglishLanguageProvider::new);
        event.createProvider(ModLootTableProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModEquipmentAssetProvider::new);
        event.createProvider(ModWorldgenProvider::new);
    }
}