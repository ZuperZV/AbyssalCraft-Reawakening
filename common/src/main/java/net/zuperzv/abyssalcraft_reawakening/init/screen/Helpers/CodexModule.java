package net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers;

import java.util.List;
import java.util.Map;

public class CodexModule {
    public String module_type; // fx "text", "recipe", "furnace_recipe"

    public String text;
    public String text_key;

    // Crafting recipe
    public String recipe_type; // fx "crafting_table"
    public String recipeId; // fx "abyssalcraft_reawakening:my_sword_recipe"
    public List<String> pattern;
    public Map<String, String> key;
    public String result;

    // Furnace recipe
    public String input;
    public String output;
    public float experience;
    public int cooking_time;
}
