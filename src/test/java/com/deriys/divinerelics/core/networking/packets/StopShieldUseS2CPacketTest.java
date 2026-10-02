package com.deriys.divinerelics.core.networking.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StopShieldUseS2CPacketTest {
    @Test
    void roundTripHasNoPayload() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            new StopShieldUseS2CPacket().toBytes(buffer);
            assertEquals(0, buffer.readableBytes());
            new StopShieldUseS2CPacket(buffer).toBytes(buffer);
            assertEquals(0, buffer.readableBytes());
        } finally {
            buffer.release();
        }
    }

    @Test
    void handlerSchedulesClientWorkOnMainThread() {
        NetworkEvent.Context context = mock(NetworkEvent.Context.class);
        assertTrue(new StopShieldUseS2CPacket().handle(() -> context));
        verify(context).enqueueWork(any(Runnable.class));
    }
}
