package net.zuperzv.abyssalcraft_reawakening.init.creativetab;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ModCreativeTabItemSorter {

    private ModCreativeTabItemSorter() {
    }

    public static List<Item> getOrderedItems() {
        List<Item> masterItems = getModItemsInDeclarationOrder();

        List<Item> blocks = BuiltInRegistries.ITEM.stream()
                .filter(ModCreativeTabItemSorter::isOurItem)
                .filter(item -> item instanceof BlockItem)
                .filter(ModCreativeTabItemSorter::shouldDisplay)
                .toList();

        Set<Item> alreadyPresent = Collections.newSetFromMap(
                new IdentityHashMap<>()
        );

        alreadyPresent.addAll(masterItems);

        List<Item> unplacedBlocks = blocks.stream()
                .filter(item -> !alreadyPresent.contains(item))
                .toList();

        List<Item> result = new ArrayList<>(masterItems);

        insertRelatedBlocks(result, unplacedBlocks);

        Set<Item> resultSet = Collections.newSetFromMap(
                new IdentityHashMap<>()
        );

        resultSet.addAll(result);

        for (Item item : BuiltInRegistries.ITEM) {

            if (!isOurItem(item)) {
                continue;
            }

            if (!shouldDisplay(item)) {
                continue;
            }

            if (resultSet.contains(item)) {
                continue;
            }

            result.add(item);
            resultSet.add(item);
        }


        return List.copyOf(result);
    }

    private static List<Item> getModItemsInDeclarationOrder() {

        List<Item> result = new ArrayList<>();

        for (Field field : ModItems.class.getDeclaredFields()) {

            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (!RegistryHandle.class.isAssignableFrom(field.getType())) {
                continue;
            }

            try {

                field.setAccessible(true);

                Object value = field.get(null);

                if (!(value instanceof RegistryHandle<?> handle)) {
                    continue;
                }

                Object registered = handle.get();

                if (!(registered instanceof Item item)) {
                    continue;
                }

                if (!isOurItem(item)) {
                    continue;
                }

                if (!shouldDisplay(item)) {
                    continue;
                }

                result.add(item);

            } catch (IllegalAccessException ignored) {
            }
        }

        return result;
    }

    private static void insertRelatedBlocks(
            List<Item> result,
            List<Item> blocks
    ) {
        List<BlockPlacement> placements = new ArrayList<>();

        for (Item block : blocks) {

            int bestIndex = -1;
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < result.size(); i++) {

                Item anchor = result.get(i);

                if (block == anchor) {
                    continue;
                }

                int score = relationScore(block, anchor);

                if (score > bestScore) {

                    bestScore = score;
                    bestIndex = i;
                }
            }

            if (bestIndex >= 0 && bestScore >= MIN_RELATION_SCORE) {

                placements.add(
                        new BlockPlacement(
                                block,
                                bestIndex,
                                bestScore
                        )
                );
            }
        }

        placements.sort(
                Comparator
                        .comparingInt(BlockPlacement::score)
                        .reversed()
                        .thenComparingInt(BlockPlacement::anchorIndex)
        );

        Map<Integer, List<Item>> grouped = new HashMap<>();

        for (BlockPlacement placement : placements) {

            grouped
                    .computeIfAbsent(
                            placement.anchorIndex(),
                            ignored -> new ArrayList<>()
                    )
                    .add(placement.block());
        }

        List<Integer> indexes = new ArrayList<>(grouped.keySet());

        indexes.sort(Comparator.reverseOrder());

        for (int index : indexes) {

            List<Item> related = grouped.get(index);

            related.sort(
                    Comparator
                            .<Item>comparingInt(
                                    block -> -relationScore(
                                            block,
                                            result.get(index)
                                    )
                            )
                            .thenComparing(
                                    ModCreativeTabItemSorter::getPath
                            )
            );

            result.addAll(
                    index + 1,
                    related
            );
        }
    }

    private static int relationScore(
            Item first,
            Item second
    ) {

        String firstId = getPath(first);
        String secondId = getPath(second);

        if (first == second) {
            return Integer.MIN_VALUE;
        }


        int score = 0;

        if (firstId.equals(secondId)) {
            return 10000;
        }

        String firstNormalized = normalize(firstId);
        String secondNormalized = normalize(secondId);


        if (firstNormalized.equals(secondNormalized)) {
            score += 5000;
        }

        String firstMaterial = extractMaterialFamily(firstId);
        String secondMaterial = extractMaterialFamily(secondId);


        if (!firstMaterial.isEmpty()
                && firstMaterial.equals(secondMaterial)) {

            score += 2500;
        }

        Set<String> firstWords = meaningfulWords(firstId);
        Set<String> secondWords = meaningfulWords(secondId);


        for (String word : firstWords) {

            if (secondWords.contains(word)) {

                score += wordScore(word);
            }
        }

        String commonPrefix =
                longestMeaningfulPrefix(
                        firstId,
                        secondId
                );

        if (!commonPrefix.isEmpty()) {

            score += Math.min(
                    commonPrefix.length() * 80,
                    1000
            );
        }

        score += tokenSimilarity(
                firstWords,
                secondWords
        );

        int firstDimension = getDimension(firstId);
        int secondDimension = getDimension(secondId);


        if (firstDimension == secondDimension) {

            score += 500;

        } else if (
                firstDimension != DIMENSION_UNKNOWN
                        && secondDimension != DIMENSION_UNKNOWN
        ) {
            score += 50;
        }

        if (first instanceof BlockItem
                || second instanceof BlockItem) {

            score += blockRelationBonus(
                    firstId,
                    secondId
            );
        }

        int firstFamily = getFamily(firstId);
        int secondFamily = getFamily(secondId);


        if (firstFamily == secondFamily) {

            score += 250;
        }

        int firstForm = getMaterialForm(firstId);
        int secondForm = getMaterialForm(secondId);


        if (firstMaterial.equals(secondMaterial)
                && firstForm != FORM_UNKNOWN
                && secondForm != FORM_UNKNOWN) {

            score += formRelationship(
                    firstForm,
                    secondForm
            );
        }


        return score;
    }

    private static String extractMaterialFamily(String id) {

        String normalized = normalize(id);

        normalized = removePrefixes(
                normalized,

                "abyssal_wasteland_",
                "dreadlands_",
                "dreaded_",
                "omothol_",
                "overworld_"
        );

        if (normalized.startsWith("block_of_")) {

            normalized = normalized.substring(
                    "block_of_".length()
            );
        }

        if (normalized.startsWith("raw_")) {

            normalized = normalized.substring(
                    "raw_".length()
            );
        }

        String[] forms = {
                "_ingot",
                "_nugget",
                "_ore",
                "_block",
                "_dust",
                "_fragment",
                "_shard",
                "_gem",
                "_pearl",
                "_sword",
                "_pickaxe",
                "_axe",
                "_shovel",
                "_hoe",
                "_spear",
                "_helmet",
                "_chestplate",
                "_leggings",
                "_boots",
                "_stairs",
                "_slab",
                "_wall",
                "_fence",
                "_button",
                "_pressure_plate",
                "_planks",
                "_log",
                "_leaves",
                "_sapling"
        };


        boolean changed;

        do {

            changed = false;

            for (String form : forms) {

                if (normalized.endsWith(form)) {

                    normalized = normalized.substring(
                            0,
                            normalized.length() - form.length()
                    );

                    changed = true;

                    break;
                }
            }

        } while (changed);

        return normalized;
    }

    private static Set<String> meaningfulWords(String id) {

        String normalized = normalize(id);

        String[] rawWords =
                normalized.split("_");


        Set<String> words = new LinkedHashSet<>();


        for (String word : rawWords) {

            if (word.isEmpty()) {
                continue;
            }

            if (isGenericWord(word)) {
                continue;
            }

            words.add(word);
        }

        for (String word : new ArrayList<>(words)) {

            if (word.startsWith("abyssalnite")) {

                words.add("abyssal");
                words.add("abyssalnite");
            }

            if (word.startsWith("dreadlands")) {

                words.add("dread");
                words.add("dreadlands");
            }

            if (word.startsWith("dreadstone")) {

                words.add("dread");
                words.add("dreadstone");
            }

            if (word.startsWith("dreadwood")) {

                words.add("dread");
                words.add("dreadwood");
            }

            if (word.startsWith("coralium")) {

                words.add("coral");
                words.add("coralium");
            }

            if (word.startsWith("ethaxium")) {

                words.add("ethaxium");
            }

            if (word.startsWith("omothol")) {

                words.add("omothol");
            }
        }


        return words;
    }

    private static int wordScore(String word) {

        return switch (word) {

            case "abyssalnite" -> 1400;

            case "abyssal" -> 600;

            case "dreadlands" -> 900;

            case "dread" -> 500;

            case "dreadstone" -> 1000;

            case "dreadwood" -> 1000;

            case "coralium" -> 1000;

            case "ethaxium" -> 1000;

            case "omothol" -> 1000;

            case "stone" -> 300;

            case "wood" -> 300;

            case "coral" -> 300;

            default -> 200;
        };
    }

    private static int tokenSimilarity(
            Set<String> first,
            Set<String> second
    ) {

        int score = 0;

        for (String a : first) {

            for (String b : second) {

                if (a.equals(b)) {
                    continue;
                }

                if (a.length() >= 4
                        && b.length() >= 4
                        && (a.startsWith(b) || b.startsWith(a))) {

                    score += Math.min(
                            Math.min(a.length(), b.length()) * 60,
                            600
                    );
                }
            }
        }

        return score;
    }

    private static String longestMeaningfulPrefix(
            String first,
            String second
    ) {

        String[] a = normalize(first).split("_");
        String[] b = normalize(second).split("_");


        int length = Math.min(
                a.length,
                b.length
        );


        StringBuilder result =
                new StringBuilder();


        for (int i = 0; i < length; i++) {

            if (!a[i].equals(b[i])) {
                break;
            }

            if (isGenericWord(a[i])) {
                break;
            }

            if (!result.isEmpty()) {
                result.append("_");
            }

            result.append(a[i]);
        }


        return result.toString();
    }

    private static int blockRelationBonus(
            String first,
            String second
    ) {

        String a = normalize(first);
        String b = normalize(second);

        if (a.endsWith("_ore")
                || b.endsWith("_ore")) {

            return 350;
        }

        if (a.startsWith("block_of_")
                || b.startsWith("block_of_")
                || a.endsWith("_block")
                || b.endsWith("_block")) {

            return 500;
        }

        if (containsAny(
                a,
                "stone",
                "brick",
                "planks",
                "log",
                "wood"
        ) && containsAny(
                b,
                "stone",
                "brick",
                "planks",
                "log",
                "wood"
        )) {

            return 250;
        }


        return 0;
    }

    private static int getMaterialForm(String id) {

        String normalized = normalize(id);


        if (normalized.endsWith("_ore")) {
            return FORM_ORE;
        }

        if (normalized.startsWith("raw_")) {
            return FORM_RAW;
        }

        if (normalized.endsWith("_fragment")) {
            return FORM_FRAGMENT;
        }

        if (normalized.endsWith("_shard")) {
            return FORM_SHARD;
        }

        if (normalized.endsWith("_gem")) {
            return FORM_GEM;
        }

        if (normalized.endsWith("_nugget")) {
            return FORM_NUGGET;
        }

        if (normalized.endsWith("_ingot")) {
            return FORM_INGOT;
        }

        if (normalized.startsWith("block_of_")
                || normalized.endsWith("_block")) {
            return FORM_BLOCK;
        }


        return FORM_UNKNOWN;
    }


    private static int formRelationship(
            int first,
            int second
    ) {

        if (first == second) {
            return 200;
        }


        /*
         * Ore -> Raw -> Nugget -> Ingot -> Block
         */
        int distance =
                Math.abs(first - second);


        return Math.max(
                0,
                250 - distance * 25
        );
    }

    private static int getFamily(String id) {

        String normalized = normalize(id);


        if (normalized.endsWith("_ore")) {
            return 1;
        }

        if (normalized.contains("ingot")
                || normalized.contains("nugget")
                || normalized.startsWith("raw_")) {
            return 2;
        }

        if (normalized.contains("sword")
                || normalized.contains("axe")
                || normalized.contains("pickaxe")
                || normalized.contains("shovel")
                || normalized.contains("hoe")
                || normalized.contains("spear")) {
            return 3;
        }

        if (normalized.contains("helmet")
                || normalized.contains("chestplate")
                || normalized.contains("leggings")
                || normalized.contains("boots")) {
            return 4;
        }

        if (normalized.contains("staff")) {
            return 5;
        }

        if (normalized.contains("essence")) {
            return 6;
        }

        if (normalized.contains("flesh")) {
            return 7;
        }

        if (normalized.contains("stone")
                || normalized.contains("wood")
                || normalized.contains("log")
                || normalized.contains("planks")) {
            return 8;
        }


        return 100;
    }

    private static final int DIMENSION_UNKNOWN = -1;

    private static int getDimension(String id) {

        String normalized = normalize(id);


        if (normalized.contains("abyssal_wasteland")) {
            return 1;
        }

        if (normalized.contains("dreadlands")
                || normalized.startsWith("dreaded_")
                || normalized.startsWith("dreadstone")
                || normalized.startsWith("dreadwood")) {
            return 2;
        }

        if (normalized.contains("omothol")) {
            return 3;
        }

        if (normalized.contains("coralium")) {
            return 4;
        }

        if (normalized.contains("elysian")) {
            return 5;
        }

        if (normalized.contains("ethaxium")) {
            return 6;
        }


        return DIMENSION_UNKNOWN;
    }

    private static boolean isGenericWord(String word) {

        return switch (word) {

            case "item",
                 "block",
                 "of",
                 "the",
                 "ore",
                 "raw",
                 "ingot",
                 "nugget",
                 "fragment",
                 "shard",
                 "gem",
                 "pearl",
                 "dust",
                 "sword",
                 "axe",
                 "pickaxe",
                 "shovel",
                 "hoe",
                 "spear",
                 "helmet",
                 "chestplate",
                 "leggings",
                 "boots",
                 "stairs",
                 "slab",
                 "wall",
                 "fence",
                 "button",
                 "sign",
                 "door",
                 "trapdoor",
                 "planks",
                 "log",
                 "leaves",
                 "sapling" -> true;

            default -> false;
        };
    }

    private static boolean shouldDisplay(Item item) {

        String id = getPath(item);

        if (id.equals("potential_energy")) {
            return false;
        } else if (id.equals("potted_witherwood_sapling")) {
            return false;
        }

        return !containsAny(
                id,
                "debug",
                "internal",
                "test",
                "dummy",
                "portal_block"
        );
    }

    private static boolean isOurItem(Item item) {

        var key = BuiltInRegistries.ITEM.getKey(item);

        return key != null
                && Constants.MOD_ID.equals(
                key.getNamespace()
        );
    }

    private static String getPath(Item item) {

        var key = BuiltInRegistries.ITEM.getKey(item);

        return key == null
                ? ""
                : key.getPath();
    }

    private static String normalize(String value) {

        return value
                .toLowerCase(Locale.ROOT)
                .replace('-', '_');
    }

    private static String removePrefixes(
            String value,
            String... prefixes
    ) {

        boolean changed;

        do {

            changed = false;

            for (String prefix : prefixes) {

                if (value.startsWith(prefix)) {

                    value = value.substring(
                            prefix.length()
                    );

                    changed = true;

                    break;
                }
            }

        } while (changed);


        return value;
    }

    private static boolean containsAny(
            String value,
            String... values
    ) {

        for (String test : values) {

            if (value.contains(test)) {
                return true;
            }
        }

        return false;
    }

    private static final int FORM_UNKNOWN = 0;
    private static final int FORM_ORE = 1;
    private static final int FORM_RAW = 2;
    private static final int FORM_FRAGMENT = 3;
    private static final int FORM_SHARD = 4;
    private static final int FORM_GEM = 5;
    private static final int FORM_NUGGET = 6;
    private static final int FORM_INGOT = 7;
    private static final int FORM_BLOCK = 8;

    private static final int MIN_RELATION_SCORE = 450;

    private record BlockPlacement(
            Item block,
            int anchorIndex,
            int score
    ) {
    }
}