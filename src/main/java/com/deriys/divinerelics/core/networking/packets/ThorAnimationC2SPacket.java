package com.deriys.divinerelics.core.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
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

    /**
     * The mod never sends this packet: Thor's attack timer is fully server-authoritative (see
     * {@code ThorEntity#aiStep}). Applying the client-supplied value let any (modified) client freeze or skip the
     * boss' attacks with arbitrary tick counts, so the payload is still decoded (keeping the channel's packet ids
     * stable) but intentionally ignored.
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        return true;
    }
}
