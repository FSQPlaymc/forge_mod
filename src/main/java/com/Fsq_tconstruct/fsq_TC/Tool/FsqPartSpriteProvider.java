package com.Fsq_tconstruct.fsq_TC.Tool;

import com.Fsq_tconstruct.fsq_TC.other.Fsq_StatlessMaterialStats;
import com.Fsq_tconstruct.fsq_tconstruct;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;

public class FsqPartSpriteProvider extends AbstractPartSpriteProvider {

    public FsqPartSpriteProvider() {
        super(fsq_tconstruct.MODID);
    }

    @Override
    public String getName() {
        return "FSQ Tool Parts";
    }

    @Override
    protected void addAllSpites() {
        addBinding("f_cross_knot/cross_knot");
        addPart("f_soul_orb/soul_orb", Fsq_StatlessMaterialStats.HUN_ZHU.getIdentifier()); // 魂珠独立部件使用自定义 stat，非 binding

        buildTool("soul_spear")
                .withLarge()//大贴图
                .addBreakableHead("smallblade")
                .addHead("largeplate")
                .addPart("soul_orb", Fsq_StatlessMaterialStats.HUN_ZHU)
                .addHandle("toolhandle")
                .addBinding("toughbinding");

        buildTool("western_sword")
                .addBreakableHead("blade1")
                .addHead("blade2")
                .addHandle("toolhandle")
                .addBinding("knot1")
                .addBinding("knot2");
    }
}