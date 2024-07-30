package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.LeviathanBindingC2SPacket;
import com.deriys.divinerelics.core.networking.packets.MjolnirBindingC2SPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DRForgeEventBusEvents {

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().isEmpty()) {
            MjolnirBindingC2SPacket mjolnirPacket = new MjolnirBindingC2SPacket();
            DRMessages.sendToServer(mjolnirPacket);
            LeviathanBindingC2SPacket leviathanPacket = new LeviathanBindingC2SPacket();
            DRMessages.sendToServer(leviathanPacket);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().isEmpty()) {
            MjolnirBindingC2SPacket packet = new MjolnirBindingC2SPacket();
            DRMessages.sendToServer(packet);

            LeviathanBindingC2SPacket leviathanPacket = new LeviathanBindingC2SPacket();
            DRMessages.sendToServer(leviathanPacket);
        }
    }
}
