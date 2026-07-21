package net.zuperzv.abyssalcraft_reawakening.init.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.zuperzv.abyssalcraft_reawakening.services.types.IRegistryHelper;

public class ModRecipes {
    private ModRecipes() {}

    public static IRegistryHelper.RecipeRegistryHandle<
            RecipeType<StoneRitualAltarRecipe>,
            RecipeSerializer<StoneRitualAltarRecipe>
            > ASTRAL_ALTAR;

    public static void load(IRegistryHelper registry) {
        ASTRAL_ALTAR = registry.registerRecipeTypeAndSerializer(
                "astral_altar",

                () -> new RecipeType<StoneRitualAltarRecipe>()
                {@Override
                public String toString() {
                    return "astral_altar";
                    }
                },
                () -> new RecipeSerializer<>(StoneRitualAltarRecipe.CODEC, StoneRitualAltarRecipe.STREAM_CODEC)
        );
    }
}