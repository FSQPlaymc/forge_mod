package com.Fsq_tconstruct.fsq_TC.Modifiers;

import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class RegisterModifiers {
    static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(fsq_tconstruct.MODID);

    public static final StaticModifier<fsq_Modifier_1> myTrait = MODIFIERS.register("my_trait", fsq_Modifier_1::new);
    public static final StaticModifier<Crystal_Oscillator> Crystal_Oscillator = MODIFIERS.register("crystal_oscillator", Crystal_Oscillator::new);
    public static final StaticModifier<Crystal_Thorn> Crystal_Thorn = MODIFIERS.register("crystal_thorn", Crystal_Thorn::new);
    public static final StaticModifier<TargetedAttackModifier> targetedAttack = MODIFIERS.register("targeted_attack", TargetedAttackModifier::new);

    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
        //MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
