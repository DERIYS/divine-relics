package com.deriys.divinerelics.event;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.items.Mjolnir;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.SharedConstants;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OffhandShieldKeyHandlerTest {
    private OffhandShieldKeyHandler handler;
    private Player player;
    private MultiPlayerGameMode gameMode;
    private AtomicBoolean useDown;

    @BeforeAll
    static void setupMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        CommentedConfig config = CommentedConfig.inMemory();
        DivineRelicsCommonConfig.SPEC.correct(config);
        DivineRelicsCommonConfig.SPEC.setConfig(config);
    }

    @AfterAll
    static void unloadConfig() {
        DivineRelicsCommonConfig.SPEC.setConfig(null);
    }

    @BeforeEach
    void setup() {
        useDown = new AtomicBoolean();
        handler = new OffhandShieldKeyHandler(useDown::set);
        player = mock(Player.class);
        gameMode = mock(MultiPlayerGameMode.class);
        ItemStack shield = mock(ItemStack.class);
        when(shield.getItem()).thenReturn(Items.SHIELD);
        when(shield.getUseAnimation()).thenReturn(UseAnim.BLOCK);
        when(player.getOffhandItem()).thenReturn(shield);
        when(player.getUseItem()).thenReturn(shield);
        ItemStack weapon = mock(ItemStack.class);
        when(weapon.getItem()).thenReturn(mock(Mjolnir.class));
        when(player.getMainHandItem()).thenReturn(weapon);
        when(player.getCooldowns()).thenReturn(mock(ItemCooldowns.class));
        when(player.isUsingItem()).thenReturn(true);
        when(player.getUsedItemHand()).thenReturn(InteractionHand.OFF_HAND);
        when(gameMode.useItem(player, InteractionHand.OFF_HAND)).thenReturn(InteractionResult.CONSUME);
    }

    @Test
    void pressUsesOffhandAndReleaseSendsStopUse() {
        assertTrue(handler.handleInput(InputConstants.PRESS, player, gameMode, true));
        assertTrue(useDown.get());
        verify(gameMode).useItem(player, InteractionHand.OFF_HAND);
        assertTrue(handler.handleInput(InputConstants.RELEASE, player, gameMode, true));
        assertFalse(useDown.get());
        verify(gameMode).releaseUsingItem(player);
    }

    @Test
    void repeatDoesNotRaiseShieldAgain() {
        handler.handleInput(InputConstants.PRESS, player, gameMode, true);
        assertFalse(handler.handleInput(GLFW.GLFW_REPEAT, player, gameMode, true));
        assertTrue(useDown.get());
        verify(gameMode, times(1)).useItem(player, InteractionHand.OFF_HAND);
    }

    @Test
    void releaseStillWorksAfterMainhandOrScreenChanges() {
        handler.handleInput(InputConstants.PRESS, player, gameMode, true);
        when(player.getMainHandItem().getItem()).thenReturn(Items.DIAMOND_SWORD);
        assertTrue(handler.handleInput(InputConstants.RELEASE, player, gameMode, false));
        assertFalse(useDown.get());
        verify(gameMode).releaseUsingItem(player);
    }

    @Test
    void forceReleaseDoesNotReblockWhileKeyRemainsHeld() {
        handler.handleInput(InputConstants.PRESS, player, gameMode, true);
        handler.interrupt(player);
        verify(player).stopUsingItem();
        assertFalse(useDown.get());
        handler.handleInput(GLFW.GLFW_REPEAT, player, gameMode, true);
        handler.tick(player, gameMode, true);
        assertFalse(handler.handleInput(InputConstants.RELEASE, player, gameMode, true));
        verify(gameMode, times(1)).useItem(player, InteractionHand.OFF_HAND);
        verify(gameMode, never()).releaseUsingItem(player);
        assertTrue(handler.handleInput(InputConstants.PRESS, player, gameMode, true));
        verify(gameMode, times(2)).useItem(player, InteractionHand.OFF_HAND);
    }

    @Test
    void disabledShieldCannotStartAndCooldownStopsActiveShortcut() {
        when(player.getCooldowns().isOnCooldown(Items.SHIELD)).thenReturn(true);
        assertFalse(handler.handleInput(InputConstants.PRESS, player, gameMode, true));
        verify(gameMode, never()).useItem(player, InteractionHand.OFF_HAND);
        when(player.getCooldowns().isOnCooldown(Items.SHIELD)).thenReturn(false);
        handler.handleInput(InputConstants.PRESS, player, gameMode, true);
        when(player.getCooldowns().isOnCooldown(Items.SHIELD)).thenReturn(true);
        handler.tick(player, gameMode, true);
        assertFalse(useDown.get());
        verify(gameMode).releaseUsingItem(player);
    }

    @Test
    void failedUseDoesNotLatchUseKey() {
        when(gameMode.useItem(player, InteractionHand.OFF_HAND)).thenReturn(InteractionResult.PASS);
        assertFalse(handler.handleInput(InputConstants.PRESS, player, gameMode, true));
        assertFalse(useDown.get());
        assertFalse(handler.handleInput(InputConstants.RELEASE, player, gameMode, true));
    }

    @Test
    void menuPressIsIgnoredAndOpeningMenuReleasesExistingBlock() {
        assertFalse(handler.handleInput(InputConstants.PRESS, player, gameMode, false));
        handler.handleInput(InputConstants.PRESS, player, gameMode, true);
        handler.tick(player, gameMode, false);
        assertFalse(useDown.get());
        verify(gameMode).releaseUsingItem(player);
    }

    @Test
    void disconnectClearsHeldShortcut() {
        handler.handleInput(InputConstants.PRESS, player, gameMode, true);
        handler.tick(null, null, false);
        assertFalse(useDown.get());
        assertFalse(handler.handleInput(InputConstants.RELEASE, null, null, false));
    }
}
