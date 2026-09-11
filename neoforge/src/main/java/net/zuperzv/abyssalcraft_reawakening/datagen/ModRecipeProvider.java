package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.StoneRitualAltarRecipe;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.customCraftingTable.CoraliumGemRecipe;
import net.zuperzv.abyssalcraft_reawakening.commonCode.worldgen.dimension.ModDimensions;
import net.zuperzv.abyssalcraft_reawakening.datagen.custom.StoneRitualAltarRecipeBuilder;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItemTags;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.helper.TimeOfDay;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        //EarlyGame
            //Necronomicon
        shaped(RecipeCategory.MISC, ModItems.NECRONOMICON.get(),
                new String[]{
                        "AAC",
                        "ABA",
                        "AAC"
                },
                new Key('A', Items.ROTTEN_FLESH),
                new Key('B', Items.BOOK),
                new Key('C', Items.IRON_INGOT));

        dyedItem(ModItems.NECRONOMICON.get(), "dyed_item");

        shaped(RecipeCategory.MISC, ModItems.GATEWAY_KEY.get(),
                new String[]{
                        " BA",
                        " CB",
                        "C  "
                },
                new Key('A', ModItems.OBLIVION_CATALYST.get()),
                new Key('B', ModItems.CORALIUM_PEARL.get()),
                new Key('C', Items.BLAZE_ROD));


        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC, ModBlocks.ABYSSAL_STONE.block().get().asItem(),
                        Ingredient.of(Items.STONE)
                )
                .addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)) //Venster
                .addIngredient(Ingredient.of(Items.GUNPOWDER)) //Up
                .addIngredient(Ingredient.of(Items.SLIME_BALL)) //Højere
                .addIngredient(Ingredient.of(Items.COPPER_NUGGET)) //Ned
                .time(TimeOfDay.BOTH)
                .duration(100)
                .potentialEnergy(50)
                .unlockedBy(
                        "has_stone",
                        has(Items.STONE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssal_stone")));


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
                new Key('B', ModItems.DREAD_FRAGMENT.get()));

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

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC, ModItems.OBLIVION_CATALYST.get(),
                        Ingredient.of(Items.ENDER_EYE)
                )
                .addIngredient(Ingredient.of(Items.REDSTONE)) //Venster
                .addIngredient(Ingredient.of(Items.REDSTONE)) //Up
                .addIngredient(Ingredient.of(Items.REDSTONE)) //Højere
                .addIngredient(Ingredient.of(Items.REDSTONE)) //Ned
                .addIngredient(Ingredient.of(ModItems.OBLIVION_SHARD.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.OBLIVION_SHARD.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.OBLIVION_SHARD.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.OBLIVION_SHARD.get())) //Venster Ned
                .time(TimeOfDay.BOTH)
                .duration(1000)
                .potentialEnergy(5000)
                .unlockedBy(
                        "has_oblivion_shard",
                        has(ModItems.OBLIVION_SHARD.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("oblivion_catalyst")));

        //Staf of rendering
        shaped(RecipeCategory.MISC, ModItems.STAFF_OF_RENDING.get(),
                new String[]{
                        " BA",
                        " BB",
                        "B  "
                },
                new Key('A', ModItems.OBLIVION_SHARD.get()),
                new Key('B', ModItems.SHADOW_SHARD.get()));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC, ModItems.ABYSSAL_WASTELAND_STAFF_OF_RENDING.get(),
                        Ingredient.of(ModItems.STAFF_OF_RENDING.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLAGUED_FLESH.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.SHADOW_GEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLAGUED_FLESH.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLAGUED_FLESH.get())) //Ned
                .addIngredient(Ingredient.of(ModBlocks.ABYSSAL_STONE.item().get())) //Venster Up
                .addIngredient(Ingredient.of(ModBlocks.ABYSSAL_STONE.item().get())) //Højere Up
                .addIngredient(Ingredient.of(ModBlocks.ABYSSAL_STONE.item().get())) //Højere Ned
                .addIngredient(Ingredient.of(ModBlocks.ABYSSAL_STONE.item().get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .unlockedBy(
                        "has_staff_of_rending",
                        has(ModItems.STAFF_OF_RENDING.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssal_wasteland_staff_of_rending")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC, ModItems.DREADLANDS_STAFF_OF_RENDING.get(),
                        Ingredient.of(ModItems.ABYSSAL_WASTELAND_STAFF_OF_RENDING.get())
                )
                .addIngredient(Ingredient.of(ModItems.DREAD_FRAGMENT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.SHADOW_GEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.DREAD_FRAGMENT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.DREAD_FRAGMENT.get())) //Ned
                .addIngredient(Ingredient.of(ModBlocks.DREADIUM_STONE.item().get())) //Venster Up
                .addIngredient(Ingredient.of(ModBlocks.DREADIUM_STONE.item().get())) //Højere Up
                .addIngredient(Ingredient.of(ModBlocks.DREADIUM_STONE.item().get())) //Højere Ned
                .addIngredient(Ingredient.of(ModBlocks.DREADIUM_STONE.item().get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(900)
                .potentialEnergy(2000)
                .dimension(Level.NETHER)//Dreadlands
                .unlockedBy(
                        "has_abyssal_wasteland_staff_of_rending",
                        has(ModItems.ABYSSAL_WASTELAND_STAFF_OF_RENDING.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("dreadlands_staff_of_rending")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC, ModItems.OMOTHOL_STAFF_OF_RENDING.get(),
                        Ingredient.of(ModItems.DREADLANDS_STAFF_OF_RENDING.get())
                )
                .addIngredient(Ingredient.of(ModItems.OMOTHOL_GHOUL_FLESH.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.SHADOW_GEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.OMOTHOL_GHOUL_FLESH.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.OMOTHOL_GHOUL_FLESH.get())) //Ned
                .addIngredient(Ingredient.of(ModBlocks.OMOTHOL_STONE.item().get())) //Venster Up
                .addIngredient(Ingredient.of(ModBlocks.OMOTHOL_STONE.item().get())) //Højere Up
                .addIngredient(Ingredient.of(ModBlocks.OMOTHOL_STONE.item().get())) //Højere Ned
                .addIngredient(Ingredient.of(ModBlocks.OMOTHOL_STONE.item().get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(1300)
                .potentialEnergy(3000)
                .dimension(Level.END)//Omothol
                .unlockedBy(
                        "has_dreadlands_staff_of_rending",
                        has(ModItems.DREADLANDS_STAFF_OF_RENDING.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("omothol_staff_of_rending")));

        //Abyssalnite
            //Tools
                //Sword
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_SWORD.get(),
                        Ingredient.of(Items.NETHERITE_SWORD)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_SWORD))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_sword_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        new ItemStackTemplate(
                                ModItems.ABYSSALNITE_SWORD.get(),
                                DataComponentPatch.builder()
                                        .set(ModDataComponentTypes.WOOD.get(), true)
                                        .build()
                        ),
                        Ingredient.of(Items.DIAMOND_SWORD)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_SWORD))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_sword_diamond")));

                //Pickaxe
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_PICKAXE.get(),
                        Ingredient.of(Items.NETHERITE_PICKAXE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_PICKAXE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_pickaxe_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        new ItemStackTemplate(
                                ModItems.ABYSSALNITE_PICKAXE.get(),
                                DataComponentPatch.builder()
                                        .set(ModDataComponentTypes.WOOD.get(), true)
                                        .build()
                        ),
                        Ingredient.of(Items.DIAMOND_PICKAXE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_PICKAXE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_pickaxe_diamond")));

                //Axe
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_AXE.get(),
                        Ingredient.of(Items.NETHERITE_AXE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_AXE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_axe_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        new ItemStackTemplate(
                                ModItems.ABYSSALNITE_AXE.get(),
                                DataComponentPatch.builder()
                                        .set(ModDataComponentTypes.WOOD.get(), true)
                                        .build()
                        ),
                        Ingredient.of(Items.DIAMOND_AXE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_AXE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_axe_diamond")));


                //Shovel
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_SHOVEL.get(),
                        Ingredient.of(Items.NETHERITE_SHOVEL)
                )
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_SHOVEL))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_axe_shovel")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        new ItemStackTemplate(
                                ModItems.ABYSSALNITE_SHOVEL.get(),
                                DataComponentPatch.builder()
                                        .set(ModDataComponentTypes.WOOD.get(), true)
                                        .build()
                        ),
                        Ingredient.of(Items.DIAMOND_SHOVEL)
                )
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_SHOVEL))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_shovel_diamond")));

                //Hoe
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_HOE.get(),
                        Ingredient.of(Items.NETHERITE_HOE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_HOE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_hoe_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        new ItemStackTemplate(
                                ModItems.ABYSSALNITE_HOE.get(),
                                DataComponentPatch.builder()
                                        .set(ModDataComponentTypes.WOOD.get(), true)
                                        .build()
                        ),
                        Ingredient.of(Items.DIAMOND_HOE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_HOE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_hoe_diamond")));

                //Spear
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_SPEAR.get(),
                        Ingredient.of(Items.NETHERITE_SPEAR)
                )
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_SPEAR))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_spear_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        new ItemStackTemplate(
                                ModItems.ABYSSALNITE_SPEAR.get(),
                                DataComponentPatch.builder()
                                        .set(ModDataComponentTypes.WOOD.get(), true)
                                        .build()
                        ),
                        Ingredient.of(Items.DIAMOND_SPEAR)
                )
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_SPEAR))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_spear_diamond")));

            //Armor
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_HELMET.get(),
                        Ingredient.of(Items.NETHERITE_HELMET)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_HELMET))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_helmet_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_HELMET.get(),
                        Ingredient.of(Items.DIAMOND_HELMET)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_HELMET))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_helmet_diamond")));


        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_CHESTPLATE.get(),
                        Ingredient.of(Items.NETHERITE_CHESTPLATE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_CHESTPLATE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_chestplate_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_CHESTPLATE.get(),
                        Ingredient.of(Items.DIAMOND_CHESTPLATE)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_CHESTPLATE))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_chestplate_diamond")));


        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_LEGGINGS.get(),
                        Ingredient.of(Items.NETHERITE_LEGGINGS)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_LEGGINGS))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_leggings_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_LEGGINGS.get(),
                        Ingredient.of(Items.DIAMOND_LEGGINGS)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_LEGGINGS))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_leggings_diamond")));


        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_BOOTS.get(),
                        Ingredient.of(Items.NETHERITE_BOOTS)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_netherite",
                        has(Items.NETHERITE_BOOTS))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_boots_netherite")));

        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.ABYSSALNITE_BOOTS.get(),
                        Ingredient.of(Items.DIAMOND_BOOTS)
                )
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Ned
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Venster Up
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) //Højere Up
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Højere Ned
                .addIngredient(Ingredient.of(ModItems.ABYSSALNITE_INGOT.get())) //Venster Ned
                .time(TimeOfDay.NIGHT)
                .duration(800)
                .potentialEnergy(1600)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_diamond",
                        has(Items.DIAMOND_BOOTS))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("abyssalnite_boots_diamond")));

        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.ABYSSALNITE_NUGGET.get(), RecipeCategory.MISC,
                ModItems.ABYSSALNITE_INGOT.get());

        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.ABYSSALNITE_INGOT.get(), RecipeCategory.MISC,
                ModBlocks.ABYSSALNITE_BLOCK.item().get());

        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModItems.RAW_ABYSSALNITE.get(), RecipeCategory.MISC,
                ModBlocks.RAW_ABYSSALNITE_BLOCK.item().get());

        rawToIngot(ModItems.RAW_ABYSSALNITE.get(), RecipeCategory.MISC, ModItems.ABYSSALNITE_INGOT.get(), 0.7f, 200, output);
        rawToIngot(ModBlocks.ABYSSALNITE_ORE.item().get(), RecipeCategory.MISC, ModItems.ABYSSALNITE_INGOT.get(), 0.9f, 200, output);

        // Coralium
            // Tools
            // Sword
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_SWORD.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_SWORD.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get())) // Venstre
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) // Up
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get())) // Højre
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get())) // Ned
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_SWORD.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_sword_abyssalnite")));

            // Pickaxe
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_PICKAXE.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_PICKAXE.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_PICKAXE.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_pickaxe_abyssalnite")));

            // Axe
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_AXE.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_AXE.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_AXE.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_axe_abyssalnite")));


            // Shovel
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_SHOVEL.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_SHOVEL.get())
                )
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_SHOVEL.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_shovel_abyssalnite")));

            // Hoe
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_HOE.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_HOE.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_HOE.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_hoe_abyssalnite")));

            // Spear
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_SPEAR.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_SPEAR.get())
                )
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(1000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_SPEAR.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_spear_abyssalnite")));
        // Armor
            // Helmet
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_HELMET.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_HELMET.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PEARL.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PEARL.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(2000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_HELMET.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_helmet_abyssalnite")));


// Chestplate
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_CHESTPLATE.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_CHESTPLATE.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(2000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_CHESTPLATE.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_chestplate_abyssalnite")));


// Leggings
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_LEGGINGS.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_LEGGINGS.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(2000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_LEGGINGS.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_leggings_abyssalnite")));


// Boots
        StoneRitualAltarRecipeBuilder.altar(
                        RecipeCategory.MISC,
                        ModItems.CORALIUM_BOOTS.get(),
                        Ingredient.of(ModItems.ABYSSALNITE_BOOTS.get())
                )
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_INGOT.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.RECIPE_ITEM.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .addIngredient(Ingredient.of(ModItems.CORALIUM_PLATE.get()))
                .time(TimeOfDay.NIGHT)
                .duration(600)
                .potentialEnergy(2000)
                .dimension(ModDimensions.THE_ABYSSAL_WASTELAND_LEVEL_KEY)
                .copyComponents(
                        StoneRitualAltarRecipe.ComponentSource.MOLD,
                        Identifier.parse("minecraft:damage"),
                        Identifier.parse("minecraft:enchantments")
                )
                .unlockedBy(
                        "has_abyssalnite",
                        has(ModItems.ABYSSALNITE_BOOTS.get()))
                .save(output, ResourceKey.create(Registries.RECIPE,
                        Constants.id("coralium_boots_abyssalnite")));


// Storage
        nineBlockStorageRecipes(
                output,
                RecipeCategory.MISC,
                ModItems.CORALIUM_NUGGET.get(),
                RecipeCategory.MISC,
                ModItems.CORALIUM_INGOT.get()
        );

        nineBlockStorageRecipes(
                output,
                RecipeCategory.MISC,
                ModItems.CORALIUM_INGOT.get(),
                RecipeCategory.MISC,
                ModBlocks.CORALIUM_BLOCK.item().get()
        );

        nineBlockStorageRecipes(
                output,
                RecipeCategory.MISC,
                ModItems.RAW_CORALIUM.get(),
                RecipeCategory.MISC,
                ModBlocks.RAW_CORALIUM_BLOCK.item().get()
        );

        // CORALIUM Items
        shaped(RecipeCategory.MISC, ModItems.CHUNK_OF_CORALIUM.get(), 4,
                new String[]{
                        " A ",
                        "ABA",
                        " A "
                },
                new Key('A', DataComponentIngredient.of(
                        true,
                        ModDataComponentTypes.CORALIUM_GEMS.get(),
                        new CoraliumGemsData(9),
                        ModItems.CORALIUM_GEM.get()
                ), ModItems.CORALIUM_GEM.get()),
                new Key('B', ModBlocks.ABYSSAL_STONE.block().get()));

        shaped(RecipeCategory.MISC, ModItems.CORALIUM_PLATE.get(),
                new String[]{
                        "ABA",
                        "ABA",
                        "ABA"
                },
                new Key('A', ModItems.CORALIUM_INGOT.get()),
                new Key('B', ModItems.CORALIUM_PEARL.get()));

        //Furnace
        rawToIngot(ModItems.CHUNK_OF_CORALIUM.get(), RecipeCategory.MISC, ModItems.CORALIUM_INGOT.get(), 0.6f, 250, output);

        //WITHERWOOD
        this.planksFromLogs(ModBlocks.WITHERWOOD_PLANKS.block().get(), ModItemTags.WITHERWOOD_LOGS, 4);
        this.woodFromLogs(ModBlocks.WITHERWOOD_WOOD.block().get(), ModBlocks.WITHERWOOD_LOG.block().get());
        this.woodFromLogs(ModBlocks.STRIPPED_WITHERWOOD_WOOD.block().get(), ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get());
        this.shelf(ModBlocks.WITHERWOOD_SHELF.block().get(), ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get());
        this.woodenBoat(ModItems.WITHERWOOD_BOAT.get(), ModBlocks.WITHERWOOD_PLANKS.block().get());
        this.chestBoat(ModItems.WITHERWOOD_CHEST_BOAT.get(), ModItems.WITHERWOOD_BOAT.get());
        this.hangingSign(ModItems.WITHERWOOD_HANGING_SIGN.get(), ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get());

        this.stairBuilder(ModBlocks.WITHERWOOD_STAIRS.block().get(), Ingredient.of(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .unlockedBy(getHasName(ModBlocks.WITHERWOOD_PLANKS.block().get()), this.has(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .save(this.output);

        this.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WITHERWOOD_TRAPDOOR.block().get())
                .define('#', ModBlocks.WITHERWOOD_PLANKS.block().get())
                .pattern("###")
                .unlockedBy(getHasName(ModBlocks.WITHERWOOD_PLANKS.block().get()), this.has(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .save(this.output);

        this.doorBuilder(ModBlocks.WITHERWOOD_DOOR.block().get(), Ingredient.of(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .unlockedBy(getHasName(ModBlocks.WITHERWOOD_PLANKS.block().get()), this.has(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .save(this.output);

        this.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WITHERWOOD_SIGN.get(), 3)
                .define('#', ModBlocks.WITHERWOOD_PLANKS.block().get())
                .define('A', Items.STICK)
                .pattern("###")
                .pattern("###")
                .pattern(" A ")
                .unlockedBy(getHasName(ModBlocks.WITHERWOOD_PLANKS.block().get()), this.has(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .save(this.output);

        this.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WITHERWOOD_BUTTON.block().get())
                .requires(ModBlocks.WITHERWOOD_PLANKS.block().get())
                .unlockedBy(getHasName(ModBlocks.WITHERWOOD_PLANKS.block().get()), this.has(ModBlocks.WITHERWOOD_PLANKS.block().get()))
                .save(this.output);

        this.pressurePlate(ModBlocks.WITHERWOOD_PRESSURE_PLATE.block().get(), ModBlocks.WITHERWOOD_PLANKS.block().get());

        this.fenceBuilder(ModBlocks.WITHERWOOD_FENCE.block().get(), Ingredient.of(ModBlocks.WITHERWOOD_PLANKS.block().get()));
        this.fenceGateBuilder(ModBlocks.WITHERWOOD_FENCE.block().get(), Ingredient.of(ModBlocks.WITHERWOOD_PLANKS.block().get()));

        //Abyssal Stone
        fourBlockStorageRecipes(output, RecipeCategory.MISC, ModBlocks.ABYSSAL_STONE_BRICKS.block().get(), RecipeCategory.MISC,
                ModBlocks.ABYSSAL_STONE.block().get());

        this.fenceBuilder(ModBlocks.ABYSSAL_STONE_BRICKS_FENCE.block().get(), Ingredient.of(ModBlocks.ABYSSAL_STONE.block().get()));

        this.stairBuilder(ModBlocks.ABYSSAL_STONE_BRICKS_STAIRS.block().get(), Ingredient.of(ModBlocks.ABYSSAL_STONE_BRICKS.block().get()))
                .unlockedBy(getHasName(ModBlocks.ABYSSAL_STONE_BRICKS.block().get()), this.has(ModBlocks.ABYSSAL_STONE_BRICKS.block().get()))
                .save(this.output);
        this.slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ABYSSAL_STONE_BRICKS_SLAB.block().get(), Ingredient.of(ModBlocks.ABYSSAL_STONE_BRICKS.block().get()))
                .unlockedBy(getHasName(ModBlocks.ABYSSAL_STONE_BRICKS.block().get()), this.has(ModBlocks.ABYSSAL_STONE_BRICKS.block().get()))
                .save(this.output);
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

    protected record Key(char key, Ingredient ingredient, ItemLike unlockItem) {
        public Key(char key, ItemLike item) {
            this(key, Ingredient.of(item), item);
        }

        public Key(char key, Ingredient ingredient) {
            this(key, ingredient, null);
        }

        public Key(char key, Ingredient ingredient, ItemLike unlockItem) {
            this.key = key;
            this.ingredient = ingredient;
            this.unlockItem = unlockItem;
        }
    }

    protected void shaped(RecipeCategory category, ItemLike result, String[] pattern, Key... keys) {
        shaped(category, result, 1, pattern, keys);
    }

    protected void shaped(RecipeCategory category, ItemLike result, int count, String[] pattern, Key... keys) {
        ShapedRecipeBuilder builder = shaped(category, result, count);

        for (String line : pattern) {
            builder.pattern(line);
        }

        ItemLike unlockItem = null;

        for (Key key : keys) {
            builder.define(key.key(), key.ingredient());

            if (unlockItem == null && key.unlockItem() != null) {
                unlockItem = key.unlockItem();
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

        float blastingExperience = experience + 1.00f;
        int blastingTime = cookingTime - 100 >= 0 ? cookingTime - 100 : cookingTime;

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(rawItem), category, CookingBookCategory.MISC, ingotItem, blastingExperience, blastingTime)
                .unlockedBy("has_" + getItemName(rawItem), has(rawItem))
                .save(pWriter, recipeKey(getItemName(ingotItem)  + "_from_" + getItemName(rawItem) + "_with_blasting"));
    }

    private static ResourceKey<Recipe<?>> recipeKey(String name) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
