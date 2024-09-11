package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.entities.entity.ThorEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThorAnimationC2SPacket {

    int attackingTicksClient;
    int entityId;
    public ThorAnimationC2SPacket(int attackingTicksClient, int entityId) {
        this.attackingTicksClient = attackingTicksClient;
        this.entityId = entityId;
    }

    public ThorAnimationC2SPacket(FriendlyByteBuf buf) {
        attackingTicksClient = buf.readInt();
        entityId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(attackingTicksClient);
        buf.writeInt(entityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.level.getEntity(this.entityId) instanceof ThorEntity thorEntity) {
                thorEntity.setAttackingTicks(this.attackingTicksClient);
            }
        });
        return true;
    }
}
