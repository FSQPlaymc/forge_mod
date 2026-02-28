package com.MaaaMod;

import com.TC.TC2;
import com.TC.TCcs;
import com.items.GG_Items;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

import static com.MaaaMod.MaaaMod.getLogger;
@Mod.EventBusSubscriber(modid = MaaaMod.MODID)
public class zhuce  {
    private static Logger logger=getLogger();


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        logger = event.getModLog();

    }
}
