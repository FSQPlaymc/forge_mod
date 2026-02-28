package com.MaaaMod;

import com.TC.TC2;
import com.TC.TCcs;
import com.items.GG_Items;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

import static com.MaaaMod.MaaaMod.getLogger;
@Mod.EventBusSubscriber(modid = MaaaMod.MODID)
public class zhuce {
    private static Logger logger=getLogger();
    public static Material TCa;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TCa=new Material("TCa",Integer.parseInt("7AFF83", 16)){{
            this.setCraftable(true);
            this.addCommonItems("aaa");
        this.addItem(GG_Items.a,1,114);
        this.addTrait(new TCcs());
        this.addTrait(new TC2("TC2",Integer.parseInt("7AFF83", 16)));//Integer.parseInt("0x88FF88", 16)把16进制转为rgb输入
        this.setRepresentativeItem(GG_Items.a);
    }};
        TinkerRegistry.addMaterial(TCa);
        TinkerRegistry.addMaterialStats(TCa, new BowMaterialStats(0.5F, 1.5F, 7.0F));
        TinkerRegistry.addMaterialStats(TCa, new HeadMaterialStats(204, 6.0F, 4.0F, 2), new IMaterialStats[]{new HandleMaterialStats(0.85F, 60), new ExtraMaterialStats(50)});

        // 1. 创建材料实例
        // 参数说明：材料名称(唯一标识符)，颜色(十六进制)
        //在这里按照这个格式为游戏注册词条
        //TraitCorpseMountain()可以换成其他你任意想要注册词条的构建方法，如果构建方法存在参数，那你还需要填上参数
        logger = event.getModLog();
    }
}
