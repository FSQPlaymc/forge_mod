package com.Fsq_tconstruct.fsq_TC.Tool;

import com.Fsq_tconstruct.fsq_TC.Modifiers.RegisterModifiers;
import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.ToolActions;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.tools.definition.module.build.*;
import slimeknights.tconstruct.library.tools.definition.module.display.FixedMaterialToolName;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveModule;
import slimeknights.tconstruct.library.tools.definition.module.weapon.SweepWeaponAttack;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static com.Fsq_tconstruct.fsq_TC.Tool.FsqToolParts.CROSS_KNOT;
import static slimeknights.tconstruct.tools.TinkerToolParts.*;

public class FsqToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    public FsqToolDefinitionDataProvider(PackOutput output) {
        super(output, fsq_tconstruct.MODID);
    }
    @Override
    protected void addToolDefinitions() {
        define(FsqTools.SOUL_SPEAR)
                .module(PartStatsModule.parts()
                        .part(smallBlade,  1f)   // blade1
                        .part(largePlate,0.5f)   // 2 大版
                        .part(FsqToolParts.SOUL_ORB)//3
                        .part(toolHandle)        // 4 handle → 手柄
                        .part(toughBinding)// 5 binding → 大绑定结（或你的 cross_knot）
                        .build())
                .module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 5f)
                        .set(ToolStats.ATTACK_SPEED,  1.6f)//攻速
                        .set(ToolStats.BLOCK_AMOUNT,  10)
                        .build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.DURABILITY, 1.5f)//耐久都
                        .set(ToolStats.MINING_SPEED, 1.5f)//破坏方块速度
                        .build()))
                .largeToolStartingSlots()//强化槽数大
                .module(new SweepWeaponAttack(2))//横扫
                .module(ToolTraitsModule.builder()//自带强化
                        .trait(RegisterModifiers.targetedAttack)
                        //.trait(RegisterModifiers.)// 示例
                        .build())
                .module(ToolActionsModule.of(ToolActions.SWORD_DIG))
                .module(IsEffectiveModule.tag(TinkerTags.Blocks.MINABLE_WITH_SWORD))
                .module(FixedMaterialToolName.FIRST);

        ;
        //西洋剑
        define(FsqTools.XI_YANG_JIAN)
                .module(PartStatsModule.parts()
                        .part(smallBlade,  0.5f)   // blade1: 50% 头部属性
                        .part(smallBlade,  0.6f)   // blade2: 50% 头部属性        // head → 剑刃
                        .part(toolHandle)        // handle → 手柄
                        .part(CROSS_KNOT)//
                        .part(toolBinding)// binding → 绑定结（或你的 cross_knot）
                        .build())
                .module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.ATTACK_DAMAGE, 4f)
                        .set(ToolStats.ATTACK_SPEED,  2.6f)//攻速
                        .set(ToolStats.BLOCK_AMOUNT,  10)
                        .build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.DURABILITY, 1.5f)//耐久都
                        .set(ToolStats.MINING_SPEED, 0.5f)
                        .build()))
                .smallToolStartingSlots()//强化槽数
                .module(ToolTraitsModule.builder()
                        .trait(ModifierIds.reach,1)//长臂一
                        .trait(ModifierIds.silkyShears)
                        .trait(ModifierIds.pierce,5)//穿甲5
                        //.trait(RegisterModifiers.)// 示例
                        .build())
                .module(ToolActionsModule.of(ToolActions.SWORD_DIG))
                .module(IsEffectiveModule.tag(TinkerTags.Blocks.MINABLE_WITH_SWORD))
                //.module(new SweepWeaponAttack(1))//横扫
                .module(FixedMaterialToolName.FIRST);

    }

    @Override
    public String getName() {
        return "aaa";
    }
}
