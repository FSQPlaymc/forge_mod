package common;

import net.minecraftforge.fml.common.Mod;
import scala.tools.nsc.transform.SpecializeTypes;
import slimeknights.tconstruct.library.traits.AbstractTrait;
//@Mod.EventBusSubscriber()
//Mod.EventBusSubscriber是一个注解，用于在Minecraft Forge mod中标记一个类，以便它可以注册到Forge的事件总线上
//当一个类使用@Mod.EventBusSubscriber注解时，它表示这个类是一个事件订阅者，可以监听和处理特定类型的事件。
//被标记的类中可以添加用于处理事件的方法，并使用事件注解@SubscribeEvent来标记这些方法。
//也就是说如果你想要使用@SubscribeEvent注册一个事件，那么你必须在类头部加上这个注解，当然如果没有订阅事件就别加它
public class CS extends AbstractTrait {
    public CS(){
        super("trait_corpse_mountain",0xff0000);

    }
}


