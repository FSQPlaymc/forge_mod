package com.maaamod;

import com.maaamod.TC.TC2;
import com.maaamod.TC.TCcs;
import com.maaamod.items.GG_Items;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
/*
方案二：手动删除残留文件
如果你不想重新编译整个项目，可以直接去操作系统的文件管理器里，手动删除那个残留的 .class 文件。它的路径通常在：
build/classes/java/main/com/maaamod/
找到对应的文件删掉即可。
 */
@Mod(
        modid = Maaamod.MODID, name = Maaamod.NAME, version = Maaamod.VERSION,
        dependencies="required-after:tconstruct"
)
public class Maaamod
{
    public static Material TCa;

    public static final String MODID = "maaamod";
    public static final String NAME = "Exa Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        TCa=new Material("TCa",Integer.parseInt("7AFF83", 16)){{
            this.setCraftable(true);
            this.addCommonItems("aaa");
            this.addItem(GG_Items.a,1,144);
            this.addTrait(new TCcs());
            this.addTrait(new TC2("TC2",Integer.parseInt("7AFF83", 16)));//Integer.parseInt("0x88FF88", 16)把16进制转为rgb输入
            this.setRepresentativeItem(GG_Items.a);
        }};
        TinkerRegistry.addMaterialStats(TCa, new BowMaterialStats(0.5F, 1.5F, 7.0F));
        TinkerRegistry.addMaterialStats(TCa, new HeadMaterialStats(204, 6.0F, 6.0F, 2), new IMaterialStats[]{new HandleMaterialStats(0.85F, 60), new ExtraMaterialStats(50)});
        // 3. 添加材料统计数据
        MaterialIntegration integration = new MaterialIntegration(TCa);
        // 1. 创建材料实例
        // 参数说明：材料名称(唯一标识符)，颜色(十六进制)
        //在这里按照这个格式为游戏注册词条
        //TraitCorpseMountain()可以换成其他你任意想要注册词条的构建方法，如果构建方法存在参数，那你还需要填上参数
        TinkerRegistry.addMaterial(TCa);
        TinkerRegistry.integrate(integration);
        //TinkerRegistry.addTrait(new TCcs());
        // 1. 创建材料实例
        // 参数说明：材料名称(唯一标识符)，颜色(十六进制)
        //Material myCustomMaterial = new Material("my_custom_material", 0x88FF88);
        //在这里按照这个格式为游戏注册词条
        //myCustomMaterial.addItem(GG_Items.a);
        //myCustomMaterial.addTrait(new TCcs());
        //TraitCorpseMountain()可以换成其他你任意想要注册词条的构建方法，如果构建方法存在参数，那你还需要填上参数
        logger = event.getModLog();
    }
    public static Logger getLogger() {
        return logger;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
