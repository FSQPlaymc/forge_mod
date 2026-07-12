package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MonsterMeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Objects;

/*
 * 圣洁 — 命中敌人时额外造成"原伤害 × 目标当前中毒等级"的魔法伤害
 *
 * 攻击命中后检测目标是否处于中毒状态，
 * 若是则读取其中毒放大器（0-indexed）并加 1 作为中毒等级，
 * 额外伤害 = 本次实际伤害 × 中毒等级，以魔法伤害的形式二次攻击。
 * 同时实现 MonsterMeleeHitModifierHook 以兼容怪物持有工具时的攻击。
 */
public class HolyModifier extends Modifier implements MeleeHitModifierHook, MonsterMeleeHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册近战命中钩子和怪物近战命中钩子
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.MONSTER_MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        // 处理圣洁额外伤害逻辑
        applyHolyDamage(context, damageDealt);
    }

    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        // 怪物攻击时执行相同逻辑
        applyHolyDamage(context, damage);
    }

    /**
     * 核心逻辑：读取目标的中毒等级，计算并造成额外伤害
     */
    private void applyHolyDamage(ToolAttackContext context, float damageDealt) {
        // 仅在有实际伤害时触发
        if (damageDealt <= 0) return;

        // 获取目标实体
        if (context.getLivingTarget() == null) return;

        // 读取目标当前的中毒效果
        MobEffectInstance poison = context.getLivingTarget().getEffect(MobEffects.POISON);
        if (poison == null) return;

        // 中毒等级（1-indexed）= 放大器 + 1，无中毒时跳过
        int poisonLevel = poison.getAmplifier() + 1;

        // 计算额外伤害 = 本次实际伤害 × 中毒等级
        float extraDamage = damageDealt * poisonLevel;

        // 构造魔法伤害源
        DamageSource source = TinkerDamageTypes.source(
            context.getLevel().registryAccess(),
            TinkerDamageTypes.SMELTERY_MAGIC,
            Objects.requireNonNullElse(context.getPlayerAttacker(), context.getAttacker()));

        // 对目标造成无视无敌帧的二次魔法伤害
        ToolAttackUtil.attackEntitySecondary(source, extraDamage, context.getTarget(), context.getLivingTarget(), true);
    }
}
