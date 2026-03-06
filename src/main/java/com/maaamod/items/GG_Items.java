package com.maaamod.items;
import com.maaamod.Maaamod;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Maaamod.MODID)
public class GG_Items {
    public static RegistryEventItem a,b;
    @SubscribeEvent//告知注册物品
    public static void addItem(RegistryEvent.Register<Item> add){
        a=new RegistryEventItem("aaa","aaa");
        b=new RegistryEventItem("bbb","bbb");
        {
            add.getRegistry().registerAll(a,b);
        }
    }
}
