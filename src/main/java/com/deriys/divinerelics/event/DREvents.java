package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBinding;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.GauntletParticleS2CPacket;
import com.deriys.divinerelics.effects.DREffects;
import com.deriys.divinerelics.items.DRItems;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.deriys.divinerelics.items.HeimdallGauntlet.*;


public class DREvents {
    @Mod.EventBusSubscriber(modid = DivineRelics.MODID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void LivingAttackEvent(LivingAttackEvent event) {
            Entity hurtEntity = event.getEntity();
            Entity attacker = event.getSource().getEntity();

            if (hurtEntity instanceof LivingEntity livingEntity) {
                Level level = livingEntity.getLevel();
                if (livingEntity.hasEffect(DREffects.BIFROST_PROTECTION.get())){
                    if (isValidAttacker(attacker)){
                        Vec3 entityPos = livingEntity.position();
                        Vec3 attackerPos = attacker.position();

                        Vec2 attackVector = new Vec2((float) (entityPos.x - attackerPos.x), (float) (entityPos.z - attackerPos.z));
                        dodgeAttack(level, livingEntity, attackVector);
                        hurtEntity.lookAt(EntityAnchorArgument.Anchor.EYES, attackerPos.add(0, attacker.getBbHeight() * 0.8, 0));
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(DivineRelics.MODID, "properties"), new MjolnirBindingProvider());
            }
        }

//        @SubscribeEvent
//        public static void onPlayerCloned(PlayerEvent.Clone event) {
//            System.out.println("asdfasdf");
//            if(event.isWasDeath()) {
//                System.out.println("1111");
//                event.getOriginal().getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(oldStore -> {
//                    event.getEntity().getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(newStore -> {
//                        newStore.copyFrom(oldStore);
//                    });
//                });
//            }
//        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(MjolnirBinding.class);
        }

        private static boolean isValidAttacker(Entity attacker) {
            return attacker instanceof AbstractArrow || (attacker instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().getItem() != DRItems.DRAUPNIR_SPEAR.get());
        }

        public static void dodgeAttack(Level level, LivingEntity hurtEntity,  Vec2 attackVector) {
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

            level.playSound(null, entityX, entityY, entityZ,
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
        }

        public static void drawTPArea(Level level, Vec2 normVector, Vec3 entityPos, double length) {
            for (int i = -45; i <= 225; i++) {
                double vectorAngle = Math.PI * i / 180;

                Vec3 vector = rotateVector(normVector, vectorAngle, length);
                for (int j = 0; j <= 10 ; j++) {
                    Vec3 end = entityPos.add(vector.scale((double) j / 10));

                    level.addParticle(ParticleTypes.FLAME, end.x, entityPos.y+1, end.z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
