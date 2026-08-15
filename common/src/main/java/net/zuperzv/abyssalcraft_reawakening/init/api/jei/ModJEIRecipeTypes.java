package net.zuperzv.abyssalcraft_reawakening.init.api.jei;

import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.api.multiblock.MultiblockDisplay;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.StoneRitualAltarRecipe;

public class ModJEIRecipeTypes {

    public static final IRecipeType<RecipeHolder<StoneRitualAltarRecipe>> RITUAL_ALTAR =
            create(
                    Constants.MOD_ID,
                    "ritual_altar",
                    StoneRitualAltarRecipe.class
            );

    public static final IRecipeType<MultiblockDisplay> MULTIBLOCK =
            IRecipeType.create(
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "multiblock"
                    ),
                    MultiblockDisplay.class
            );

    // From Occultism:
    // https://github.com/klikli-dev/occultism/blob/version/26.1.2/src/main/java/com/klikli_dev/occultism/integration/jei/impl/JeiRecipeTypes.java
    // Under MIT License
    public static <R extends Recipe<?>> IRecipeType<RecipeHolder<R>> create(
            String modid,
            String name,
            Class<? extends R> recipeClass
    ) {
        Identifier uid =
                Identifier.fromNamespaceAndPath(
                        modid,
                        name
                );

        @SuppressWarnings({
                "unchecked",
                "RedundantCast"
        })
        Class<? extends RecipeHolder<R>> holderClass =
                (Class<? extends RecipeHolder<R>>)
                        (Object) RecipeHolder.class;

        return IRecipeType.create(
                uid,
                holderClass
        );
    }
}