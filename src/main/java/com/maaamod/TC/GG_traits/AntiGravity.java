package com.maaamod.TC.GG_traits;

import net.minecraft.nbt.NBTTagCompound;
import scala.reflect.internal.Symbols;
import scala.tools.nsc.transform.SpecializeTypes;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class AntiGravity extends AbstractTrait {

    public AntiGravity() {
        super("AntiGravity", Integer.parseInt("B18CEC", 16));
    }

    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        ToolNBT data = TagUtil.getToolStats(rootCompound);
        data.attackSpeedMultiplier = 5.1F;
        TagUtil.setToolTag(rootCompound, data.get());
    }
}
