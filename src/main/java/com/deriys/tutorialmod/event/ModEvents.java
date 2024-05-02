package com.deriys.tutorialmod.event;

import com.deriys.tutorialmod.TutorialMod;
import com.deriys.tutorialmod.core.networking.ModMessages;
import com.deriys.tutorialmod.core.networking.packets.GauntletParticleS2CPacket;
import com.deriys.tutorialmod.effects.ModEffects;
import com.deriys.tutorialmod.items.HeimdallGauntlet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class ModEvents {
    @Mod.EventBusSubscriber(modid = TutorialMod.MODID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void LivingAttackEvent(LivingAttackEvent event) {
            Entity hurtEntity = event.getEntity();
            Entity attacker = event.getSource().getEntity();
            if (hurtEntity instanceof LivingEntity livingEntity) {
                Level level = hurtEntity.getLevel();
                if (livingEntity.hasEffect(ModEffects.BIFROST_PROTECTION.get())){
                    if (attacker instanceof AbstractArrow || (attacker instanceof LivingEntity && ((LivingEntity) attacker).getMainHandItem().getItem() != Items.TRIDENT)){
                        dodgeAttack(level, livingEntity);
                        event.setCanceled(true);
                    }
                }
            }
        }

        public static void dodgeAttack(Level level, LivingEntity hurtEntity) {
            double playerX = hurtEntity.getX();
            double playerY = hurtEntity.getY();
            double playerZ = hurtEntity.getZ();

            double tpX = HeimdallGauntlet.getRandomPos(playerX);
            double tpZ = HeimdallGauntlet.getRandomPos(playerZ);

            hurtEntity.teleportTo(tpX, playerY, tpZ);
            if (level instanceof ServerLevel) {
                GauntletParticleS2CPacket packet = new GauntletParticleS2CPacket(playerX, playerY, playerZ, tpX, tpZ);
                ModMessages.sendToChunk(packet, level.getChunkAt(hurtEntity.blockPosition()));
            }
            level.playSound(null, playerX, playerY, playerZ,
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
        }
    }
}
