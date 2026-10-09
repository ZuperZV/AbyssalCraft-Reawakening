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
import java.util.Optional;

public record CrystalGrowthRecipe(
        List<Ingredient> ingredients,
        Optional<EssenceBoilerFluid> inputFluid,
        Optional<ItemStackTemplate> result,
        Optional<EssenceBoilerFluid> outputFluid,
        boolean preserveFluidAmount,
        int time,
        int minimumInputFluid
) implements Recipe<FluidRecipeInput> {

    private static final int MAX_ITEM_INGREDIENTS = 1;

    public static final MapCodec<CrystalGrowthRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.listOf()
                            .optionalFieldOf("ingredients", List.of())
                            .forGetter(CrystalGrowthRecipe::ingredients),
                    EssenceBoilerFluid.CODEC.codec()
                            .optionalFieldOf("input_fluid")
                            .forGetter(CrystalGrowthRecipe::inputFluid),
                    ItemStackTemplate.CODEC
                            .optionalFieldOf("result")
                            .forGetter(CrystalGrowthRecipe::result),
                    EssenceBoilerFluid.CODEC.codec()
                            .optionalFieldOf("output_fluid")
                            .forGetter(CrystalGrowthRecipe::outputFluid),
                    Codec.BOOL.optionalFieldOf("preserve_fluid_amount", false)
                            .forGetter(CrystalGrowthRecipe::preserveFluidAmount),
                    Codec.INT.fieldOf("time")
                            .forGetter(CrystalGrowthRecipe::time),
                    Codec.INT.optionalFieldOf("minimum_input_fluid", 0)
                            .forGetter(CrystalGrowthRecipe::minimumInputFluid)
            ).apply(instance, CrystalGrowthRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrystalGrowthRecipe> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(
                        RegistryFriendlyByteBuf buffer,
                        CrystalGrowthRecipe recipe
                ) {
                    buffer.writeVarInt(recipe.ingredients().size());
                    for (Ingredient ingredient : recipe.ingredients()) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                    }

                    buffer.writeBoolean(recipe.inputFluid().isPresent());
                    recipe.inputFluid().ifPresent(fluid ->
                            EssenceBoilerFluid.STREAM_CODEC.encode(buffer, fluid));

                    buffer.writeBoolean(recipe.result().isPresent());
                    recipe.result().ifPresent(result ->
                            ItemStackTemplate.STREAM_CODEC.encode(buffer, result));

                    buffer.writeBoolean(recipe.outputFluid().isPresent());
                    recipe.outputFluid().ifPresent(fluid ->
                            EssenceBoilerFluid.STREAM_CODEC.encode(buffer, fluid));

                    buffer.writeBoolean(recipe.preserveFluidAmount());
                    buffer.writeVarInt(recipe.time());
                    buffer.writeVarInt(recipe.minimumInputFluid());
                }

                @Override
                public CrystalGrowthRecipe decode(RegistryFriendlyByteBuf buffer) {
                    int ingredientCount = buffer.readVarInt();
                    List<Ingredient> ingredients = new ArrayList<>(ingredientCount);
                    for (int i = 0; i < ingredientCount; i++) {
                        ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                    }

                    Optional<EssenceBoilerFluid> inputFluid = buffer.readBoolean()
                            ? Optional.of(EssenceBoilerFluid.STREAM_CODEC.decode(buffer))
                            : Optional.empty();

                    Optional<ItemStackTemplate> result = buffer.readBoolean()
                            ? Optional.of(ItemStackTemplate.STREAM_CODEC.decode(buffer))
                            : Optional.empty();

                    Optional<EssenceBoilerFluid> outputFluid = buffer.readBoolean()
                            ? Optional.of(EssenceBoilerFluid.STREAM_CODEC.decode(buffer))
                            : Optional.empty();

                    boolean preserveFluidAmount = buffer.readBoolean();

                    return new CrystalGrowthRecipe(
                            ingredients,
                            inputFluid,
                            result,
                            outputFluid,
                            preserveFluidAmount,
                            buffer.readVarInt(),
                            buffer.readVarInt()
                    );
                }
            };

    public CrystalGrowthRecipe {
        ingredients = List.copyOf(ingredients);
        inputFluid = inputFluid == null ? Optional.empty() : inputFluid;
        result = result == null ? Optional.empty() : result;
        outputFluid = outputFluid == null ? Optional.empty() : outputFluid;

        if (ingredients.size() > MAX_ITEM_INGREDIENTS) {
            throw new IllegalArgumentException(
                    "Crystal growth recipes support at most one item input"
            );
        }
        if (result.isPresent() && result.get().count() > 1) {
            throw new IllegalArgumentException(
                    "Crystal growth recipes support at most one item result"
            );
        }
        if (ingredients.isEmpty() && inputFluid.isEmpty()) {
            throw new IllegalArgumentException("Crystal growth recipes require an item or fluid input");
        }
        if (result.isEmpty() && outputFluid.isEmpty()) {
            throw new IllegalArgumentException("Crystal growth recipes require an item or fluid result");
        }
        if (inputFluid.filter(EssenceBoilerFluid::isEmpty).isPresent()
                || outputFluid.filter(EssenceBoilerFluid::isEmpty).isPresent()) {
            throw new IllegalArgumentException("Crystal growth recipe fluid amounts must be greater than zero");
        }
        if (preserveFluidAmount && (inputFluid.isEmpty() || outputFluid.isEmpty())) {
            throw new IllegalArgumentException(
                    "Preserving fluid amount requires both input_fluid and output_fluid"
            );
        }
        if (minimumInputFluid < 0 || (minimumInputFluid > 0 && inputFluid.isEmpty())) {
            throw new IllegalArgumentException("Minimum input fluid requires an input fluid and cannot be negative");
        }
        if (time <= 0) {
            throw new IllegalArgumentException("Crystal growth time must be greater than zero");
        }
    }

    public boolean hasItemInput() {
        return !ingredients.isEmpty();
    }

    public boolean hasFluidInput() {
        return inputFluid.isPresent();
    }

    public boolean hasItemResult() {
        return result.isPresent();
    }

    public boolean hasFluidResult() {
        return outputFluid.isPresent();
    }

    public int getMinimumInputFluid() {
        if (inputFluid.isEmpty()) {
            return 0;
        }
        if (minimumInputFluid > 0) {
            return minimumInputFluid;
        }
        return outputFluid.isPresent() ? 1 : inputFluid.get().amount();
    }

    public Optional<ItemStack> createResult() {
        return result.map(ItemStackTemplate::create);
    }

    @Override
    public boolean matches(FluidRecipeInput input, Level level) {
        if (inputFluid.isPresent()) {
            EssenceBoilerFluid requiredFluid = inputFluid.get();
            EssenceBoilerFluid tankFluid = input.fluid();
            if (tankFluid.isEmpty()
                    || tankFluid.fluid() != requiredFluid.fluid()
                    || (requiredFluid.hasPotionContents()
                    && !requiredFluid.potionContents().equals(tankFluid.potionContents()))
                    || tankFluid.amount() < getMinimumInputFluid()) {
                return false;
            }
        }

        return getMatchedIngredientSlots(input) != null;
    }

    public int[] getMatchedIngredientSlots(FluidRecipeInput input) {
        if (ingredients.isEmpty()) {
            return result.isPresent() && !input.getItem(0).isEmpty() ? null : new int[0];
        }

        return ingredients.getFirst().test(input.getItem(0)) ? new int[]{0} : null;
    }

    @Override
    public ItemStack assemble(FluidRecipeInput input) {
        return result.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
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
        return ingredients.isEmpty()
                ? PlacementInfo.NOT_PLACEABLE
                : PlacementInfo.create(ingredients.getFirst());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
