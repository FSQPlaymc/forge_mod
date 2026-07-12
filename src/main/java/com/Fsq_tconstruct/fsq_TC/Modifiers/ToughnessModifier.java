package com.Fsq_tconstruct.fsq_TC.Modifiers;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

/*
 * 坚韧 — 每级提高工具 2000 点基础耐久
 *
 * 通过 ToolStatsModifierHook 在工具属性构建时，
 * 向 DURABILITY 属性直接添加固定数值（每级 +2000）。
 * 非百分比加成，会直接显示在工具提示的耐久值上。
 */
public class ToughnessModifier extends Modifier implements ToolStatsModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册 TOOL_STATS 钩子，用于修改工具的基础属性
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        // 每级增加 2000 点耐久，线性叠加
        ToolStats.DURABILITY.add(builder, 2000 * modifier.intEffectiveLevel());
    }
}
