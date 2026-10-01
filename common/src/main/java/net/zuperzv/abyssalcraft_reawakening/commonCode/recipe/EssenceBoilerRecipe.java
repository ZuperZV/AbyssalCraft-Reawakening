package net.zuperzv.abyssalcraft_reawakening.commonCode.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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
import java.util.Optional;

public record EssenceBoilerRecipe(
        List<Ingredient> ingredients,
        Optional<EssenceBoilerFluid> inputFluid,
        List<ItemStackTemplate> results,
        Optional<EssenceBoilerFluid> outputFluid,
        boolean preserveFluidAmount,
        int recipeTime
) implements Recipe<FluidRecipeInput> {

    private static final int MAX_ITEM_INGREDIENTS = 3;
    private static final int MAX_ITEM_RESULTS = 3;

    public static final MapCodec<EssenceBoilerRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.listOf()
                            .optionalFieldOf("ingredients", List.of())
                            .forGetter(EssenceBoilerRecipe::ingredients),
                    EssenceBoilerFluid.CODEC.codec()
                            .optionalFieldOf("input_fluid")
                            .forGetter(EssenceBoilerRecipe::inputFluid),
                    ItemStackTemplate.CODEC.listOf()
                            .optionalFieldOf("results", List.of())
                            .forGetter(EssenceBoilerRecipe::results),
                    EssenceBoilerFluid.CODEC.codec()
                            .optionalFieldOf("output_fluid")
                            .forGetter(EssenceBoilerRecipe::outputFluid),
                    Codec.BOOL.optionalFieldOf("preserve_fluid_amount", false)
                            .forGetter(EssenceBoilerRecipe::preserveFluidAmount),
                    Codec.INT.fieldOf("time")
                            .forGetter(EssenceBoilerRecipe::recipeTime)
            ).apply(instance, EssenceBoilerRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, EssenceBoilerRecipe> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(
                        RegistryFriendlyByteBuf buffer,
                        EssenceBoilerRecipe recipe
                ) {
                    buffer.writeVarInt(recipe.ingredients().size());
                    for (Ingredient ingredient : recipe.ingredients()) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                    }

                    buffer.writeBoolean(recipe.inputFluid().isPresent());
                    recipe.inputFluid().ifPresent(fluid ->
                            EssenceBoilerFluid.STREAM_CODEC.encode(buffer, fluid));

                    buffer.writeVarInt(recipe.results().size());
                    for (ItemStackTemplate result : recipe.results()) {
                        ItemStackTemplate.STREAM_CODEC.encode(buffer, result);
                    }

                    buffer.writeBoolean(recipe.outputFluid().isPresent());
                    recipe.outputFluid().ifPresent(fluid ->
                            EssenceBoilerFluid.STREAM_CODEC.encode(buffer, fluid));

                    buffer.writeBoolean(recipe.preserveFluidAmount());
                    buffer.writeVarInt(recipe.recipeTime());
                }

                @Override
                public EssenceBoilerRecipe decode(RegistryFriendlyByteBuf buffer) {
                    int ingredientCount = buffer.readVarInt();
                    List<Ingredient> ingredients = new ArrayList<>(ingredientCount);
                    for (int i = 0; i < ingredientCount; i++) {
                        ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                    }

                    Optional<EssenceBoilerFluid> inputFluid = buffer.readBoolean()
                            ? Optional.of(EssenceBoilerFluid.STREAM_CODEC.decode(buffer))
                            : Optional.empty();

                    int resultCount = buffer.readVarInt();
                    List<ItemStackTemplate> results = new ArrayList<>(resultCount);
                    for (int i = 0; i < resultCount; i++) {
                        results.add(ItemStackTemplate.STREAM_CODEC.decode(buffer));
                    }

                    Optional<EssenceBoilerFluid> outputFluid = buffer.readBoolean()
                            ? Optional.of(EssenceBoilerFluid.STREAM_CODEC.decode(buffer))
                            : Optional.empty();
                    boolean preserveFluidAmount = buffer.readBoolean();

                    return new EssenceBoilerRecipe(
                            ingredients,
                            inputFluid,
                            results,
                            outputFluid,
                            preserveFluidAmount,
                            buffer.readVarInt()
                    );
                }
            };

    public EssenceBoilerRecipe {
        ingredients = List.copyOf(ingredients);
        inputFluid = inputFluid == null ? Optional.empty() : inputFluid;
        results = List.copyOf(results);
        outputFluid = outputFluid == null ? Optional.empty() : outputFluid;

        if (ingredients.size() > MAX_ITEM_INGREDIENTS
                || results.size() > MAX_ITEM_RESULTS) {
            throw new IllegalArgumentException("Boiler recipes support at most three item inputs and outputs");
        }
        if (ingredients.isEmpty() && inputFluid.isEmpty()) {
            throw new IllegalArgumentException("Boiler recipes require an item or fluid input");
        }
        if (results.isEmpty() && outputFluid.isEmpty()) {
            throw new IllegalArgumentException("Boiler recipes require an item or fluid result");
        }
        if (inputFluid.filter(EssenceBoilerFluid::isEmpty).isPresent()
                || outputFluid.filter(EssenceBoilerFluid::isEmpty).isPresent()) {
            throw new IllegalArgumentException("Boiler recipe fluid amounts must be greater than zero");
        }
        if (preserveFluidAmount && (inputFluid.isEmpty() || outputFluid.isEmpty())) {
            throw new IllegalArgumentException(
                    "Preserving fluid amount requires both input_fluid and output_fluid"
            );
        }
        if (recipeTime <= 0) {
            throw new IllegalArgumentException("Boiler recipe time must be greater than zero");
        }
    }

    @Override
    public boolean matches(FluidRecipeInput input, Level level) {
        if (inputFluid.isPresent()) {
            EssenceBoilerFluid requiredFluid = inputFluid.get();
            EssenceBoilerFluid tankFluid = input.fluid();
            if (tankFluid.isEmpty()
                    || !tankFluid.isSame(requiredFluid)
                    || (!preserveFluidAmount
                    && tankFluid.amount() < requiredFluid.amount())) {
                return false;
            }
        }

        return getMatchedIngredientSlots(input) != null;
    }

    public int[] getMatchedIngredientSlots(FluidRecipeInput input) {
        if (ingredients.isEmpty()) {
            return new int[0];
        }

        boolean[] usedSlots = new boolean[input.size()];
        int[] matchedSlots = new int[ingredients.size()];
        for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ingredientIndex++) {
            Ingredient ingredient = ingredients.get(ingredientIndex);
            int matchedSlot = -1;

            for (int slot = 0; slot < input.size(); slot++) {
                if (!usedSlots[slot] && ingredient.test(input.getItem(slot))) {
                    matchedSlot = slot;
                    break;
                }
            }

            if (matchedSlot < 0) {
                return null;
            }

            usedSlots[matchedSlot] = true;
            matchedSlots[ingredientIndex] = matchedSlot;
        }
        return matchedSlots;
    }

    @Override
    public ItemStack assemble(FluidRecipeInput input) {
        return results.isEmpty() ? ItemStack.EMPTY : results.getFirst().create();
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
        return ingredients.isEmpty()
                ? PlacementInfo.NOT_PLACEABLE
                : PlacementInfo.create(ingredients.getFirst());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
