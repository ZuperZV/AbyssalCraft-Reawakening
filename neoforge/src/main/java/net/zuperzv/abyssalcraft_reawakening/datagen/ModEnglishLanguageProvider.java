package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.ModCreativeTabs;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Set;

public class ModEnglishLanguageProvider extends LanguageProvider {
    private final Set<String> generatedKeys = new HashSet<>();

    public ModEnglishLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.ABYSSALNITE_BLOCK.block().get(), "Block of Abyssalnite");
        add(ModBlocks.RAW_ABYSSALNITE_BLOCK.block().get(), "Block of Raw Abyssalnite");

        add(ModCreativeTabs.ABYSSALCRAFT_TAB.get().getDisplayName(), "Abyssalcraft Reawakening");

        getKnownItems().forEach(this::addIfMissing);
        getKnownBlocks().forEach(this::addIfMissing);
    }

    @Override
    public void add(String key, String value) {
        generatedKeys.add(key);
        super.add(key, value);
    }

    private void add(Component component, String value) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(), value);
        }
    }

    private void add(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(), format(component.getString()));
        }
    }

    private void add(Item item) {
        add(item.getDescriptionId(), format(item.getDescriptionId()));
    }

    private void add(Block block) {
        Item item = block.asItem();

        if (item != null) {
            add(item);
        }
    }

    private void addIfMissing(Item item) {
        String key = item.getDescriptionId();

        if (!generatedKeys.contains(key)) {
            add(item);
        }
    }

    private void addIfMissing(Block block) {
        Item item = block.asItem();

        if (item != null) {
            addIfMissing(item);
        }
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

    protected @NonNull Iterable<Block> getKnownBlocks() {
        return NeoForgeRegistryHelper.BLOCKS.getEntries()
                .stream()
                .map(entry -> (Block) entry.value())
                .toList();
    }

    protected @NonNull Iterable<Item> getKnownItems() {
        return NeoForgeRegistryHelper.ITEMS.getEntries()
                .stream()
                .map(entry -> (Item) entry.value())
                .toList();
    }
}