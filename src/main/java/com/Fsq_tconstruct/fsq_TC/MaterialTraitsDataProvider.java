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
    }
}
