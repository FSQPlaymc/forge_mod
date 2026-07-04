package com.Fsq_tconstruct.fsq_TC;

import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import static slimeknights.tconstruct.library.materials.MaterialRegistry.*;
@Deprecated
public class fsqMaterialTraits extends AbstractMaterialTraitDataProvider {
    public fsqMaterialTraits(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }
//特性添加
    @Override
    public String getName() {
        return "Tinker's Construct Material Traits";
    }

    @Override
    protected void addMaterialTraits(){
        addDefaultTraits(fsq_Materials.zxc, ModifierIds.cultivated);
        addTraits(fsq_Materials.zxc,MELEE_HARVEST, ModifierIds.spike);
    }
}
