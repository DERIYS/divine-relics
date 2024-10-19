package com.deriys.divinerelics.core.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

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
        context.enqueueWork(() -> {
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(this.soundLocation);
            if (soundEvent != null) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forRecord(soundEvent, x, y, z));
            }
        });
        return true;
    }
}
