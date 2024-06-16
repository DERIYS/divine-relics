package com.deriys.tutorialmod.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OracleCompass extends Item {
    public OracleCompass(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide) {
            StringBuilder text = new StringBuilder();
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            List<ServerPlayer> Splayers = server.getPlayerList().getPlayers();
            List<ServerPlayer> players = new ArrayList<>(Splayers);
            players.sort(Collections.reverseOrder(Comparator.comparingDouble(p -> p.position().distanceToSqr(player.position()))));
            for (ServerPlayer Splayer: players) {
                if (Splayer != player) {
                    sendMessage(player, Splayer.getDisplayName().getString() + ": ");
                    sendMessage(player, "DIM: " + getDimentionName(Splayer) + "; X: " + Splayer.getBlockX() + "; Y: " + Splayer.getBlockY() + "; Z: " + Splayer.getBlockZ() + "; DIST: " + Splayer.position().subtract(player.position()).length());
                }
            }
            return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(interactionHand));
    }

    private String getDimentionName(ServerPlayer splayer) {
        return splayer.level.dimension().location().toString();
    }

    private static void sendMessage(Player player, String text) {
        Component message = Component.literal(text);
        player.sendSystemMessage(message);
    }
}
