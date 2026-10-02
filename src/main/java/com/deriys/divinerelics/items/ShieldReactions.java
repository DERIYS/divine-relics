package com.deriys.divinerelics.items;

import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.StopShieldUseS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.Map;
import java.util.WeakHashMap;

public final class ShieldReactions {
    private static final Map<Player, Item> PENDING_PARRY_RELEASES = new WeakHashMap<>();
    private static final int SHIELD_DISABLE_TICKS = 100;

    private ShieldReactions() {
    }

    public static boolean isShield(ItemStack stack) {
        return !stack.isEmpty() && stack.getUseAnimation() == UseAnim.BLOCK;
    }

    public static boolean canUseOffhandShield(Player player) {
        Item weapon = player.getMainHandItem().getItem();
        return isShield(player.getOffhandItem())
                && (weapon instanceof LeviathanAxe || weapon instanceof DraupnirSpear || weapon instanceof Mjolnir)
                && !player.getCooldowns().isOnCooldown(player.getOffhandItem().getItem());
    }

    public static void queueParryRelease(Player player) {
        if (!player.level.isClientSide) {
            PENDING_PARRY_RELEASES.put(player, player.getUseItem().getItem());
        }
    }

    /** Runs after damage processing so lowering the shield cannot undo the hit's block or parry detection. */
    public static void finishParry(Player player) {
        Item shield = PENDING_PARRY_RELEASES.remove(player);
        if (shield != null) {
            clearParriedShieldCooldown(player, shield);
            if (isShield(player.getUseItem()) || !player.isUsingItem()) {
                interruptBlocking(player);
            }
        }
    }

    public static void releaseParry(Player player, Item shield) {
        clearParriedShieldCooldown(player, shield);
        interruptBlocking(player);
    }

    private static void clearParriedShieldCooldown(Player player, Item shield) {
        // Vanilla axe blocking may have disabled the shield while hurt() was still processing the parry.
        if (player.getCooldowns().isOnCooldown(shield)) {
            player.getCooldowns().removeCooldown(shield);
        }
    }

    public static void interruptBlocking(Player player) {
        PENDING_PARRY_RELEASES.remove(player);
        player.stopUsingItem();
        if (player instanceof ServerPlayer serverPlayer) {
            DRMessages.sendToPlayer(new StopShieldUseS2CPacket(), serverPlayer);
        }
    }

    public static void disableBlocking(Player player, Item shield) {
        player.getCooldowns().addCooldown(shield, SHIELD_DISABLE_TICKS);
        interruptBlocking(player);
        player.level.broadcastEntityEvent(player, (byte) 30);
    }
}
