package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.helper.SimpleItemHandler;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.CrystalGrowthRecipe;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.helper.FluidRecipeInput;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidTankAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CrystalGrowthBlockEntity extends BlockEntity implements WorldlyContainer, IFluidTankAccess {
    public static final int SLOT_ITEM = 0;
    public static final int TANK_CAPACITY = 4000;

    private static final int[] ITEM_SLOT = {SLOT_ITEM};
    private static final int REWIND_SPEED = 5;

    public int progress;
    public int maxProgress = 200;

    public final SimpleItemHandler inventory = new SimpleItemHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }
    };

    private EssenceBoilerFluid fluidTank = EssenceBoilerFluid.EMPTY;
    private int craftingIngredientMask;

    public CrystalGrowthBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRYSTAL_GROWTH_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrystalGrowthBlockEntity growth) {
        if (level.isClientSide()) {
            return;
        }

        if (growth.progress == 0) {
            growth.pickUpDroppedItems(level, pos);
        }

        Optional<RecipeHolder<CrystalGrowthRecipe>> recipe = growth.getCurrentRecipe();
        if (recipe.isEmpty()) {
            growth.progress = Math.max(0, growth.progress - REWIND_SPEED);
            growth.craftingIngredientMask = 0;
        } else {
            CrystalGrowthRecipe value = recipe.get().value();
            int[] matchedSlots = value.getMatchedIngredientSlots(growth.getRecipeInput());
            growth.craftingIngredientMask = 0;
            for (int slot : matchedSlots) {
                growth.craftingIngredientMask |= 1 << slot;
            }
            growth.maxProgress = value.time();

            ItemStack output = value.result().create();
            if (!growth.canOutput(output)) {
                growth.progress = growth.maxProgress;
            } else if (++growth.progress >= growth.maxProgress) {
                growth.craft(value, matchedSlots, output);
            }
        }

        growth.setChanged();
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    private void pickUpDroppedItems(Level level, BlockPos pos) {
        AABB area = new AABB(pos.getX() + 0.1, pos.getY() + 0.35, pos.getZ() + 0.1,
                pos.getX() + 0.9, pos.getY() + 1.25, pos.getZ() + 0.9);
        for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, area)) {
            ItemStack dropped = itemEntity.getItem();
            if (!inventory.getStackInSlot(SLOT_ITEM).isEmpty()) {
                continue;
            }
            ItemStack remainder = inventory.insertItem(SLOT_ITEM, dropped, false);
            if (remainder.isEmpty()) {
                itemEntity.discard();
            } else if (remainder.getCount() != dropped.getCount()) {
                itemEntity.setItem(remainder);
            }
        }
    }

    private void craft(CrystalGrowthRecipe recipe, int[] matchedSlots, ItemStack output) {
        if (level == null || !canOutput(output) || fluidTank.amount() < recipe.inputFluid().amount()) {
            progress = maxProgress;
            return;
        }
        for (int slot : matchedSlots) {
            inventory.extractItem(slot, 1, false);
        }
        drainFluidTank(recipe.inputFluid().amount());
        inventory.setStackInSlot(SLOT_ITEM, output);
        progress = 0;
        craftingIngredientMask = 0;
    }

    private boolean canOutput(ItemStack output) {
        return output.getCount() <= inventory.getSlotLimit(SLOT_ITEM);
    }

    public ItemStack getGrowingResult() {
        return inventory.getStackInSlot(SLOT_ITEM);
    }

    public int getGrowingResultSlot() {
        return inventory.getStackInSlot(SLOT_ITEM).isEmpty() ? -1 : SLOT_ITEM;
    }

    public float getGrowthProgress() {
        return maxProgress <= 0 ? 0.0F : Math.min(1.0F, (float) progress / maxProgress);
    }

    public boolean isCraftingIngredient(int slot) {
        return (craftingIngredientMask & (1 << slot)) != 0;
    }

    private FluidRecipeInput getRecipeInput() {
        return new FluidRecipeInput(
                new SimpleContainer(inventory.getStackInSlot(SLOT_ITEM)),
                fluidTank
        );
    }

    private Optional<RecipeHolder<CrystalGrowthRecipe>> getCurrentRecipe() {
        if (level == null || level.getServer() == null) {
            return Optional.empty();
        }
        MinecraftServer server = Objects.requireNonNull(level.getServer());
        return server.getRecipeManager().getRecipeFor(
                ModRecipes.CRYSTAL_GROWTH.type().get(),
                getRecipeInput(),
                level
        );
    }

    @Override
    public EssenceBoilerFluid getFluidTank() {
        return fluidTank;
    }

    @Override
    public int getFluidTankAmount() {
        return fluidTank.amount();
    }

    @Override
    public int getFluidTankCapacity() {
        return TANK_CAPACITY;
    }

    @Override
    public int fillFluidTank(EssenceBoilerFluid incoming) {
        if (incoming == null || incoming.isEmpty()
                || !fluidTank.isEmpty() && !fluidTank.isSame(incoming)) {
            return 0;
        }
        int accepted = Math.min(incoming.amount(), TANK_CAPACITY - fluidTank.amount());
        if (accepted > 0) {
            fluidTank = fluidTank.isEmpty()
                    ? incoming.withAmount(accepted)
                    : fluidTank.withAmount(fluidTank.amount() + accepted);
            setChanged();
            sync();
        }
        return accepted;
    }

    @Override
    public EssenceBoilerFluid drainFluidTank(int amount) {
        if (amount <= 0 || fluidTank.isEmpty()) {
            return EssenceBoilerFluid.EMPTY;
        }
        int drained = Math.min(amount, fluidTank.amount());
        EssenceBoilerFluid result = fluidTank.withAmount(drained);
        fluidTank = fluidTank.amount() == drained
                ? EssenceBoilerFluid.EMPTY
                : fluidTank.withAmount(fluidTank.amount() - drained);
        setChanged();
        sync();
        return result;
    }

    public void drops() {
        if (level == null) {
            return;
        }
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(level, worldPosition, container);
    }

    private void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);
        inventory.save(output);
        if (!fluidTank.isEmpty()) {
            output.store("fluid", EssenceBoilerFluid.CODEC.codec(), fluidTank);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", 200);
        craftingIngredientMask = 0;
        inventory.load(input);
        fluidTank = input.read("fluid", EssenceBoilerFluid.CODEC.codec())
                .orElse(EssenceBoilerFluid.EMPTY);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return ITEM_SLOT;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == SLOT_ITEM;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return direction != Direction.DOWN && canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_ITEM && direction == Direction.DOWN;
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return inventory.extractItem(slot, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        return inventory.extractItem(slot, stack.getCount(), false);
    }

    @Override
    public void clearContent() {
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            inventory.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) < 64.0;
    }
}
