package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.entities.entity.ThrownMjolnir;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
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
                List<String> thrownMjolnirList = binding.getMjolnirList();
                List<String> copyList = List.copyOf(thrownMjolnirList);
                if (!thrownMjolnirList.isEmpty()) {
                    for (String mjolnir: copyList) {
                        Entity thrownMjolnirEntity = serverLevel.getEntity(UUID.fromString(mjolnir));
                        if (thrownMjolnirEntity instanceof ThrownMjolnir thrownMjolnir) {
                            thrownMjolnir.setReturning(true);
                            thrownMjolnir.relaxed = false;
                        }
                    }
                }
            });
        });
        return true;
    }
}
