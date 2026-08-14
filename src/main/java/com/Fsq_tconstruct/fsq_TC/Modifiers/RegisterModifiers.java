package com.Fsq_tconstruct.fsq_TC.Modifiers;

import com.Fsq_tconstruct.fsq_TC.Modifiers.armor.FlexibleModifier;
import com.Fsq_tconstruct.fsq_TC.Modifiers.armor.HolyArmorModifier;
import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

/*
 * 修饰符注册中心
 * 所有自定义修饰符必须在此通过 MODIFIERS.register() 注册，
 * 并在 fsq_tconstruct 主类的构造方法中调用 register() 方法。
 */
public class RegisterModifiers {
    static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(fsq_tconstruct.MODID);

    // === 已有修饰符 ===
    public static final StaticModifier<fsq_Modifier_1> myTrait = MODIFIERS.register("my_trait", fsq_Modifier_1::new);
    public static final StaticModifier<Crystal_Oscillator> Crystal_Oscillator = MODIFIERS.register("crystal_oscillator", Crystal_Oscillator::new);
    public static final StaticModifier<Crystal_Thorn> Crystal_Thorn = MODIFIERS.register("crystal_thorn", Crystal_Thorn::new);
    public static final StaticModifier<TargetedAttackModifier> targetedAttack = MODIFIERS.register("targeted_attack", TargetedAttackModifier::new);
    public static final StaticModifier<Ignite> IGNITE = MODIFIERS.register("ignite", Ignite::new);
    public static final StaticModifier<Undying> UNDYING_STATIC_MODIFIER = MODIFIERS.register("undying", Undying::new);

    // === 新修饰符 ===
    /** 坚韧 — 每级提高工具 2000 点基础耐久 */
    public static final StaticModifier<ToughnessModifier> TOUGHNESS = MODIFIERS.register("toughness", ToughnessModifier::new);
    /** 无礼 — 对玩家和 BOSS 造成的近战伤害 ×5 */
    public static final StaticModifier<RudeModifier> RUDE = MODIFIERS.register("rude", RudeModifier::new);
    /** 龙击 — 近战攻击附加 20% 额外魔法伤害 */
    public static final StaticModifier<DragonStrikeModifier> DRAGON_STRIKE = MODIFIERS.register("dragon_strike", DragonStrikeModifier::new);

    /** 蜂蛰 — 攻击给予虚弱 40s + 中毒 30s，放大器 = 5^n / 2 */
    public static final StaticModifier<BeeStingModifier> BEE_STING = MODIFIERS.register("bee_sting", BeeStingModifier::new);
    /** 圣洁 — 命中时额外造成"原伤害 × 目标中毒等级"的魔法伤害 */
    public static final StaticModifier<HolyModifier> HOLY = MODIFIERS.register("holy", HolyModifier::new);
    /** 剧毒 — 攻击给予缓慢 III 30s + 中毒 II 10s */
    public static final StaticModifier<VirulentPoisonModifier> VIRULENT_POISON = MODIFIERS.register("virulent_poison", VirulentPoisonModifier::new);
    /** 破甲 — 攻击伤害一分为二：50% 正常伤害 + 50% 无视护甲的破甲伤害，且攻击无视无敌帧 */
    public static final StaticModifier<ArmorPiercingModifier> ARMOR_PIERCING = MODIFIERS.register("armor_piercing", ArmorPiercingModifier::new);

    // === 盔甲材料特性 ===
    /** 柔展 — 构建盔甲时额外增加 4*n 点护甲值、2*n 点盔甲韧性（n 为材料特性等级） */
    public static final StaticModifier<FlexibleModifier> FLEXIBLE =
            MODIFIERS.register("flexible", FlexibleModifier::new);
    /** 圣洁(甲) — 负面效果少于 4 个时持续获得生命恢复 10 秒；全身该特性等级叠加 > 3 时升级为生命恢复 II */
    public static final StaticModifier<HolyArmorModifier> HOLY_ARMOR =
            MODIFIERS.register("holy_armor", HolyArmorModifier::new);
    /** 范围冲击 — 攻击时以主目标为圆心造成范围魔法伤害，1级占1能力槽，2~5级升级不占槽 */
    public static final StaticModifier<AoEShockwaveModifier> AOE_SHOCKWAVE =
            MODIFIERS.register("aoe_shockwave", AoEShockwaveModifier::new);

    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
        //MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
