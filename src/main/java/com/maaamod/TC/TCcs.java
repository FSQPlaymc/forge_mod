package com.maaamod.TC;

import com.maaamod.maaamod;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

@Mod.EventBusSubscriber(modid = maaamod.MODID)
public class TCcs extends AbstractTrait {
    public TCcs(){
        super("trait_wat_can_i_see",Integer.parseInt("7AFF83", 16));
        //第一个参数是特性的注册名，即"trait_corpse_mountain"（注意，注册名不能有大写字母），通过魔法匠魂的配置文件调用注册名就可以将词条附加到你想要的材料上
        //当然具体请看相关教程
        //第二个参数是特性的颜色，即红色(0xff0000)
    }
    @SubscribeEvent
    public static void onTargetKilled(LivingDeathEvent event) {
        // 1. 检查事件来源：是否是玩家造成的伤害？死亡的实体是否是生物？
        if (event.getSource().getTrueSource() instanceof EntityPlayer &&
                event.getEntity() instanceof EntityLiving) {

            // 2. 获取玩家、工具、NBT数据等
            World world = event.getSource().getTrueSource().world;
            ItemStack tool = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
            // 意思是在 LivingDeathEvent 事件中获取造成伤害的玩家手中的主手物品，并将其保存到 tool 变量中
            //event.getSource()
            // 返回事件源对象，类型为 DamageSource，表示造成伤害的来源，比如实体、环境、魔法等等。
            //event.getSource().getTrueSource()
            // 返回实际的伤害源对象，类型为 Entity，表示造成伤害的实体，比如玩家、生物、陷阱等等。如果伤害源不是实体，则返回 null。
            //(EntityPlayer) event.getSource().getTrueSource()
            // 将实际的伤害源对象转换为玩家对象，类型为 EntityPlayer。如果实际伤害源不是玩家，则会抛出 ClassCastException 异常。
            //((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand()
            // 获取玩家手中的主手物品，类型为 ItemStack，即物品栏中的一个物品栏槽对应的物品。如果手中没有物品，则返回一个空的 ItemStack 对象。
            //         获取事件中实体玩家手中的主手物品，并将其保存到 ItemStack 类型的变量 tool 中
            NBTTagCompound tag = TagUtil.getExtraTag(tool); // 获取工具的额外NBT标签，用于存储自定义数据
            CxmxNBTData data = CxmxNBTData.read(tag);
            //读取工具中CxmxNBTData类的数据并存储在新的CxmxNBTData类data对象中
            //      注意！注意！注意！这里的CxmxNBTData是我自定义的一个工具类，具体的data是什么请看后面我给的CxmxNBTData类代码
            //      如果要抄的话就自己新建个类把CxmxNBTData类的源代码复制上去就行
            if (!world.isRemote && TinkerUtil.hasTrait(TagUtil.getTagSafe(tool), "trait_corpse_mountain")&&data.bonus<50)
            //判断当前代码运行的环境是否为客户端:!w.isRemote
            //检查手持物品（即主手物品）是否包含了名为identifier的特质（Trait）TinkerUtil.hasTrait(TagUtil.getTagSafe(tool), identifier)
            //&& 是逻辑与运算符，用于判断两个布尔值是否都为 true
            //如果两个操作数都是 true，则表达式返回 true，否则返回 false
            //TinkerUtil.hasTrait()方法用于检查指定的物品是否包含指定的特质
            // 而TagUtil.getTagSafe()方法则用于获取物品的NBT标签数据，以便判断物品是否包含指定特质。
            //     意思就是如果当前代码运行的环境不是为客户端，且击杀生物时手持的物品包含了名为trait_corpse_mountain的匠魂词条，则会执行条件块中的代码
            {
                float health = ((EntityLiving) event.getEntity()).getMaxHealth();
                //event.getEntity()获取到了事件触发时受到伤害的实体
                //强制类型转换为EntityLiving
                //由于EntityLiving继承自EntityLivingBase类
                //可以使用getMaxHealth()方法来获取最大生命值
                //最终将该值存储在float类型的变量health中
                //         获取事件触发时实体的最大生命值并存储在float类型的health中

                data.killcount += 1;
                //击杀计数加一
                data.health = health;
                //记录击杀目标的最大生命值
                if (health>500)health=500;

                //定义了一个float类型的变量divisor，并将其赋值为25000f，用作后续的数值计算。

                float bonus = health / 500;
                //随机生成一个浮点数，范围为0到1，乘以实体的最大生命值并乘以100，得到一个值。
                //将上一步得到的值除以divisor，得到bonus的值，并四舍五入到最近的整数。
                //       定义了一个浮点型变量bonus，并通过一系列计算得出击杀目标后提升的伤害

                data.bonus += bonus;
                //成长奖励增加

                data.bonus = (float) Math.round(data.bonus * 100f) / 100f;
                //Math.round(data.bonus * 100f) / 100f的意思是将data.bonus乘以100并四舍五入到最接近的整数
                //然后再除以100来保留两位小数。最后将计算得到的结果赋值给data.bonus变量
                //       作用是将data.bonus变量的值经过计算后保留两位小数

                data.write(tag);
                //将 data 对象中的数据写入 tag 对象中

                TagUtil.setExtraTag(tool, tag);

                // TagUtil 是一个工具类，它提供了一组方便的静态方法来操作 NBT 标签数据
                //setExtraTag 方法用于在物品栏中设置一个附加的 NBT 标签数据
                //这个方法的第一个参数是一个 ItemStack 对象，表示需要设置标签数据的物品，
                // 第二个参数是一个 NBTTagCompound 对象，表示需要写入的 NBT 标签数据。
                //     也就是将计算后增加的伤害 tag 对象作为附加数据写入 tool 物品栏中进行保存
                //MC里想要保存数据你需要使用nbt来进行保存
            }
            // 3. 在这里实现你的核心逻辑，例如：
        }
    }
    //将上面的结果用来改变伤害
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
        NBTTagCompound tag = TagUtil.getExtraTag(tool);
        //获取 tool 物品栏中附加标签的 NBTTagCompound 对象并记录为tag

        CxmxNBTData data = CxmxNBTData.read(tag);
        //读取工具中CxmxNBTData类的数据并存储在新的CxmxNBTData类data对象中

        float bonus = data.bonus;
        //获取成长奖励

        return newDamage + bonus;
        //将源伤害加上这个新伤害并返回，也就是修改伤害
    }



//    @Override
//    public void onPlayerHurt(ItemStack tool, EntityPlayer player, EntityLivingBase attacker, LivingHurtEvent event) {
//        super.onPlayerHurt(tool, player, attacker, event);
//    }
}




