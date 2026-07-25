package net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom;

import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.StoneRitualAltarBlock;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.helper.SimpleItemHandler;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.component.PotentialEnergyData;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.StoneRitualAltarRecipe;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.ModRecipes;
import org.jetbrains.annotations.Nullable;
import net.zuperzv.abyssalcraft_reawakening.Constants;

import java.util.*;

import static net.zuperzv.abyssalcraft_reawakening.init.block.custom.StoneRitualAltarBlock.DONE;

public class StoneRitualAltarBlockEntity extends BlockEntity implements WorldlyContainer {
    public int progress = 0;
    public int maxProgress = 80;
    private int prevProgress = 0;
    private StoneRitualAltarRecipe currentRecipe;
    public Optional<Holder.Reference<EntityType<?>>> entityLastSacrificed = Optional.empty();

    public static final int INVENTORY_SIZE = 1;
    public final SimpleItemHandler inventory = new SimpleItemHandler (INVENTORY_SIZE) {
        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private float rotation;

    public StoneRitualAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STONE_RITUAL_ALTAR_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StoneRitualAltarBlockEntity altar) {
        BlockState oldState = level.getBlockState(pos);

        oldState = oldState.setValue(DONE, false);

        altar.prevProgress = altar.progress;
        if (altar.hasRecipe()) {
            altar.progress++;
            setCrafting(pos, level, true);
            if (altar.progress >= altar.maxProgress) {
                altar.craftItem();
                oldState = oldState.setValue(DONE, true);
            }
            altar.setChanged();
        } else {
            altar.progress = 0;
            altar.setChanged();
        }

        oldState = oldState.setValue(StoneRitualAltarBlock.CRAFTING, altar.progress > 0);

        level.setBlockAndUpdate(pos, oldState);

        giveNexusInfoAboutStoneRitualAltar(level, pos, altar);
    }

    public boolean hasRecipe() {
        if (level == null) return false;

        Optional<RecipeHolder<StoneRitualAltarRecipe>> recipeOpt = Objects.requireNonNull(level.getServer()).getRecipeManager()
                .getRecipeFor(ModRecipes.ASTRAL_ALTAR.type().get(), new BlockRecipeInput(inventory.getStackInSlot(0), worldPosition), level);

        if (recipeOpt.isEmpty()) return false;

        StoneRitualAltarRecipe altarRecipe = recipeOpt.get().value();
        ItemStack inputStack = inventory.getStackInSlot(0);

        if (!altarRecipe.moldIngredient().test(inputStack)) return false;

        List<Ingredient> ingredientsToMatch = new ArrayList<>(altarRecipe.additionalIngredients());

        boolean allMatched = true;

        allMatched = hasAllIngredientsToMatch(ingredientsToMatch, allMatched);

        if (allMatched) {
            maxProgress = altarRecipe.recipeTime();
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        }

        return allMatched;
    }

