package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.core.networking.DRClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpearExplosionParticleS2CPacket {
    private final double spearX;
    private final double spearY;
    private final double spearZ;
    private final double factor;
    private final double yRandom;
    private final int quantity;

    public SpearExplosionParticleS2CPacket(double spearX, double spearY, double playerZ, double factor, double yRandom, int quantity) {
        this.spearX = spearX;
        this.spearY = spearY;
        this.spearZ = playerZ;
        this.factor = factor;
        this.yRandom = yRandom;
        this.quantity = quantity;

    }

    public SpearExplosionParticleS2CPacket(FriendlyByteBuf buf) {
        spearX = buf.readDouble();
        spearY = buf.readDouble();
        spearZ = buf.readDouble();
        factor = buf.readDouble();
        yRandom = buf.readDouble();
        quantity = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(spearX);
        buf.writeDouble(spearY);
        buf.writeDouble(spearZ);
        buf.writeDouble(factor);
        buf.writeDouble(yRandom);
        buf.writeInt(quantity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                DRClientPacketHandler.handleSpearExplosionParticles(spearX, spearY, spearZ, factor, yRandom, quantity)));
        return true;
    }
}
