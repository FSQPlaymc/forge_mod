package com.Fsq_tconstruct.Jei;

import com.Fsq_tconstruct.MySeared_melter.CustomMelterRecipe;
import com.Fsq_tconstruct.MySeared_melter.ModRegistries;
import com.Fsq_tconstruct.MySeared_melter.client.CustomMelterScreen;
import com.Fsq_tconstruct.fsq_tconstruct;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class CustomMelterJEIPlugin implements IModPlugin {

    public static final RecipeType<CustomMelterRecipe> MELTING_RECIPE_TYPE =
            RecipeType.create(fsq_tconstruct.MODID, "custom_melter", CustomMelterRecipe.class);

    private static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new CustomMelterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            RecipeManager recipeManager = mc.level.getRecipeManager();
            List<CustomMelterRecipe> recipes = recipeManager.getAllRecipesFor(
                    ModRegistries.CUSTOM_MELTER_RECIPE_TYPE.get());
            registration.addRecipes(MELTING_RECIPE_TYPE, recipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModRegistries.CUSTOM_MELTER_BLOCK.get()),
                MELTING_RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                CustomMelterScreen.class,
                58, 16, 50, 20,
                MELTING_RECIPE_TYPE);
    }
}
