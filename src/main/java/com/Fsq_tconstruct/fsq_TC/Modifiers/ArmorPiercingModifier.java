package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
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
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Objects;

/*
 * 破甲 — 攻击时将最终伤害一分为二，一半为正常伤害，一半为无视护甲的破甲伤害
 *
 * 核心机制：
 * 1. 在伤害计算阶段（getMeleeDamage）将原始伤害保存，并只返还一半作为正常伤害
 * 2. 在命中后（afterMeleeHit）用 PIERCING 伤害类型补回另一半作为破甲伤害
 * 3. 正常伤害部分通过清除目标 invulnerableTime 无视无敌帧
 * 4. 破甲伤害部分通过「直接设置生命值（setHealth）」的底层方式结算，
 *    完全绕过伤害管线（hurt → LivingHurtEvent / LivingDamageEvent → 护甲/护盾/无敌帧/伤害吸收），
 *    因此其他 mod 无法拦截、减免或阻挡该伤害
 * 5. 底层扣血的同时保留伤害源（CombatTracker、lastHurtBy、lastDamageSource），
 *    使死亡消息、击杀归属、受击 AI（HurtBySensor）、幽匿催化等机制仍能正确关联攻击者
 * 6. 同时实现 MonsterMeleeHitModifierHook 使怪物持有工具时也能生效
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
     * 该类型在 DamageTypeTagProvider 中已被标记 BYPASSES_ARMOR。
     *
     * 伤害结算采用「直接设置生命值」的底层方式：
     * - 不调用 LivingEntity.hurt()，因此不会触发 ForgeHooks 的
     *   LivingAttackEvent / LivingDamageEvent 等事件，其他 mod 无法阻挡或减免该伤害
     * - 不经过护甲计算、护盾格挡、无敌帧、伤害吸收等任何减伤逻辑
     * - 通过 setHealth 结算后由 SynchedEntityData 自动同步给客户端，血条正常更新
     *
     * 伤害源仍然保留（applyDamageSourceMetadata）：
     * - CombatTracker.recordDamage → 死亡消息（"xxx 被 yyy 杀死"）
     * - setLastHurtByPlayer / setLastHurtByMob → 击杀分数、击杀统计、凋灵玫瑰归属
     * - lastDamageSource / lastDamageStamp（经 accesstransformer.cfg 开放）→
     *   受击 AI（HurtBySensor）、女巫、幽匿催化等机制识别伤害来源
     */
    private void applyArmorPiercing(ToolAttackContext context) {
        // 没有活体目标时跳过
        LivingEntity target = context.getLivingTarget();
        if (target == null) return;

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

        // 保留伤害源，使死亡消息、击杀归属、受击 AI 等正确关联攻击者
        applyDamageSourceMetadata(target, source, armorPiercingDamage);

        // 目标已被正常伤害击杀时跳过（与原有 attackEntitySecondary 行为一致）
        if (target.isDeadOrDying()) return;

        // ==== 底层扣血：直接设置生命值，绕过整个伤害管线 ====
        float newHealth = target.getHealth() - armorPiercingDamage;
        if (newHealth <= 0.0F) {
            // 触发死亡流程，使用保留的伤害源（死亡消息、掉落、经验、统计均正常）
            target.setHealth(0.0F);
            target.die(source);
        } else {
            target.setHealth(newHealth);
            // 手动补上正常受伤的视觉反馈（受击红屏/抖动/音效），不经过伤害管线
            target.invulnerableTime = 20;
            target.hurtDuration = 10;
            target.hurtTime = 10;
            target.hurtMarked = true;
            target.level().broadcastDamageEvent(target, source);
        }
    }

    /**
     * 保留伤害源：不经过伤害管线也能让攻击者与本次伤害正确关联
     */
    private void applyDamageSourceMetadata(LivingEntity target, DamageSource source, float damage) {
        // 记录击杀归属（lastHurtByPlayer / lastHurtByMob，含驯养宠物归属主人），
        // 对齐 LivingEntity.hurt 中设置 lastHurtBy 的原生逻辑
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            if (!source.is(DamageTypeTags.NO_ANGER)) {
                target.setLastHurtByMob(livingAttacker);
            }
            if (livingAttacker instanceof Player player) {
                target.setLastHurtByPlayer(player);
            } else if (livingAttacker instanceof TamableAnimal tamable && tamable.isTame()) {
                LivingEntity owner = tamable.getOwner();
                target.setLastHurtByPlayer(owner instanceof Player player ? player : null);
            }
        }

        // CombatTracker 记录本次伤害，保证死亡消息使用正确的伤害源
        target.getCombatTracker().recordDamage(source, damage);

        // 直接写入 private 字段（accesstransformer.cfg 已开放），
        // 供 getLastDamageSource() 在 40 tick 内返回本次伤害源
        target.lastDamageSource = source;
        target.lastDamageStamp = target.level().getGameTime();
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
