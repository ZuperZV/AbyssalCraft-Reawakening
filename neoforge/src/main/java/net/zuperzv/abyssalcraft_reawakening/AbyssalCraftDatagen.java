package net.zuperzv.abyssalcraft_reawakening;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zuperzv.abyssalcraft_reawakening.datagen.ModEnglishLanguageProvider;
import net.zuperzv.abyssalcraft_reawakening.datagen.ModItemTagProvider;
import net.zuperzv.abyssalcraft_reawakening.datagen.ModLootTableProvider;
import net.zuperzv.abyssalcraft_reawakening.datagen.ModModelProvider;

public class AbyssalCraftDatagen {
    private AbyssalCraftDatagen() {}

    public static void onGatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModEnglishLanguageProvider::new);
        event.createProvider(ModItemTagProvider::new);
        event.createProvider(ModLootTableProvider::new);
    }
}