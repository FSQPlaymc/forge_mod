package com.Fsq_tconstruct.fsq_TC.Tool;

import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

public class FsqTools {
    public static final DeferredRegister<Item> TOOS =
            DeferredRegister.create(ForgeRegistries.ITEMS, fsq_tconstruct.MODID);
    public static final ToolDefinition XI_YANG_JIAN =
            ToolDefinition.create(ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "western_sword"));
    public static final ToolDefinition SOUL_SPEAR =//魂戈
            ToolDefinition.create(ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID,"soul_spear"));
    public static final RegistryObject<ModifiableItem>HJ=TOOS.register("soul_spear",
            ()->new ModifiableItem(new Item.Properties(), SOUL_SPEAR));
    public static final RegistryObject<ModifiableItem> XJ=TOOS.register("western_sword",
            ()->new ModifiableItem(new Item.Properties(),XI_YANG_JIAN));
    public static void register(IEventBus bus) { TOOS.register(bus); }
}
