package com.Fsq_tconstruct;

import com.Fsq_tconstruct.MySeared_melter.ModRegistries;
import com.Fsq_tconstruct.MySeared_melter.client.CustomMelterScreen;
import com.Fsq_tconstruct.fluid.ModFluidBlocks;
import com.Fsq_tconstruct.fluid.ModFluids;
import com.Fsq_tconstruct.fsq_TC.*;
import com.Fsq_tconstruct.fsq_TC.Modifiers.RegisterModifiers;
import com.Fsq_tconstruct.fsq_TC.Tool.*;
import com.Fsq_tconstruct.item.createTAB;
import com.Fsq_tconstruct.item.fsq_items;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.data.material.GeneratorPartTextureJsonGenerator;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(fsq_tconstruct.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class fsq_tconstruct
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "fsq_tconstruct";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public fsq_tconstruct(FMLJavaModLoadingContext context)
    {

        IEventBus modEventBus = context.getModEventBus();
        RegisterModifiers.register(modEventBus);
        fsq_items.register(modEventBus);
        ModRegistries.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidBlocks.register(modEventBus);
        FsqToolParts.register(modEventBus);
        FsqTools.register(modEventBus);
        createTAB.register(modEventBus);
    }


    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();

        // 这三个必须按顺序：先 materials，后 stats/traits（依赖 validation）
        fsq_Materials materials = new fsq_Materials(output);
        gen.addProvider(event.includeServer(), materials);
        gen.addProvider(event.includeServer(), new Stats(output, materials));
        gen.addProvider(event.includeServer(), new MaterialTraitsDataProvider(output, materials));
        gen.addProvider(event.includeServer(), new relationItems(output));
        gen.addProvider(event.includeServer(), new FsqToolDefinitionDataProvider(output));
        gen.addProvider(event.includeServer(), new FsqStationSlotLayoutProvider(output));

        // client 端：渲染和材质
        MyMaterialSpriteProvider sprites = new MyMaterialSpriteProvider();
        gen.addProvider(event.includeClient(),
                new MyMaterialRenderInfoProvider(output, sprites, event.getExistingFileHelper()));//记得添加材料id在'MyMaterialRenderInfoProvider'

        // 部件纹理：Sprite 定义 + JSON 清单 + 纹理生成
        FsqPartSpriteProvider partSprites = new FsqPartSpriteProvider();
        gen.addProvider(event.includeClient(),
                new GeneratorPartTextureJsonGenerator(output, fsq_tconstruct.MODID, partSprites));
        gen.addProvider(event.includeClient(),
                new GeneratorPartTextureJsonGenerator(output, TConstruct.MOD_ID, partSprites));

        // 你的部件 × 你的材料
        gen.addProvider(event.includeClient(),
                new MaterialPartTextureGenerator(output, event.getExistingFileHelper(), partSprites, sprites));
        // 你的部件 × TiC 材料
        gen.addProvider(event.includeClient(),
                new MaterialPartTextureGenerator(output, event.getExistingFileHelper(), partSprites, new TinkerMaterialSpriteProvider()));
    }

    // 将自定义部件添加到 TiC 的 "Tool Parts" 创造物品栏中
    @SubscribeEvent
    public static void addToTinkerTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(TinkerToolParts.tabToolParts.getKey())) {
            FsqToolParts.CROSS_KNOT.get().addVariants(event::accept, "");
            FsqToolParts.SOUL_ORB.get().addVariants(event::accept,"");
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> MenuScreens.register(ModRegistries.CUSTOM_MELTER_MENU.get(), CustomMelterScreen::new));
        }
    }
}