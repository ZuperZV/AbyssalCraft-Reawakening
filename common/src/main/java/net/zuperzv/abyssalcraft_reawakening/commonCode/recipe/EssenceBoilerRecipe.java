package net.zuperzv.abyssalcraft_reawakening.commonCode.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.EssenceBoilerBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.helper.FluidRecipeInput;

import java.util.ArrayList;
import java.util.List;

public record EssenceBoilerRecipe(
        Ingredient mainIngredient,
        List<Ingredient> additionalIngredients,
        EssenceBoilerFluid inputFluid,
        EssenceBoilerFluid outputFluid,
        int recipeTime
) implements Recipe<FluidRecipeInput> {

    public static final MapCodec<EssenceBoilerRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(

                    Ingredient.CODEC
                            .fieldOf("ingredient")
                            .forGetter(EssenceBoilerRecipe::mainIngredient),

                    Ingredient.CODEC
                            .listOf()
                            .optionalFieldOf("ingredients", List.<Ingredient>of())
                            .forGetter(EssenceBoilerRecipe::additionalIngredients),

                    EssenceBoilerFluid.CODEC
                            .fieldOf("input_fluid")
                            .forGetter(EssenceBoilerRecipe::inputFluid),

                    EssenceBoilerFluid.CODEC
                            .fieldOf("output_fluid")
                            .forGetter(EssenceBoilerRecipe::outputFluid),

                    Codec.INT
                            .fieldOf("time")
                            .forGetter(EssenceBoilerRecipe::recipeTime)

            ).apply(instance, EssenceBoilerRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, EssenceBoilerRecipe> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public void encode(
                        RegistryFriendlyByteBuf buffer,
                        EssenceBoilerRecipe recipe
                ) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(
                            buffer,
                            recipe.mainIngredient()
                    );

                    buffer.writeVarInt(
                            recipe.additionalIngredients().size()
                    );

                    for (Ingredient ingredient : recipe.additionalIngredients()) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(
                                buffer,
                                ingredient
                        );
                    }

                    EssenceBoilerFluid.STREAM_CODEC.encode(
                            buffer,
                            recipe.inputFluid()
                    );

                    EssenceBoilerFluid.STREAM_CODEC.encode(
                            buffer,
                            recipe.outputFluid()
                    );

                    buffer.writeVarInt(recipe.recipeTime());
                }

                @Override
                public EssenceBoilerRecipe decode(
                        RegistryFriendlyByteBuf buffer
                ) {
                    Ingredient main =
                            Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

                    int size = buffer.readVarInt();

                    List<Ingredient> extras =
                            new ArrayList<>(size);

                    for (int i = 0; i < size; i++) {
                        extras.add(
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buffer)
                        );
                    }

                    EssenceBoilerFluid input =
                            EssenceBoilerFluid.STREAM_CODEC.decode(buffer);

                    EssenceBoilerFluid output =
                            EssenceBoilerFluid.STREAM_CODEC.decode(buffer);

                    int time = buffer.readVarInt();

                    return new EssenceBoilerRecipe(
                            main,
                            extras,
                            input,
                            output,
                            time
                    );
                }
            };

    @Override
    public boolean matches(
            FluidRecipeInput input,
            Level level
    ) {
        EssenceBoilerFluid tankFluid =
                input.fluid();

        if (!tankFluid.isSame(inputFluid.fluid())) {
            return false;
        }

        if (tankFluid.amount() < inputFluid.amount()) {
            return false;
        }

        boolean mainMatched = false;

        boolean[] matchedExtras =
                new boolean[additionalIngredients.size()];

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.isEmpty()) {
                continue;
            }

            if (!mainMatched && mainIngredient.test(stack)) {
                mainMatched = true;
                continue;
            }

            for (int j = 0; j < additionalIngredients.size(); j++) {
                if (matchedExtras[j]) {
                    continue;
                }

                Ingredient ingredient =
                        additionalIngredients.get(j);

                if (ingredient.test(stack)) {
                    matchedExtras[j] = true;
                    break;
                }
            }
        }

        if (!mainMatched) {
            return false;
        }

        for (boolean matched : matchedExtras) {
            if (!matched) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(FluidRecipeInput input) {
        return ItemStack.EMPTY;
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
        return ModRecipes.ESSENCE_BOILER.serializer().get();
    }

    @Override
    public RecipeType<? extends Recipe<FluidRecipeInput>> getType() {
        return ModRecipes.ESSENCE_BOILER.type().get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(mainIngredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}