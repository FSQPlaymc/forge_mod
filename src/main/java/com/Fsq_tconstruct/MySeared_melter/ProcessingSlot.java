package com.Fsq_tconstruct.MySeared_melter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ProcessingSlot {

    private static final int MAX_STACK = 64;

    private ItemStack input = ItemStack.EMPTY;

    public ItemStack getStack() {
        return input;
    }

    public boolean canInsert(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!input.isEmpty() && !ItemStack.isSameItemSameTags(input, stack)) return false;
        return input.getCount() < MAX_STACK;
    }

    public int insertInput(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return 0;
        if (!input.isEmpty() && !ItemStack.isSameItemSameTags(input, stack)) return 0;

        int maxInsert = Math.min(stack.getCount(), MAX_STACK - input.getCount());
        if (maxInsert <= 0) return 0;

        if (!simulate) {
            if (input.isEmpty()) {
                input = stack.copy();
                input.setCount(maxInsert);
            } else {
                input.grow(maxInsert);
            }
        }
        return maxInsert;
    }

    public void shrinkInput(int count) {
        input.shrink(count);
        if (input.isEmpty()) input = ItemStack.EMPTY;
    }

    public void setStack(ItemStack stack) {
        input = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    public CompoundTag writeToTag() {
        CompoundTag tag = new CompoundTag();
        if (!input.isEmpty()) {
            CompoundTag inputTag = new CompoundTag();
            input.save(inputTag);
            tag.put("input", inputTag);
        }
        return tag;
    }

    public void readFromTag(CompoundTag tag) {
        input = tag.contains("input") ? ItemStack.of(tag.getCompound("input")) : ItemStack.EMPTY;
    }
}
