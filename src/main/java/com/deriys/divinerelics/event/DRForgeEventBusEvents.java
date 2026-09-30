package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.LeviathanBindingC2SPacket;
import com.deriys.divinerelics.core.networking.packets.MjolnirBindingC2SPacket;
import com.deriys.divinerelics.core.networking.packets.StuckSpearsS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DRForgeEventBusEvents {

    // Both interaction events fire once per hand, and RightClickBlock also fires on the logical server, where
    // sendToServer() touches net.minecraft.client.Minecraft: on a dedicated server that threw inside the packet
    // handler, aborting every empty-handed block interaction (doors, levers, chests...) with a logged error. Only the
    // client's main-hand event may send the recall packets.
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        if (player.getLevel().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            MjolnirBindingC2SPacket mjolnirPacket = new MjolnirBindingC2SPacket();
            DRMessages.sendToServer(mjolnirPacket);
            LeviathanBindingC2SPacket leviathanPacket = new LeviathanBindingC2SPacket();
            DRMessages.sendToServer(leviathanPacket);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.getLevel().isClientSide && event.getHand() == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            MjolnirBindingC2SPacket packet = new MjolnirBindingC2SPacket();
            DRMessages.sendToServer(packet);

            LeviathanBindingC2SPacket leviathanPacket = new LeviathanBindingC2SPacket();
            DRMessages.sendToServer(leviathanPacket);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getTarget();
            entity.getCapability(StuckSpearsProvider.STUCK_SPEARS).ifPresent(cap -> DRMessages.sendToPlayer(new StuckSpearsS2CPacket(entity.getId(), cap.getSpears()), (ServerPlayer) event.getEntity()));
        }
    }

//    @SubscribeEvent
//    public static void thorChangeTargetEvent(LivingChangeTargetEvent event) {
//        Entity entity = event.getEntity();
//
//        if (entity instanceof ThorEntity && event.getNewTarget() == null) {
//            if (event.getOriginalTarget() instanceof Player player && player.isAlive()) {
//                event.setCanceled(true);
//            }
//        }
//    }
}
