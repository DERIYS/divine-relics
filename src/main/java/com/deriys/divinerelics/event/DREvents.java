package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBinding;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.GauntletParticleS2CPacket;
import com.deriys.divinerelics.effects.DREffects;
import com.deriys.divinerelics.entities.ThrownDraupnirSpear;
import com.deriys.divinerelics.entities.ThrownMjolnir;
import com.deriys.divinerelics.items.DRItems;
import com.deriys.divinerelics.items.Mjolnir;
import com.deriys.divinerelics.items.Motosignir;
import com.deriys.divinerelics.sound.DRSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;
import static com.deriys.divinerelics.items.HeimdallGauntlet.*;


public class DREvents {
    @Mod.EventBusSubscriber(modid = DivineRelics.MODID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void LivingAttackEvent(LivingAttackEvent event) {
            Entity hurtEntity = event.getEntity();
            Entity attacker = event.getSource().getEntity();
            Entity directAttacker = event.getSource().getDirectEntity();

            if (hurtEntity instanceof LivingEntity livingEntity) {
                Level level = livingEntity.getLevel();
                if (livingEntity.hasEffect(DREffects.BIFROST_PROTECTION.get())) {
                    System.out.println(attacker instanceof Player);
                    System.out.println(directAttacker instanceof ThrownDraupnirSpear);
                    if (isValidAttacker(attacker, directAttacker)) {
                        Vec3 entityPos = livingEntity.position();
                        Vec3 attackerPos = attacker.position();

                        Vec2 attackVector = new Vec2((float) (entityPos.x - attackerPos.x), (float) (entityPos.z - attackerPos.z));
                        dodgeAttack(level, livingEntity, attackVector);
                        hurtEntity.lookAt(EntityAnchorArgument.Anchor.EYES, attackerPos.add(0, attacker.getBbHeight() * 0.8, 0));
                        event.setCanceled(true);
                    }
                }
            }

            if (hurtEntity instanceof Player player && attacker instanceof LivingEntity livingAttacker) {
                if (player.isBlocking() && player.getOffhandItem().getItem() == DRItems.GUARDIAN_SHIELD.get()) {
                    int ticksInUse = player.getTicksUsingItem();
                    if (ticksInUse <= 12) {
                        float amount = event.getAmount();
                        float damage = (amount < 3)? 2: amount / 1.2f;
                        player.getLevel().playSound(null, player.getOnPos(), DRSounds.GUARDIAN_SHIELD_PARRY.get(), SoundSource.PLAYERS, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
                        Motosignir.hurtAndKnockbackEntites(List.of(livingAttacker), player, Motosignir.NEGATIVE_EFFECTS, damage, Math.min(Math.log10(damage) / 2.5, 1.0f), 1, 100);
                        player.stopUsingItem();
                    }
                }
            }

            if (hurtEntity instanceof LivingEntity livingHurt && attacker instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.getItem() instanceof Mjolnir mjolnir && !player.getLevel().isClientSide) {
                    if (mjolnir.isFlying) {
                        player.getCooldowns().addCooldown(mjolnir, 20);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onShieldBlock(ShieldBlockEvent event) {
            LivingEntity entity = event.getEntity();
            Entity attacker = event.getDamageSource().getDirectEntity();
            if (attacker instanceof ThrownMjolnir || attacker instanceof ThrownDraupnirSpear) {
                if (entity.getOffhandItem().getItem() != DRItems.GUARDIAN_SHIELD.get()) {
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerFall(LivingFallEvent event) {
            if (event.getEntity() instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.getItem() instanceof Mjolnir mjolnir && !player.getLevel().isClientSide) {
                    if (mjolnir.isFlying) {
                        if (event.getDistance() > 20) {
                            System.out.println(getVelocity(player));
                            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 80, 2));
                            ThrownMjolnir.onHitLighnting(event.getEntity().getLevel(), player, player.getOnPos(), player, ((float) Math.min(Math.max(10f, 5f * getVelocity(player)), 40f)), Math.max(1.0f, (float) (getVelocity(player) / 2)), 5);
                            player.getCooldowns().addCooldown(mjolnir, 70);
                        }
                        player.startAutoSpinAttack(1);
                        mjolnir.isFlying = false;
                        event.setCanceled(true);
                    }

                }
            }
        }

        @SubscribeEvent
        public static void onLightningStrike(EntityStruckByLightningEvent event) {
            if (event.getEntity() instanceof Player player && (player.getMainHandItem().getItem() == DRItems.MJOLNIR.get() || player.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).isPresent() || player.hasEffect(DREffects.BIFROST_PROTECTION.get()))) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(DivineRelics.MODID, "properties"), new MjolnirBindingProvider());
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(MjolnirBinding.class);
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if(event.isWasDeath()) {
                Player original = event.getOriginal();
                original.revive();
                original.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(oldStore -> {
                    event.getEntity().getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        public static double getVelocity(Player player) {
            double deltaX = player.getX() - player.xOld;
            double deltaY = player.getY() - player.yOld;
            double deltaZ = player.getZ() - player.zOld;
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        }

        private static boolean isValidAttacker(Entity attacker, Entity directAttacker) {
            return !(directAttacker instanceof ThrownDraupnirSpear) && (attacker instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().getItem() != DRItems.DRAUPNIR_SPEAR.get());
        }

        public static void dodgeAttack(Level level, LivingEntity hurtEntity, Vec2 attackVector) {
            Vec3 entityPos = hurtEntity.position();

            double entityX = hurtEntity.getX();
            double entityY = hurtEntity.getY();
            double entityZ = hurtEntity.getZ();

            Vec2 attackVNorm = findNormVec(attackVector);

            double scale = new Random().nextFloat() * 1.5 + 0.5;

            Vec3 tpVector = getTPVector(level, attackVNorm, entityPos, scale);

            hurtEntity.teleportTo(tpVector.x, tpVector.y, tpVector.z);

            if (level instanceof ServerLevel) {
                GauntletParticleS2CPacket packet = new GauntletParticleS2CPacket(entityX, entityY, entityZ, tpVector.x, tpVector.z);
                DRMessages.sendToChunk(packet, level.getChunkAt(hurtEntity.blockPosition()));
            }

            level.playSound(null, entityX, entityY, entityZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
        }

        public static void drawTPArea(Level level, Vec2 normVector, Vec3 entityPos, double length) {
            for (int i = -45; i <= 225; i++) {
                double vectorAngle = Math.PI * i / 180;

                Vec3 vector = rotateVector(normVector, vectorAngle, length);
                for (int j = 0; j <= 10; j++) {
                    Vec3 end = entityPos.add(vector.scale((double) j / 10));

                    level.addParticle(ParticleTypes.FLAME, end.x, entityPos.y + 1, end.z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
