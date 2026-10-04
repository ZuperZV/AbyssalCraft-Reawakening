package net.zuperzv.abyssalcraft_reawakening.datagen.custom;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.CrystalGrowthRecipe;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CrystalGrowthRecipeBuilder implements RecipeBuilder {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private Optional<EssenceBoilerFluid> inputFluid = Optional.empty();
    private Optional<ItemStackTemplate> result = Optional.empty();
    private Optional<EssenceBoilerFluid> outputFluid = Optional.empty();
    private boolean preserveFluidAmount;
    private final RecipeUnlockAdvancementBuilder advancementBuilder =
            new RecipeUnlockAdvancementBuilder();
    private int recipeTime = 200;

    private CrystalGrowthRecipeBuilder() {
    }

    public static CrystalGrowthRecipeBuilder create() {
        return new CrystalGrowthRecipeBuilder();
    }

    public CrystalGrowthRecipeBuilder ingredient(ItemLike item) {
        return ingredient(Ingredient.of(item));
    }

    public CrystalGrowthRecipeBuilder ingredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public CrystalGrowthRecipeBuilder inputFluid(Fluid fluid, int amount) {
        inputFluid = Optional.of(new EssenceBoilerFluid(fluid, amount, null));
        return this;
    }

    public CrystalGrowthRecipeBuilder result(ItemLike item) {
        return result(new ItemStackTemplate(item.asItem()));
    }

    public CrystalGrowthRecipeBuilder result(ItemLike item, int count) {
        return result(new ItemStackTemplate(item.asItem(), count));
    }

    public CrystalGrowthRecipeBuilder result(ItemStackTemplate result) {
        this.result = Optional.of(result);
        return this;
    }

    public CrystalGrowthRecipeBuilder outputFluid(Fluid fluid, int amount) {
        outputFluid = Optional.of(new EssenceBoilerFluid(fluid, amount, null));
        return this;
    }

    public CrystalGrowthRecipeBuilder preserveFluidAmount() {
        preserveFluidAmount = true;
        return this;
    }

    public CrystalGrowthRecipeBuilder duration(int ticks) {
        recipeTime = ticks;
        return this;
    }

    @Override
    public CrystalGrowthRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public CrystalGrowthRecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        var outputId = result.isPresent()
                ? BuiltInRegistries.ITEM.getKey(result.get().item().value())
                : BuiltInRegistries.FLUID.getKey(outputFluid.orElseThrow().fluid());
        return ResourceKey.create(
                Registries.RECIPE,
                outputId.withPath(path -> "crystal_growth/" + path)
        );
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        CrystalGrowthRecipe recipe = new CrystalGrowthRecipe(
                ingredients,
                inputFluid,
                result,
                outputFluid,
                preserveFluidAmount,
                recipeTime
        );
        output.accept(
                id,
                recipe,
                advancementBuilder.build(output, id, RecipeCategory.MISC)
        );
    }
}
