package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.data.PackOutput;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)

public class fsq_Materials extends AbstractMaterialDataProvider {
    //public static final MaterialId asda=createMaterial("asda");
    public static final MaterialId zxc=id("zxc");
    public static final MaterialId Amethyst_shardDiamond=id("amethyst_diamond");//晶钻
    public static final MaterialId M_ESSENCE_SOUL=id("m_essence_soul");
    public static final MaterialId FIRE_CRYSTAL=id("fire_crystal");//火之精
    public static final MaterialId WITHER_BONE =id("wither_bone");//凋骨合金
    public static final MaterialId DRAGON_STEEL=id("dragon_steel");//龙钢

    public fsq_Materials(PackOutput packOutput) {
        super(packOutput);
    }
    @Override
    public @NotNull String getName() {
        return "asd";
    }

    public static MaterialId id(String name) {
        return new MaterialId(fsq_tconstruct.MODID, name);
    }
    //public static MaterialId createMaterial(String name){
    //    return new MaterialId(new ResourceLocation(fsq_tconstruct.MODID, name)); //*是你的模组主类名
    //}
    //public static final Material asdad = new Material(asda,3,0,false,false);
    //private static final HeadMaterialStats asd=new HeadMaterialStats(1250,2.5f, Tiers.NETHERITE,10.9f);
    //public static final ToolPartItem aaaa=new ToolPartItem(new Item.Properties(),asd.ID);
    @Override
    protected void addMaterials() {
        addMaterial(Amethyst_shardDiamond,3,ORDER_WEAPON,false);
        addMaterial(zxc, 3, ORDER_GENERAL, true);
        addMaterial(M_ESSENCE_SOUL,2,ORDER_WEAPON,true);
        addMaterial(FIRE_CRYSTAL,2,ORDER_WEAPON,true);
        addMaterial(WITHER_BONE,4,ORDER_GENERAL,false);
        addMaterial(DRAGON_STEEL,5,ORDER_GENERAL,false);
    }

}
