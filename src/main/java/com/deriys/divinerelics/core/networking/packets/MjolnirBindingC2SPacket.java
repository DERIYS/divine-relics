package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.entities.ThrownMjolnir;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MjolnirBindingC2SPacket {
    public MjolnirBindingC2SPacket() {

    }

    public MjolnirBindingC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel serverLevel = player.getLevel();

            player.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(binding -> {
                UUID thrownMjolnirUUID = binding.getMjolnir();
                if (thrownMjolnirUUID != null) {
                    Entity thrownMjolnirEntity = serverLevel.getEntity(thrownMjolnirUUID);
                    if (thrownMjolnirEntity instanceof ThrownMjolnir thrownMjolnir) {
                        thrownMjolnir.shouldReturn = true;
                        thrownMjolnir.relaxed = false;
                    } else {
                        System.out.print("NOT MJOLNIR | IF null -> ");
                        System.out.println(thrownMjolnirEntity == null);
                    }
                }

            });
        });
        return true;
    }
}
