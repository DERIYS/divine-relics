package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.core.networking.DRClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MotosignirParticleS2CPacket {
    private final double playerX;
    private final double playerY;
    private final double playerZ;
    private final double viewVectorX;
    private final double viewVectorZ;

    public MotosignirParticleS2CPacket(double playerX, double playerY, double playerZ, double viewVectorX, double viewVectorZ) {
        this.playerX = playerX;
        this.playerY= playerY;
        this.playerZ = playerZ;
        this.viewVectorX = viewVectorX;
        this.viewVectorZ = viewVectorZ;
    }

    public MotosignirParticleS2CPacket(FriendlyByteBuf buf) {
        playerX = buf.readDouble();
        playerY = buf.readDouble();
        playerZ = buf.readDouble();
        viewVectorX = buf.readDouble();
        viewVectorZ = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(playerX);
        buf.writeDouble(playerY);
        buf.writeDouble(playerZ);
        buf.writeDouble(viewVectorX);
        buf.writeDouble(viewVectorZ);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                DRClientPacketHandler.handleMotosignirParticles(playerX, playerY, playerZ, viewVectorX, viewVectorZ)));
        return true;
    }
}
