package com.Fsq_tconstruct.fsq_TC.Modifiers;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Crystal_Oscillator extends Modifier implements BlockBreakModifierHook {//晶振
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.BLOCK_BREAK);
    }
    public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context){
        Player player= context.getPlayer();
        MobEffectInstance a=new MobEffectInstance(MobEffects.DIG_SPEED,(100+100*modifier.intEffectiveLevel()),modifier.intEffectiveLevel()-1);
        if (player != null) {
            player.addEffect(a);//MobEffectInstance
        }
    }
}
