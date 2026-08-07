package com.Fsq_tconstruct.fsq_TC.Modifiers;


import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Undying extends Modifier implements DamageBlockModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK);
    }

public class Undying extends Modifier implements  DamageBlockModifierHook {//不灭
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        // 注册 TOOL_STATS 钩子，用于修改工具的基础属性
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK);
    }
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount){
        if (source.is(DamageTypeTags.IS_FIRE)){
            LivingEntity entity = context.getEntity();
            entity.setSecondsOnFire(100);
            return true;
        }
        return false;
        //return amount; ON_ATTACKED
    }
}
