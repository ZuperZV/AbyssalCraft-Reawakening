package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.datagen.custom.StoneRitualAltarRecipeBuilder;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.helper.TimeOfDay;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        //Necronomicon
        dyedItem(ModItems.NECRONOMICON.get(), "dyed_item");


        //Skin Of
        shaped(RecipeCategory.MISC, ModItems.SKIN_OF_THE_ABYSSAL_WASTELAND.get(),
                new String[]{
                        "BBB",
                        "BAB",
                        "BBB"
                },
                new Key('A', ModItems.ABYSSAL_WASTELAND_ESSENCE.get()),
                new Key('B', ModItems.CORALIUM_PLAGUED_FLESH.get()));

        shaped(RecipeCategory.MISC, ModItems.SKIN_OF_THE_DREADLANDS.get(),
                new String[]{
                        "BBB",
                        "BAB",
                        "BBB"
                },
                new Key('A', ModItems.DREADLANDS_ESSENCE.get()),
                new Key('B', ModItems.CORALIUM_PLAGUED_FLESH.get()));

        shaped(RecipeCategory.MISC, ModItems.SKIN_OF_THE_OMOTHOL.get(),
                new String[]{
                        "BBB",
                        "BAB",
                        "BBB"
                },
                new Key('A', ModItems.OMOTHOL_ESSENCE.get()),
                new Key('B', ModItems.OMOTHOL_GHOUL_FLESH.get()));

        //Shadow Items
        fourBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.SHADOW_FRAGMENT.get(), RecipeCategory.MISC,
                ModItems.SHADOW_SHARD.get());

        fourBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.SHADOW_SHARD.get(), RecipeCategory.MISC,
                ModItems.SHADOW_GEM.get());

        shaped(RecipeCategory.MISC, ModItems.OBLIVION_SHARD.get(),
                new String[]{
                        " A ",
                        "ABA",
                        " A "
                },
                new Key('A', ModItems.SHADOW_GEM.get()),
                new Key('B', ModItems.TRANSMUTATION_GEM.get()));

        //Coralium
        StoneRitualAltarRecipeBuilder.altar(
                RecipeCategory.MISC, ModItems.TRANSMUTATION_GEM.get(),

                        Ingredient.of(ModItems.CORALIUM_PEARL.get())
                )
                .addIngredient(Ingredient.of(Items.ENDER_PEARL))
                .addIngredient(Ingredient.of(Items.DIAMOND))
                .addIngredient(Ingredient.of(Items.ENDER_PEARL))
                .addIngredient(Ingredient.of(Items.DIAMOND))
                .addIngredient(Ingredient.of(Items.BLAZE_POWDER))
                .addIngredient(Ingredient.of(Items.BLAZE_POWDER))
                .addIngredient(Ingredient.of(Items.BLAZE_POWDER))
                .addIngredient(Ingredient.of(Items.BLAZE_POWDER))
                .time(TimeOfDay.BOTH)
                .duration(400)
                .unlockedBy(
                        "has_coralium_pearl",
                        has(ModItems.CORALIUM_PEARL.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("transmutation_gem")));


        //Abyssalnite
        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.ABYSSALNITE_NUGGET.get(), RecipeCategory.MISC,
                ModItems.ABYSSALNITE_INGOT.get());

        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.ABYSSALNITE_INGOT.get(), RecipeCategory.MISC,
                ModBlocks.ABYSSALNITE_BLOCK.item().get());

        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.RAW_ABYSSALNITE.get(), RecipeCategory.MISC,
                ModBlocks.RAW_ABYSSALNITE_BLOCK.item().get());

        rawToIngot(ModItems.RAW_ABYSSALNITE.get(), RecipeCategory.MISC, ModItems.ABYSSALNITE_INGOT.get(), 0.7f, 200, output);
        rawToIngot(ModBlocks.ABYSSALNITE_OVERWORLD_ORE.item().get(), RecipeCategory.MISC, ModItems.ABYSSALNITE_INGOT.get(), 0.9f, 200, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NonNull String getName() {
            return Constants.MOD_NAME + " Recipes";
        }
    }

    protected record Key(char key, ItemLike item) {}

    protected void shaped(RecipeCategory category, ItemLike result, String[] pattern, Key... keys) {
        ShapedRecipeBuilder builder = shaped(category, result);

        for (String line : pattern) {
            builder.pattern(line);
        }

        ItemLike unlockItem = null;

        for (Key key : keys) {
            builder.define(key.key(), key.item());

            if (unlockItem == null) {
                unlockItem = key.item();
            }
        }

        if (unlockItem != null) {
            builder.unlockedBy(getHasName(unlockItem), has(unlockItem));
        }

        builder.save(output);
    }

    protected void nineBlockStorageRecipes(
            RecipeOutput pWriter,
            RecipeCategory smallCat, ItemLike small,
            RecipeCategory largeCat, ItemLike large
    ) {
        // 9 nuggets -> 1 ingot or block
        shapeless(largeCat, large)
                .requires(small, 9)
                .unlockedBy(getHasName(small), has(small))
                .save(pWriter, recipeKey(getSimpleRecipeName(large) + "_from_" + getSimpleRecipeName(small)));

        // 1 ingot -> 9 nuggets or block
        shapeless(smallCat, small, 9)
                .requires(large)
                .unlockedBy(getHasName(large), has(large))
                .save(pWriter, recipeKey(getSimpleRecipeName(small) + "_from_" + getSimpleRecipeName(large)));
    }

    protected void fourBlockStorageRecipes(
            RecipeOutput p_301057_, RecipeCategory p_251203_, ItemLike p_251689_, RecipeCategory p_251376_, ItemLike p_248771_
    ) {
        fourBlockStorageRecipes(
                p_301057_, p_251203_, p_251689_, p_251376_, p_248771_, getSimpleRecipeName(p_248771_), null, getSimpleRecipeName(p_251689_), null
        );
    }

    protected void fourBlockStorageRecipes(
            RecipeOutput p_301222_,
            RecipeCategory p_250083_,
            ItemLike p_250042_,
            RecipeCategory p_248977_,
            ItemLike p_251911_,
            String p_250475_,
            @Nullable String p_248641_,
            String p_252237_,
            @Nullable String p_250414_
    ) {
        shapeless(p_250083_, p_250042_, 4)
                .requires(p_251911_)
                .group(p_250414_)
                .unlockedBy(getHasName(p_251911_), has(p_251911_))
                .save(p_301222_, recipeKey(p_252237_ + "_from_" + p_250475_));
        shaped(p_248977_, p_251911_)
                .define('#', p_250042_)
                .pattern("##")
                .pattern("##")
                .group(p_248641_)
                .unlockedBy(getHasName(p_250042_), has(p_250042_))
                .save(p_301222_, recipeKey(p_250475_ + "_from_" + p_252237_));
    }

    public void rawToIngot(ItemLike rawItem, RecipeCategory category, ItemLike ingotLike, float experience, int cookingTime, RecipeOutput pWriter) {
        String ingot = getItemName(ingotLike);
        ItemLike ingotItem = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, ingot));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(rawItem), category, CookingBookCategory.MISC, ingotItem, experience, cookingTime)
                .unlockedBy("has_" + getItemName(rawItem), has(rawItem))
                .save(pWriter, recipeKey(getItemName(ingotItem) + "_from_" + getItemName(rawItem) + "_with_smelting"));

        float blastingExperience = experience - 1.00f;
        int blastingTime = cookingTime - 100 >= 0 ? cookingTime - 100 : cookingTime;

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(rawItem), category, CookingBookCategory.MISC, ingotItem, blastingExperience, blastingTime)
                .unlockedBy("has_" + getItemName(rawItem), has(rawItem))
                .save(pWriter, recipeKey(getItemName(ingotItem)  + "_from_" + getItemName(rawItem) + "_with_blasting"));
    }

    private static ResourceKey<Recipe<?>> recipeKey(String name) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
