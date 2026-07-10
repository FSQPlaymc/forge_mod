package com.Fsq_tconstruct.item;

import com.Fsq_tconstruct.MySeared_melter.ModRegistries;
import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Parameter;


public class createTAB {
    public static final DeferredRegister<CreativeModeTab> FSQ_TAB=
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, fsq_tconstruct.MODID);
    public static final RegistryObject<CreativeModeTab> TAB_REGISTRY_OBJECT=
            FSQ_TAB.register(fsq_tconstruct.MODID,()->CreativeModeTab.builder()
                    .icon(()->new ItemStack(fsq_items.ONE_ITEM.get()))
                    .title(Component.translatable("itemGroup.fsq_tconstruct_TAB"))
                    .displayItems((parameters, output) -> {
                        output.accept(fsq_items.ONE_ITEM.get());
                        output.accept(fsq_items.FIRE_CRYSTAL.get());
                        output.accept(fsq_items.ESSENCE_SOUL.get());
                        output.accept(fsq_items.Amethyst_shardDiamond.get());
                        output.accept(ModRegistries.CUSTOM_MELTER_ITEM.get());
                    })
                    .build());
    public static void register(IEventBus eventBus){
        FSQ_TAB.register(eventBus);
    }
}
