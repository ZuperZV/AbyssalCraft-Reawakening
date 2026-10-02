package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.ModBlockEntities;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.helper.SimpleItemHandler;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerPotionFluid;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.EssenceBoilerRecipe;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.StoneRitualAltarRecipe;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.helper.FluidRecipeInput;
import net.zuperzv.abyssalcraft_reawakening.services.types.IFluidTankAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class EssenceBoilerBlockEntity extends BlockEntity implements WorldlyContainer, IFluidTankAccess {
    public static final int SLOT_INGREDIENT_1 = 0;
    public static final int SLOT_INGREDIENT_2 = 1;
    public static final int SLOT_INGREDIENT_3 = 2;
    public static final int SLOT_CONTAINER = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int TOTAL_SLOTS = 5;

    private static final int[] INPUT_SLOTS = {
            SLOT_INGREDIENT_1,
            SLOT_INGREDIENT_2,
            SLOT_INGREDIENT_3,
            SLOT_CONTAINER
    };

    private static final int[] OUTPUT_SLOTS = {SLOT_OUTPUT};

    public static final int DEFAULT_FLUID_CAPACITY = 1000;
    public static final int EVENT_WOBBLE = 1;
    private static final int CRAFTING_REWIND_SPEED = 5;

    public int progress = 0;
    public int maxProgress = 60;
    public long wobbleStartedAtTick;

    @Nullable
    public WobbleStyle lastWobbleStyle;

    public final SimpleItemHandler inventory = new SimpleItemHandler(TOTAL_SLOTS) {
        @Override
        public int getSlotLimit(int slot) {
            return slot == SLOT_OUTPUT ? 64 : 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(
                        getBlockPos(),
                        getBlockState(),
                        getBlockState(),
                        Block.UPDATE_CLIENTS
                );
            }
        }
    };

    private EssenceBoilerFluid fluidTank = EssenceBoilerFluid.EMPTY;
    private EssenceBoilerFluid craftingOutputFluid = EssenceBoilerFluid.EMPTY;
    private int craftingIngredientMask;

    public EssenceBoilerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ESSENCE_BOILER_BE.get(), pos, state);
    }

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            EssenceBoilerBlockEntity boiler
    ) {
        if (level.isClientSide()) {
            return;
        }

        if (boiler.progress <= 0) {
            boiler.pickUpDroppedItems(level, pos);
        }

        if (boiler.tryCraftAmulet(level, pos, state)) {
            return;
        }

        boolean hasAllIngredients =
                !boiler.inventory.getStackInSlot(SLOT_INGREDIENT_1).isEmpty()
                        && !boiler.inventory.getStackInSlot(SLOT_INGREDIENT_2).isEmpty()
                        && !boiler.inventory.getStackInSlot(SLOT_INGREDIENT_3).isEmpty();

        boolean hasBottle = boiler.inventory
                .getStackInSlot(SLOT_CONTAINER)
                .is(Items.GLASS_BOTTLE);

        boolean hasWater = boiler.getFluidTankAmount() >= 1000
                && boiler.getFluidTank().isSame(Fluids.WATER);

        if (hasAllIngredients && hasBottle && hasWater) {
            boiler.setCraftingIngredientMask(0b111);
            boiler.progress++;

            level.playSound(
                    null,
                    pos,
                    SoundEvents.WATER_AMBIENT,
                    SoundSource.BLOCKS,
                    0.12F,
                    0.17F
            );

            if (boiler.progress >= boiler.maxProgress) {
                ItemStack input0 = boiler.inventory
                        .getStackInSlot(SLOT_INGREDIENT_1)
                        .copy();
                ItemStack input1 = boiler.inventory
                        .getStackInSlot(SLOT_INGREDIENT_2)
                        .copy();
                ItemStack input2 = boiler.inventory
                        .getStackInSlot(SLOT_INGREDIENT_3)
                        .copy();

                ItemStack essenceBottle = new ItemStack(Items.GLASS_BOTTLE);
                /*
                essenceBottle.set(
                        ModDataComponentTypes.ESSENCE_BOTTLE.get(),
                        new EssenceBottleData(input0, input1, input2)
                );
                 */

                if (!boiler.canOutput(essenceBottle)) {
                    boiler.progress = boiler.maxProgress;
                    boiler.setChanged();
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                    return;
                }

                boiler.inventory.extractItem(SLOT_INGREDIENT_1, 1, false);
                boiler.inventory.extractItem(SLOT_INGREDIENT_2, 1, false);
                boiler.inventory.extractItem(SLOT_INGREDIENT_3, 1, false);
                boiler.inventory.extractItem(SLOT_CONTAINER, 1, false);
                boiler.drainFluidTank(1000);
                boiler.popOutItem(essenceBottle);
                boiler.progress = 0;
            }
        } else if (boiler.hasRecipe()) {
            boiler.progress++;

            if (boiler.progress >= boiler.maxProgress) {
                if (!boiler.craftRecipe()) {
                    boiler.progress = boiler.maxProgress;
                }
            }
        } else {
            if (boiler.progress > 0) {
                boiler.progress = Math.max(0, boiler.progress - CRAFTING_REWIND_SPEED);
            }

            if (boiler.progress == 0) {
                boiler.setCraftingOutputFluid(EssenceBoilerFluid.EMPTY);
                boiler.setCraftingIngredientMask(0);
            }
        }

        boiler.setChanged();
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    private void pickUpDroppedItems(Level level, BlockPos pos) {
        AABB pickupArea = new AABB(
                pos.getX() + 0.1D,
                pos.getY() + 0.45D,
                pos.getZ() + 0.1D,
                pos.getX() + 0.9D,
                pos.getY() + 1.25D,
                pos.getZ() + 0.9D
        );

        for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, pickupArea)) {
            ItemStack droppedStack = itemEntity.getItem();
            int slot = findDropInputSlot(droppedStack);
            if (slot < 0) {
                continue;
            }

            ItemStack remainder = inventory.insertItem(slot, droppedStack, false);
            if (remainder.isEmpty()) {
                itemEntity.discard();
            } else if (remainder.getCount() != droppedStack.getCount()) {
                itemEntity.setItem(remainder);
            } else {
                continue;
            }

            level.playSound(
                    null,
                    pos,
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.BLOCKS,
                    0.7F,
                    1.2F
            );
        }
    }

    private int findDropInputSlot(ItemStack stack) {
        if (stack.isEmpty()) {
            return -1;
        }

        if (stack.is(Items.GLASS_BOTTLE)) {
            return inventory.getStackInSlot(SLOT_CONTAINER).isEmpty()
                    ? SLOT_CONTAINER
                    : -1;
        }

        for (int slot = SLOT_INGREDIENT_1; slot <= SLOT_INGREDIENT_3; slot++) {
            if (inventory.getStackInSlot(slot).isEmpty()) {
                return slot;
            }
        }

        return -1;
    }

    private boolean tryCraftAmulet(Level level, BlockPos pos, BlockState state) {
        int essenceBottleSlot = -1;
        int ghastTearSlot = -1;
        int emptyAmuletSlot = -1;

        for (int slot : INPUT_SLOTS) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }

            if (essenceBottleSlot == -1 && stack.is(Items.GLASS_BOTTLE)) {
                essenceBottleSlot = slot;
            } else if (ghastTearSlot == -1 && stack.is(Items.GHAST_TEAR)) {
                ghastTearSlot = slot;
            } else if (emptyAmuletSlot == -1 && stack.is(Items.GLASS_BOTTLE)) {
                emptyAmuletSlot = slot;
            }
        }

        if (essenceBottleSlot == -1 || ghastTearSlot == -1 || emptyAmuletSlot == -1) {
            return false;
        }

        ItemStack essenceBottleStack = inventory.getStackInSlot(essenceBottleSlot).copy();
        ItemStack amulet = new ItemStack(Items.GLASS_BOTTLE);

        /*
        EssenceBottleData data = essenceBottleStack.get(ModDataComponentTypes.ESSENCE_BOTTLE.get());
        if (data != null) {
            amulet.set(ModDataComponentTypes.ESSENCE_BOTTLE.get(), data);
        }
         */

        if (!canOutput(amulet)) {
            return true;
        }

        inventory.extractItem(essenceBottleSlot, 1, false);
        inventory.extractItem(ghastTearSlot, 1, false);
        inventory.extractItem(emptyAmuletSlot, 1, false);

        popOutItem(amulet);

        progress = 0;
        setChanged();
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        return true;
    }

    private boolean canOutput(ItemStack output) {
        ItemStack currentOutput = inventory.getStackInSlot(SLOT_OUTPUT);

        if (currentOutput.isEmpty()) {
            return true;
        }

        if (!ItemStack.isSameItemSameComponents(currentOutput, output)) {
            return false;
        }

        return currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize();
    }

    private void popOutItem(ItemStack stack) {
        if (level == null || level.isClientSide()) {
            return;
        }

        ItemEntity itemEntity = new ItemEntity(
                level,
                worldPosition.getX() + 0.5D,
                worldPosition.getY() + 1.1D,
                worldPosition.getZ() + 0.5D,
                stack.copy()
        );

        itemEntity.setDeltaMovement(
                (level.getRandom().nextDouble() - 0.5D) * 0.1D,
                0.2D,
                (level.getRandom().nextDouble() - 0.5D) * 0.1D
        );

        level.playSound(
                null,
                worldPosition,
                SoundEvents.ITEM_PICKUP,
                SoundSource.BLOCKS,
                0.3F,
                1.0F
        );

        level.addFreshEntity(itemEntity);
    }

    public SimpleItemHandler getInputItems() {
        return inventory;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return direction == Direction.DOWN ? OUTPUT_SLOTS : INPUT_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(
            int slot,
            ItemStack stack,
            @Nullable Direction direction
    ) {
        if (slot == SLOT_OUTPUT || direction == Direction.DOWN) {
            return false;
        }

        if (slot == SLOT_CONTAINER) {
            return inventory.getStackInSlot(slot).isEmpty()
                    && (stack.is(Items.GLASS_BOTTLE)
                    || stack.is(Items.GLASS_BOTTLE));
        }

        return slot >= SLOT_INGREDIENT_1
                && slot <= SLOT_INGREDIENT_3
                && inventory.getStackInSlot(slot).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && slot == SLOT_OUTPUT;
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty(); ///TO-DO fix
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
        return inventory.extractItem(slot, inventory.getStackInSlot(slot).getCount(), false);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        double maxDistance = 64.0D;
        return player.distanceToSqr(
                worldPosition.getX() + 0.5D,
                worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D
        ) <= maxDistance;
    }

    public void clearContents() {
        clearContent();
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

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("progress", progress);
        output.putInt("maxProgress", maxProgress);
        output.putInt("CraftingIngredientMask", craftingIngredientMask);
        inventory.save(output);

        if (!fluidTank.isEmpty()) {
            Identifier id = BuiltInRegistries.FLUID.getKey(fluidTank.fluid());
            if (id != null) {
                output.putString("Fluid", id.toString());
                output.putInt("FluidAmount", fluidTank.amount());

                if (fluidTank.potionContents() != null
                        && fluidTank.potionContents() != PotionContents.EMPTY) {
                    output.store(
                            "PotionContents",
                            PotionContents.CODEC,
                            fluidTank.potionContents()
                    );
                }
            }
        }
        if (!craftingOutputFluid.isEmpty()) {
            output.store(
                    "CraftOutputFluid",
                    EssenceBoilerFluid.CODEC.codec(),
                    craftingOutputFluid
            );
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        progress = input.getIntOr("progress", 0);
        maxProgress = input.getIntOr("maxProgress", 60);
        craftingIngredientMask = input.getIntOr("CraftingIngredientMask", 0);
        inventory.load(input);
        fluidTank = readFluid(input);
        craftingOutputFluid = input
                .read("CraftOutputFluid", EssenceBoilerFluid.CODEC.codec())
                .orElse(EssenceBoilerFluid.EMPTY);
    }

    private static EssenceBoilerFluid readFluid(ValueInput input) {
        Optional<String> fluidId = input.getString("Fluid");
        if (fluidId.isEmpty()) {
            return EssenceBoilerFluid.EMPTY;
        }

        try {
            Fluid fluid = BuiltInRegistries.FLUID.getValue(Identifier.parse(fluidId.get()));
            if (fluid == null) {
                return EssenceBoilerFluid.EMPTY;
            }

            int amount = Math.max(0, input.getIntOr("FluidAmount", 0));
            if (amount <= 0) {
                return EssenceBoilerFluid.EMPTY;
            }

            PotionContents potionContents = input
                    .read("PotionContents", PotionContents.CODEC)
                    .orElse(PotionContents.EMPTY);

            return new EssenceBoilerFluid(
                    fluid,
                    Math.min(amount, DEFAULT_FLUID_CAPACITY),
                    potionContents
            );
        } catch (Exception ignored) {
            return EssenceBoilerFluid.EMPTY;
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public EssenceBoilerFluid getFluidTank() {
        return fluidTank;
    }

    public EssenceBoilerFluid getCraftingOutputFluid() {
        return craftingOutputFluid;
    }

    public boolean isCraftingIngredient(int slot) {
        return slot >= 0 && slot < SLOT_CONTAINER
                && (craftingIngredientMask & (1 << slot)) != 0;
    }

    public int getCraftingIngredientMask() {
        return craftingIngredientMask;
    }

    private void setCraftingIngredientMask(int mask) {
        if (craftingIngredientMask != mask) {
            craftingIngredientMask = mask;
            setChanged();
        }
    }

    private void setCraftingOutputFluid(EssenceBoilerFluid fluid) {
        EssenceBoilerFluid output = fluid == null
                ? EssenceBoilerFluid.EMPTY
                : fluid;
        if (!craftingOutputFluid.equals(output)) {
            craftingOutputFluid = output;
            setChanged();
        }
    }

    public int getFluidTankAmount() {
        return fluidTank.amount();
    }

    public int getFluidTankCapacity() {
        return DEFAULT_FLUID_CAPACITY;
    }

    public int fillFluidTank(EssenceBoilerFluid incoming) {
        if (incoming == null || incoming.isEmpty()) {
            return 0;
        }

        if (fluidTank.isEmpty()) {
            int accepted = Math.min(incoming.amount(), getFluidTankCapacity());
            if (accepted <= 0) {
                return 0;
            }

            fluidTank = incoming.withAmount(accepted);
            setChanged();
            return accepted;
        }

        if (!fluidTank.isSame(incoming)) {
            return 0;
        }

        int accepted = Math.min(
                incoming.amount(),
                getFluidTankCapacity() - fluidTank.amount()
        );

        if (accepted > 0) {
            fluidTank = fluidTank.withAmount(fluidTank.amount() + accepted);
            setChanged();
        }

        return accepted;
    }

    public int fillPotionFluidTank(EssenceBoilerFluid incoming) {
        if (incoming == null
                || incoming.isEmpty()
                || incoming.fluid() != EssenceBoilerPotionFluid.fluid()) {
            return 0;
        }

        if (fluidTank.isEmpty()) {
            return fillFluidTank(incoming);
        }

        if (!fluidTank.isSame(incoming.fluid())) {
            return 0;
        }

        int accepted = Math.min(
                incoming.amount(),
                getFluidTankCapacity() - fluidTank.amount()
        );
        if (accepted <= 0) {
            return 0;
        }

        var balancedContents = EssenceBoilerPotionFluid.getBalancedContents(
                fluidTank.potionContents(),
                incoming.potionContents()
        );
        if (balancedContents.isEmpty()) {
            return 0;
        }

        fluidTank = new EssenceBoilerFluid(
                fluidTank.fluid(),
                fluidTank.amount() + accepted,
                balancedContents.get()
        );
        setChanged();
        return accepted;
    }

    public EssenceBoilerFluid drainFluidTank(int amount) {
        if (amount <= 0 || fluidTank.isEmpty()) {
            return EssenceBoilerFluid.EMPTY;
        }

        int drained = Math.min(amount, fluidTank.amount());
        EssenceBoilerFluid result = fluidTank.withAmount(drained);
        int remaining = fluidTank.amount() - drained;

        fluidTank = remaining <= 0
                ? EssenceBoilerFluid.EMPTY
                : fluidTank.withAmount(remaining);

        setChanged();
        return result;
    }

    public EssenceBoilerFluid getTank() {
        return fluidTank;
    }

    public enum WobbleStyle {
        POSITIVE(7),
        NEGATIVE(10);

        public final int duration;

        WobbleStyle(int duration) {
            this.duration = duration;
        }
    }

    public void wobble(WobbleStyle style) {
        if (level != null && !level.isClientSide()) {
            level.blockEvent(
                    getBlockPos(),
                    getBlockState().getBlock(),
                    EVENT_WOBBLE,
                    style.ordinal()
            );
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == EVENT_WOBBLE
                && type >= 0
                && type < WobbleStyle.values().length) {
            if (level != null) {
                wobbleStartedAtTick = level.getGameTime();
            }

            lastWobbleStyle = WobbleStyle.values()[type];
            return true;
        }

        return super.triggerEvent(id, type);
    }

    private FluidRecipeInput getRecipeInput() {
        SimpleContainer inv = new SimpleContainer(
                inventory.getStackInSlot(SLOT_INGREDIENT_1),
                inventory.getStackInSlot(SLOT_INGREDIENT_2),
                inventory.getStackInSlot(SLOT_INGREDIENT_3)
        );

        return new FluidRecipeInput(inv, fluidTank);
    }

    private Optional<RecipeHolder<EssenceBoilerRecipe>> getCurrentRecipe() {
        if (level == null) {
            return Optional.empty();
        }

        return Objects.requireNonNull(level.getServer()).getRecipeManager().getRecipeFor(
                        ModRecipes.ESSENCE_BOILER.type().get(), getRecipeInput(), level);
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<EssenceBoilerRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            setCraftingIngredientMask(0);
            return false;
        }

        EssenceBoilerRecipe value = recipe.get().value();
        if (!canAcceptFluidOutput(value)) {
            setCraftingOutputFluid(EssenceBoilerFluid.EMPTY);
            setCraftingIngredientMask(0);
            return false;
        }

        int[] matchedSlots = value.getMatchedIngredientSlots(getRecipeInput());
        if (matchedSlots == null) {
            setCraftingIngredientMask(0);
            return false;
        }
        int ingredientMask = 0;
        for (int slot : matchedSlots) {
            ingredientMask |= 1 << slot;
        }
        setCraftingIngredientMask(ingredientMask);
        setCraftingOutputFluid(getFluidOutput(value).orElse(EssenceBoilerFluid.EMPTY));
        maxProgress = value.recipeTime();
        return true;
    }

    private boolean canAcceptFluidOutput(EssenceBoilerRecipe recipe) {
        int consumedAmount = recipe.inputFluid()
                .map(fluid -> recipe.preserveFluidAmount()
                        ? fluidTank.amount()
                        : fluid.amount())
                .orElse(0);
        int remainingAmount = fluidTank.amount() - consumedAmount;
        if (remainingAmount < 0) {
            return false;
        }

        if (recipe.outputFluid().isEmpty()) {
            return true;
        }

        EssenceBoilerFluid output = getFluidOutput(recipe).orElseThrow();
        if (remainingAmount > 0 && !fluidTank.isSame(output)) {
            return false;
        }

        return remainingAmount + output.amount() <= getFluidTankCapacity();
    }

    private Optional<EssenceBoilerFluid> getFluidOutput(EssenceBoilerRecipe recipe) {
        if (recipe.outputFluid().isEmpty()) {
            return Optional.empty();
        }

        EssenceBoilerFluid output = recipe.outputFluid().get();
        if (recipe.preserveFluidAmount() && !fluidTank.isEmpty()) {
            output = output.withAmount(fluidTank.amount());
        }
        return Optional.of(output);
    }

    public boolean craftRecipe() {
        Optional<RecipeHolder<EssenceBoilerRecipe>> recipeOpt = getCurrentRecipe();
        if (recipeOpt.isEmpty()) {
            return false;
        }

        EssenceBoilerRecipe recipe = recipeOpt.get().value();
        if (!canAcceptFluidOutput(recipe)) {
            return false;
        }

        int[] matchedSlots = recipe.getMatchedIngredientSlots(getRecipeInput());
        if (matchedSlots == null) {
            return false;
        }

        for (int slot : matchedSlots) {
            inventory.extractItem(slot, 1, false);
        }

        Optional<EssenceBoilerFluid> fluidOutput = getFluidOutput(recipe);
        recipe.inputFluid().ifPresent(fluid -> drainFluidTank(
                recipe.preserveFluidAmount() ? fluidTank.amount() : fluid.amount()
        ));

        if (fluidOutput.isPresent()) {
            EssenceBoilerFluid output = fluidOutput.get();
            if (fillFluidTank(output) != output.amount()) {
                return false;
            }
        }

        for (var result : recipe.results()) {
            popOutItem(result.create());
        }

        setCraftingOutputFluid(EssenceBoilerFluid.EMPTY);
        progress = 0;
        setChanged();
        return true;
    }
}