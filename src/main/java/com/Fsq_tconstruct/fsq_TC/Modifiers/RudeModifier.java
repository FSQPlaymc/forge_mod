package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Set;

/*
 * 无礼 — 对玩家和 BOSS 类生物造成的近战伤害 ×5
 *
 * 通过 MeleeDamageModifierHook 在伤害计算阶段（暴击前）拦截伤害值，
 * 判断目标是否为玩家或预设的 BOSS 实体（凋灵、末影龙、远古守卫者、循声守卫），
 * 匹配则将当前伤害乘以 5。
 * 不修改基础伤害，只对最终累计伤害做倍数放大。
 */
public class RudeModifier extends Modifier implements MeleeDamageModifierHook {

    // 预设的 BOSS 实体类型集合（不含玩家，玩家单独判断）
    private static final Set<EntityType<?>> BOSS_ENTITIES = Set.of(
        EntityType.WITHER,              // 凋灵
        EntityType.ENDER_DRAGON,        // 末影龙
        EntityType.ELDER_GUARDIAN,      // 远古守卫者
        EntityType.WARDEN               // 循声守卫
    );

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册 MELEE_DAMAGE 钩子，用于修改近战伤害数值
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        // 获取被攻击的目标实体
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            // 判断目标是否为玩家 / 预设 BOSS / 血量上限超过 20000 的强力生物
            if (target.getType() == EntityType.PLAYER || BOSS_ENTITIES.contains(target.getType()) || target.getMaxHealth() > 590) {
                // 伤害 ×5
                return damage * 5.0f;
            }
        }
        return damage;
    }
}
