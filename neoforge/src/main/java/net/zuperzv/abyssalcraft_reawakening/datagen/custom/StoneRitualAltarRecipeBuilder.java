package net.zuperzv.abyssalcraft_reawakening.datagen.custom;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.StoneRitualAltarRecipe;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.helper.TimeOfDay;
import org.jspecify.annotations.Nullable;

import java.util.*;


public class StoneRitualAltarRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;

    private final ItemStackTemplate output;
    private final Ingredient moldIngredient;

    private final List<Ingredient> additionalIngredients = new ArrayList<>();

    private Optional<String> requiredEssenceType = Optional.empty();
    private Optional<String> entityType = Optional.empty();

    private Optional<Block> additionalBlock = Optional.empty();
    private Optional<Map<String,String>> blockState = Optional.empty();

    private Optional<Boolean> needsBlock = Optional.empty();
    private Optional<Block> blockOutput = Optional.empty();

    private Optional<TimeOfDay> timeOfDay = Optional.empty();
    private Optional<TimeOfDay> fakeTimeOfDay = Optional.empty();

    private int recipeTime = 200;
    private int potentialEnergy = 200;


    private final RecipeUnlockAdvancementBuilder advancementBuilder =
            new RecipeUnlockAdvancementBuilder();

    private @Nullable String group;


    private StoneRitualAltarRecipeBuilder(
            RecipeCategory category,
            ItemStackTemplate output,
            Ingredient moldIngredient
    ) {
        this.category = category;
        this.output = output;
        this.moldIngredient = moldIngredient;
    }

    public static StoneRitualAltarRecipeBuilder altar(
            RecipeCategory category,
            ItemStackTemplate output,
            Ingredient mold
    ) {
        return new StoneRitualAltarRecipeBuilder(
                category,
                output,
                mold
        );
    }

    public static StoneRitualAltarRecipeBuilder altar(
            RecipeCategory category,
            Item output,
            Ingredient mold
    ) {
        return new StoneRitualAltarRecipeBuilder(
                category,
                new ItemStackTemplate(output),
                mold
        );
    }

    public StoneRitualAltarRecipeBuilder addIngredient(Ingredient ingredient) {
        this.additionalIngredients.add(ingredient);
        return this;
    }

    public StoneRitualAltarRecipeBuilder essence(String essence) {
        this.requiredEssenceType = Optional.of(essence);
        return this;
    }

    public StoneRitualAltarRecipeBuilder needsBlock(Block block) {
        this.additionalBlock = Optional.of(block);
        this.needsBlock = Optional.of(true);
        return this;
    }

    public StoneRitualAltarRecipeBuilder blockState(
            String key,
            String value
    ) {
        Map<String,String> states =
                this.blockState.orElseGet(HashMap::new);

        states.put(key,value);

        this.blockState = Optional.of(states);

        return this;
    }

    public StoneRitualAltarRecipeBuilder time(TimeOfDay time) {
        this.timeOfDay = Optional.of(time);
        return this;
    }

    public StoneRitualAltarRecipeBuilder fakeTime(TimeOfDay time) {
        this.fakeTimeOfDay = Optional.of(time);
        return this;
    }

    public StoneRitualAltarRecipeBuilder duration(int ticks) {
        this.recipeTime = ticks;
        return this;
    }

    public StoneRitualAltarRecipeBuilder potentialEnergy(int energy) {
        this.potentialEnergy = energy;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(
            String name,
            Criterion<?> criterion
    ) {
        advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(output);
    }

    @Override
    public void save(
            RecipeOutput output,
            ResourceKey<Recipe<?>> id
    ) {

        StoneRitualAltarRecipe recipe =
                new StoneRitualAltarRecipe(
                        this.output,
                        this.moldIngredient,
                        this.additionalIngredients,
                        Optional.empty(), // entityType
                        this.requiredEssenceType,
                        this.additionalBlock,
                        this.blockState,
                        this.needsBlock,
                        this.blockOutput,
                        this.timeOfDay,
                        this.fakeTimeOfDay,
                        this.recipeTime,
                        this.potentialEnergy
                );


        output.accept(
                id,
                recipe,
                advancementBuilder.build(
                        output,
                        id,
                        category
                )
        );
    }
}