    public void craftItem() {
        Level level = this.level;
        if (level == null) return;

        Optional<RecipeHolder<StoneRitualAltarRecipe>> recipe = Objects.requireNonNull(level.getServer()).getRecipeManager()
                .getRecipeFor(ModRecipes.ASTRAL_ALTAR.type().get(), new BlockRecipeInput(inventory.getStackInSlot(0), worldPosition), level);

        if (recipe.isEmpty()) return;

        StoneRitualAltarRecipe altarRecipe = recipe.get().value();
        ItemStack inputStack = inventory.getStackInSlot(0);

        if (!altarRecipe.moldIngredient().test(inputStack)) return;

        List<MatchedItem> matchedIngredientSources = new ArrayList<>();

        List<Ingredient> ingredientsToMatch = new ArrayList<>(altarRecipe.additionalIngredients());

        boolean allMatched = true;

        allMatched = isAllMatched(ingredientsToMatch, level, matchedIngredientSources, allMatched);


        if (allMatched) {
            if (altarRecipe.entityType().isPresent()) {
                    if (entityLastSacrificed.equals(altarRecipe.entityType().get())) {
                    setSacrificedEntity(null);
                    Constants.LOG.debug("Text: {}", entityLastSacrificed);
                }
            }

            inventory.extractItem(0, 1, false);
            for (MatchedItem matched : matchedIngredientSources) {

                matched.nexus.inventory.extractItem(
                        matched.slot,
                        matched.amount,
                        false
                );

                matched.nexus.inventory.setChangeCallback(this::setChanged);
                matched.nexus.setChanged();

                level.sendBlockUpdated(
                        matched.nexus.getBlockPos(),
                        matched.nexus.getBlockState(),
                        matched.nexus.getBlockState(),
                        3
                );
            }
            inventory.setStackInSlot(0, altarRecipe.output().create().copy());

            progress = 0;
            prevProgress = 0;
            setCrafting(worldPosition, level, false);

            inventory.setChangeCallback(this::setChanged);

            List<Player> players = level.getEntitiesOfClass(
                    Player.class,
                    new AABB(
                            worldPosition.getX() - 7,
                            worldPosition.getY() - 7,
                            worldPosition.getZ() - 7,
                            worldPosition.getX() + 8,
                            worldPosition.getY() + 8,
                            worldPosition.getZ() + 8
                    )
            );

            players.sort(Comparator.comparingDouble(player ->
                    player.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ())
            ));

            boolean hasRemovedItems = false;

            for (Player player : players) {
                if (hasRemovedItems || player.isCreative()) {
                    continue;
                }

                for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {

                    if (stack.is(ModItems.NECRONOMICON.get())) {

                        PotentialEnergyData pe = stack.get(ModDataComponentTypes.POTENTIAL_ENERGY.get());

                        if (pe == null) {
                            continue;
                        }

                        int requiredPE = altarRecipe.potentialEnergy();
                        int currentPE = pe.getPotentialEnergy();

                        if (currentPE >= requiredPE) {

                            stack.set(
                                    ModDataComponentTypes.POTENTIAL_ENERGY.get(),
                                    new PotentialEnergyData(
                                            pe.getPotentialEnergy() - requiredPE
                                    )
                            );

                            player.getInventory().setChanged();
                            player.containerMenu.broadcastChanges();

                            hasRemovedItems = true;
                            break;
                        }
                    }
                }
            }

            setChanged();

            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_CLIENTS
            );

            itemCraftingParticles(level);

