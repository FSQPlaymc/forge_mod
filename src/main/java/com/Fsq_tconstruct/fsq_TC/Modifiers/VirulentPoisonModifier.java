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
 * 剧毒 — 攻击时给予敌人缓慢 III（30s）和中毒 II（10s）
 *
 * 通过 MeleeHitModifierHook 在命中目标后施加移动减速和持续伤害效果，
 * 适合追击或控制敌人。
 * 同时实现 MonsterMeleeHitModifierHook 以兼容怪物持有工具时的攻击。
 */
public class VirulentPoisonModifier extends Modifier implements MeleeHitModifierHook, MonsterMeleeHitModifierHook {

    // 缓慢 III 持续 30 秒
    private static final int SLOWNESS_DURATION = 600;
    // 缓慢放大器：2（0-indexed）= 缓慢 III
    private static final int SLOWNESS_AMPLIFIER = 2;

    // 中毒 II 持续 10 秒
    private static final int POISON_DURATION = 200;
    // 中毒放大器：1（0-indexed）= 中毒 II
    private static final int POISON_AMPLIFIER = 1;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册近战命中钩子和怪物近战命中钩子
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.MONSTER_MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        // 对目标施加剧毒效果
        applyEffects(context.getLivingTarget());
    }

    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        // 怪物攻击时执行相同逻辑
        applyEffects(context.getLivingTarget());
    }

    /**
     * 对目标施加缓慢 III 和中毒 II
     */
    private void applyEffects(LivingEntity target) {
        if (target == null) return;

        // 施加缓慢 III（30s）
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION, SLOWNESS_AMPLIFIER));
        // 施加中毒 II（10s）
        target.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, POISON_AMPLIFIER));
    }
}
