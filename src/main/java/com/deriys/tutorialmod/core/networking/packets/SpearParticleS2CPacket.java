package com.deriys.tutorialmod.core.networking.packets;

import com.deriys.tutorialmod.entities.ThrownDraupnirSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpearParticleS2CPacket {
    private final double spearX;
    private final double spearY;
    private final double spearZ;
    private final float pitch;
    private final float yaw;
    private final float height;

    public SpearParticleS2CPacket(double spearX, double spearY, double playerZ, float pitch, float yaw, float height) {
        this.spearX = spearX;
        this.spearY = spearY;
        this.spearZ = playerZ;
        this.pitch = pitch;
        this.yaw = pitch;
        this.height = height;
    }

    public SpearParticleS2CPacket(FriendlyByteBuf buf) {
        spearX = buf.readDouble();
        spearY = buf.readDouble();
        spearZ = buf.readDouble();
        pitch = buf.readFloat();
        yaw = buf.readFloat();
        height = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(spearX);
        buf.writeDouble(spearY);
        buf.writeDouble(spearZ);
        buf.writeFloat(pitch);
        buf.writeFloat(yaw);
        buf.writeFloat(height);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                ThrownDraupnirSpear.spawnSpearParticles(level, spearX, spearY, spearZ, pitch, yaw, height);
            }
        });
        return true;
    }
}
