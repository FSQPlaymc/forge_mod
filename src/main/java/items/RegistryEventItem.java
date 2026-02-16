package items;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;

public class RegistryEventItem extends Item {
    public RegistryEventItem(String outName, String RegistryName, CreativeTabs tab, int number){
        this.setRegistryName(RegistryName);
        this.setUnlocalizedName(outName);
        this.setCreativeTab(tab);
        this.setMaxStackSize(number);
    }
    public  RegistryEventItem(String outName, String RegistryName){
        this(outName,RegistryName,CreativeTabs.MATERIALS,64);
    }
}
