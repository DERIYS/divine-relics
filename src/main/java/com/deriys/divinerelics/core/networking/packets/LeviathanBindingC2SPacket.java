package com.deriys.divinerelics.core.networking.packets;

import com.deriys.divinerelics.capabilities.leviathan.LeviathanBindingProvider;
import com.deriys.divinerelics.entities.entity.ThrownLeviathanAxe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class LeviathanBindingC2SPacket {
    public LeviathanBindingC2SPacket() {

    }

    public LeviathanBindingC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel serverLevel = player.getLevel();

            player.getCapability(LeviathanBindingProvider.LEVIATHAN_BINDING).ifPresent(binding -> {
                List<String> thrownLeviathanList = binding.getLeviathanList();
                List<String> copyList = List.copyOf(thrownLeviathanList);
                if (!thrownLeviathanList.isEmpty()) {
                    for (String leviathan: copyList) {
                        Entity thrownLeviathanEntity = serverLevel.getEntity(UUID.fromString(leviathan));
                        if (thrownLeviathanEntity instanceof ThrownLeviathanAxe thrownLeviathanAxe) {
                            thrownLeviathanAxe.shouldReturn = true;
                            thrownLeviathanAxe.relaxed = false;
                            binding.removeLeviathan(leviathan);
                        }
                    }
                }

            });
        });
        return true;
    }
}
