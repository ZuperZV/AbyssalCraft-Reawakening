package net.zuperzv.abyssalcraft_reawakening.init.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualAltarBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualPedestalBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.helper.TimeOfDay;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStackTemplate;


import java.util.*;

public record StoneRitualAltarRecipe(
        ItemStackTemplate output,
        Ingredient moldIngredient,
        List<Ingredient> additionalIngredients,
        Optional<EntityType<?>> entityType,
        Optional<String> requiredEssenceType,
        Optional<Block> additionalBlock,
        Optional<Map<String, String>> blockState,
        Optional<Boolean> needsBlock,
        Optional<Block> blockOutput,
        Optional<TimeOfDay> timeOfDay,
        Optional<TimeOfDay> fakeTimeOfDay,
        int recipeTime
) implements Recipe<StoneRitualAltarBlockEntity.BlockRecipeInput> {

    public static final MapCodec<StoneRitualAltarRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ItemStackTemplate.CODEC.fieldOf("output")
                            .forGetter(StoneRitualAltarRecipe::output),

                    Ingredient.CODEC.fieldOf("ingredient")
                            .forGetter(StoneRitualAltarRecipe::moldIngredient),

                    Ingredient.CODEC.listOf()
                            .optionalFieldOf("ingredients", List.of())
                            .forGetter(StoneRitualAltarRecipe::additionalIngredients),

                    BuiltInRegistries.ENTITY_TYPE.byNameCodec()
                            .optionalFieldOf("entityType")
                            .forGetter(StoneRitualAltarRecipe::entityType),

                    Codec.STRING.optionalFieldOf("essence_type")
                            .forGetter(StoneRitualAltarRecipe::requiredEssenceType),

                    BuiltInRegistries.BLOCK.byNameCodec()
                            .optionalFieldOf("block")
                            .forGetter(StoneRitualAltarRecipe::additionalBlock),

                    Codec.unboundedMap(
                                    Codec.STRING,
                                    Codec.STRING
                            ).optionalFieldOf("block_state")
                            .forGetter(StoneRitualAltarRecipe::blockState),

                    Codec.BOOL.optionalFieldOf("needs_block")
                            .forGetter(StoneRitualAltarRecipe::needsBlock),

                    BuiltInRegistries.BLOCK.byNameCodec()
                            .optionalFieldOf("block_output")
                            .forGetter(StoneRitualAltarRecipe::blockOutput),

                    TimeOfDay.CODEC.optionalFieldOf("time_of_day")
                            .forGetter(StoneRitualAltarRecipe::timeOfDay),

                    TimeOfDay.CODEC.optionalFieldOf("fake_time_of_day")
                            .forGetter(StoneRitualAltarRecipe::fakeTimeOfDay),

                    Codec.INT.fieldOf("time")
                            .forGetter(StoneRitualAltarRecipe::recipeTime)
            ).apply(instance, StoneRitualAltarRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StoneRitualAltarRecipe> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public void encode(RegistryFriendlyByteBuf buf, StoneRitualAltarRecipe recipe) {
                    ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.output());
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.moldIngredient());
                }

                @Override
                public StoneRitualAltarRecipe decode(RegistryFriendlyByteBuf buf) {
                    ItemStackTemplate output = ItemStackTemplate.STREAM_CODEC.decode(buf);
                    Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);

                    return new StoneRitualAltarRecipe(
                            output,
                            ingredient,
                            List.of(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            0
                    );
                }
            };

    @Override
    public ItemStack assemble(StoneRitualAltarBlockEntity.BlockRecipeInput input) {
        return output.create().copy();
    }

    @Override
    public boolean matches(StoneRitualAltarBlockEntity.BlockRecipeInput blockInput, Level level) {

        ItemStack moldStack = blockInput.stack();
        BlockPos center = blockInput.pos();

        if (!moldIngredient.test(moldStack)) return false;

        if (entityType.isPresent()) {
            BlockEntity be = level.getBlockEntity(center);
            if (!(be instanceof StoneRitualAltarBlockEntity altar)) return false;
            if (altar.entityLastSacrificed == null) return false;
            if (!altar.entityLastSacrificed.equals(entityType.get())) return false;
        }

        if (additionalBlock.isPresent() && needsBlock.orElse(false)) {
            boolean found = false;

            for (int dx = -2; dx <= 2 && !found; dx++) {
                for (int dz = -2; dz <= 2 && !found; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockPos checkPos = center.offset(dx, 0, dz);
                    BlockState stateAt = level.getBlockState(checkPos);

                    if (stateAt.getBlock().equals(additionalBlock.get())) {

                        if (blockState.isPresent()) {
                            boolean allMatch = true;

                            for (var entry : blockState.get().entrySet()) {
                                Property<?> property =
                                        stateAt.getBlock().getStateDefinition().getProperty(entry.getKey());

                                if (property == null) {
                                    allMatch = false;
                                    break;
                                }

                                Optional<? extends Comparable<?>> parsed = property.getValue(entry.getValue());
                                if (parsed.isEmpty() || !stateAt.getValue(property).equals(parsed.get())) {
                                    allMatch = false;
                                    break;
                                }
                            }

                            if (allMatch) found = true;

                        } else {
                            found = true;
                        }
                    }
                }
            }

            if (!found) return false;
        }

        if (fakeTimeOfDay.isPresent()) {
            long time = level.getLevelData().getGameTime() % 24000;
            boolean isDay = time >= 0 && time < 13000;
            Constants.LOG.debug("isDay: {}", isDay);

            switch (fakeTimeOfDay.get()) {
                case DAY -> { if (!isDay) return false; }
                case NIGHT -> { if (isDay) return false; }
                case BOTH -> {}
            }
        }

        Set<Ingredient> unmatched = new HashSet<>(additionalIngredients);

        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (dx == 0 && dz == 0) continue;

                BlockPos checkPos = center.offset(dx, 0, dz);
                BlockEntity be = level.getBlockEntity(checkPos);
                if (!(be instanceof StoneRitualPedestalBlockEntity nexus)) continue;

                for (int slot = 0; slot < nexus.inventory.getSlots(); slot++) {
                    ItemStack stack = nexus.inventory.getStackInSlot(slot);
                    if (stack.isEmpty()) continue;

                    Ingredient matched = null;

                    for (Ingredient ing : unmatched) {
                        if (ing.test(stack)) {
                            matched = ing;
                            break;
                        }
                    }

                    if (matched != null) {
                        unmatched.remove(matched);
                    }

                    if (unmatched.isEmpty()) return true;
                }
            }
        }

        return unmatched.isEmpty();
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
    public RecipeSerializer<? extends Recipe<StoneRitualAltarBlockEntity.BlockRecipeInput>> getSerializer() {
        return ModRecipes.ASTRAL_ALTAR.serializer().get();
    }

    @Override
    public RecipeType<? extends Recipe<StoneRitualAltarBlockEntity.BlockRecipeInput>> getType() {
        return ModRecipes.ASTRAL_ALTAR.type().get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(moldIngredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}