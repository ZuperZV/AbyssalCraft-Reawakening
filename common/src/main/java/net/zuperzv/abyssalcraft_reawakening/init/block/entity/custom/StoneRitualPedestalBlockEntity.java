package net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.helper.SimpleItemHandler;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.init.recipe.StoneRitualAltarRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StoneRitualPedestalBlockEntity extends BlockEntity implements WorldlyContainer {
    public static final float MERGE_THRESHOLD = 0.85f;

    public long craftingStartTime = -1;
    public long animationStartTime = 1;
    public float clientProgress = 0f;
    public int progress = 0;
    public int maxProgress = 80;
    public boolean isUsedInActiveCraft = false;

    public static final int INVENTORY_SIZE = 1;
    public final SimpleItemHandler inventory = new SimpleItemHandler (INVENTORY_SIZE) {
        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private float rotation;
    private BlockPos savedPos;

    public static void tickClient(Level level, BlockPos pos, BlockState state, StoneRitualPedestalBlockEntity blockEntity) {
        if (level.isClientSide()) {
            blockEntity.clientProgress = blockEntity.progress;
        }

        ItemStack currentStack = blockEntity.inventory.getStackInSlot(0);

        if (!currentStack.isEmpty() && blockEntity.progress > 0) {
            if (blockEntity.animationStartTime == 1) {
                blockEntity.animationStartTime = -1;
            }

            long elapsedTicks = level.getGameTime() - blockEntity.animationStartTime;
            if (elapsedTicks >= blockEntity.maxProgress) {
                blockEntity.animationStartTime = 1;
            }
        } else {
            blockEntity.animationStartTime = 1;
        }
    }

    public SimpleItemHandler getInputItems() {
        return inventory;
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, StoneRitualPedestalBlockEntity blockEntity) {
        level.scheduleTick(pos, state.getBlock(), 1);

        //System.out.println("isUsedInActiveCraft: " + blockEntity.isUsedInActiveCraft);
    }

    public void setSavedPos(BlockPos pos) {
        this.savedPos = pos;
        setChanged();

        if (!level.isClientSide()) {
            level.sendBlockUpdated(this.getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public BlockPos getSavedPos() {
        return savedPos;
    }


    public StoneRitualPedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STONE_RITUAL_PEDESTAL_BE.get(), pPos, pBlockState);
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);

        inventory.save(output);

        if (savedPos != null) {
            output.putInt("SavedX", savedPos.getX());
            output.putInt("SavedY", savedPos.getY());
            output.putInt("SavedZ", savedPos.getZ());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", 80);

        inventory.load(input);

        int savedX = input.getIntOr("SavedX", Integer.MIN_VALUE);
        int savedY = input.getIntOr("SavedY", Integer.MIN_VALUE);
        int savedZ = input.getIntOr("SavedZ", Integer.MIN_VALUE);
        if (savedX != Integer.MIN_VALUE && savedY != Integer.MIN_VALUE && savedZ != Integer.MIN_VALUE) {
            savedPos = new BlockPos(savedX, savedY, savedZ);
        }
    }
    
    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
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

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 0) {
            return inventory.getStackInSlot(0).isEmpty();
        }
        return false;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && slot == 0;
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? inventory.getStackInSlot(0) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            inventory.setStackInSlot(0, stack);
            setChanged();
        }
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        if (slot == 0) {
            return inventory.extractItem(0, count, false);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0) {
            ItemStack stack = inventory.getStackInSlot(0);
            inventory.setStackInSlot(0, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void clearContent() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        final double MAX_DISTANCE = 64.0;
        return player.distanceToSqr(worldPosition.getCenter()) < MAX_DISTANCE;
    }

    public Optional<Vec3> getFlyingItemPosition(float partialTicks) {
        if (!isUsedInActiveCraft()) return Optional.empty();

        float prog = getFlyProgress(partialTicks);
        if (prog <= 0f || prog >= MERGE_THRESHOLD) return Optional.empty();

        float smoothProgress = prog * prog * (3f - 2f * prog);

        double startX = worldPosition.getX() + 0.5;
        double startY = worldPosition.getY() + 1.15;
        double startZ = worldPosition.getZ() + 0.5;

        double endX = savedPos.getX() + 0.5;
        double endY = savedPos.getY() + 1.15;
        double endZ = savedPos.getZ() + 0.5;

        double x = Mth.lerp(smoothProgress, startX, endX);
        double y = Mth.lerp(smoothProgress, startY, endY);
        double z = Mth.lerp(smoothProgress, startZ, endZ);

        return Optional.of(new Vec3(x, y, z));
    }

    public float getFlyProgress(float partialTicks) {
        if (!isUsedInActiveCraft()) return 0f;

        float interpolatedProgress = Mth.lerp(partialTicks, clientProgress, progress);
        if (maxProgress == 0) return 0f;
        return Mth.clamp(interpolatedProgress / maxProgress, 0f, 1f);
    }

    public float getMergeProgress(float partialTicks) {
        float flyProgress = getFlyProgress(partialTicks);
        if (flyProgress < MERGE_THRESHOLD) return 0f;
        return Mth.clamp((flyProgress - MERGE_THRESHOLD) / (1f - MERGE_THRESHOLD), 0f, 1f);
    }

    public boolean isUsedInActiveCraft() {
        return this.isUsedInActiveCraft;
    }

    public boolean setUsedInActiveCraft() {
        if (level == null || savedPos == null || inventory.getStackInSlot(0).isEmpty() || progress <= 0) {
            return false;
        }

        BlockEntity be = level.getBlockEntity(savedPos);
        if (!(be instanceof StoneRitualAltarBlockEntity altar)) return false;

        ItemStack altarStack = altar.inventory.getStackInSlot(0);
        if (altarStack.isEmpty()) return false;

        Optional<RecipeHolder<StoneRitualAltarRecipe>> recipeOpt = Objects.requireNonNull(level.getServer()).getRecipeManager()
                .getRecipeFor(ModRecipes.ASTRAL_ALTAR.type().get(), new StoneRitualAltarBlockEntity.BlockRecipeInput(inventory.getStackInSlot(0), worldPosition), level);

        return recipeOpt.filter(recipe -> isIngredientUsedInRecipeForThisNexus(this, recipe.value())).isPresent();
    }

    private boolean isIngredientUsedInRecipeForThisNexus(StoneRitualPedestalBlockEntity nexus, StoneRitualAltarRecipe recipe) {
        BlockPos thisPos = nexus.getBlockPos();
        Level level = nexus.getLevel();
        if (level == null) return false;

        List<Ingredient> ingredientsToMatch = new ArrayList<>(recipe.additionalIngredients());

        Set<Ingredient> matchedIngredients = new HashSet<>();

        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (dx == 0 && dz == 0) continue;

                BlockPos checkPos = nexus.getSavedPos().offset(dx, 0, dz);
                BlockEntity be = level.getBlockEntity(checkPos);

                if (!(be instanceof StoneRitualPedestalBlockEntity nearbyNexus)) continue;

                for (int slot = 0; slot < nearbyNexus.inventory.getSlots(); slot++) {
                    ItemStack stack = nearbyNexus.inventory.getStackInSlot(slot);
                    if (stack.isEmpty()) continue;

                    for (Ingredient ingredient : ingredientsToMatch) {
                        if (matchedIngredients.contains(ingredient)) continue;

                        if (ingredient.test(stack)) {
                            matchedIngredients.add(ingredient);

                            if (nearbyNexus.getBlockPos().equals(thisPos)) {
                                return true;
                            }

                            break;
                        }
                    }
                }
            }
        }
        return false;
    }
}