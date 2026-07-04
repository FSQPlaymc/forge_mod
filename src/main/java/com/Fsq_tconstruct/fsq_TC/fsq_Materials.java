package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fsq_tconstruct;
import com.Fsq_tconstruct.item.fsq_items;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.Material;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)

public class fsq_Materials extends AbstractMaterialDataProvider {
    //public static final MaterialId asda=createMaterial("asda");
    public static final MaterialId zxc=id("zxc");
    public static final MaterialId Amethyst_shardDiamond=id("amethyst_diamond");//晶钻
    public static final MaterialId M_ESSENCE_SOUL=id("m_essence_soul");

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
    //IModifiable
    //ToolPartItem
    @Override
    protected void addMaterials() {
        addMaterial(Amethyst_shardDiamond,3,ORDER_WEAPON,false);
        addMaterial(zxc, 3, ORDER_GENERAL, true);
        addMaterial(M_ESSENCE_SOUL,2,ORDER_WEAPON,false);
    }

}
