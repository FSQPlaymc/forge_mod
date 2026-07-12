package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MonsterMeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

/*
 * 蜂蛰 — 攻击时给予敌人虚弱 40s 和中毒 30s
 * 效果放大器的公式为：5^n / 2（n = 修饰符等级），然后转换为 0-indexed
 * 同时实现 MonsterMeleeHitModifierHook 以兼容怪物持有工具时的攻击
 */
public class BeeStingModifier extends Modifier implements MeleeHitModifierHook, MonsterMeleeHitModifierHook {

    // 虚弱持续 40 秒（20 tick/s）
    private static final int WEAKNESS_DURATION = 800;
    // 中毒持续 30 秒
    private static final int POISON_DURATION = 600;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册近战命中钩子和怪物近战命中钩子
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.MONSTER_MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        // 对目标施加状态效果
        applyEffects(context.getLivingTarget(), modifier.intEffectiveLevel());
    }

    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        // 怪物攻击时执行相同逻辑
        applyEffects(context.getLivingTarget(), modifier.intEffectiveLevel());
    }

    /**
     * 根据修饰符等级计算虚弱和中毒的放大器并施加效果
     * 放大器公式：(5^n / 2) - 1，其中 n 为修饰符等级
     */
    private void applyEffects(LivingEntity target, int level) {
        if (target == null) return;

        // 计算放大器（0-indexed）：floor(5^n / 2) - 1
        int amp = (int)(Math.pow(5, level) / 2.0) - 1;
        if (amp < 0) return;

        // 施加虚弱效果
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, WEAKNESS_DURATION, amp));
        // 施加中毒效果
        target.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, amp));
    }
}
