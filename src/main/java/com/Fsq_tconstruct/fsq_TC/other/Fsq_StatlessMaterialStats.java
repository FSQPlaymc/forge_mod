package com.Fsq_tconstruct.fsq_TC.other;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import java.util.List;
/*
 *处理部件类型
 */
public enum Fsq_StatlessMaterialStats implements IMaterialStats {
    HUN_ZHU("soul_orb");//魂珠
    private final MaterialStatType<Fsq_StatlessMaterialStats> type;
    Fsq_StatlessMaterialStats(String name){
        this.type=MaterialStatType.singleton(new MaterialStatsId(TConstruct.getResource(name)), this);
    }
    private static final List<Component> LOCALIZED = List.of(IMaterialStats.makeTooltip(TConstruct.getResource("extra.no_stats")));
    private static final List<Component> DESCRIPTION = List.of(Component.empty());

    @Override
    public @NotNull MaterialStatType<?> getType() {
        return this.type;
    }

    @Override
    public List<Component> getLocalizedInfo() {
        return LOCALIZED;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {

    }
}
