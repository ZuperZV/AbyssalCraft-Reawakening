package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeHelper {
    public static ItemStack parseItem(String id) {
        if (id == null || id.isEmpty()) return ItemStack.EMPTY;
        Identifier rl = Identifier.parse(id);

        if (BuiltInRegistries.ITEM.containsKey(rl)) {
            return new ItemStack(BuiltInRegistries.ITEM.getValue(rl));
        }

        if (BuiltInRegistries.BLOCK.containsKey(rl)) {
            return new ItemStack(BuiltInRegistries.BLOCK.getValue(rl));
        }

        return ItemStack.EMPTY;
    }


    public static List<ItemStack> buildCraftingGrid(CodexModule module) {
        List<ItemStack> grid = new ArrayList<>();
        if (module.pattern == null || module.key == null) return grid;

        for (String row : module.pattern) {
            for (char c : row.toCharArray()) {
                String keyStr = String.valueOf(c);
                if (module.key.containsKey(keyStr)) {
                    grid.add(parseItem(module.key.get(keyStr)));
                } else {
                    grid.add(ItemStack.EMPTY);
                }
            }
        }
        return grid;
    }
}