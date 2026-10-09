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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.EssenceBoilerRecipe;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EssenceBoilerRecipeBuilder implements RecipeBuilder {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<ItemStackTemplate> results = new ArrayList<>();
    private Optional<EssenceBoilerFluid> inputFluid = Optional.empty();
    private Optional<EssenceBoilerFluid> outputFluid = Optional.empty();
    private boolean preserveFluidAmount;
    private final RecipeUnlockAdvancementBuilder advancementBuilder =
            new RecipeUnlockAdvancementBuilder();
    private int recipeTime = 100;

    private EssenceBoilerRecipeBuilder() {
    }

    public static EssenceBoilerRecipeBuilder create() {
        return new EssenceBoilerRecipeBuilder();
    }

    public EssenceBoilerRecipeBuilder ingredient(ItemLike item) {
        return ingredient(Ingredient.of(item));
    }

    public EssenceBoilerRecipeBuilder ingredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public EssenceBoilerRecipeBuilder inputFluid(Fluid fluid, int amount) {
        inputFluid = Optional.of(new EssenceBoilerFluid(fluid, amount, null));
        return this;
    }

    public EssenceBoilerRecipeBuilder inputPotion(Holder<Potion> potion, int amount) {
        return inputPotion(new PotionContents(potion), amount);
    }

    public EssenceBoilerRecipeBuilder inputPotion(PotionContents potion, int amount) {
        inputFluid = Optional.of(new EssenceBoilerFluid(
                net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.ModFluids.SOURCE_POTION.get(),
                amount,
                potion
        ));
        return this;
    }

    public EssenceBoilerRecipeBuilder result(ItemLike item) {
        return result(new ItemStackTemplate(item.asItem()));
    }

    public EssenceBoilerRecipeBuilder result(ItemLike item, int count) {
        return result(new ItemStackTemplate(item.asItem(), count));
    }

    public EssenceBoilerRecipeBuilder result(ItemStackTemplate result) {
        results.add(result);
        return this;
    }

    public EssenceBoilerRecipeBuilder outputFluid(Fluid fluid, int amount) {
        outputFluid = Optional.of(new EssenceBoilerFluid(fluid, amount, null));
        return this;
    }

    public EssenceBoilerRecipeBuilder preserveFluidAmount() {
        preserveFluidAmount = true;
        return this;
    }

    public EssenceBoilerRecipeBuilder duration(int ticks) {
        recipeTime = ticks;
        return this;
    }

    @Override
    public EssenceBoilerRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public EssenceBoilerRecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        var outputId = !results.isEmpty()
                ? BuiltInRegistries.ITEM.getKey(results.getFirst().item().value())
                : BuiltInRegistries.FLUID.getKey(outputFluid.orElseThrow().fluid());
        return ResourceKey.create(
                Registries.RECIPE,
                outputId.withPath(path -> "essence_boiler/" + path)
        );
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        EssenceBoilerRecipe recipe = new EssenceBoilerRecipe(
                ingredients,
                inputFluid,
                results,
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
