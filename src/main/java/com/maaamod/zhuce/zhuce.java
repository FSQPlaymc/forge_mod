package com.maaamod.zhuce;

import com.maaamod.maaamod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import static com.maaamod.maaamod.getLogger;
@Mod.EventBusSubscriber(modid = maaamod.MODID)
public class zhuce  {
    private static Logger logger=getLogger();


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        logger = event.getModLog();

    }
}
