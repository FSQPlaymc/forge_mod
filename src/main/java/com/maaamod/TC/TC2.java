package com.maaamod.TC;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;

public class TC2 extends AbstractTrait {
    public TC2(String identifier, int color) {
        super(identifier, color);
    }
    @Override//覆盖方法
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical)
    //该方法的名称是 damage，返回一个 float 类型的值。该方法有 6 个参数：
    //tool：一个 ItemStack 类型的参数，表示正在使用的工具。
    //player：一个 EntityLivingBase 类型的参数，表示使用工具的玩家。
    //target：一个 EntityLivingBase 类型的参数，表示被攻击的实体。
    //damage：一个 float 类型的参数，表示原始伤害值。
    //newDamage：一个 float 类型的参数，表示计算后的伤害值。
    //isCritical：一个 boolean 类型的参数，表示攻击是否为暴击。
    //       该方法的作用是根据玩家使用的工具和攻击目标的不同，计算并返回修改后的伤害值。
    //       补充，你这个类所继承AbstractTrait抽象类还有很多方法用来修改匠魂工具不同行为的效果，想要查看这些方法可以使用Ctrl点击damage这个方法名跳转查看
    {


        return newDamage + 100;
        //将源伤害加上这个新伤害并返回，也就是修改伤害
    }
}
