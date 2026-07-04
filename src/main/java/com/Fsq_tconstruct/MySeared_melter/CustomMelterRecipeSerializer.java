package com.Fsq_tconstruct.MySeared_melter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CustomMelterRecipeSerializer implements RecipeSerializer<CustomMelterRecipe> {

    @Override
    public CustomMelterRecipe fromJson(ResourceLocation id, JsonObject json) {
        List<CustomMelterRecipe.InputEntry> inputs = new ArrayList<>();
        JsonArray inputsJson = GsonHelper.getAsJsonArray(json, "inputs");
        for (JsonElement element : inputsJson) {
            JsonObject entry = element.getAsJsonObject();
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(entry, "ingredient"));
            int count = GsonHelper.getAsInt(entry, "count", 1);
            inputs.add(new CustomMelterRecipe.InputEntry(ingredient, count));
        }
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
        int temperature = GsonHelper.getAsInt(json, "temperature");
        int time = GsonHelper.getAsInt(json, "time");
        return new CustomMelterRecipe(id, inputs, output, temperature, time);
    }

    @Override
    public CustomMelterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<CustomMelterRecipe.InputEntry> inputs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int count = buf.readVarInt();
            inputs.add(new CustomMelterRecipe.InputEntry(ingredient, count));
        }
        ItemStack output = buf.readItem();
        int temperature = buf.readVarInt();
        int time = buf.readVarInt();
        return new CustomMelterRecipe(id, inputs, output, temperature, time);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CustomMelterRecipe recipe) {
        List<CustomMelterRecipe.InputEntry> inputs = recipe.getInputs();
        buf.writeVarInt(inputs.size());
        for (CustomMelterRecipe.InputEntry entry : inputs) {
            entry.ingredient().toNetwork(buf);
            buf.writeVarInt(entry.count());
        }
        buf.writeItem(recipe.getOutput());
        buf.writeVarInt(recipe.getTemperature());
        buf.writeVarInt(recipe.getTime());
    }
}
