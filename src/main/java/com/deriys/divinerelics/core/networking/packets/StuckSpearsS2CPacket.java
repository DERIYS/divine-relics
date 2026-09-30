package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpear;
import com.deriys.divinerelics.core.networking.DRClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StuckSpearsS2CPacket {
    private static final int ENTRY_BYTES = Double.BYTES * 3 + Float.BYTES * 5;

    private final int entityId;
    private final List<StuckSpear> stuckSpears;

    public StuckSpearsS2CPacket(int entityId, List<StuckSpear> stuckSpears) {
        this.entityId = entityId;
        this.stuckSpears = stuckSpears;
    }

    public StuckSpearsS2CPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        int size = buf.readInt();
        // Each entry is 3 doubles + 5 floats; never trust the size prefix beyond what the buffer holds.
        if (size < 0 || size > buf.readableBytes() / ENTRY_BYTES) {
            throw new IllegalArgumentException("Invalid stuck spear count " + size);
        }
        stuckSpears = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            stuckSpears.add(new StuckSpear(buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(stuckSpears.size());
        for (StuckSpear stuckSpear : stuckSpears) {
            buf.writeDouble(stuckSpear.x);
            buf.writeDouble(stuckSpear.y);
            buf.writeDouble(stuckSpear.z);
            buf.writeFloat(stuckSpear.dirX);
            buf.writeFloat(stuckSpear.dirY);
            buf.writeFloat(stuckSpear.dirZ);
            buf.writeFloat(stuckSpear.depth);
            buf.writeFloat(stuckSpear.roll);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                DRClientPacketHandler.handleStuckSpears(entityId, stuckSpears)));
        return true;
    }
}
