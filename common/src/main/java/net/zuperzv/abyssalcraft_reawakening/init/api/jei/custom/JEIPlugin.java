package net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.category.RitualAltarRecipeCategory;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.ModRecipes;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static RecipeMap syncedRecipes = RecipeMap.EMPTY;
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "jei_plugin");
    }

    // From Occultism
    // Under MIT License
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipes(RecipeMap recipeMap, RecipeType<T> type) {
        return (List) recipeMap.byType(type);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RitualAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ModJEIRecipeTypes.RITUAL_ALTAR, this.getRecipes(syncedRecipes, ModRecipes.ASTRAL_ALTAR.type().get()));
    }

    /*
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(astralAltarScreen.class, 74, 30, 22, 20,
                ModJEIRecipeTypes.RITUAL_ALTAR);
    }
     */

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ModJEIRecipeTypes.RITUAL_ALTAR, new ItemStack(ModBlocks.STONE_RITUAL_ALTAR.item().get()));
        registration.addCraftingStation(ModJEIRecipeTypes.RITUAL_ALTAR, new ItemStack(ModBlocks.STONE_RITUAL_PEDESTAL.item().get()));
    }
}