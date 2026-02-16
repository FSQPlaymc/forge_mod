package common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GG_CreativeTabs {
    public static final CreativeTabs GG_1=new CreativeTabs(CreativeTabs.getNextID(),"fsqwmc_1") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(Blocks.TNT));
        }
    };
}
