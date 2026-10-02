package com.deriys.divinerelics.core.networking;

import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpear;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.entities.client.render.StuckSpearRenderer;
import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import com.deriys.divinerelics.items.DraupnirSpear;
import com.deriys.divinerelics.items.HeimdallGauntlet;
import com.deriys.divinerelics.items.Motosignir;
import com.deriys.divinerelics.event.DRClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * Client-side bodies of the S2C packets.
 * <p>
 * The packet classes themselves are loaded on both physical sides (they are registered in common setup). When their
 * handlers referenced {@code ClientLevel}/{@code SoundInstance} directly, the bytecode verifier had to load those
 * client-only classes while linking the packet class, which made the mod fail to load on dedicated servers
 * ("Attempted to load class net/minecraft/client/multiplayer/ClientLevel for invalid dist DEDICATED_SERVER").
 * Everything that touches client classes now lives here and is only reached through
 * {@link net.minecraftforge.fml.DistExecutor} on the physical client.
 */
public final class DRClientPacketHandler {
    private DRClientPacketHandler() {
    }

    public static void handleStopShieldUse() {
        DRClientEvents.ForgeClientEvents.interruptShieldUse();
    }

    public static void handleThorKnockback(Vec3 motion) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.setDeltaMovement(motion);
            Minecraft.getInstance().player.hasImpulse = true;
        }
    }

    public static void handleMotosignirParticles(double playerX, double playerY, double playerZ, double viewVectorX, double viewVectorZ) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            Motosignir.addSoundWaveParticles(level, playerX, playerY, playerZ, viewVectorX, viewVectorZ);
        }
    }

    public static void handleGauntletParticles(double playerX, double playerY, double playerZ, double tpX, double tpZ) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            HeimdallGauntlet.addTeleportParticles(level, playerX, playerY, playerZ, tpX, tpZ);
        }
    }

    public static void handleSpearExplosionParticles(double x, double y, double z, double factor, double yRandom, int quantity) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            DraupnirSpear.spawnSpearExplosionParticles(level, x, y, z, factor, yRandom, quantity);
        }
    }

    public static void handleSpearParticles(double x, double y, double z, double throwerX, double throwerY, double throwerZ, float pitch, float height) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            ThrownDraupnirSpear.spawnSpearParticles(level, x, y, z, throwerX, throwerY, throwerZ, pitch, height);
        }
    }

    public static void handleStuckSpears(int entityId, List<StuckSpear> stuckSpears) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(entityId);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.getCapability(StuckSpearsProvider.STUCK_SPEARS).ifPresent(cap -> cap.setSpears(stuckSpears));
                StuckSpearRenderer.onSpearsChanged(livingEntity, !stuckSpears.isEmpty());
            }
        }
    }

    public static void handlePlayMusic(ResourceLocation soundLocation, int x, int y, int z) {
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
        if (soundEvent != null) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forRecord(soundEvent, x, y, z));
        }
    }

    public static void handleStopMusic(ResourceLocation soundLocation, SoundSource soundSource) {
        Minecraft.getInstance().getSoundManager().stop(soundLocation, soundSource);
    }
}
