package com.maaamod.TC;

import com.maaamod.TC.GG_traits.AntiGravity;
import com.maaamod.items.GG_Items;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

public class TMaterials_set {
    public static Material TCb;
    public void addMaterials(){
        this.registerToolMaterialStats();
        this.addMaterial();
        this.addMaterialIntegration();
    }

    public TMaterials_set(){
        TCb=new MyTMaterials("666","e3fa50"){{
            this.setCraftable(true);
            this.addCommonItems("bbb");
            addItem(GG_Items.b,1,144);
            this.addTrait(new AntiGravity());
        }};
    }
    public void registerToolMaterialStats(){
        TinkerRegistry.addMaterialStats(TCb, new HeadMaterialStats(35, 2.0F, 2.0F, 0), new IMaterialStats[]{new HandleMaterialStats(1.0F, 25), new ExtraMaterialStats(15)});
    }
    public void addMaterial(){
        TinkerRegistry.addMaterial(TCb);
    }
    public void addMaterialIntegration(){
        MaterialIntegration TCbM = new MaterialIntegration(TCb);
        TinkerRegistry.integrate(TCbM);
    }
}
