package com;

import com.MaaaMod.MaaaMod;
import com.items.GG_Items;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = MaaaMod.MODID)

public class GG_CreativeTabs {
    public static final CreativeTabs GG_1=new CreativeTabs(CreativeTabs.getNextID(),"fsqwmc_1") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(Blocks.TNT));
        }
    };
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void modles_items(ModelRegistryEvent a){
        ModelLoader.setCustomModelResourceLocation(GG_Items.a,0,new ModelResourceLocation(GG_Items.a.getRegistryName(),"inventory"));
    }
}
