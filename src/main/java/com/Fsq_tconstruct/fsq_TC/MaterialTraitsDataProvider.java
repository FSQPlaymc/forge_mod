package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fsq_TC.Modifiers.RegisterModifiers;
import com.Fsq_tconstruct.fsq_TC.other.Fsq_StatlessMaterialStats;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static slimeknights.tconstruct.library.materials.MaterialRegistry.*;

public  class MaterialTraitsDataProvider extends AbstractMaterialTraitDataProvider {
    public MaterialTraitsDataProvider(PackOutput output, AbstractMaterialDataProvider materials) {
        super(output, materials);
    }
    @Override
    public String getName() {
        return "Tinker's Construct Material Traits";
    }
    @Override protected void addMaterialTraits() {
        addDefaultTraits(fsq_Materials.Amethyst_shardDiamond,RegisterModifiers.Crystal_Oscillator.getId());
        addTraits(fsq_Materials.Amethyst_shardDiamond,MELEE_HARVEST,RegisterModifiers.Crystal_Oscillator.getId());
        addTraits(fsq_Materials.Amethyst_shardDiamond,MELEE_HARVEST,RegisterModifiers.Crystal_Thorn.getId());
        addTraits(fsq_Materials.M_ESSENCE_SOUL, Fsq_StatlessMaterialStats.HUN_ZHU.getType().getId(), ModifierIds.woodwind);
        addTraits(fsq_Materials.zxc, MELEE_HARVEST, ModifierIds.crumbling);
        addTraits(fsq_Materials.zxc, RANGED,         ModifierIds.crystalbound);
        addTraits(fsq_Materials.zxc, ARMOR,          ModifierIds.crystalstrike);
        addTraits(fsq_Materials.zxc,MELEE_HARVEST, RegisterModifiers.myTrait.getId());
        addTraits(fsq_Materials.FIRE_CRYSTAL,Fsq_StatlessMaterialStats.HUN_ZHU.getType().getId(),RegisterModifiers.IGNITE.getId());
        addTraits(fsq_Materials.WITHER_BONE,MELEE_HARVEST,RegisterModifiers.myTrait.getId());
        addTraits(fsq_Materials.WITHER_BONE,ARMOR,RegisterModifiers.UNDYING_STATIC_MODIFIER.getId());
        addTraits(fsq_Materials.DRAGON_STEEL,MELEE_HARVEST,RegisterModifiers.RUDE);
        addTraits(fsq_Materials.DRAGON_STEEL,MELEE_HARVEST,RegisterModifiers.TOUGHNESS);
        addTraits(fsq_Materials.DRAGON_STEEL,ARMOR,ModifierIds.recurrentProtection);
        addTraits(fsq_Materials.REINFORCED_SILVER,ARMOR,RegisterModifiers.HOLY_ARMOR.getId());// 圣洁(甲) — 负面效果少于 4 个时持续获得生命恢复；全身等级叠加 > 3 升级为生命恢复 II
        addTraits(fsq_Materials.REINFORCED_SILVER,ARMOR,RegisterModifiers.FLEXIBLE.getId());// 柔展 — 构建盔甲时额外增加 4*n 护甲值、2*n 盔甲韧性
        addTraits(fsq_Materials.REINFORCED_SILVER,MELEE_HARVEST,RegisterModifiers.HOLY.getId());
        addTraits(fsq_Materials.POISON_COATED_BONE,MELEE_HARVEST,RegisterModifiers.VIRULENT_POISON.getId());
        addTraits(fsq_Materials.POISON_COATED_BONE,MELEE_HARVEST,ModifierIds.pierce );

    }
}
