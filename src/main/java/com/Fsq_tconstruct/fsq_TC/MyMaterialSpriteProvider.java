package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fsq_TC.other.Fsq_StatlessMaterialStats;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

public class MyMaterialSpriteProvider extends AbstractMaterialSpriteProvider {
    @Override public @NotNull String getName() { return "My Mod Materials"; }
    @Override
    protected void addAllMaterials() {
        buildMaterial(fsq_Materials.zxc)
                .meleeHarvest()//近战
                .ranged()//远程
                .armor()//箭头
                .arrowShaft()//箭杆
                .shieldCore()//盾芯
                .fallbacks("bone", "rock")
                .colorMapper(PaletteHelper.fromMidColor(0xFFF5FF5B));//主色(216灰阶)
        buildMaterial(fsq_Materials.Amethyst_shardDiamond)
                .meleeHarvest()//近战
                .fallbacks("bone", "rock")
                .colorMapper(PaletteHelper.fromMidColor(0xFF5B6AB3));
        buildMaterial(fsq_Materials.M_ESSENCE_SOUL)
                .statType(Fsq_StatlessMaterialStats.HUN_ZHU)
                .fallbacks("bone", "rock")
                .colorMapper(PaletteHelper.fromMidColor(0xFF633D30));
        buildMaterial(fsq_Materials.FIRE_CRYSTAL)
                .statType(Fsq_StatlessMaterialStats.HUN_ZHU)
                .fallbacks("bone", "rock")
                .colorMapper(PaletteHelper.fromMidColor(0xFFffb30f));
        buildMaterial(fsq_Materials.WITHER_BONE)
                .meleeHarvest()//近战
                .armor()//盔甲all
                .fallbacks("bone", "rock")
                .colorMapper(PaletteHelper.fromMidColor(0xFF7300ff));

    }//5b6ab3
}
/*getFallbackColor() 用中等灰度 0xFFD8D8D8（灰阶值 = 216）作为输入，通过你的 GreyToColorMapping 映射得到颜色。

因为你的映射中 灰阶 216 精确等于 addARGB(216, 0xFFDFA8CF)，所以 fallback 颜色就是 FFDFA8CF。这是 TConstruct 自动取"中间调"作为材料代表色，用于 UI 显示。

如果你想让主色是别的值，把想要的颜色放在 216 灰阶位置即可，例如：

.addARGB(216, 0xFF你想要的色)

 */
