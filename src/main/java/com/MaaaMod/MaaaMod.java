package com.MaaaMod;

import com.TC.TCcs;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.TinkerRegistry;

@Mod(modid = MaaaMod.MODID, name = MaaaMod.NAME, version = MaaaMod.VERSION)
public class MaaaMod
{
    public static final String MODID = "maaamod";
    public static final String NAME = "Exa Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        TinkerRegistry.addTrait(new TCcs());
        //在这里按照这个格式为游戏注册词条
        //TraitCorpseMountain()可以换成其他你任意想要注册词条的构建方法，如果构建方法存在参数，那你还需要填上参数
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
