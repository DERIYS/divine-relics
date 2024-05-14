package com.deriys.tutorialmod.core.networking.packets;

import com.deriys.tutorialmod.items.DraupnirSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpearExplosionParticleS2CPacket {
    private final double spearX;
    private final double spearY;
    private final double spearZ;

    public SpearExplosionParticleS2CPacket(double spearX, double spearY, double playerZ) {
        this.spearX = spearX;
        this.spearY = spearY;
        this.spearZ = playerZ;
    }

    public SpearExplosionParticleS2CPacket(FriendlyByteBuf buf) {
        spearX = buf.readDouble();
        spearY = buf.readDouble();
        spearZ = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(spearX);
        buf.writeDouble(spearY);
        buf.writeDouble(spearZ);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                DraupnirSpear.spawnSpearExplosionParticles(level, spearX, spearY, spearZ);
            }
        });
        return true;
    }
}
