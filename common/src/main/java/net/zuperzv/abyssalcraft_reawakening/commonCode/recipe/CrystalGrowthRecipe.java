package net.zuperzv.abyssalcraft_reawakening.commonCode.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.helper.FluidRecipeInput;

import java.util.ArrayList;
import java.util.List;

public record CrystalGrowthRecipe(
        List<Ingredient> ingredients,
        EssenceBoilerFluid inputFluid,
        ItemStackTemplate result,
        int time
) implements Recipe<FluidRecipeInput> {
    public static final MapCodec<CrystalGrowthRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.listOf().fieldOf("ingredients")
                            .forGetter(CrystalGrowthRecipe::ingredients),
                    EssenceBoilerFluid.CODEC.codec().fieldOf("input_fluid")
                            .forGetter(CrystalGrowthRecipe::inputFluid),
                    ItemStackTemplate.CODEC.fieldOf("result")
                            .forGetter(CrystalGrowthRecipe::result),
                    Codec.INT.fieldOf("time").forGetter(CrystalGrowthRecipe::time)
            ).apply(instance, CrystalGrowthRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrystalGrowthRecipe> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(RegistryFriendlyByteBuf buffer, CrystalGrowthRecipe recipe) {
                    buffer.writeVarInt(recipe.ingredients.size());
                    for (Ingredient ingredient : recipe.ingredients) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                    }
                    EssenceBoilerFluid.STREAM_CODEC.encode(buffer, recipe.inputFluid);
                    ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
                    buffer.writeVarInt(recipe.time);
                }

                @Override
                public CrystalGrowthRecipe decode(RegistryFriendlyByteBuf buffer) {
                    int count = buffer.readVarInt();
                    List<Ingredient> ingredients = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                    }
                    return new CrystalGrowthRecipe(
                            ingredients,
                            EssenceBoilerFluid.STREAM_CODEC.decode(buffer),
                            ItemStackTemplate.STREAM_CODEC.decode(buffer),
                            buffer.readVarInt()
                    );
                }
            };

    public CrystalGrowthRecipe {
        ingredients = List.copyOf(ingredients);
        if (ingredients.size() != 1) {
            throw new IllegalArgumentException("Crystal growth recipes require exactly one item ingredient");
        }
        if (inputFluid == null || inputFluid.isEmpty()) {
            throw new IllegalArgumentException("Crystal growth recipes require a fluid input");
        }
        if (time <= 0) {
            throw new IllegalArgumentException("Crystal growth time must be greater than zero");
        }
    }

    public int[] getMatchedIngredientSlots(FluidRecipeInput input) {
        return ingredients.getFirst().test(input.getItem(0)) ? new int[]{0} : null;
    }

    @Override
    public boolean matches(FluidRecipeInput input, Level level) {
        return !input.fluid().isEmpty()
                && input.fluid().isSame(inputFluid)
                && input.fluid().amount() >= inputFluid.amount()
                && getMatchedIngredientSlots(input) != null;
    }

    @Override
    public ItemStack assemble(FluidRecipeInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<FluidRecipeInput>> getSerializer() {
        return ModRecipes.CRYSTAL_GROWTH.serializer().get();
    }

    @Override
    public RecipeType<? extends Recipe<FluidRecipeInput>> getType() {
        return ModRecipes.CRYSTAL_GROWTH.type().get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients.getFirst());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
