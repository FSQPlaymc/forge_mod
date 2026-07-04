package com.Fsq_tconstruct.MySeared_melter;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CustomMelterRecipe implements Recipe<Container> {

    public record InputEntry(Ingredient ingredient, int count) {}

    private final ResourceLocation id;
    private final List<InputEntry> inputs;
    private final ItemStack output;
    private final int temperature;
    private final int time;

    public CustomMelterRecipe(ResourceLocation id, List<InputEntry> inputs, ItemStack output, int temperature, int time) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
        this.temperature = temperature;
        this.time = time;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        List<Integer> availableSlots = new ArrayList<>();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (!inv.getItem(i).isEmpty()) {
                availableSlots.add(i);
            }
        }
        for (InputEntry entry : inputs) {
            boolean found = false;
            for (int j = 0; j < availableSlots.size(); j++) {
                int slot = availableSlots.get(j);
                ItemStack stack = inv.getItem(slot);
                if (entry.ingredient.test(stack) && stack.getCount() >= entry.count) {
                    availableSlots.remove(j);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess access) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistries.CUSTOM_MELTER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRegistries.CUSTOM_MELTER_RECIPE_TYPE.get();
    }

    public int getTemperature() {
        return temperature;
    }

    public int getTime() {
        return time;
    }

    public List<InputEntry> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output.copy();
    }
}
