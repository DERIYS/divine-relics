package com.deriys.tutorialmod.core.networking;

import com.deriys.tutorialmod.TutorialMod;
import com.deriys.tutorialmod.core.networking.packets.GauntletParticleS2CPacket;
import com.deriys.tutorialmod.core.networking.packets.MotosignirParticleS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    public static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TutorialMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

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
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToChunk(MSG message, LevelChunk chunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }
}
