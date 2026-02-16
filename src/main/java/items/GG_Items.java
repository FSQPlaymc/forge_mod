package items;
import com.gameforg.gameforge;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = gameforge.MODID)
public class GG_Items {
    public static  RegistryEventItem a;
    @SubscribeEvent//告知注册物品
    public static void addItem(RegistryEvent.Register<Item> add){
        a=new RegistryEventItem("aaa","aaa");
        {
            add.getRegistry().register(a);
        }
    }
}
