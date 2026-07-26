package net.zuperzv.abyssalcraft_reawakening.init.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualAltarBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualPedestalBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
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
        int recipeTime,
        int potentialEnergy,
        Optional<ResourceKey<Level>> dimension
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
                            .forGetter(StoneRitualAltarRecipe::recipeTime),

                    Codec.INT.fieldOf("pe")
                            .forGetter(StoneRitualAltarRecipe::potentialEnergy),

                    ResourceKey.codec(Registries.DIMENSION)
                            .optionalFieldOf("dimension")
                            .forGetter(StoneRitualAltarRecipe::dimension)
            ).apply(instance, StoneRitualAltarRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StoneRitualAltarRecipe> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public void encode(RegistryFriendlyByteBuf buf, StoneRitualAltarRecipe recipe) {

                    ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.output());

                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.moldIngredient());


                    buf.writeVarInt(recipe.additionalIngredients().size());

                    for (Ingredient ingredient : recipe.additionalIngredients()) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                    }

                    buf.writeBoolean(recipe.entityType().isPresent());

                    recipe.entityType().ifPresent(entity ->
                            ByteBufCodecs.registry(Registries.ENTITY_TYPE)
                                    .encode(buf, entity)
                    );

                    buf.writeBoolean(recipe.requiredEssenceType().isPresent());

                    recipe.requiredEssenceType()
                            .ifPresent(buf::writeUtf);

                    buf.writeBoolean(recipe.additionalBlock().isPresent());

                    recipe.additionalBlock().ifPresent(block ->
                            buf.writeIdentifier(
                                    BuiltInRegistries.BLOCK.getKey(block)
                            )
                    );

                    buf.writeBoolean(recipe.blockState().isPresent());

                    recipe.blockState().ifPresent(state -> {

                        buf.writeVarInt(state.size());

                        for (var entry : state.entrySet()) {
                            buf.writeUtf(entry.getKey());
                            buf.writeUtf(entry.getValue());
                        }
                    });

                    buf.writeBoolean(recipe.needsBlock().isPresent());

                    recipe.needsBlock()
                            .ifPresent(buf::writeBoolean);

                    buf.writeBoolean(recipe.blockOutput().isPresent());

                    recipe.blockOutput().ifPresent(block ->
                            buf.writeIdentifier(
                                    BuiltInRegistries.BLOCK.getKey(block)
                            )
                    );

                    buf.writeBoolean(recipe.timeOfDay().isPresent());

                    recipe.timeOfDay()
                            .ifPresent(time ->
                                    buf.writeUtf(time.name())
                            );

                    buf.writeBoolean(recipe.fakeTimeOfDay().isPresent());

                    recipe.fakeTimeOfDay()
                            .ifPresent(time ->
                                    buf.writeUtf(time.name())
                            );

                    buf.writeVarInt(recipe.recipeTime());
                    buf.writeVarInt(recipe.potentialEnergy());

                    buf.writeBoolean(recipe.dimension().isPresent());

                    recipe.dimension().ifPresent(dimension ->
                            buf.writeIdentifier(dimension.identifier())
                    );
                }

                @Override
                public StoneRitualAltarRecipe decode(RegistryFriendlyByteBuf buf) {

                    ItemStackTemplate output =
                            ItemStackTemplate.STREAM_CODEC.decode(buf);

                    Ingredient moldIngredient =
                            Ingredient.CONTENTS_STREAM_CODEC.decode(buf);

                    int ingredientSize = buf.readVarInt();

                    List<Ingredient> additionalIngredients = new ArrayList<>();

                    for (int i = 0; i < ingredientSize; i++) {

                        additionalIngredients.add(
                                Ingredient.CONTENTS_STREAM_CODEC.decode(buf)
                        );
                    }

                    Optional<EntityType<?>> entityType = Optional.empty();

                    if (buf.readBoolean()) {

                        entityType = Optional.of(
                                ByteBufCodecs.registry(Registries.ENTITY_TYPE)
                                        .decode(buf)
                        );
                    }

                    Optional<String> requiredEssenceType = Optional.empty();

                    if (buf.readBoolean()) {

                        requiredEssenceType =
                                Optional.of(buf.readUtf());
                    }

                    Optional<Block> additionalBlock = Optional.empty();

                    if (buf.readBoolean()) {
                        additionalBlock =
                                BuiltInRegistries.BLOCK
                                        .get(buf.readIdentifier())
                                        .map(Holder.Reference::value);
                    }

                    Optional<Map<String,String>> blockState = Optional.empty();

                    if (buf.readBoolean()) {

                        int size = buf.readVarInt();

                        Map<String,String> map = new HashMap<>();

                        for (int i = 0; i < size; i++) {

                            map.put(
                                    buf.readUtf(),
                                    buf.readUtf()
                            );
                        }

                        blockState = Optional.of(map);
                    }

                    Optional<Boolean> needsBlock = Optional.empty();

                    if (buf.readBoolean()) {
                        needsBlock =
                                Optional.of(
                                        buf.readBoolean()
                                );
                    }

                    Optional<Block> blockOutput = Optional.empty();

                    if (buf.readBoolean()) {
                        blockOutput =
                                BuiltInRegistries.BLOCK
                                        .get(buf.readIdentifier())
                                        .map(Holder.Reference::value);
                    }

                    Optional<TimeOfDay> timeOfDay = Optional.empty();

                    if (buf.readBoolean()) {

                        timeOfDay =
                                Optional.of(
                                        TimeOfDay.valueOf(
                                                buf.readUtf()
                                        )
                                );
                    }

                    Optional<TimeOfDay> fakeTimeOfDay = Optional.empty();

                    if (buf.readBoolean()) {

                        fakeTimeOfDay =
                                Optional.of(
                                        TimeOfDay.valueOf(
                                                buf.readUtf()
                                        )
                                );
                    }

                    int recipeTime =
                            buf.readVarInt();

                    int potentialEnergy =
                            buf.readVarInt();

                    Optional<ResourceKey<Level>> dimension = Optional.empty();

                    if (buf.readBoolean()) {
                        dimension = Optional.of(
                                ResourceKey.create(
                                        Registries.DIMENSION,
                                        buf.readIdentifier()
                                )
                        );
                    }

                    return new StoneRitualAltarRecipe(
                            output,
                            moldIngredient,
                            additionalIngredients,
                            entityType,
                            requiredEssenceType,
                            additionalBlock,
                            blockState,
                            needsBlock,
                            blockOutput,
                            timeOfDay,
                            fakeTimeOfDay,
                            recipeTime,
                            potentialEnergy,
                            dimension
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

        if (dimension.isPresent() && !level.dimension().equals(dimension.get())) {
            return false;
        }

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
            long time = level.getDefaultClockTime() % 24000;
            boolean isDay = time < 13000;

            Constants.LOG.debug("isDay: {}", isDay);

            switch (fakeTimeOfDay.get()) {
                case DAY -> { if (!isDay) return false; }
                case NIGHT -> { if (isDay) return false; }
                case BOTH -> {}
            }
        }

        List<Ingredient> remainingIngredients = new ArrayList<>(additionalIngredients);

        Set<String> usedPedestals = new HashSet<>();

        ingredients:
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {

                if (dx == 0 && dz == 0)
                    continue;

                BlockPos checkPos = center.offset(dx, 0, dz);
                BlockEntity be = level.getBlockEntity(checkPos);


                if (!(be instanceof StoneRitualPedestalBlockEntity nexus))
                    continue;

                for (int slot = 0; slot < nexus.inventory.getSlots(); slot++) {

                    String pedestalId = nexus.getBlockPos() + ":" + slot;

                    if (usedPedestals.contains(pedestalId))
                        continue;

                    ItemStack stack = nexus.inventory.getStackInSlot(slot);

                    if (stack.isEmpty())
                        continue;

                    for (int i = 0; i < remainingIngredients.size(); i++) {

                        Ingredient ingredient = remainingIngredients.get(i);

                        if (ingredient.test(stack)) {

                            usedPedestals.add(pedestalId);

                            remainingIngredients.remove(i);

                            break;
                        }
                    }

                    if (remainingIngredients.isEmpty()) {
                        break ingredients;
                    }
                }
            }
        }

        if (!remainingIngredients.isEmpty()) {
            return false;
        }

        BlockPos pos = blockInput.pos();

        List<Player> players = level.getEntitiesOfClass(
                Player.class,
                new AABB(
                        pos.getX() - 7,
                        pos.getY() - 7,
                        pos.getZ() - 7,
                        pos.getX() + 8,
                        pos.getY() + 8,
                        pos.getZ() + 8
                )
        );

        players.sort(Comparator.comparingDouble(player ->
                player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())
        ));

        if (players.isEmpty()) {
            return false;
        }

        for (Player player : players) {
            if (player.isCreative()) {
                return true;
            }

            for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
                if (stack.is(ModItems.NECRONOMICON.get())) {

                    PotentialEnergyData pe = stack.get(
                            ModDataComponentTypes.POTENTIAL_ENERGY.get()
                    );

                    if (pe == null) {
                        continue;
                    }

                    if (pe.getPotentialEnergy() >= potentialEnergy) {
                        return true;
                    }
                }
            }
        }

        return false;
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

    public int getRecipeTime() {
        return recipeTime;
    }

    public Optional<TimeOfDay> getTimeOfDay() {
        return timeOfDay;
    }
}