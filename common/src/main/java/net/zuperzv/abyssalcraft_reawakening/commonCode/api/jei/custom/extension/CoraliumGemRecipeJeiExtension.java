package net.zuperzv.abyssalcraft_reawakening.commonCode.api.jei.custom.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.custom.CoraliumGemRecipe;

import java.util.ArrayList;
import java.util.List;

public class CoraliumGemRecipeJeiExtension implements ICraftingCategoryExtension<CoraliumGemRecipe> {

    private static ItemStack createGem(int amount) {
        ItemStack stack = new ItemStack(
                ModItems.CORALIUM_GEM.get()
        );

        stack.set(
                ModDataComponentTypes.CORALIUM_GEMS.get(),
                new CoraliumGemsData(amount)
        );

        return stack;
    }

    private static SlotDisplay createGemDisplay(int amount) {
        return new SlotDisplay.ItemStackSlotDisplay(
                ItemStackTemplate.fromNonEmptyStack(
                        createGem(amount)
                )
        );
    }

    @Override
    public List<SlotDisplay> getIngredients(RecipeHolder<CoraliumGemRecipe> recipeHolder) {
        if (!(recipeHolder.value() instanceof CoraliumGemJeiRecipe recipe)) {
            return List.of();
        }

        return recipe.getCombination()
                .stream()
                .map(CoraliumGemRecipeJeiExtension::createGemDisplay)
                .toList();
    }

    @Override
    public void setRecipe(RecipeHolder<CoraliumGemRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        if (!(recipeHolder.value() instanceof CoraliumGemJeiRecipe recipe)) {
            return;
        }

        List<SlotDisplay> inputs = new ArrayList<>();

        for (int amount : recipe.getCombination()) {
            inputs.add(createGemDisplay(amount));
        }

        while (inputs.size() < 9) {
            inputs.add(SlotDisplay.Empty.INSTANCE);
        }

        craftingGridHelper.createAndSetIngredientsFromDisplays(
                builder,
                inputs,
                3,
                3
        );

        craftingGridHelper.createAndSetOutputs(
                builder,
                createGemDisplay(recipe.getTotal())
        );
    }

    @Override
    public int getWidth(RecipeHolder<CoraliumGemRecipe> recipeHolder) {
        return 3;
    }

    @Override
    public int getHeight(RecipeHolder<CoraliumGemRecipe> recipeHolder) {
        return 3;
    }
}