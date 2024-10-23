package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.init.DRKeyBindings;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class DRClientEvents {
    @Mod.EventBusSubscriber(modid = DivineRelics.MODID, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){
            if (event.getKey() == DRKeyBindings.GUARDIAN_SHIELD_KEY.getKey().getValue()) {
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer player = mc.player;
                MultiPlayerGameMode gameMode = mc.gameMode;
                if (player != null && gameMode != null && player.getOffhandItem().getItem() == DRItems.GUARDIAN_SHIELD.get()) {
                    if (event.getAction() == InputConstants.PRESS) {
                        gameMode.useItem(player, InteractionHand.OFF_HAND);
                        mc.options.keyUse.setDown(true);
                    }
                    if (event.getAction() == InputConstants.RELEASE) {
                        mc.options.keyUse.setDown(false);
                        gameMode.releaseUsingItem(player);
                    }
                }

            }
        }
    }
}
