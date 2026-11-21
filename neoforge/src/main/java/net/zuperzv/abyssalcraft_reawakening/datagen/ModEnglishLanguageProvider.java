package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.creativetab.ModCreativeTabs;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
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
        // Recipe mods
        add("recipe_mods.abyssalcraft_reawakening.ritual_altar", "Ritual Altar");
        add("recipe_mods.abyssalcraft_reawakening.essence_boiler", "Essence Boiler");
        add("recipe_mods.abyssalcraft_reawakening.crystal_growth", "Crystal Growth Chamber");

        add("recipe_mods.abyssalcraft_reawakening.time", "Time");
        add("recipe_mods.abyssalcraft_reawakening.works_in_dimension", "Works in Dimension");
        add("recipe_mods.abyssalcraft_reawakening.works_at", "Works at");
        add("recipe_mods.abyssalcraft_reawakening.day", "Day");
        add("recipe_mods.abyssalcraft_reawakening.night", "Night");
        add("recipe_mods.abyssalcraft_reawakening.both", "Both");

        add("tooltip.abyssalcraft_reawakening.gems", "Gems");
        add("tooltip.abyssalcraft_reawakening.fragment", "Fragment: %s");
        add("tooltip.abyssalcraft_reawakening.place_crystal_fragment", "Place a crystal fragment");
        add("tooltip.abyssalcraft_reawakening.growth_requires_fluid", "Next step: add at least %s mB of %s");
        add("tooltip.abyssalcraft_reawakening.growth_requires_fluid_any_amount", "Next step: add %s");
        add("tooltip.abyssalcraft_reawakening.potion", "Potion: %s");
        add("tooltip.abyssalcraft_reawakening.fluid", "Fluid: %s");
        add("tooltip.abyssalcraft_reawakening.amount", "Amount: %s / %s mB");
        add("tooltip.abyssalcraft_reawakening.progress", "Progress: %s s / %s s");
        add("block.abyssalcraft_reawakening.sulfuric_arcanum", "Sulfuric Arcanum");
        add("item.abyssalcraft_reawakening.sulfuric_arcanum_bucket", "Sulfuric Arcanum Bucket");
        add("block.abyssalcraft_reawakening.lunar_jar", "Lunar Jar");
        add("block.abyssalcraft_reawakening.lunar_infuser", "Lunar Infuser");
        add("block.abyssalcraft_reawakening.lunar_light_beam_emitter", "Lunar Light Beam Emitter");
        add("fluid.abyssalcraft_reawakening.coralium", "Liquid Coralium");
        add("item.abyssalcraft_reawakening.coralium_bucket", "Liquid Coralium Bucket");
        add("effect.abyssalcraft_reawakening.crystallization", "Crystallization");
        add("tooltip.abyssalcraft_reawakening.pe", "Potential Energy: %s / %s PE");
        add("tooltip.abyssalcraft_reawakening.sacrificial_altar_extract", "Right-click with a Stone Tablet or Necronomicon to take stored PE");
        add("message.abyssalcraft_reawakening.place_of_power_active", "Place of Power active (%s Monolith Stone, %s Pillars, %s Statues)");
        add("message.abyssalcraft_reawakening.place_of_power_incomplete", "Place of Power incomplete (%s Monolith Stone, %s Pillars, %s Statues)");

        add("tag.item.abyssalcraft_reawakening.abyssalnite_materials", "Abyssalnite Materials");
        add("tag.item.abyssalcraft_reawakening.refined_coralium_materials", "Refined Coralium Materials");
        add("tag.item.abyssalcraft_reawakening.dreadium_materials", "Dreadium Materials");
        add("tag.item.abyssalcraft_reawakening.ethaxium_materials", "Ethaxium Materials");
        add("tag.item.abyssalcraft_reawakening.witherwood_logs", "Witherwood Logs");
        add("tag.item.abyssalcraft_reawakening.gems", "Gems");
        add("tag.item.abyssalcraft_reawakening.crystal_fragments", "Crystal Fragments");
        add("tag.item.abyssalcraft_reawakening.crystal_shards", "Crystal Shards");
        add("tag.item.abyssalcraft_reawakening.crystals", "Crystals");
        add("tag.item.abyssalcraft_reawakening.bundles", "Crystal Bags");

        add("name.abyssalcraft_reawakening.cluster", "Cluster");

        add("item.minecraft.potion.effect.crystallization", "Potion of Crystallization");
        add("item.minecraft.potion.effect.long_crystallization", "Long Potion of Crystallization");

        add("item.abyssalcraft_reawakening.abyssal_trim_smithing_template", "Abyssal Armor Trim Smithing Template");
        add("trim_pattern.abyssalcraft_reawakening.abyssal", "Abyssal");

        add("item.abyssalcraft_reawakening.necronomicon", "Necronomicon");
        add("item.abyssalcraft_reawakening.necronomicon_tier", "%s %s");
        add("item.abyssalcraft_reawakening.music_disc_raw_sewer", "Music Disc - Raw Sewer");
        add("jukebox_song.abyssalcraft_reawakening.raw_sewer", "Raw Sewer");

        add("item.abyssalcraft_reawakening.abyssal_wasteland", "Abyssal Wasteland");
        add("item.abyssalcraft_reawakening.dreadlands", "Dreadlands");
        add("item.abyssalcraft_reawakening.omothol", "Omothol");
        add("item.abyssalcraft_reawakening.abyssal", "Abyssal");

        // Abyssalnite
        add(ModBlocks.ABYSSALNITE_BLOCK.block().get(), "Block of Abyssalnite");
        add(ModBlocks.RAW_ABYSSALNITE_BLOCK.block().get(), "Block of Raw Abyssalnite");
        add(ModBlocks.CORALIUM_BLOCK.block().get(), "Block of Coralium");
        add(ModBlocks.RAW_CORALIUM_BLOCK.block().get(), "Block of Raw Coralium");

        // Darkstone
        add(ModBlocks.DARK_STONE.block().get(), "Dark Stone");
        add(ModBlocks.DARK_COBBLESTONE.block().get(), "Dark Cobblestone");
        add(ModBlocks.DARK_STONE_BRICKS.block().get(), "Dark Stone Bricks");
        add(ModBlocks.CHISELED_DARK_STONE_BRICKS.block().get(), "Chiseled Dark Stone Bricks");
        add(ModBlocks.CRACKED_DARK_STONE_BRICKS.block().get(), "Cracked Dark Stone Bricks");
        add(ModBlocks.DARK_STONE_BUTTON.block().get(), "Dark Stone Button");
        add(ModBlocks.DARK_STONE_PRESSURE_PLATE.block().get(), "Dark Stone Pressure Plate");
        add(ModBlocks.DARK_STONE_STAIRS.block().get(), "Dark Stone Stairs");
        add(ModBlocks.DARK_STONE_SLAB.block().get(), "Dark Stone Slab");
        add(ModBlocks.DARK_STONE_WALL.block().get(), "Dark Stone Wall");
        add(ModBlocks.DARK_COBBLESTONE_STAIRS.block().get(), "Dark Cobblestone Stairs");
        add(ModBlocks.DARK_COBBLESTONE_SLAB.block().get(), "Dark Cobblestone Slab");
        add(ModBlocks.DARK_COBBLESTONE_WALL.block().get(), "Dark Cobblestone Wall");
        add(ModBlocks.DARK_STONE_BRICKS_STAIRS.block().get(), "Dark Stone Bricks Stairs");
        add(ModBlocks.DARK_STONE_BRICKS_SLAB.block().get(), "Dark Stone Bricks Slab");
        add(ModBlocks.DARK_STONE_BRICKS_WALL.block().get(), "Dark Stone Bricks Wall");

        // Coralium additional blocks
        add(ModBlocks.CHISELED_CORALIUM_BRICKS.block().get(), "Chiseled Coralium Bricks");
        add(ModBlocks.CRACKED_CORALIUM_BRICKS.block().get(), "Cracked Coralium Bricks");
        add(ModBlocks.CORALIUM_BUTTON.block().get(), "Coralium Button");
        add(ModBlocks.CORALIUM_PRESSURE_PLATE.block().get(), "Coralium Pressure Plate");

        // Dreadium additional blocks
        add(ModBlocks.DREADIUM_BRICKS.block().get(), "Dreadium Bricks");
        add(ModBlocks.CHISELED_DREADIUM_BRICKS.block().get(), "Chiseled Dreadium Bricks");
        add(ModBlocks.CRACKED_DREADIUM_BRICKS.block().get(), "Cracked Dreadium Bricks");
        add(ModBlocks.DREADIUM_BUTTON.block().get(), "Dreadium Button");
        add(ModBlocks.DREADIUM_PRESSURE_PLATE.block().get(), "Dreadium Pressure Plate");
        add(ModBlocks.DREADIUM_BRICKS_STAIRS.block().get(), "Dreadium Bricks Stairs");
        add(ModBlocks.DREADIUM_BRICKS_SLAB.block().get(), "Dreadium Bricks Slab");
        add(ModBlocks.DREADIUM_BRICKS_WALL.block().get(), "Dreadium Bricks Wall");

        // Omothol additional blocks
        add(ModBlocks.OMOTHOL_BRICKS.block().get(), "Omothol Bricks");
        add(ModBlocks.CHISELED_OMOTHOL_BRICKS.block().get(), "Chiseled Omothol Bricks");
        add(ModBlocks.CRACKED_OMOTHOL_BRICKS.block().get(), "Cracked Omothol Bricks");
        add(ModBlocks.OMOTHOL_BUTTON.block().get(), "Omothol Button");
        add(ModBlocks.OMOTHOL_PRESSURE_PLATE.block().get(), "Omothol Pressure Plate");
        add(ModBlocks.OMOTHOL_BRICKS_STAIRS.block().get(), "Omothol Bricks Stairs");
        add(ModBlocks.OMOTHOL_BRICKS_SLAB.block().get(), "Omothol Bricks Slab");
        add(ModBlocks.OMOTHOL_BRICKS_WALL.block().get(), "Omothol Bricks Wall");

        // Abyssalnite Stone
        add(ModBlocks.ABYSSALNITE_STONE.block().get(), "Abyssalnite Stone");
        add(ModBlocks.ABYSSALNITE_STONE_STAIRS.block().get(), "Abyssalnite Stone Stairs");
        add(ModBlocks.ABYSSALNITE_STONE_SLAB.block().get(), "Abyssalnite Stone Slab");
        add(ModBlocks.ABYSSALNITE_STONE_WALL.block().get(), "Abyssalnite Stone Wall");
        add(ModBlocks.ABYSSALNITE_COBBLESTONE.block().get(), "Abyssalnite Cobblestone");
        add(ModBlocks.ABYSSALNITE_COBBLESTONE_STAIRS.block().get(), "Abyssalnite Cobblestone Stairs");
        add(ModBlocks.ABYSSALNITE_COBBLESTONE_SLAB.block().get(), "Abyssalnite Cobblestone Slab");
        add(ModBlocks.ABYSSALNITE_COBBLESTONE_WALL.block().get(), "Abyssalnite Cobblestone Wall");
        add(ModBlocks.ABYSSALNITE_BRICKS.block().get(), "Abyssalnite Bricks");
        add(ModBlocks.ABYSSALNITE_BRICKS_STAIRS.block().get(), "Abyssalnite Bricks Stairs");
        add(ModBlocks.ABYSSALNITE_BRICKS_SLAB.block().get(), "Abyssalnite Bricks Slab");
        add(ModBlocks.ABYSSALNITE_BRICKS_WALL.block().get(), "Abyssalnite Bricks Wall");
        add(ModBlocks.CHISELED_ABYSSALNITE_BRICKS.block().get(), "Chiseled Abyssalnite Bricks");
        add(ModBlocks.CRACKED_ABYSSALNITE_BRICKS.block().get(), "Cracked Abyssalnite Bricks");
        add(ModBlocks.ABYSSALNITE_BUTTON.block().get(), "Abyssalnite Button");
        add(ModBlocks.ABYSSALNITE_PRESSURE_PLATE.block().get(), "Abyssalnite Pressure Plate");

        // Ethaxium Stone
        add(ModBlocks.ETHAXIUM_STONE.block().get(), "Ethaxium Stone");
        add(ModBlocks.ETHAXIUM_STONE_STAIRS.block().get(), "Ethaxium Stone Stairs");
        add(ModBlocks.ETHAXIUM_STONE_SLAB.block().get(), "Ethaxium Stone Slab");
        add(ModBlocks.ETHAXIUM_STONE_WALL.block().get(), "Ethaxium Stone Wall");
        add(ModBlocks.ETHAXIUM_COBBLESTONE.block().get(), "Ethaxium Cobblestone");
        add(ModBlocks.ETHAXIUM_COBBLESTONE_STAIRS.block().get(), "Ethaxium Cobblestone Stairs");
        add(ModBlocks.ETHAXIUM_COBBLESTONE_SLAB.block().get(), "Ethaxium Cobblestone Slab");
        add(ModBlocks.ETHAXIUM_COBBLESTONE_WALL.block().get(), "Ethaxium Cobblestone Wall");
        add(ModBlocks.ETHAXIUM_BRICKS.block().get(), "Ethaxium Bricks");
        add(ModBlocks.ETHAXIUM_BRICKS_STAIRS.block().get(), "Ethaxium Bricks Stairs");
        add(ModBlocks.ETHAXIUM_BRICKS_SLAB.block().get(), "Ethaxium Bricks Slab");
        add(ModBlocks.ETHAXIUM_BRICKS_WALL.block().get(), "Ethaxium Bricks Wall");
        add(ModBlocks.CHISELED_ETHAXIUM_BRICKS.block().get(), "Chiseled Ethaxium Bricks");
        add(ModBlocks.CRACKED_ETHAXIUM_BRICKS.block().get(), "Cracked Ethaxium Bricks");
        add(ModBlocks.ETHAXIUM_BUTTON.block().get(), "Ethaxium Button");
        add(ModBlocks.ETHAXIUM_PRESSURE_PLATE.block().get(), "Ethaxium Pressure Plate");

        // Creative tab
        add(ModCreativeTabs.ABYSSALCRAFT_TAB.get().getDisplayName(),
                "Abyssalcraft Reawakening");

        // Automatically generate missing translations
        getKnownItems().forEach(this::addIfMissing);
        getKnownBlocks().forEach(this::addIfMissing);
        getKnownEntityTypes().forEach(this::addIfMissing);
        getKnownEffects().forEach(this::addIfMissing);
    }

    @Override
    public void add(String key, String value) {
        generatedKeys.add(key);
        super.add(key, value);
    }

    private void add(Component component, String value) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            generatedKeys.add(translatableContents.getKey());
            add(translatableContents.getKey(), value);
        }
    }

    private void add(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            generatedKeys.add(translatableContents.getKey());
            add(
                    translatableContents.getKey(),
                    format(component.getString())
            );
        }
    }

    private void add(Item item) {
        generatedKeys.add(item.getDescriptionId());
        add(
                item.getDescriptionId(),
                format(item.getDescriptionId())
        );
    }

    private void add(Block block) {
        Item item = block.asItem();
        generatedKeys.add(block.getDescriptionId());

        if (item != null) {
            add(item);
        }
    }

    public void add(Block block, String name) {
        Item item = block.asItem();
        generatedKeys.add(block.getDescriptionId());

        if (item != null) {
            add(item, name);
        }
    }

    private void add(EntityType<?> entityType) {
        generatedKeys.add(entityType.getDescriptionId());
        add(
                entityType.getDescriptionId(),
                format(entityType.getDescriptionId())
        );
    }

    private void add(MobEffect effect) {
        generatedKeys.add(effect.getDescriptionId());
        add(
                effect.getDescriptionId(),
                format(effect.getDescriptionId())
        );
    }

    private void addIfMissing(MobEffect effect) {
        String key = effect.getDescriptionId();

        if (!generatedKeys.contains(key)) {
            add(effect);
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

    private void addIfMissing(EntityType<?> entityType) {
        String key = entityType.getDescriptionId();

        if (!generatedKeys.contains(key)) {
            add(entityType);
        }
    }

    private String format(String key) {
        String path = key.substring(key.lastIndexOf('.') + 1);

        String[] parts = path.split("_");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

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

    protected @NonNull Iterable<? extends EntityType<?>> getKnownEntityTypes() {
        return NeoForgeRegistryHelper.ENTITIES.getEntries()
                .stream()
                .map(entry -> entry.value())
                .toList();
    }

    protected @NonNull Iterable<MobEffect> getKnownEffects() {
        return NeoForgeRegistryHelper.MOB_EFFECTS.getEntries()
                .stream()
                .map(entry -> (MobEffect) entry.value())
                .toList();
    }
}
