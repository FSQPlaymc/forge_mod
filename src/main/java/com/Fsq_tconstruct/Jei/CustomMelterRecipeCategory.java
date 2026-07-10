package com.Fsq_tconstruct.Jei;

import com.Fsq_tconstruct.MySeared_melter.CustomMelterRecipe;
import com.Fsq_tconstruct.MySeared_melter.ModRegistries;
import com.Fsq_tconstruct.fsq_tconstruct;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomMelterRecipeCategory implements IRecipeCategory<CustomMelterRecipe> {

    private static final int WIDTH = 116;
    private static final int HEIGHT = 64;

    private final IDrawable background;
    private final IDrawable icon;

    private static final int[][] INPUT_POSITIONS = {
            {20, 15}, {38, 15}, {20, 33}, {38, 33}
    };
    private static final int OUTPUT_X = 95;
    private static final int OUTPUT_Y = 24;

    public CustomMelterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(ModRegistries.CUSTOM_MELTER_BLOCK.get()));
    }

    @Override
    public RecipeType<CustomMelterRecipe> getRecipeType() {
        return CustomMelterJEIPlugin.MELTING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.fsq_tconstruct.custom_melter");
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CustomMelterRecipe recipe, IFocusGroup focuses) {
        List<CustomMelterRecipe.InputEntry> inputs = recipe.getInputs();

        for (int i = 0; i < inputs.size() && i < INPUT_POSITIONS.length; i++) {
            CustomMelterRecipe.InputEntry entry = inputs.get(i);
            List<ItemStack> displayStacks = Stream.of(entry.ingredient().getItems())
                    .map(s -> {
                        ItemStack copy = s.copy();
                        copy.setCount(entry.count());
                        return copy;
                    })
                    .collect(Collectors.toList());

            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_POSITIONS[i][0], INPUT_POSITIONS[i][1])
                    .addItemStacks(displayStacks);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(CustomMelterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();

        Component tempText = Component.translatable("jei.fsq_tconstruct.custom_melter.temperature",
                recipe.getTemperature());
        guiGraphics.drawString(mc.font, tempText, 5, 44, 0xFF808080, false);

        Component timeText = Component.translatable("jei.fsq_tconstruct.custom_melter.time",
                recipe.getTime() / 20.0f);
        guiGraphics.drawString(mc.font, timeText, 5, 54, 0xFF808080, false);
    }
}
