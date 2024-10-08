package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpear;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StuckSpearsS2CPacket {
    private final int entityId;
    private final List<StuckSpear> stuckSpears;

    public StuckSpearsS2CPacket(int entityId, List<StuckSpear> stuckSpears) {
        this.entityId = entityId;
        this.stuckSpears = stuckSpears;
    }

    public StuckSpearsS2CPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        int size = buf.readInt();
        stuckSpears = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            float rotation = buf.readFloat();
            boolean isFront = buf.readBoolean();
            stuckSpears.add(new StuckSpear(x, y, z, rotation, isFront));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(stuckSpears.size());
        for (StuckSpear stuckSpear : stuckSpears) {
            buf.writeDouble(stuckSpear.x);
            buf.writeDouble(stuckSpear.y);
            buf.writeDouble(stuckSpear.z);
            buf.writeFloat(stuckSpear.rotation);
            buf.writeBoolean(stuckSpear.isFront);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                Entity entity = level.getEntity(entityId);
                if (entity instanceof LivingEntity) {
                    entity.getCapability(StuckSpearsProvider.STUCK_SPEARS).ifPresent(cap -> cap.setSpears(stuckSpears));
                }
            }
        });
        return true;
    }
}
