package com.deriys.tutorialmod.items.custom;

import com.deriys.tutorialmod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class Motosignir extends Item {
    private final MobEffect[] NEGATIVE_EFFECTS = {
            MobEffects.CONFUSION,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.WEAKNESS
    };

    private final int EFFECTS_DURATION = 140;
    private final int AMPLIFIER = 1;

    public Motosignir(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();

        Vec3 viewVector = getViewVector(player).normalize();

        if (!level.isClientSide()) {

            releaseSoundWave(level, player, playerX, playerY, playerZ);

            if (hand == InteractionHand.MAIN_HAND) {
                player.getCooldowns().addCooldown(this, 200);
            }
            else {
                player.getCooldowns().addCooldown(this, 400);
            }
        }
        else {
            addSoundWaveParticles(level, playerX, playerY, playerZ, viewVector);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @NotNull
    private static Vec3 getViewVector(Player player) {
        float yaw = player.getYRot();
        float pitch = player.getXRot();

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3(x, y, z);
    }

    private void addSoundWaveParticles(Level level, double playerX, double playerY, double playerZ, Vec3 viewVector) {
        int particles = 50;

        for (int i = 0; i < particles; i++) {
            level.addParticle(ParticleTypes.LAVA, playerX + viewVector.x, playerY + 1D, playerZ + viewVector.z,
                    0D, 0D, 0D);
            for (int j = 0; j < particles; j++) {
                double horizontalAngle = 2 * Math.PI * i / particles;
                double verticalAngle = Math.PI * j / particles;

                double xSpeed = Math.cos(horizontalAngle) * Math.sin(verticalAngle);
                double ySpeed = Math.cos(verticalAngle);
                double zSpeed = Math.sin(verticalAngle) * Math.sin(horizontalAngle);

                level.addParticle(ParticleTypes.FLAME, playerX, playerY + 1D, playerZ, xSpeed, ySpeed, zSpeed);
            }
        }

//        int particles = 50;
//
//        for (int i = 0; i <= particles; i++) {
//            double adjustX = (double) 2*i / particles;
//            double adjustY = Math.sin(3*adjustX)/3;
//            level.addParticle(ParticleTypes.FLAME, playerX, playerY, playerZ,
//                    viewVector.x * ((double) i/15)*Math.sin(i), viewVector.y * ((double) i/15)*Math.cos(i), viewVector.z * i/15);
//        }
//        for (int i = 0; i < particles; i++) {
//            double adjustX = (double) 2*i / particles;
//            double adjustY = Math.sin(3*adjustX);
//
//            level.addParticle(ParticleTypes.FLAME, playerX, playerY, playerZ,
//                    viewVector.get(Direction.Axis.X) * adjustX, adjustY/3, viewVector.get(Direction.Axis.Z) * 0D);
//        }
//        level.addParticle(ParticleTypes.LAVA, playerX, playerY, playerZ, 0D, 0D, 0D);
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

                float baseDamage = 20F; // Your initial damage value
                float armorModifier = livingEntity.getArmorValue() * 1.5F;
                float adjustedDamage = baseDamage + armorModifier;

                livingEntity.hurt(DamageSource.playerAttack(player), adjustedDamage);
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
