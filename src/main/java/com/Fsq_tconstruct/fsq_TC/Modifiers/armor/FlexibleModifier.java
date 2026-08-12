package com.Fsq_tconstruct.fsq_TC.Modifiers.armor;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

/*
 * 柔展 — 盔甲材料特性
 *
 * 效果：构建盔甲时，额外增加 4*n 点护甲值（ARMOR）和 2*n 点盔甲韧性（ARMOR_TOUGHNESS），
 *       其中 n 为该材料特性当前的等级（modifier.getLevel()）。
 *       由于本特性挂在 ARMOR 材料统计类型上，穿戴同材料盔甲件数越多，特性等级越高，
 *       因此整套龙钢盔甲（4 件，n=4）可额外获得 16 点护甲与 8 点盔甲韧性。
 *
 * 实现方式：实现 ToolStatsModifierHook 中的 addToolStats 方法，
 *           在工具属性构建阶段向属性构建器（ModifierStatsBuilder）直接追加固定数值。
 *           TC 的盔甲属性与韧性均通过 ToolStats.ARMOR / ToolStats.ARMOR_TOUGHNESS 表达，
 *           与官方 Pluripotent 等盔甲增益修饰符的写法一致。
 */
public class FlexibleModifier extends Modifier implements ToolStatsModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册 TOOL_STATS 钩子：该钩子会在工具/盔甲构建属性时回调 addToolStats()
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    /**
     * 在工具（盔甲）属性构建阶段追加护甲值与盔甲韧性
     *
     * @param context  工具上下文（包含材料信息等，此处无需使用）
     * @param modifier 当前修饰符条目，getLevel() 即材料特性等级 n
     * @param builder  属性构建器，通过 ToolStats.XXX.add() 向最终属性追加数值
     */
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        // 获取材料特性等级 n（整数等级，向下取整；TC 中特性等级 = 同材料部件/盔甲件数）
        int level = modifier.intEffectiveLevel();
        // 护甲值 +4n（例如满级 4 件时 +16 护甲值，受 ToolStats.ARMOR 上限 30 钳制）
        ToolStats.ARMOR.add(builder, 4.0f * level);
        // 盔甲韧性 +2n（例如满级 4 件时 +8 韧性，受 ToolStats.ARMOR_TOUGHNESS 上限 20 钳制）
        ToolStats.ARMOR_TOUGHNESS.add(builder, 2.0f * level);
    }
}
