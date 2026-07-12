package com.Fsq_tconstruct.fsq_TC;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class MyMaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public MyMaterialRenderInfoProvider(PackOutput packOutput, AbstractMaterialSpriteProvider spriteProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, spriteProvider, existingFileHelper);
    }

    @Override
    public String getName() {
        return "FSQ Material Render Info";
    }

    @Override
    protected void addMaterialRenderInfo() {//处理纹理
        buildRenderInfo(fsq_Materials.Amethyst_shardDiamond);
        buildRenderInfo(fsq_Materials.zxc);
        buildRenderInfo(fsq_Materials.M_ESSENCE_SOUL);
        buildRenderInfo(fsq_Materials.FIRE_CRYSTAL);
        buildRenderInfo(fsq_Materials.WITHER_BONE);
        buildRenderInfo(fsq_Materials.DRAGON_STEEL);
    }
}
