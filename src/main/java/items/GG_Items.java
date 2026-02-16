package items;
import items.RegistryEventItem ;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class GG_Items {
    public static RegistryEventItem a;
    public static void addItem(RegistryEvent.Register<Item> add){
        a=new RegistryEventItem("aaa","aaa");
        {
            add.getRegistry().register(a);
        }
    }
}
