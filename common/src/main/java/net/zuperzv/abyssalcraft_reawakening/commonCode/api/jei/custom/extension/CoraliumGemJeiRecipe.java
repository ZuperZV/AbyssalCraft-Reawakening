package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.extension;

import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.customCraftingTable.CoraliumGemRecipe;

import java.util.ArrayList;
import java.util.List;

public class CoraliumGemJeiRecipe extends CoraliumGemRecipe {

    private final List<Integer> combination;

    public CoraliumGemJeiRecipe(List<Integer> combination) {
        super(CraftingBookCategory.MISC);
        this.combination = List.copyOf(combination);
    }

    public List<Integer> getCombination() {
        return combination;
    }

    public int getTotal() {
        return combination.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static List<List<Integer>> generateCombinations() {
        List<List<Integer>> result = new ArrayList<>();

        generateCombinations(
                result,
                new ArrayList<>(),
                0,
                1
        );

        return result;
    }

    private static void generateCombinations(List<List<Integer>> result, List<Integer> current, int total, int start) {
        if (current.size() >= 2) {
            result.add(List.copyOf(current));
        }

        if (current.size() >= 9) {
            return;
        }

        for (int amount = start; amount <= 9; amount++) {
            int newTotal = total + amount;

            if (newTotal > 9) {
                break;
            }

            current.add(amount);

            generateCombinations(
                    result,
                    current,
                    newTotal,
                    amount
            );

            current.remove(current.size() - 1);
        }
    }
}