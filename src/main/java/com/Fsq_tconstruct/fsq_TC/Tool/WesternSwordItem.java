package com.Fsq_tconstruct.fsq_TC.Tool;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

public class WesternSwordItem extends ModifiableSwordItem {
    public WesternSwordItem() {
        super(new Item.Properties(), FsqTools.XI_YANG_JIAN);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (player.level().isClientSide && entity instanceof LivingEntity) {
            player.level().addParticle(ParticleTypes.SWEEP_ATTACK,
                    entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                    0, 0, 0);
            player.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1.0F, 0.8F);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
