package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MonsterMeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

/*
 * 范围冲击 — 能力型升级修饰符（占用 1 个能力槽，2~5 级升级不额外占槽）
 *
 * 效果：近战攻击命中时，以"主要攻击目标"（准星所指，即被击中的目标）为圆心，
 *       以 n 格为半径，对范围内所有生物执行一次"工具攻击"（ToolAttackContext +
 *       ToolAttackUtil.performAttack），每次攻击造成"本次实际伤害 × b"的伤害，
 *       且不给武器添加使用冷却（区别于 TargetedAttackModifier 的右键远程打击）。
 *       范围攻击只波及主目标以外的实体，且不伤害攻击者自己和"队友"
 *       （同计分板队伍成员、攻击者驯服的宠物），中立生物会受到伤害。
 *
 * 各等级数值：
 *   1级: n=1    , b=10%
 *   2级: n=2    , b=20%
 *   3级: n=3.5  , b=30%
 *   4级: n=6    , b=40%
 *   5级: n=10   , b=50%
 *
 * 实现方式（仿照 TargetedAttackModifier 的 ToolAttackContext 写法）：
 *   - 实现 MeleeHitModifierHook（玩家近战）与 MonsterMeleeHitModifierHook（怪物持械攻击）。
 *   - 在 afterMeleeHit 回调中拿到攻击上下文 context 与本次实际伤害 damageDealt。
 *   - 对主目标周围半径内的每个生物，用 Builder 构造一个新的 ToolAttackContext：
 *       attacker(攻击者).target(范围目标).hand(原攻击的手).baseDamage(实际伤害×b)
 *       .cooldown(1.0f).extraAttack()，然后调用 ToolAttackUtil.performAttack(tool, context)。
 *   - cooldown(1.0f)：满充能，伤害不打折扣；且全程不调用 addCooldown()（不去添加冷却）。
 *   - extraAttack()：标记为额外攻击（与 TC 镰刀等 AOE 相同），跳过暴击判定，
 *     并配合下面的 isExtraAttack() 递归防护——本修饰符自身引发的范围攻击不会再触发范围攻击。
 *   - 范围攻击属于真实攻击，会经过护甲减伤、触发其他修饰符的命中钩子、消耗工具耐久
 *     （每个命中目标消耗 1 点，非主力武器 2 点），伤害数值也会被工具上其他增伤修饰符
 *     再次放大（简单传值，接受叠加）。
 */
public class AoEShockwaveModifier extends Modifier implements MeleeHitModifierHook, MonsterMeleeHitModifierHook {

    /** 各等级的爆炸半径（单位：格），下标 = 等级 - 1 */
    private static final float[] RADIUS = { 1.0f, 2.0f, 3.5f, 6.0f, 10.0f };
    /** 各等级的伤害比例（造成原伤害的百分比），下标 = 等级 - 1 */
    private static final float[] DAMAGE_PERCENT = { 0.10f, 0.20f, 0.30f, 0.40f, 0.50f };

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册近战命中钩子与怪物近战命中钩子
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.MONSTER_MELEE_HIT);
    }

    /** 玩家/生物用工具近战命中后回调 */
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        // 递归防护：本修饰符的范围攻击是通过 extraAttack 上下文执行的，
        // 命中后仍会回调 afterMeleeHit，但 isExtraAttack() 为 true，直接跳过，
        // 避免"范围攻击又触发范围攻击"的无限递归。
        if (context.isExtraAttack()) return;
        applyShockwave(tool, context, damageDealt, modifier.getLevel());
    }

    /** 怪物手持该工具攻击时回调（与上面逻辑一致） */
    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isExtraAttack()) return;
        applyShockwave(tool, context, damage, modifier.getLevel());
    }

    /**
     * 范围冲击核心逻辑：构造 ToolAttackContext 对范围内每个目标执行一次工具攻击
     *
     * @param tool        当前武器
     * @param context     原始攻击上下文（主目标、攻击者、手部信息）
     * @param damageDealt 本次原始攻击实际造成的伤害
     * @param level       该修饰符当前等级
     */
    private void applyShockwave(IToolStackView tool, ToolAttackContext context, float damageDealt, int level) {
        // 武器损坏时不触发
        if (tool.isBroken()) return;
        // 没有造成伤害时（例如被护甲完全抵消）不触发
        if (damageDealt <= 0) return;

        // 取得主要攻击目标（准星所指的目标）
        LivingEntity target = context.getLivingTarget();
        if (target == null) return;

        // 等级钳制在 1~5，防止异常数据越界
        int clampedLevel = Math.max(1, Math.min(5, level));
        // 按等级查表得到半径 n 与伤害比例 b
        float radius = RADIUS[clampedLevel - 1];
        float aoeDamage = damageDealt * DAMAGE_PERCENT[clampedLevel - 1];

        // 取得攻击者（玩家攻击时是玩家，怪物攻击时是怪物）
        LivingEntity attacker = context.getAttacker();

        // 以主目标的碰撞箱为中心，向外扩张 radius 格，收集范围内符合条件的生物：
        // - 存活
        // - 不是攻击者自己（不误伤自己）
        // - 不是主目标（主目标只吃原始攻击，不吃范围伤害）
        // - 不是队友（同计分板队伍 / 攻击者驯服的宠物）
        AABB area = target.getBoundingBox().inflate(radius);
        List<LivingEntity> entities = context.getLevel().getEntitiesOfClass(LivingEntity.class, area,
                e -> e.isAlive() && e != attacker && e != target && !isFriendly(e, attacker));
        if (entities.isEmpty()) return;

        // 对范围内每个实体构造攻击上下文并执行一次完整工具攻击
        for (LivingEntity entity : entities) {
            ToolAttackContext aoeContext = ToolAttackContext.attacker(attacker)
                    // 目标：当前范围实体
                    .target(entity)
                    // 手部：沿用原始攻击的手（主手/副手）
                    .hand(context.getHand())
                    // 基础伤害 = 原始实际伤害 × b（会被其他增伤修饰符再次放大，简单传值接受叠加）
                    .baseDamage(aoeDamage)
                    // 满充能：伤害不打折扣，且不调用 addCooldown()（不去添加冷却）
                    .cooldown(1.0f)
                    // 标记为额外攻击：跳过暴击判定，并配合 isExtraAttack() 防止递归
                    .extraAttack()
                    .build();
            // 执行攻击（真实攻击：走护甲减伤、命中钩子、消耗工具耐久等完整流程）
            ToolAttackUtil.performAttack(tool, aoeContext);
        }
    }

    /**
     * 判断某个生物对攻击者来说是否算"队友"（不应被范围伤害波及）
     *
     * @param entity   范围内待判断的生物
     * @param attacker 攻击者
     * @return true 表示是队友，应跳过
     */
    private static boolean isFriendly(LivingEntity entity, Entity attacker) {
        // 原版 isAlliedTo：玩家按计分板队伍判断盟友关系
        if (entity.isAlliedTo(attacker)) return true;
        // 额外处理驯养宠物：属于攻击者的狼/猫/马等宠物不受伤
        if (attacker instanceof Player player && entity instanceof TamableAnimal pet) {
            return pet.isOwnedBy(player);
        }
        return false;
    }
}