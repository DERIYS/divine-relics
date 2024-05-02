package com.deriys.tutorialmod.core.networking.packets;

import com.deriys.tutorialmod.items.HeimdallGauntlet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GauntletParticleS2CPacket {
    private final double playerX;
    private final double playerY;
    private final double playerZ;
    private final double tpX;
    private final double tpZ;

    public GauntletParticleS2CPacket(double playerX, double playerY, double playerZ, double tpX, double tpZ) {
        this.playerX = playerX;
        this.playerY= playerY;
        this.playerZ = playerZ;
        this.tpX = tpX;
        this.tpZ = tpZ;
    }

    public GauntletParticleS2CPacket(FriendlyByteBuf buf) {
        playerX = buf.readDouble();
        playerY = buf.readDouble();
        playerZ = buf.readDouble();
        tpX = buf.readDouble();
        tpZ = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(playerX);
        buf.writeDouble(playerY);
        buf.writeDouble(playerZ);
        buf.writeDouble(tpX);
        buf.writeDouble(tpZ);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                level.addParticle(ParticleTypes.PORTAL, playerX, playerY, playerZ, 0.0D, 0.0D, 0.0D);
                HeimdallGauntlet.addTeleportParticles(level, playerX, playerY, playerZ, tpX, tpZ);
            }
        });
        return true;
    }
}
