package com.Fsq_tconstruct.fsq_TC.Tool;

import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.tools.TinkerToolParts;

import static com.Fsq_tconstruct.fsq_TC.Tool.FsqTools.HG;
import static com.Fsq_tconstruct.fsq_TC.Tool.FsqTools.XJ;

public class FsqStationSlotLayoutProvider extends AbstractStationSlotLayoutProvider {
    /*
     *添加工具组装格子在工具台中
     */
    public FsqStationSlotLayoutProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addLayouts() {
        defineModifiable(HG.get())
                .sortIndex(SORT_WEAPON + SORT_LARGE)
                .addInputItem(TinkerToolParts.smallBlade,  63, 18)   // blade1
                .addInputItem(TinkerToolParts.largePlate,  44, 29)   // blade2
                .addInputItem(FsqToolParts.SOUL_ORB.get(),  25, 46)  // 魂珠3
                .addInputItem(TinkerToolParts.toolHandle,   10, 10)  // handle → part 2
                .addInputItem(TinkerToolParts.toughBinding,  1, 1)  // binding → part 4
                .build();
        // 随机默认材料（与部件数量一致）
        defineModifiable(XJ.get())
                .sortIndex(SORT_WEAPON + SORT_LARGE)
                .addInputItem(TinkerToolParts.smallBlade,  63, 18)   // blade1
                .addInputItem(TinkerToolParts.smallBlade,  44, 29)   // blade2
                .addInputItem(TinkerToolParts.toolHandle,   33, 42)  // handle → part 2
                .addInputItem(FsqToolParts.CROSS_KNOT.get(),  25, 46)  // knot2
                .addInputItem(TinkerToolParts.toolBinding,  15, 60)  // binding → part 4
                .build();
    }

    @Override
    public String getName() {
        return "123";
    }//贴图UI
}