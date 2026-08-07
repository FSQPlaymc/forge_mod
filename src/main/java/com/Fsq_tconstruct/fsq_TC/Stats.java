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
        addArmor();//盔甲
        //addSlimesuit();
        //addMisc();
        addMaterialStats(fsq_Materials.M_ESSENCE_SOUL, Fsq_StatlessMaterialStats.HUN_ZHU);//添加魂珠
        addMaterialStats(fsq_Materials.FIRE_CRYSTAL,Fsq_StatlessMaterialStats.HUN_ZHU);
    }
//@Override
/*
HandleMaterialStats.multipliers() 以乘法模式构建手柄属性：

miningSpeed(0.94f) — percent(0.94) = (94 - 100)/100 = -0.06（挖掘速度 -6%）
attackSpeed(1.15f) — percent(1.15) = (115 - 100)/100 = +0.15（攻击速度 +15%）
未设置的 durability 耐久 和 attackDamage 保持默认 0.0F（无变化）
最终构建出 HandleMaterialStats(0.0, -0.06, +0.15, 0.0)，表示该手柄材料挖掘速度减 6%、攻击速度加 15%，其余属性无影响。
 */
    private  void addMeleeHarvest(){
        System.out.print("load-stats-head");
        addMaterialStats(fsq_Materials.DRAGON_STEEL,
                new HeadMaterialStats(2720, 5f, NETHERITE, 9f),
                HandleMaterialStats.multipliers().miningSpeed(1.00f).attackSpeed(1.40f).durability(1.50f).attackDamage(1.15f).build(), // 手柄
                StatlessMaterialStats.BINDING);
        addMaterialStats(fsq_Materials.WITHER_BONE,
                new HeadMaterialStats(1220, 5f, DIAMOND, 8f),
                HandleMaterialStats.multipliers().miningSpeed(1.10f).attackSpeed(1.20f).durability(1.15f).attackDamage(1.15f).build(), // 手柄
                StatlessMaterialStats.BINDING
                );
        addMaterialStats(fsq_Materials.zxc,
                a_head,
                HandleMaterialStats.multipliers().miningSpeed(1.10f).attackSpeed(1.05f).build(), // 手柄
                StatlessMaterialStats.BINDING// 绑定结
                //Fsq_StatlessMaterialStats.HUN_ZHU
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
        addArmorShieldStats(fsq_Materials.DRAGON_STEEL,
                PlatingMaterialStats.builder().durabilityFactor(90).armor(8,13,11,8).toughness(4),
                StatlessMaterialStats.MAILLE);
        addArmorShieldStats(fsq_Materials.WITHER_BONE,
                PlatingMaterialStats.builder().durabilityFactor(40).armor(5,7,6,5).toughness(1),
                StatlessMaterialStats.MAILLE);
        addArmorShieldStats(fsq_Materials.zxc,
                PlatingMaterialStats.builder().durabilityFactor(28).armor(2, 5, 6, 2).toughness(2),
                StatlessMaterialStats.MAILLE);
    }
}
