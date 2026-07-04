package com.Fsq_tconstruct.MySeared_melter;

import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistries {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, fsq_tconstruct.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, fsq_tconstruct.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, fsq_tconstruct.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, fsq_tconstruct.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, fsq_tconstruct.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, fsq_tconstruct.MODID);

    public static final RegistryObject<CustomMelterBlock> CUSTOM_MELTER_BLOCK = BLOCKS.register("custom_melter",
            () -> new CustomMelterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Item> CUSTOM_MELTER_ITEM = ITEMS.register("custom_melter",
            () -> new BlockItem(CUSTOM_MELTER_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<CustomMelterBlockEntity>> CUSTOM_MELTER_BE = BLOCK_ENTITIES.register("custom_melter",
            () -> BlockEntityType.Builder.of(CustomMelterBlockEntity::new, CUSTOM_MELTER_BLOCK.get()).build(null));

    public static final RegistryObject<MenuType<CustomMelterContainerMenu>> CUSTOM_MELTER_MENU = MENUS.register("custom_melter",
            () -> IForgeMenuType.create(CustomMelterContainerMenu::new));

    public static final RegistryObject<RecipeSerializer<CustomMelterRecipe>> CUSTOM_MELTER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("custom_melter",
            CustomMelterRecipeSerializer::new);

    public static final RegistryObject<RecipeType<CustomMelterRecipe>> CUSTOM_MELTER_RECIPE_TYPE = RECIPE_TYPES.register("custom_melter",
            () -> new RecipeType<CustomMelterRecipe>() {});

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENUS.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
    }
}
