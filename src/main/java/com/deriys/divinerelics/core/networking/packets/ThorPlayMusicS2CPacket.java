package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.core.networking.DRClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThorPlayMusicS2CPacket {
    private final ResourceLocation soundLocation;
    private final int x;
    private final int y;
    private final int z;

    public ThorPlayMusicS2CPacket(ResourceLocation soundLocation, int x, int y, int z) {
        this.soundLocation = soundLocation;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ThorPlayMusicS2CPacket(FriendlyByteBuf buf) {
        this.soundLocation = buf.readResourceLocation();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.soundLocation);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                DRClientPacketHandler.handlePlayMusic(this.soundLocation, x, y, z)));
        return true;
    }
}
