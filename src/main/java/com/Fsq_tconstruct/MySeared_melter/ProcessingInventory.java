package com.Fsq_tconstruct.MySeared_melter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ProcessingInventory implements IItemHandlerModifiable, ContainerData {

    private static final String TAG_SLOTS = "slots";
    private static final String TAG_OUTPUT = "Output";
    private static final String TAG_CURRENT_TIME = "CurrentTime";
    private static final String TAG_REQUIRED_TIME = "RequiredTime";
    private static final String TAG_REQUIRED_TEMP = "RequiredTemp";
    private static final String TAG_RECIPE_OUTPUT = "RecipeOutput";

    private final ProcessingSlot[] slots;
    private final Supplier<Level> levelSupplier;

    private ItemStack outputStack = ItemStack.EMPTY;
    private CustomMelterRecipe currentRecipe;
    private int currentTime;
    private int requiredTime;
    private int requiredTemp;
    private ItemStack recipeOutput = ItemStack.EMPTY;

    public ProcessingInventory(int size, Supplier<Level> levelSupplier) {
        this.levelSupplier = levelSupplier;
        this.slots = new ProcessingSlot[size];
        for (int i = 0; i < size; i++) {
            slots[i] = new ProcessingSlot();
        }
    }

    public ProcessingSlot getModule(int slot) {
        return slots[slot];
    }

    public int getInputSlots() {
        return slots.length;
    }

    private Level getLevel() {
        return levelSupplier.get();
    }

    private void tryFindRecipe() {
        Level level = getLevel();
        if (level == null) return;

        SimpleContainer container = new SimpleContainer(slots.length);
        for (int i = 0; i < slots.length; i++) {
            container.setItem(i, slots[i].getStack());
        }

        var holder = level.getRecipeManager()
                .getRecipeFor(ModRegistries.CUSTOM_MELTER_RECIPE_TYPE.get(), container, level);
        if (holder.isPresent()) {
            currentRecipe = holder.get();
            requiredTime = currentRecipe.getTime();
            requiredTemp = currentRecipe.getTemperature();
            recipeOutput = currentRecipe.getOutput();
            currentTime = 0;
        } else {
            currentRecipe = null;
            requiredTime = 0;
            requiredTemp = 0;
            recipeOutput = ItemStack.EMPTY;
            currentTime = 0;
        }
    }

    public boolean canHeat(int temperature) {
        if (currentRecipe == null) {
            tryFindRecipe();
        }
        return currentRecipe != null && currentTime >= 0 && temperature >= requiredTemp;
    }

    public void heatItems(int temperature, int rate) {
        if (currentRecipe == null) {
            tryFindRecipe();
            if (currentRecipe == null) return;
        }

        if (temperature < requiredTemp) return;

        if (!hasSpaceForOutput()) {
            if (currentTime > 0) {
                currentTime = -1;
            }
            return;
        }

        currentTime += rate;
        if (currentTime >= requiredTime) {
            completeRecipe();
        }
    }

    private boolean hasSpaceForOutput() {
        if (recipeOutput.isEmpty()) return true;
        if (outputStack.isEmpty()) return true;
        return ItemStack.isSameItemSameTags(outputStack, recipeOutput)
                && outputStack.getCount() + recipeOutput.getCount() <= 64;
    }

    private void completeRecipe() {
        if (currentRecipe == null || recipeOutput.isEmpty()) return;

        SimpleContainer container = new SimpleContainer(slots.length);
        for (int i = 0; i < slots.length; i++) {
            container.setItem(i, slots[i].getStack());
        }

        if (!currentRecipe.matches(container, getLevel())) {
            currentRecipe = null;
            currentTime = 0;
            requiredTime = 0;
            requiredTemp = 0;
            recipeOutput = ItemStack.EMPTY;
            return;
        }

        consumeInputs();
        addOutputToSlot();

        currentTime = 0;
        tryFindRecipe();
    }

    private void consumeInputs() {
        if (currentRecipe == null) return;
        List<CustomMelterRecipe.InputEntry> inputs = currentRecipe.getInputs();
        List<Integer> availableSlots = new ArrayList<>();
        for (int i = 0; i < slots.length; i++) {
            if (!slots[i].getStack().isEmpty()) {
                availableSlots.add(i);
            }
        }
        for (CustomMelterRecipe.InputEntry entry : inputs) {
            for (int j = 0; j < availableSlots.size(); j++) {
                int si = availableSlots.get(j);
                ItemStack stack = slots[si].getStack();
                if (entry.ingredient().test(stack) && stack.getCount() >= entry.count()) {
                    slots[si].shrinkInput(entry.count());
                    availableSlots.remove(j);
                    break;
                }
            }
        }
    }

    private void addOutputToSlot() {
        if (recipeOutput.isEmpty()) return;
        if (outputStack.isEmpty()) {
            outputStack = recipeOutput.copy();
        } else {
            outputStack.grow(recipeOutput.getCount());
        }
    }

    public void coolItems() {
        if (currentTime > 0 && requiredTime > 0) {
            currentTime -= 5;
        }
    }

    public void trackInts(Consumer<ContainerData> consumer) {
        consumer.accept(this);
    }

    @Override
    public int getSlots() {
        return slots.length + 1;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot < slots.length;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= getSlots()) return ItemStack.EMPTY;
        if (slot == slots.length) return outputStack;
        return slots[slot].getStack();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getSlots()) return;
        if (slot == slots.length) {
            outputStack = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
            return;
        }
        slots[slot].setStack(stack);
        tryFindRecipe();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (slot < 0 || slot >= slots.length) return stack;

        int inserted = slots[slot].insertInput(stack, simulate);
        if (!simulate && inserted > 0) {
            tryFindRecipe();
        }
        return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - inserted);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) return ItemStack.EMPTY;
        if (slot < 0 || slot >= getSlots()) return ItemStack.EMPTY;

        if (slot == slots.length) {
            if (outputStack.isEmpty()) return ItemStack.EMPTY;
            int extract = Math.min(amount, outputStack.getCount());
            if (simulate) {
                ItemStack result = outputStack.copy();
                result.setCount(extract);
                return result;
            }
            ItemStack taken = outputStack.split(extract);
            if (outputStack.isEmpty()) outputStack = ItemStack.EMPTY;
            return taken;
        }

        ItemStack stack = slots[slot].getStack();
        if (stack.isEmpty()) return ItemStack.EMPTY;
        int extract = Math.min(amount, stack.getCount());
        if (simulate) {
            ItemStack result = stack.copy();
            result.setCount(extract);
            return result;
        }
        ItemStack taken = stack.split(extract);
        if (stack.isEmpty()) slots[slot].setStack(ItemStack.EMPTY);
        return taken;
    }

    public CompoundTag writeToTag() {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        for (ProcessingSlot slot : slots) {
            list.add(slot.writeToTag());
        }
        nbt.put(TAG_SLOTS, list);
        if (!outputStack.isEmpty()) {
            CompoundTag outTag = new CompoundTag();
            outputStack.save(outTag);
            nbt.put(TAG_OUTPUT, outTag);
        }
        nbt.putInt(TAG_CURRENT_TIME, currentTime);
        nbt.putInt(TAG_REQUIRED_TIME, requiredTime);
        nbt.putInt(TAG_REQUIRED_TEMP, requiredTemp);
        if (!recipeOutput.isEmpty()) {
            CompoundTag roTag = new CompoundTag();
            recipeOutput.save(roTag);
            nbt.put(TAG_RECIPE_OUTPUT, roTag);
        }
        return nbt;
    }

    public void readFromTag(CompoundTag nbt) {
        ListTag list = nbt.getList(TAG_SLOTS, Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(list.size(), slots.length); i++) {
            slots[i].readFromTag(list.getCompound(i));
        }
        outputStack = nbt.contains(TAG_OUTPUT) ? ItemStack.of(nbt.getCompound(TAG_OUTPUT)) : ItemStack.EMPTY;
        currentTime = nbt.getInt(TAG_CURRENT_TIME);
        requiredTime = nbt.getInt(TAG_REQUIRED_TIME);
        requiredTemp = nbt.getInt(TAG_REQUIRED_TEMP);
        recipeOutput = nbt.contains(TAG_RECIPE_OUTPUT) ? ItemStack.of(nbt.getCompound(TAG_RECIPE_OUTPUT)) : ItemStack.EMPTY;
        if (requiredTime > 0 && getLevel() != null) {
            tryFindRecipe();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> currentTime;
            case 1 -> requiredTime;
            case 2 -> requiredTemp;
            default -> 0;
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> currentTime = value;
            case 1 -> requiredTime = value;
            case 2 -> requiredTemp = value;
        }
    }
}