            blockCraftingParticles(altarRecipe, level);
        }
    }

    private static void giveNexusInfoAboutStoneRitualAltar(Level level, BlockPos pos, StoneRitualAltarBlockEntity altar) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (dx == 0 && dz == 0) continue;

                BlockPos checkPos = pos.offset(dx, 0, dz);
                BlockEntity be = level.getBlockEntity(checkPos);
                if (be instanceof StoneRitualPedestalBlockEntity nexus) {
                    nexus.setSavedPos(pos);

                    nexus.progress = altar.progress;
                    nexus.maxProgress = altar.maxProgress;
                    nexus.setChanged();

                    level.sendBlockUpdated(nexus.getBlockPos(), nexus.getBlockState(), nexus.getBlockState(), 3);
                }
            }
        }
    }

    private boolean hasAllIngredientsToMatch(List<Ingredient> ingredientsToMatch, boolean allMatched) {
        for (Ingredient ingredient : ingredientsToMatch) {
            boolean matched = false;

            outer:
            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockPos checkPos = worldPosition.offset(dx, 0, dz);
                    BlockEntity be = level.getBlockEntity(checkPos);

                    if (!(be instanceof StoneRitualPedestalBlockEntity nexus)) continue;

                    for (int slot = 0; slot < nexus.inventory.getSlots(); slot++) {
                        ItemStack stack = nexus.inventory.getStackInSlot(slot);
                        if (!stack.isEmpty() && ingredient.test(stack)) {
                            matched = true;
                            break outer;
                        }
                    }
                }
            }

            if (!matched) {
                allMatched = false;
                break;
            }
        }
        return allMatched;
    }

    private boolean isAllMatched(
            List<Ingredient> ingredientsToMatch,
            Level level,
            List<MatchedItem> matchedIngredientSources,
            boolean allMatched
    ) {

        List<Ingredient> remaining = new ArrayList<>(ingredientsToMatch);

        Set<String> usedSlots = new HashSet<>();

        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {

                if (dx == 0 && dz == 0)
                    continue;

                BlockPos checkPos = worldPosition.offset(dx, 0, dz);
                BlockEntity be = level.getBlockEntity(checkPos);

                if (!(be instanceof StoneRitualPedestalBlockEntity nexus))
                    continue;

                for (int slot = 0; slot < nexus.inventory.getSlots(); slot++) {

                    String slotId = nexus.getBlockPos() + ":" + slot;

                    if (usedSlots.contains(slotId))
                        continue;

                    ItemStack stack = nexus.inventory.getStackInSlot(slot);

                    if (stack.isEmpty())
                        continue;

                    for (int i = 0; i < remaining.size(); i++) {

                        Ingredient ingredient = remaining.get(i);

                        if (ingredient.test(stack)) {

                            matchedIngredientSources.add(
                                    new MatchedItem(
                                            nexus,
                                            slot,
                                            1
                                    )
                            );

                            usedSlots.add(slotId);

                            remaining.remove(i);

                            break;
                        }
                    }

                    if (remaining.isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void itemCraftingParticles(Level level) {
        if (level instanceof ServerLevel serverLevel) {

            spawnVisualLightningBolt(serverLevel, worldPosition);

            serverLevel.sendParticles(ParticleTypes.ASH,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1.3,
                    worldPosition.getZ() + 0.5,
                    20, // antal partikler
                    0.1, 0.1, 0.1, // spread
                    0.01 // fart
            );

            serverLevel.sendParticles(ParticleTypes.CRIT,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1.3,
                    worldPosition.getZ() + 0.5,
                    10, // antal partikler
                    0.1, 0.1, 0.1, // spread
                    0.01 // fart
            );
        }

        level.playSound(null, worldPosition, SoundEvents.ALLAY_HURT,
                SoundSource.BLOCKS, 0.12f, 0.17f);
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.BLOCKS, 0.3f, 0.2f);
    }

    private void blockCraftingParticles(StoneRitualAltarRecipe altarRecipe, Level level) {
        if (altarRecipe.additionalBlock().isPresent() && altarRecipe.blockOutput().isPresent()) {
            Block requiredBlock = altarRecipe.additionalBlock().get();
            Block newBlock = altarRecipe.blockOutput().get();

            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockPos checkPos = worldPosition.offset(dx, 0, dz);
                    Block blockAt = level.getBlockState(checkPos).getBlock();

                    BlockState stateAt = level.getBlockState(checkPos);
                    if (stateAt.getBlock().equals(requiredBlock)) {
                        boolean matchesState = true;

                        if (altarRecipe.blockState().isPresent()) {
                            Map<String, String> requiredStates = altarRecipe.blockState().get();

                            for (Map.Entry<String, String> entry : requiredStates.entrySet()) {
                                Property<?> property = stateAt.getBlock().getStateDefinition().getProperty(entry.getKey());

                                if (property == null) {
                                    matchesState = false;
                                    break;
                                }

                                Optional<? extends Comparable<?>> parsed = property.getValue(entry.getValue());
                                if (parsed.isEmpty() || !stateAt.getValue(property).equals(parsed.get())) {
                                    matchesState = false;
                                    break;
                                }
                            }
                        }

                        if (matchesState) {
                            level.setBlockAndUpdate(checkPos, newBlock.defaultBlockState());

                            if (level instanceof ServerLevel serverLevel) {
                                serverLevel.sendParticles(ParticleTypes.END_ROD,
                                        checkPos.getX() + 0.5,
                                        checkPos.getY() + 0.5,
                                        checkPos.getZ() + 0.5,
                                        20,
                                        0.3, 0.3, 0.3,
                                        0.01
                                );
                            }

                            level.playSound(null, checkPos, SoundEvents.AMETHYST_BLOCK_CHIME,
                                    SoundSource.BLOCKS, 1.0f, 1.2f);
                        }

                        if (matchesState) {
                            level.setBlockAndUpdate(checkPos, newBlock.defaultBlockState());

                            if (level instanceof ServerLevel serverLevel) {
                                serverLevel.sendParticles(ParticleTypes.END_ROD,
                                        checkPos.getX() + 0.5,
                                        checkPos.getY() + 0.5,
                                        checkPos.getZ() + 0.5,
                                        20,
                                        0.3, 0.3, 0.3,
                                        0.01
                                );
                            }

                            level.playSound(null, checkPos, SoundEvents.AMETHYST_BLOCK_CHIME,
                                    SoundSource.BLOCKS, 1.0f, 1.2f);
                        }
                    }
                }
            }
        }
    }

    public static void setCrafting(BlockPos altarPos, Level level, boolean boo) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (dx == 0 && dz == 0) continue;

                BlockPos checkPos = altarPos.offset(dx, 0, dz);
                BlockState state = level.getBlockState(checkPos);
                BlockEntity be = level.getBlockEntity(checkPos);

                if (be instanceof StoneRitualPedestalBlockEntity nexus) {
                    if (boo) {
                        if (nexus.craftingStartTime == -1) {
                            nexus.craftingStartTime = level.getGameTime();
                            nexus.isUsedInActiveCraft = true;
                            nexus.setChanged();
                        }
                    } else {
                        nexus.craftingStartTime = -1;
                        nexus.isUsedInActiveCraft = false;
                        nexus.setChanged();
                    }
                }
            }
        }
    }

    // Sacrificed //

    public void setSacrificedEntity(Holder.Reference<EntityType<?>> holder) {
        this.entityLastSacrificed = Optional.ofNullable(holder);
    }

    private void spawnVisualLightningBolt(ServerLevel level, BlockPos blockPos) {
        Objects.requireNonNull(EntityType.LIGHTNING_BOLT.spawn(level, blockPos, EntitySpawnReason.TRIGGERED)).setVisualOnly(true);
    }

    public SimpleItemHandler getInputItems() {
        return inventory;
    }

    @Override
    public int[] getSlotsForFace(Direction p_58363_) {
        return new int[]{0};
        //if (p_58363_ == Direction.DOWN) {
        //    return new int[]{0};
        //} else {
        //    return p_58363_ == Direction.UP ? new int[]{0} : new int[]{0};
        //}
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        if (slot == 0) {
            return inventory.getStackInSlot(0).isEmpty();
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot == 0 && progress <= 0 && !hasRecipe();
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        if (pSlot < 4) {
            return inventory.getStackInSlot(pSlot);
        } else {
            return inventory.getStackInSlot(pSlot - 4);
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < 4) {
            inventory.setStackInSlot(slot, stack);
        }
        setChanged();
        assert level != null;
        if (!level.isClientSide()) {
            markForUpdate();
        }
    }

    private void markForUpdate() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public ItemStack removeItem(int slotIndex, int count) {
        if (slotIndex >= 0 && slotIndex < inventory.getSlots()) {
            if (progress <= 0 && !hasRecipe()) {
                return inventory.extractItem(slotIndex, count, false);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < inventory.getSlots()) {
            ItemStack stackInSlot = inventory.getStackInSlot(slotIndex);

            if (!stackInSlot.isEmpty()) {
                inventory.setStackInSlot(slotIndex, ItemStack.EMPTY);
                return stackInSlot;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        final double MAX_DISTANCE = 64.0;
        double distanceSquared = player.distanceToSqr(this.worldPosition.getX() + 0.5,
                this.worldPosition.getY() + 0.5,
                this.worldPosition.getZ() + 0.5);
        return distanceSquared <= MAX_DISTANCE;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    private static class MatchedItem {
        public final StoneRitualPedestalBlockEntity nexus;
        public final int slot;
        public final int amount;

        public MatchedItem(
                StoneRitualPedestalBlockEntity nexus,
                int slot,
                int amount
        ) {
            this.nexus = nexus;
            this.slot = slot;
            this.amount = amount;
        }
    }

    public static class BlockRecipeInput implements RecipeInput {

        private final ItemStack stack;
        private final BlockPos pos;

        public BlockRecipeInput(ItemStack stack, BlockPos pos) {
            this.stack = stack;
            this.pos = pos;
        }

        @Override
        public ItemStack getItem(int index) {
            return index == 0 ? stack : ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 1;
        }

        public ItemStack stack() {
            return stack;
        }

        public BlockPos pos() {
            return pos;
        }
    }


    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("progress", progress);
        output.putInt("prevProgress", prevProgress);
        output.putInt("maxProgress", maxProgress);

        inventory.save(output);

        if (entityLastSacrificed.isPresent()) {
            EntityType<?> type = entityLastSacrificed.get().value();
            Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            output.putString("entityLastSacrificed", id.toString());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        progress = input.getIntOr("progress", 0);
        prevProgress = input.getIntOr("prevProgress", 0);
        maxProgress = input.getIntOr("maxProgress", 80);

        inventory.load(input);

        input.getString("entityLastSacrificed").ifPresentOrElse(str -> {
            Identifier id = Identifier.parse(str);
            entityLastSacrificed = BuiltInRegistries.ENTITY_TYPE.get(id);
        }, () -> {
            entityLastSacrificed = Optional.empty();
        });
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}