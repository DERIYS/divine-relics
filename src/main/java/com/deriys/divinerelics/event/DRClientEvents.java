package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.init.DRKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class DRClientEvents {
    @Mod.EventBusSubscriber(modid = DivineRelics.MODID, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        private static final OffhandShieldKeyHandler SHIELD_KEY =
                new OffhandShieldKeyHandler(down -> Minecraft.getInstance().options.keyUse.setDown(down));

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (DRKeyBindings.OFFHAND_SHIELD_KEY.matches(event.getKey(), event.getScanCode())) {
                handleShieldKey(event.getAction());
            }
        }

        @SubscribeEvent
        public static void onMouseInput(InputEvent.MouseButton.Pre event) {
            if (DRKeyBindings.OFFHAND_SHIELD_KEY.matchesMouse(event.getButton()) && handleShieldKey(event.getAction())) {
                event.setCanceled(true);
            }
        }

        private static boolean handleShieldKey(int action) {
            Minecraft mc = Minecraft.getInstance();
            return SHIELD_KEY.handleInput(action, mc.player, mc.gameMode, mc.screen == null);
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            SHIELD_KEY.tick(mc.player, mc.gameMode, mc.screen == null && mc.isWindowActive());
        }

        public static void interruptShieldUse() {
            Minecraft mc = Minecraft.getInstance();
            SHIELD_KEY.interrupt(mc.player);
        }
    }
}
