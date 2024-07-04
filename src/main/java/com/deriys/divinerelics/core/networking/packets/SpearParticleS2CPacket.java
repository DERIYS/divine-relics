package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpearParticleS2CPacket {
    private final double spearX;
    private final double spearY;
    private final double spearZ;
    private final double throwerX;
    private final double throwerY;
    private final double throwerZ;
    private final float pitch;
    private final float height;

    public SpearParticleS2CPacket(double spearX, double spearY, double playerZ, double throwerX, double throwerY, double throwerZ, float pitch, float height) {
        this.spearX = spearX;
        this.spearY = spearY;
        this.spearZ = playerZ;
        this.throwerX = throwerX;
        this.throwerY = throwerY;
        this.throwerZ = throwerZ;
        this.pitch = pitch;
        this.height = height;
    }

    public SpearParticleS2CPacket(FriendlyByteBuf buf) {
        spearX = buf.readDouble();
        spearY = buf.readDouble();
        spearZ = buf.readDouble();
        throwerX = buf.readDouble();
        throwerY = buf.readDouble();
        throwerZ = buf.readDouble();
        pitch = buf.readFloat();
        height = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(spearX);
        buf.writeDouble(spearY);
        buf.writeDouble(spearZ);
        buf.writeDouble(throwerX);
        buf.writeDouble(throwerY);
        buf.writeDouble(throwerZ);
        buf.writeFloat(pitch);
        buf.writeFloat(height);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                ThrownDraupnirSpear.spawnSpearParticles(level, spearX, spearY, spearZ, throwerX, throwerY, throwerZ, pitch, height);
            }
        });
        return true;
    }
}
