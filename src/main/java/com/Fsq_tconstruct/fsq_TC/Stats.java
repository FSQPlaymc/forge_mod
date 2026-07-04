package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fsq_TC.other.Fsq_StatlessMaterialStats;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.tools.data.material.MaterialStatsDataProvider;
import slimeknights.tconstruct.tools.stats.*;

import static net.minecraft.world.item.Tiers.*;

public class Stats extends AbstractMaterialStatsDataProvider {
    public Stats(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }
    public static final HeadMaterialStats a_head = new HeadMaterialStats(720, 7f, DIAMOND, 9.5f);
    @Override
    public String getName() {
        return "Tinker's Construct Material Stats";
    }
    @Override
    protected void addMaterialStats(){
        System.out.print("load-stats");
        addMeleeHarvest();
        addRanged();
        //addAmmo();
        addArmor();
        //addSlimesuit();
        //addMisc();
        addMaterialStats(fsq_Materials.M_ESSENCE_SOUL, Fsq_StatlessMaterialStats.HUN_ZHU);//添加魂珠
    }
//@Override

    private  void addMeleeHarvest(){
        System.out.print("load-stats-head");
        addMaterialStats(fsq_Materials.zxc,
                a_head,
                HandleMaterialStats.multipliers().miningSpeed(1.10f).attackSpeed(1.05f).build(), // 手柄
                StatlessMaterialStats.BINDING,// 绑定结
                Fsq_StatlessMaterialStats.HUN_ZHU
                ); // 魂珠部件属性，使该材料可制作魂珠
        addMaterialStats(fsq_Materials.Amethyst_shardDiamond,
                new HeadMaterialStats(620, 5f, DIAMOND, 7.5f),
                HandleMaterialStats.multipliers().miningSpeed(0.94f).attackSpeed(1.15f).build(), // 手柄
                StatlessMaterialStats.BINDING
                //Fsq_StatlessMaterialStats.HUN_ZHU // 魂珠部件属性，使该材料可制作魂珠
        );
    }
    private void addRanged(){
        addMaterialStats(fsq_Materials.zxc,
                new LimbMaterialStats(200, 0.05f, 0.1f, 0.05f),      // 弓臂
                new GripMaterialStats(0f, 0.05f, 1.5f));              // 握把
    }
    private void addArmor(){
        addArmorShieldStats(fsq_Materials.zxc,
                PlatingMaterialStats.builder().durabilityFactor(28).armor(2, 5, 6, 2).toughness(2),
                StatlessMaterialStats.MAILLE);
    }
}
