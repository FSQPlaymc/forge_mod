package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MonsterMeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Objects;

/*
 * 破甲 — 攻击时将最终伤害一分为二，一半为正常伤害，一半为无视护甲的破甲伤害
 *
 * 核心机制：
 * 1. 在伤害计算阶段（getMeleeDamage）将原始伤害保存，并只返还一半作为正常伤害
 * 2. 在命中后（afterMeleeHit）用 PIERCING 伤害类型（已标记 BYPASSES_ARMOR）
 *    补回另一半作为破甲伤害，该伤害无视目标护甲
 * 3. 正常伤害部分通过清除目标 invulnerableTime 无视无敌帧
 * 4. 破甲伤害部分通过 attackEntitySecondary 的 ignoreInvulnerability=true 无视无敌帧
 * 5. 同时实现 MonsterMeleeHitModifierHook 使怪物持有工具时也能生效
 *
 * 钩子调用时序：
 *   getMeleeDamage → [正常伤害计算 → 命中判定] → afterMeleeHit / onMonsterMeleeHit
 */
public class ArmorPiercingModifier extends Modifier
    implements MeleeDamageModifierHook, MeleeHitModifierHook, MonsterMeleeHitModifierHook {

    /** 破甲伤害占总伤害的比例（50%） */
    private static final float ARMOR_PIERCING_RATIO = 0.5f;

    /** 保存从 getMeleeDamage 中取得的原始累计伤害，供 afterMeleeHit 中使用 */
    private float originalDamage;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        /*
         * 注册三个钩子：
         * - MELEE_DAMAGE：在伤害计算阶段拦截并修改伤害值
         * - MELEE_HIT：在成功命中后补充破甲伤害（玩家攻击）
         * - MONSTER_MELEE_HIT：在成功命中后补充破甲伤害（怪物攻击）
         */
        hookBuilder.addHook(this,
            ModifierHooks.MELEE_DAMAGE,
            ModifierHooks.MELEE_HIT,
            ModifierHooks.MONSTER_MELEE_HIT);
    }

    /**
     * 伤害计算阶段：将原始伤害一分为二
     *
     * 此处保存完整的原始累计伤害，但只返还一半作为正常伤害。
     * 另一半将在命中后以破甲伤害的形式补回。
     * 同时清除目标的无敌帧计时器，使本次正常伤害无视无敌帧。
     *
     * @param damage  经过之前所有修饰符累加后的当前伤害值
     * @return 返回一半的伤害作为正常伤害（另一半留给 afterMeleeHit 处理）
     */
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier,
                                ToolAttackContext context, float baseDamage, float damage) {
        // 保存完整的原始累计伤害，供 afterMeleeHit 计算破甲伤害时使用
        this.originalDamage = damage;

        // 清除目标的无敌帧计时器，使本次正常伤害无视无敌帧
        if (context.getLivingTarget() != null) {
            context.getLivingTarget().invulnerableTime = 0;
        }

        // 只返还一半作为正常伤害（另一半将在命中后以破甲伤害补回）
        return damage * (1.0f - ARMOR_PIERCING_RATIO);
    }

    /**
     * 玩家近战攻击命中后回调
     * 委托给 applyArmorPiercing 处理破甲伤害
     */
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                              ToolAttackContext context, float damageDealt) {
        applyArmorPiercing(context);
    }

    /**
     * 怪物近战攻击命中后回调
     * 委托给 applyArmorPiercing 处理破甲伤害
     */
    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                                  ToolAttackContext context, float damage) {
        applyArmorPiercing(context);
    }

    /**
     * 核心逻辑：对目标造成无视护甲 + 无视无敌帧的破甲伤害
     *
     * 使用 TinkerDamageTypes.PIERCING 作为伤害类型，
     * 该类型在 DamageTypeTagProvider 中已被标记 BYPASSES_ARMOR，
     * 因此造成的伤害将完全无视目标的护甲值。
     *
     * 使用 ToolAttackUtil.attackEntitySecondary 且 ignoreInvulnerability=true，
     * 使该伤害无视无敌帧，确保稳定触发。
     *
     * 伤害来源设为攻击者（玩家优先，否则为攻击实体），
     * 使得死亡消息、战利品分配等能正确关联到攻击者。
     */
    private void applyArmorPiercing(ToolAttackContext context) {
        // 没有活体目标时跳过
        if (context.getLivingTarget() == null) return;

        // 计算破甲伤害 = 原始总伤害 × 50%
        float armorPiercingDamage = this.originalDamage * ARMOR_PIERCING_RATIO;
        if (armorPiercingDamage <= 0) return;

        // 生成破甲粒子特效，用于直观查看破甲伤害是否触发
        spawnPiercingParticles(context);

        // 构造 PIERCING 伤害源，以攻击者（玩家或怪物）为伤害来源
        DamageSource source = TinkerDamageTypes.source(
            context.getLevel().registryAccess(),
            TinkerDamageTypes.PIERCING,
            Objects.requireNonNullElse(context.getPlayerAttacker(), context.getAttacker()));

        // 对目标造成无视护甲、无视无敌帧的二次破甲伤害
        // 参数：ignoreInvulnerability = true，表示无视无敌帧
        ToolAttackUtil.attackEntitySecondary(
            source, armorPiercingDamage,
            context.getTarget(), context.getLivingTarget(), true);
    }

    /**
     * 在目标位置生成破甲粒子特效（附魔火花）
     * 服务端通过 sendParticles 广播给所有客户端，客户端直接本地生成
     */
    private void spawnPiercingParticles(ToolAttackContext context) {
        LivingEntity target = context.getLivingTarget();
        if (target == null) return;

        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.5;
        double z = target.getZ();
        Level level = context.getLevel();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                x, y, z, 12, 0.3, 0.3, 0.3, 0.05);
        } else if (level.isClientSide) {
            level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, 0, 0, 0);
        }
    }
}
