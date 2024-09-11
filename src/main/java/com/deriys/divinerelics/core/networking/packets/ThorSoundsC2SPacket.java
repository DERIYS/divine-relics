package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.entities.ai.thor.ThorAttackState;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThorSoundsC2SPacket {
    int entityId;
    byte soundId;
    public ThorSoundsC2SPacket(int entityId, byte soundId) {
        this.entityId = entityId;
        this.soundId = soundId;
    }

    public ThorSoundsC2SPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        soundId = buf.readByte();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(soundId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.level.getEntity(this.entityId) instanceof ThorEntity thorEntity) {
                thorEntity.playAttackSound(ThorAttackState.values()[this.soundId]);
            }
        });
        return true;
    }
}
