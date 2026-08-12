package com.Fsq_tconstruct.fsq_TC.Modifiers.armor;

import com.Fsq_tconstruct.fsq_TC.Modifiers.RegisterModifiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

/*
 * 圣洁（盔甲版）— 盔甲材料特性
 *
 * 效果：
 *   1. 当穿戴者身上的负面效果（如中毒、虚弱、缓慢等）数量少于 4 个时，
 *      持续获得 10 秒的生命恢复（Regeneration I）。
 *   2. 若全身盔甲（4 个盔甲槽位）上该特性的等级之和 > 3（例如穿着 4 件带
 *      本特性的盔甲），则升级为生命恢复 II（Regeneration II），同样持续 10 秒。
 *
 * 实现方式：
 *   - 实现 InventoryTickModifierHook，盔甲在玩家物品栏中每个 tick 都会回调
 *     onInventoryTick()。isCorrectSlot == true 表示该件盔甲正穿在正确的盔甲槽位上，
 *     只有此时才生效（放在背包中不触发）。
 *   - 通过 holder.getActiveEffects() 遍历身上所有药水效果，用 MobEffect#isBeneficial()
 *     判断是否为负面效果并计数。
 *   - 通过 holder.getArmorSlots() 遍历 4 个盔甲槽位，对每个槽位中的物品使用
 *     ToolStack.from() 读取 TC 工具 NBT，累加本修饰符的等级得到"全身叠加等级"。
 *   - 为了避免每个 tick 反复重置计时器，仅当已有生命恢复剩余时间不足 2 秒时
 *     才重新施加，保证效果不断档的同时减少不必要的刷新。
 *
 * 注意：本类与 Modifiers 包下的 HolyModifier（武器版"圣洁"，按中毒等级追加伤害）
 *       是不同的修饰符，注册 ID 为 "holy_armor"，两者可共存。
 */
public class HolyArmorModifier extends Modifier implements InventoryTickModifierHook {

    /** 生命恢复持续时长：10 秒 = 200 tick */
    private static final int EFFECT_DURATION = 200;
    /** 剩余时间不足该值时允许刷新（2 秒 = 40 tick），避免每 tick 重复施加 */
    private static final int REFRESH_THRESHOLD = 40;
    /** 负面效果数量上限：少于 4 个时才会触发 */
    private static final int MAX_NEGATIVE_EFFECTS = 4;
    /** 全身叠加等级超过该值时升级为生命恢复 II */
    private static final int TOTAL_LEVEL_THRESHOLD = 3;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册 INVENTORY_TICK 钩子：物品栏（含盔甲栏）中的可修饰物品每个 tick 回调一次
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    /**
     * 盔甲穿戴状态下每 tick 调用一次，用于给穿戴者施加/刷新生命恢复
     *
     * @param tool         当前这件盔甲的 ToolStack
     * @param modifier     本修饰符在该盔甲上的条目
     * @param world        世界实例
     * @param holder       穿戴者（任何 LivingEntity，通常是玩家）
     * @param itemSlot     物品所在槽位
     * @param isSelected   是否在主手（盔甲恒为 false）
     * @param isCorrectSlot 是否为正确的盔甲槽位（穿在身上时为 true）
     * @param stack        对应的 ItemStack
     */
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder,
                                int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        // 只在服务端执行逻辑，客户端不做任何处理，避免双重施加与不同步
        if (world.isClientSide) return;
        // 只有穿在正确的盔甲槽位上才生效（放在背包/手里不触发）
        if (!isCorrectSlot) return;
        // 盔甲损坏时不生效
        if (tool.isBroken()) return;

        // ---------- 条件一：身上负面效果数量少于 4 个 ----------
        // 遍历身上所有活跃的药水效果，统计负面效果（isBeneficial() == false）的数量
        int negativeCount = 0;
        for (MobEffectInstance effect : holder.getActiveEffects()) {
            if (!effect.getEffect().isBeneficial()) {
                negativeCount++;
            }
        }
        // 负面效果达到 4 个或更多时不给予生命恢复
        if (negativeCount >= MAX_NEGATIVE_EFFECTS) return;

        // ---------- 条件二：计算全身盔甲上该特性的等级总和 ----------
        // 遍历 4 个盔甲槽位（头、胸、腿、脚），累加每个槽位上本修饰符的等级
        int totalLevel = 0;
        for (ItemStack armorStack : holder.getArmorSlots()) {
            // 槽位为空或不是 TC 可修饰物品（如原版钻石甲）则跳过，
            // instanceof IModifiable 判断可避免 ToolStack.from() 对非 TC 物品打印警告
            if (armorStack.isEmpty() || !(armorStack.getItem() instanceof IModifiable)) continue;
            // 读取该物品的 ToolStack，累加本修饰符的等级
            totalLevel += ToolStack.from(armorStack).getModifierLevel(RegisterModifiers.HOLY_ARMOR.getId());
        }

        // ---------- 决定生命恢复等级 ----------
        // 全身叠加等级 > 3 时使用生命恢复 II（放大器 1），否则使用生命恢复 I（放大器 0）
        int amplifier = totalLevel > TOTAL_LEVEL_THRESHOLD ? 1 : 0;

        // ---------- 施加 / 刷新生命恢复 ----------
        // 读取穿戴者当前的生命恢复效果
        MobEffectInstance existing = holder.getEffect(MobEffects.REGENERATION);
        // 若已有生命恢复且等级不低于本次的等级，并且剩余时间还足够长，则跳过刷新，
        // 防止每个 tick 都把效果重置为 10 秒（同时也避免把更高级的药水效果降级）
        if (existing != null
            && existing.getAmplifier() >= amplifier
            && existing.getDuration() > REFRESH_THRESHOLD) {
            return;
        }
        // 施加生命恢复：持续 10 秒（200 tick），不隐藏粒子（visible=true，ambient=false）
        holder.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION, amplifier, false, true));
    }
}
