package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.ModItems;

public class ModEnglishLanguageProvider extends LanguageProvider {
    public ModEnglishLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.ABYSSALNITE_INGOT.get());

        add(ModBlocks.ABYSSALNITE_BLOCK.block().get());
    }

    private void add(Component component, String value) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(), value);
        }
    }

    private void add(Item item) {
        add(item.getDescriptionId(), format(item.getDescriptionId()));
    }

    private void add(Block block) {
        Item item = block.asItem();
        add(item);
    }

    private String format(String key) {
        String path = key.substring(key.lastIndexOf('.') + 1);

        String[] parts = path.split("_");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) continue;

            result.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }

        return result.toString().trim();
    }
}