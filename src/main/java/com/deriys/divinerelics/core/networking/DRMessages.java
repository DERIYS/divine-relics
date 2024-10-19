package com.deriys.divinerelics.core.networking;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.core.networking.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class DRMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    public static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(DivineRelics.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(MjolnirBindingC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MjolnirBindingC2SPacket::new)
                .encoder(MjolnirBindingC2SPacket::toBytes)
                .consumerMainThread(MjolnirBindingC2SPacket::handle)
                .add();

        net.messageBuilder(LeviathanBindingC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LeviathanBindingC2SPacket::new)
                .encoder(LeviathanBindingC2SPacket::toBytes)
                .consumerMainThread(LeviathanBindingC2SPacket::handle)
                .add();

        net.messageBuilder(ThorAnimationC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ThorAnimationC2SPacket::new)
                .encoder(ThorAnimationC2SPacket::toBytes)
                .consumerMainThread(ThorAnimationC2SPacket::handle)
                .add();

        net.messageBuilder(MotosignirParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MotosignirParticleS2CPacket::new)
                .encoder(MotosignirParticleS2CPacket::toBytes)
                .consumerMainThread(MotosignirParticleS2CPacket::handle)
                .add();

        net.messageBuilder(GauntletParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GauntletParticleS2CPacket::new)
                .encoder(GauntletParticleS2CPacket::toBytes)
                .consumerMainThread(GauntletParticleS2CPacket::handle)
                .add();

        net.messageBuilder(SpearExplosionParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpearExplosionParticleS2CPacket::new)
                .encoder(SpearExplosionParticleS2CPacket::toBytes)
                .consumerMainThread(SpearExplosionParticleS2CPacket::handle)
                .add();

        net.messageBuilder(SpearParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpearParticleS2CPacket::new)
                .encoder(SpearParticleS2CPacket::toBytes)
                .consumerMainThread(SpearParticleS2CPacket::handle)
                .add();

        net.messageBuilder(StuckSpearsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StuckSpearsS2CPacket::new)
                .encoder(StuckSpearsS2CPacket::toBytes)
                .consumerMainThread(StuckSpearsS2CPacket::handle)
                .add();

        net.messageBuilder(ThorPlayMusicS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ThorPlayMusicS2CPacket::new)
                .encoder(ThorPlayMusicS2CPacket::toBytes)
                .consumerMainThread(ThorPlayMusicS2CPacket::handle)
                .add();

        net.messageBuilder(ThorStopMusicS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ThorStopMusicS2CPacket::new)
                .encoder(ThorStopMusicS2CPacket::toBytes)
                .consumerMainThread(ThorStopMusicS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToChunk(MSG message, LevelChunk chunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <MSG> void sendToTrackingEntity(MSG message, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
