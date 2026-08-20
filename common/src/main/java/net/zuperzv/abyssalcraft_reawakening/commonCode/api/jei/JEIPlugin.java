package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.multiblock.MultiblockDisplay;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.category.MultiblockRecipeCategory;
import net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.category.RitualAltarRecipeCategory;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

import java.util.Collection;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static RecipeMap syncedRecipes =
            RecipeMap.EMPTY;

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                "jei_plugin"
        );
    }

    @SuppressWarnings({
            "unchecked",
            "rawtypes"
    })
    private <
            I extends RecipeInput,
            T extends Recipe<I>
            > List<RecipeHolder<T>> getRecipes(
            RecipeMap recipeMap,
            RecipeType<T> type
    ) {
        return (List)
                recipeMap.byType(type);
    }

    @Override
    public void registerCategories(
            IRecipeCategoryRegistration registration
    ) {
        var guiHelper =
                registration
                        .getJeiHelpers()
                        .getGuiHelper();

        registration.addRecipeCategories(
                new RitualAltarRecipeCategory(
                        guiHelper
                )
        );

        registration.addRecipeCategories(
                new MultiblockRecipeCategory(
                        guiHelper
                )
        );
    }

    @Override
    public void registerRecipes(
            IRecipeRegistration registration
    ) {
        registerRecipe(
                registration,
                ModJEIRecipeTypes.RITUAL_ALTAR,
                ModRecipes.ASTRAL_ALTAR
                        .type()
                        .get()
        );

        registration.addRecipes(
                ModJEIRecipeTypes.MULTIBLOCK,
                MultiblockDisplay.ALL
        );
    }

    @Override
    public void registerRecipeCatalysts(
            IRecipeCatalystRegistration registration
    ) {
        System.out.println(
                "registerRecipeCatalysts Loaded. From loader: "
                        +
                        Services.PLATFORM
                                .getPlatformName()
        );

        registration.addCraftingStation(
                ModJEIRecipeTypes.RITUAL_ALTAR,
                new ItemStack(
                        ModBlocks.STONE_RITUAL_ALTAR
                                .item()
                                .get()
                )
        );

        registration.addCraftingStation(
                ModJEIRecipeTypes.RITUAL_ALTAR,
                new ItemStack(
                        ModBlocks.STONE_RITUAL_PEDESTAL
                                .item()
                                .get()
                )
        );

        for (
                ItemStack icon
                : MultiblockDisplay.getUniqueIcons()
        ) {
            registration.addCraftingStation(
                    ModJEIRecipeTypes.MULTIBLOCK,
                    icon
            );
        }
    }

    private <
            I extends RecipeInput,
            T extends Recipe<I>
            > void registerRecipe(
            IRecipeRegistration registration,
            mezz.jei.api.recipe.types.IRecipeType<RecipeHolder<T>> recipeType,
            RecipeType<T> type
    ) {
        if (
                "NeoForge".equals(
                        Services.PLATFORM
                                .getPlatformName()
                )
        ) {
            registration.addRecipes(
                    recipeType,
                    getRecipes(
                            syncedRecipes,
                            type
                    )
            );
        } else {
            registration.addRecipes(
                    recipeType,
                    Services.PLATFORM
                            .getAllOfType(type)
                            .stream()
                            .flatMap(Collection::stream)
                            .toList()
            );
        }
    }
}