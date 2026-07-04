package com.Fsq_tconstruct.fsq_TC.Tool;

import com.Fsq_tconstruct.fsq_TC.other.Fsq_StatlessMaterialStats;
import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class FsqToolParts {
    // 使用 TiC 扩展的 DeferredRegister，支持 registerCast 方法
    public static final ItemDeferredRegisterExtension PARTS =
            new ItemDeferredRegisterExtension(fsq_tconstruct.MODID);

    private static final Item.Properties ITEM_PROPS = new Item.Properties();

    // 注册十字结交条部件 (绑定结类型，无属性加成)
    public static final ItemObject<ToolPartItem> CROSS_KNOT = PARTS.register(
            "cross_knot",
            () -> new ToolPartItem(ITEM_PROPS, StatlessMaterialStats.BINDING.getIdentifier()));

    // 注册十字结的三组铸造模具 (金模具 / 砂模具 / 红砂模具)
    public static final CastItemObject CROSS_KNOT_CAST = PARTS.registerCast(
            "cross_knot",
            () -> new PartCastItem(ITEM_PROPS, CROSS_KNOT));
    //注册魂珠 魂珠类型
    public static final ItemObject<ToolPartItem> SOUL_ORB = PARTS.register(
            "soul_orb",
            () -> new ToolPartItem(ITEM_PROPS, Fsq_StatlessMaterialStats.HUN_ZHU.getIdentifier()));
    //注册魂珠的三组铸造模具 (金模具 / 砂模具 / 红砂模具)
    public static final CastItemObject SOUL_ORB_CAST = PARTS.registerCast(
            "cross/soul_orb",
            () -> new PartCastItem(ITEM_PROPS, SOUL_ORB));

    public static void register(IEventBus bus) { PARTS.register(bus); }
}