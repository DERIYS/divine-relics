package com.deriys.tutorialmod.items.custom;

import com.deriys.tutorialmod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;


public class Motosignir extends Item {
    private final MobEffect[] NEGATIVE_EFFECTS = {
            MobEffects.CONFUSION,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.WEAKNESS
    };

    private final int EFFECTS_DURATION = 100;
    private final int AMPLIFIER = 1;

    public Motosignir(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {

            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();

            releaseSoundWave(level, player, playerX, playerY, playerZ);

            if (hand == InteractionHand.MAIN_HAND) {
                player.getCooldowns().addCooldown(this, 200);
            }
            else {
                player.getCooldowns().addCooldown(this, 400);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private void releaseSoundWave(Level level, Player player, double playerX, double playerY, double playerZ) {
        level.playSound(null, player.getOnPos(), ModSounds.MOTOSIGNIR_SOUND_WAVE.get(), SoundSource.PLAYERS,
                3.0f, 1);

        int radius = 10;

        AABB aabb = new AABB(playerX - radius, playerY - radius, playerZ - radius,
                playerX + radius, playerY + radius, playerZ + radius);

        List<LivingEntity> entitiesInRadius = level.getEntitiesOfClass(LivingEntity.class, aabb);

        for(LivingEntity livingEntity: entitiesInRadius) {
            if(livingEntity != player) {
                BlockPos entityPos = livingEntity.blockPosition();
                int entityX = entityPos.getX();
                int entityZ = entityPos.getZ();

                double angle = Math.atan2(entityZ - playerZ, entityX - playerX);
                double ratioX = -Math.cos(angle);
                double ratioZ = -Math.sin(angle);

                livingEntity.hurt(DamageSource.playerAttack(player), 20F);
                livingEntity.knockback(1f, ratioX, ratioZ);
                gainPotionEffects(livingEntity, NEGATIVE_EFFECTS, EFFECTS_DURATION, AMPLIFIER);
            }
        }
    }

    private void gainPotionEffects(LivingEntity livingEntity, MobEffect[] negativeEffects, int effectsDuration, int amplifier) {
        for(MobEffect poisonEffect: negativeEffects) {
            livingEntity.addEffect(new MobEffectInstance(poisonEffect, effectsDuration, amplifier));
        }
    }
}
