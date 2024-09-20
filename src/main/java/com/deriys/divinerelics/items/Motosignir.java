package com.deriys.divinerelics.items;

import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.MotosignirParticleS2CPacket;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.deriys.divinerelics.init.DREffects;
import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import com.deriys.divinerelics.init.DRSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.deriys.divinerelics.capabilities.teammates.TeammatesProvider.hasTeammate;


public class Motosignir extends Item {
    public static final MobEffect[] STUN_EFFECTS = {
            MobEffects.CONFUSION,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.WEAKNESS
    };

    public static final int EFFECTS_DURATION = 200;
    public static final int AMPLIFIER = 2;

    public Motosignir(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Sindri's tuning fork. Somehow it survived Ragnarok..."));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();

        Vec3 viewVector = player.getLookAngle().normalize();

        if (!level.isClientSide() && !player.isShiftKeyDown()) {

            releaseSoundWave(level, player, playerX, playerY, playerZ);
            if (hand == InteractionHand.MAIN_HAND) {
                player.getCooldowns().addCooldown(this, 200);
            }
            else {
                player.getCooldowns().addCooldown(this, 400);
            }

            MotosignirParticleS2CPacket particlePacket = new MotosignirParticleS2CPacket(playerX, playerY, playerZ, viewVector.x, viewVector.z);
            DRMessages.sendToChunk(particlePacket, level.getChunkAt(player.blockPosition()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static void addSoundWaveParticles(Level level, double playerX, double playerY, double playerZ, double viewVectorX, double viewVectorZ) {
        int particles = 50;

        for (int i = 0; i < particles; i++) {
            level.addParticle(ParticleTypes.LAVA, playerX + viewVectorX, playerY + 1D, playerZ + viewVectorZ,
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
    }


    private void releaseSoundWave(Level level, Player player, double playerX, double playerY, double playerZ) {
        level.playSound(null, player.getOnPos(), DRSounds.MOTOSIGNIR_SOUND_WAVE.get(), SoundSource.PLAYERS,
                3.0f, 1);

        int radius = 10;

        AABB aabb = new AABB(playerX - radius, playerY - radius, playerZ - radius,
                playerX + radius, playerY + radius, playerZ + radius);

        List<LivingEntity> entitiesInRadius = getEntitiesInArea(level, playerX, playerY, playerZ, radius);

        hurtAndKnockbackEntites(entitiesInRadius, player, STUN_EFFECTS, 20f, 1f, AMPLIFIER, EFFECTS_DURATION);
    }

    public static List<LivingEntity> getEntitiesInArea(Level level, double x, double y, double z, double radius) {
        AABB aabb = new AABB(x - radius, y - radius, z - radius,
                x + radius, y + radius, z + radius);
        return level.getEntitiesOfClass(LivingEntity.class, aabb);
    }

    public static void hurtAndKnockbackEntites (List<LivingEntity> entities, LivingEntity owner, Entity attacker, DamageSource damageSource, float baseDamage, double force) {
        for(LivingEntity livingEntity: entities) {
            if (livingEntity != owner && !hasTeammate(owner, livingEntity)) {
                double entityX = livingEntity.getX();
                double entityZ = livingEntity.getZ();

                double angle = Math.atan2(entityZ - attacker.getZ(), entityX - attacker.getX());
                double ratioX = -Math.cos(angle);
                double ratioZ = -Math.sin(angle);

                float armorModifier = (float) (livingEntity.getAttributeValue(Attributes.ARMOR)) / 12;
                float adjustedDamage = baseDamage + armorModifier;

                ItemStack weapon = owner.getMainHandItem();
                adjustedDamage += EnchantmentHelper.getDamageBonus(weapon, livingEntity.getMobType());

                float distanceToEnemy = (float) livingEntity.getEyePosition().subtract(attacker.position()).length();

                float distanceFactor = (1f - (distanceToEnemy -1.5f) / (distanceToEnemy + 6.5f));

//                owner.sendSystemMessage(Component.literal("Dealt " + Math.floor(Math.min(adjustedDamage, adjustedDamage * distanceFactor)*100)/100 + " damage to " + livingEntity.getDisplayName().getString() + " at " + Math.floor(distanceToEnemy*100)/100 + " distance"));

                if (!livingEntity.hasEffect(DREffects.BIFROST_PROTECTION.get()) || damageSource.getEntity() instanceof ThorEntity) {
                    livingEntity.knockback(force, ratioX, ratioZ);
                }

                livingEntity.hurt(damageSource, Math.min(adjustedDamage, adjustedDamage * distanceFactor));

                EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                EnchantmentHelper.doPostDamageEffects(owner, livingEntity);

                if (attacker instanceof ThrownDraupnirSpear) {
                    livingEntity.invulnerableTime = 1;
                }
            }
        }
    }


    public static void hurtAndKnockbackEntites (List<LivingEntity> entities, Player player, MobEffect[] mobEffects, float baseDamage, double force, int amplifier, int duration) {
        for(LivingEntity livingEntity: entities) {
            if (livingEntity != player && (!(livingEntity instanceof Player && ((Player) livingEntity).isCreative())) && !hasTeammate(player, livingEntity)) {
                double entityX = livingEntity.getX();
                double entityZ = livingEntity.getZ();

                double angle = Math.atan2(entityZ - player.getZ(), entityX - player.getX());
                double ratioX = -Math.cos(angle);
                double ratioZ = -Math.sin(angle);

                float armorModifier = (float) (livingEntity.getAttributeValue(Attributes.ARMOR)) / 12;
                float adjustedDamage = baseDamage + armorModifier;

                ItemStack weapon = player.getMainHandItem();
                adjustedDamage += EnchantmentHelper.getDamageBonus(weapon, livingEntity.getMobType());

                DamageSource damageSource = DamageSource.playerAttack(player);
                float distanceToEnemy = (float) livingEntity.position().subtract(player.position()).length();

                float distanceFactor = (1f - (distanceToEnemy -1.5f) / (distanceToEnemy + 6.5f));

//                player.sendSystemMessage(Component.literal("Dealt " + Math.floor(Math.min(adjustedDamage, adjustedDamage * distanceFactor)*100)/100 + " damage to " + livingEntity.getDisplayName().getString() + " at " + Math.floor(distanceToEnemy*100)/100 + " distance"));

                livingEntity.hurt(damageSource, Math.min(adjustedDamage, adjustedDamage * distanceFactor));

                EnchantmentHelper.doPostHurtEffects(livingEntity, player);
                EnchantmentHelper.doPostDamageEffects(player, livingEntity);

                if (!livingEntity.hasEffect(DREffects.BIFROST_PROTECTION.get())) {
                    livingEntity.knockback(force, ratioX, ratioZ);
                    gainMobEffects(livingEntity, mobEffects, duration, amplifier);
                }
            }
        }
    }

    public static void applyKnockBack(LivingEntity livingEntity, Entity attacker, double force) {
        double entityX = livingEntity.getX();
        double entityZ = livingEntity.getZ();

        double angle = Math.atan2(entityZ - attacker.getZ(), entityX - attacker.getX());
        double ratioX = -Math.cos(angle);
        double ratioZ = -Math.sin(angle);
        applyKnockBack(livingEntity, force, ratioX, ratioZ);
    }

    public static void applyKnockBack(LivingEntity livingEntity, double force, double ratioX, double ratioZ) {
        force *= 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (!(force <= 0.0D)) {
            livingEntity.hasImpulse = true;
            Vec3 vec3 = livingEntity.getDeltaMovement();
            Vec3 vec31 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale(force);
            livingEntity.setDeltaMovement(vec3.x / 2.0D - vec31.x, livingEntity.isOnGround() ? Math.min(0.4D, vec3.y / 2.0D + force) : vec3.y, vec3.z / 2.0D - vec31.z);
        }
    }

    public static void gainMobEffects(LivingEntity livingEntity, MobEffect[] mobEffects, int effectsDuration, int amplifier) {
        for(MobEffect mobEffect: mobEffects) {
            livingEntity.addEffect(new MobEffectInstance(mobEffect, effectsDuration, amplifier));
        }
    }
}
