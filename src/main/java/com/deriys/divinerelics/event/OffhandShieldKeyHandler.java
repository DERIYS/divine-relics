package com.deriys.divinerelics.event;

import com.deriys.divinerelics.items.ShieldReactions;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.Consumer;

final class OffhandShieldKeyHandler {
    private final Consumer<Boolean> setUseKeyDown;
    private boolean active;

    OffhandShieldKeyHandler(Consumer<Boolean> setUseKeyDown) {
        this.setUseKeyDown = setUseKeyDown;
    }

    boolean handleInput(int action, @Nullable Player player, @Nullable MultiPlayerGameMode gameMode, boolean inGame) {
        if (action == InputConstants.RELEASE && active) {
            release(player, gameMode);
            return true;
        }
        if (action == InputConstants.PRESS && inGame && player != null && gameMode != null
                && ShieldReactions.canUseOffhandShield(player)) {
            InteractionResult result = gameMode.useItem(player, InteractionHand.OFF_HAND);
            if (result.consumesAction() && player.isUsingItem() && player.getUsedItemHand() == InteractionHand.OFF_HAND) {
                active = true;
                setUseKeyDown.accept(true);
                return true;
            }
        }
        return false;
    }

    void tick(@Nullable Player player, @Nullable MultiPlayerGameMode gameMode, boolean inGame) {
        if (active && (!inGame || player == null || !ShieldReactions.canUseOffhandShield(player))) {
            release(player, gameMode);
        }
    }

    private void release(@Nullable Player player, @Nullable MultiPlayerGameMode gameMode) {
        active = false;
        setUseKeyDown.accept(false);
        if (player != null && gameMode != null && player.isUsingItem()
                && player.getUsedItemHand() == InteractionHand.OFF_HAND && ShieldReactions.isShield(player.getUseItem())) {
            gameMode.releaseUsingItem(player);
        }
    }

    void interrupt(@Nullable Player player) {
        active = false;
        setUseKeyDown.accept(false);
        if (player != null && ShieldReactions.isShield(player.getUseItem())) {
            player.stopUsingItem();
        }
    }
}
