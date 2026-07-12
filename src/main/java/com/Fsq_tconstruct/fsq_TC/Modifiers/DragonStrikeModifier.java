package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.damagesource.DamageSource;
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
 * 龙击 — 近战攻击附加 20% 的额外魔法伤害
 *
 * 通过 MeleeHitModifierHook 在成功命中目标后，
 * 以本次实际造成的伤害为基数，再附加 20% 的魔法伤害。
 * 魔法伤害来源复用 TiC 自带的 SMELTERY_MAGIC（已标记为女巫抗性类型），
 * 且该伤害无视无敌帧，确保稳定触发。
 * 同时实现 MonsterMeleeHitModifierHook，使怪物持有工具时也能生效。
 */
public class DragonStrikeModifier extends Modifier implements MeleeHitModifierHook, MonsterMeleeHitModifierHook {

    // 附加魔法伤害的比例（20%）
    private static final float MAGIC_DAMAGE_RATIO = 0.2f;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册近战命中钩子（玩家攻击）和怪物近战命中钩子（怪物攻击）
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.MONSTER_MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        // 仅在造成实际伤害时触发
        if (damageDealt > 0) {
            // 计算附加的魔法伤害 = 实际伤害 × 20%
            float magicDamage = damageDealt * MAGIC_DAMAGE_RATIO;

            // 构造魔法伤害源（优先以玩家为伤害来源，否则以攻击者为来源）
            DamageSource source = TinkerDamageTypes.source(
                context.getLevel().registryAccess(),
                TinkerDamageTypes.SMELTERY_MAGIC,
                Objects.requireNonNullElse(context.getPlayerAttacker(), context.getAttacker()));

            // 对目标造成无视无敌帧的二次伤害
            ToolAttackUtil.attackEntitySecondary(source, magicDamage, context.getTarget(), context.getLivingTarget(), true);
        }
    }

    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        // 怪物攻击命中时，委托给 afterMeleeHit 执行相同的逻辑
        afterMeleeHit(tool, modifier, context, damage);
    }
}
