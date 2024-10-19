package com.deriys.divinerelics.core.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThorStopMusicS2CPacket {
    private final ResourceLocation soundLocation;
    private final SoundSource soundSource;

    public ThorStopMusicS2CPacket(ResourceLocation soundLocation, SoundSource soundSource) {
        this.soundLocation = soundLocation;
        this.soundSource = soundSource;
    }

    public ThorStopMusicS2CPacket(FriendlyByteBuf buf) {
        this.soundLocation = buf.readResourceLocation();
        this.soundSource = buf.readEnum(SoundSource.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.soundLocation);
        buf.writeEnum(this.soundSource);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().getSoundManager().stop(this.soundLocation, this.soundSource);
        });
        ctx.get().setPacketHandled(true);
    }
}
