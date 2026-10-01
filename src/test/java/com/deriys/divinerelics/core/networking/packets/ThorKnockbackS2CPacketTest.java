package com.deriys.divinerelics.core.networking.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThorKnockbackS2CPacketTest {
    @Test
    void roundTripPreservesFullMotionBeyondVanillaClamp() {
        FriendlyByteBuf encoded = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf decoded = new FriendlyByteBuf(Unpooled.buffer());
        try {
            Vec3 motion = new Vec3(5.6D, 0.7D, -4.2D);
            new ThorKnockbackS2CPacket(motion).toBytes(encoded);
            assertEquals(24, encoded.readableBytes());
            new ThorKnockbackS2CPacket(encoded).toBytes(decoded);
            assertEquals(motion.x, decoded.readDouble());
            assertEquals(motion.y, decoded.readDouble());
            assertEquals(motion.z, decoded.readDouble());
            assertEquals(0, decoded.readableBytes());
        } finally {
            encoded.release();
            decoded.release();
        }
    }

    @Test
    void nonFiniteMotionIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ThorKnockbackS2CPacket(new Vec3(Double.NaN, 0, 0)));
        assertThrows(IllegalArgumentException.class, () -> new ThorKnockbackS2CPacket(new Vec3(0, Double.POSITIVE_INFINITY, 0)));
        assertThrows(IllegalArgumentException.class, () -> new ThorKnockbackS2CPacket(new Vec3(0, 0, Double.NEGATIVE_INFINITY)));
    }
}
