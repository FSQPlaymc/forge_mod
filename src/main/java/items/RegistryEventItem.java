package items;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;

public class RegistryEventItem extends Item {
    public void Item(String outName, String RegistryName,CreativeTabs tab,int number){
        this.setRegistryName(RegistryName);
        this.setUnlocalizedName(outName);
        this.setCreativeTab(tab);
        this.setMaxStackSize(number);
    }
    public void Item(String outName, String RegistryName){
super().Item();
    }
}
