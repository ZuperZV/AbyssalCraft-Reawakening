package net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.helper;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SimpleItemHandler {

    public NonNullList<ItemStack> items;

    public SimpleItemHandler(int size) {
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public int getSlots() {
        return items.size();
    }

    public ItemStack getStackInSlot(int slot) {
        validate(slot);
        return items.get(slot);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        validate(slot);
        items.set(slot, stack);
        onContentsChanged(slot);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        validate(slot);

        ItemStack existing = items.get(slot);

        if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, stack)) {
            return stack;
        }

        int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());

        if (!existing.isEmpty()) {
            limit -= existing.getCount();
        }

        if (limit <= 0) return stack;

        int toInsert = Math.min(stack.getCount(), limit);

        if (!simulate) {
            if (existing.isEmpty()) {
                items.set(slot, stack.copyWithCount(toInsert));
            } else {
                existing.grow(toInsert);
            }
            onContentsChanged(slot);
        }

        return stack.copyWithCount(stack.getCount() - toInsert);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) return ItemStack.EMPTY;

        validate(slot);

        ItemStack existing = items.get(slot);

        if (existing.isEmpty()) return ItemStack.EMPTY;

        int extracted = Math.min(amount, existing.getCount());

        ItemStack result = existing.copyWithCount(extracted);

        if (!simulate) {
            if (existing.getCount() <= extracted) {
                items.set(slot, ItemStack.EMPTY);
            } else {
                existing.shrink(extracted);
            }
            onContentsChanged(slot);
        }

        return result;
    }

    public void save(ValueOutput output) {
        ValueOutput.TypedOutputList<ItemStack> list =
                output.list("items", ItemStack.CODEC);

        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }
    }

    public void load(ValueInput input) {
        for (int i = 0; i < items.size(); i++) {
            items.set(i, ItemStack.EMPTY);
        }

        input.list("items", ItemStack.CODEC).ifPresent(list -> {
            int i = 0;
            for (ItemStack stack : list) {
                if (i >= items.size()) break;
                items.set(i, stack);
                i++;
            }
        });
    }

    public int getSlotLimit(int slot) {
        return 64;
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    protected void validate(int slot) {
        if (slot < 0 || slot >= items.size()) {
            throw new RuntimeException("Slot " + slot + " out of range");
        }
    }

    private Runnable changeCallback;

    public void setChangeCallback(Runnable callback) {
        this.changeCallback = callback;
    }

    protected void onContentsChanged(int slot) {
        if (changeCallback != null) {
            changeCallback.run();
        }
    }
}