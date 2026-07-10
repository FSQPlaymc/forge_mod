package com.Fsq_tconstruct.item;

import com.Fsq_tconstruct.fluid.ModFluids;
import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class fsq_items {
    public static final DeferredRegister<Item> ITEMS=
            DeferredRegister.create(ForgeRegistries.ITEMS, fsq_tconstruct.MODID);
    public static  Item.Properties A ;
    public static final RegistryObject<Item> ONE_ITEM=ITEMS.register("asd",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> Amethyst_shardDiamond=ITEMS.register("amethyst_diamond",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORPSE_FAT, ESSENCE_SOUL,FIRE_CRYSTAL;
    public static final RegistryObject<BucketItem> MOLTEN_AMETHYST_DIAMOND_BUCKET = ITEMS.register("molten_amethyst_diamond_bucket",
            () -> new BucketItem(ModFluids.MOLTEN_AMETHYST_DIAMOND, new Item.Properties().stacksTo(1)));
    static {
        FIRE_CRYSTAL=ITEMS.register("tc_items/fire_crystal",()->new Item(new Item.Properties()));//火之精
        CORPSE_FAT=ITEMS.register("corpse_fat",()->new Item(new Item.Properties()));
        ESSENCE_SOUL =ITEMS.register("essence_soul",()->new Item(new Item.Properties()));//魂之精
    }
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
