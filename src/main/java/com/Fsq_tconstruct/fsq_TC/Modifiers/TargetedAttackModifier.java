package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
/*
注册了两种交互钩子：GENERAL_INTERACT（右键空气）和 ENTITY_INTERACT（右键实体）
onToolUse：右键空气时，通过 ProjectileUtil.getHitResultOnViewVector 在 80 格范围内射线检测一个实体目标
afterEntityUse：右键直接点击实体时，直接以该实体为目标
performTargetedAttack：核心逻辑
检查工具是否损坏、是否在冷却中
确定目标（射线检测或直接目标），检查是否可攻击
取工具基础伤害的 50% 作为伤害
构造 ToolAttackContext 执行攻击
根据攻击速度计算冷却（ceil(20 / attackSpeed) ticks），添加物品冷却
播放挥手动画
 */
public class TargetedAttackModifier extends Modifier
    implements GeneralInteractionModifierHook, EntityInteractionModifierHook {

    private static final double RANGE = 80.0;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT, ModifierHooks.ENTITY_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier,
        Player player, InteractionHand hand, InteractionSource source) {
        if (source != InteractionSource.RIGHT_CLICK) return InteractionResult.PASS;
        return performTargetedAttack(tool, player, hand, null);
    }

    @Override
    public InteractionResult afterEntityUse(IToolStackView tool, ModifierEntry modifier,
        Player player, LivingEntity target, InteractionHand hand, InteractionSource source) {
        if (source != InteractionSource.RIGHT_CLICK) return InteractionResult.PASS;
        return performTargetedAttack(tool, player, hand, target);
    }

    private InteractionResult performTargetedAttack(IToolStackView tool, Player player,
        InteractionHand hand, LivingEntity directTarget) {
        if (tool.isBroken()) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(tool.getItem())) return InteractionResult.PASS;

        if (player.level().isClientSide) {
            return InteractionResult.CONSUME;
        }

        LivingEntity target;
        if (directTarget != null) {
            target = directTarget;
            if (!ToolAttackUtil.isAttackable(player, target)) return InteractionResult.PASS;
        } else {
            HitResult hit = ProjectileUtil.getHitResultOnViewVector(player,
                entity -> entity instanceof LivingEntity && entity != player && !entity.isSpectator(), RANGE);
            if (hit.getType() != HitResult.Type.ENTITY) return InteractionResult.PASS;
            target = (LivingEntity) ((EntityHitResult) hit).getEntity();
            if (!ToolAttackUtil.isAttackable(player, target)) return InteractionResult.PASS;
        }

        float baseDamage = tool.getStats().get(ToolStats.ATTACK_DAMAGE) * 0.5f;

        ToolAttackContext context = ToolAttackContext.attacker(player)
            .target(target)
            .hand(hand)
            .baseDamage(baseDamage)
            .cooldown(1.0f)
            .build();

        ToolAttackUtil.performAttack(tool, context);

        float attackSpeed = Math.max(0.1f, tool.getStats().get(ToolStats.ATTACK_SPEED));
        int cooldown = (int)Math.ceil(20f / attackSpeed);
        player.getCooldowns().addCooldown(tool.getItem(), cooldown);
        player.swing(hand);

        return InteractionResult.CONSUME;
    }
}
