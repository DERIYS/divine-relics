package com.deriys.divinerelics.items;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.StopShieldUseS2CPacket;
import com.deriys.divinerelics.event.DREvents;
import com.deriys.divinerelics.entities.entity.ThrownMjolnir;
import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShieldReactionsTest {
    private Player player;
    private ItemStack offhand;
    private ItemCooldowns cooldowns;

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
        player = mock(Player.class);
        player.level = mock(Level.class);
        cooldowns = mock(ItemCooldowns.class);
        when(player.getCooldowns()).thenReturn(cooldowns);
        offhand = mock(ItemStack.class);
        when(offhand.getItem()).thenReturn(Items.SHIELD);
        when(offhand.getUseAnimation()).thenReturn(UseAnim.BLOCK);
        when(player.getUseItem()).thenReturn(offhand);
        when(player.getOffhandItem()).thenReturn(offhand);
        ItemStack mainhand = mock(ItemStack.class);
        when(mainhand.getItem()).thenReturn(mock(Mjolnir.class));
        when(player.getMainHandItem()).thenReturn(mainhand);
    }

    @ParameterizedTest
    @CsvSource({"AXE,false", "AXE,true", "SPEAR,false", "SPEAR,true", "HAMMER,false", "HAMMER,true"})
    void offhandKeyAcceptsNormalAndGuardianShieldsWithEachRelicWeapon(String weapon, boolean guardian) {
        Item item = switch (weapon) {
            case "AXE" -> mock(LeviathanAxe.class);
            case "SPEAR" -> mock(DraupnirSpear.class);
            case "HAMMER" -> mock(Mjolnir.class);
            default -> throw new IllegalArgumentException(weapon);
        };
        when(player.getMainHandItem().getItem()).thenReturn(item);
        if (guardian) {
            when(offhand.getItem()).thenReturn(mock(GuardianShield.class));
        }
        assertTrue(ShieldReactions.canUseOffhandShield(player));
    }

    @Test
    void keyAcceptsModdedBlockingItemWithoutRequiringShieldItemSubclass() {
        when(offhand.getItem()).thenReturn(mock(Item.class));
        assertTrue(ShieldReactions.canUseOffhandShield(player));
    }

    @Test
    void keyRejectsUnrelatedWeaponsEmptyOffhandAndNonShields() {
        when(player.getMainHandItem().getItem()).thenReturn(Items.DIAMOND_SWORD);
        assertFalse(ShieldReactions.canUseOffhandShield(player));
        when(player.getMainHandItem().getItem()).thenReturn(mock(Mjolnir.class));
        when(offhand.isEmpty()).thenReturn(true);
        assertFalse(ShieldReactions.canUseOffhandShield(player));
        when(offhand.isEmpty()).thenReturn(false);
        when(offhand.getUseAnimation()).thenReturn(UseAnim.EAT);
        assertFalse(ShieldReactions.canUseOffhandShield(player));
    }

    @Test
    void keyRejectsShieldOnCooldown() {
        when(cooldowns.isOnCooldown(Items.SHIELD)).thenReturn(true);
        assertFalse(ShieldReactions.canUseOffhandShield(player));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void disableUsesActualShieldAndFiveSecondCooldown(boolean guardian) {
        Item item = guardian ? mock(GuardianShield.class) : Items.SHIELD;
        ShieldReactions.disableBlocking(player, item);
        verify(cooldowns).addCooldown(item, 100);
        verify(player).stopUsingItem();
        verify(player.level).broadcastEntityEvent(player, (byte) 30);
    }

    @Test
    void interruptDoesNotAddCooldownAndNotifiesServerPlayer() {
        ServerPlayer serverPlayer = mock(ServerPlayer.class);
        try (MockedStatic<DRMessages> messages = mockStatic(DRMessages.class)) {
            ShieldReactions.interruptBlocking(serverPlayer);
            messages.verify(() -> DRMessages.sendToPlayer(any(StopShieldUseS2CPacket.class), eq(serverPlayer)));
        }
        verify(serverPlayer).stopUsingItem();
    }

    @Test
    void nonThorParryIsReleasedAfterBlockProcessingWithoutCooldown() {
        ShieldBlockEvent event = new ShieldBlockEvent(player, DamageSource.mobAttack(mock(LivingEntity.class)), 10.0F);
        try (MockedStatic<GuardianShield> guardian = mockStatic(GuardianShield.class)) {
            guardian.when(() -> GuardianShield.isParrying(player)).thenReturn(true);
            DREvents.ForgeEvents.onShieldBlocked(event);
        }
        verify(player, never()).stopUsingItem();
        ShieldReactions.finishParry(player);
        ShieldReactions.finishParry(player);
        verify(player, times(1)).stopUsingItem();
        verify(cooldowns, never()).addCooldown(any(), anyInt());
    }

    @Test
    void ordinaryBlockDoesNotQueueGlobalParryRelease() {
        ShieldBlockEvent event = new ShieldBlockEvent(player, DamageSource.mobAttack(mock(LivingEntity.class)), 10.0F);
        try (MockedStatic<GuardianShield> guardian = mockStatic(GuardianShield.class)) {
            DREvents.ForgeEvents.onShieldBlocked(event);
        }
        ShieldReactions.finishParry(player);
        verify(player, never()).stopUsingItem();
    }

    @Test
    void parryRemovesVanillaAxeCooldownEvenIfAxeAlreadyStoppedShieldUse() {
        ShieldReactions.queueParryRelease(player);
        when(cooldowns.isOnCooldown(Items.SHIELD)).thenReturn(true);
        when(player.getUseItem()).thenReturn(ItemStack.EMPTY);
        ShieldReactions.finishParry(player);
        verify(cooldowns).removeCooldown(Items.SHIELD);
        verify(cooldowns, never()).addCooldown(any(), anyInt());
        verify(player).stopUsingItem();
    }

    @Test
    void groundDisableClearsPendingParryReleaseWithoutRemovingNewCooldown() {
        ShieldReactions.queueParryRelease(player);
        ShieldReactions.disableBlocking(player, Items.SHIELD);
        ShieldReactions.finishParry(player);
        verify(cooldowns).addCooldown(Items.SHIELD, 100);
        verify(cooldowns, never()).removeCooldown(any());
        verify(player, times(1)).stopUsingItem();
    }

    @Test
    void parryUsesActuallyRaisedShieldRatherThanOffhandContents() {
        when(player.isBlocking()).thenReturn(true);
        ItemStack raised = mock(ItemStack.class);
        when(raised.getItem()).thenReturn(mock(GuardianShield.class));
        when(player.getUseItem()).thenReturn(raised);
        assertTrue(GuardianShield.isParrying(player));
        when(raised.getItem()).thenReturn(Items.SHIELD);
        when(offhand.getItem()).thenReturn(mock(GuardianShield.class));
        assertFalse(GuardianShield.isParrying(player));
    }

    @Test
    void thrownHammerBlockingStillRequiresActuallyRaisedGuardianShield() {
        ThrownMjolnir hammer = mock(ThrownMjolnir.class);
        DamageSource source = DamageSource.trident(hammer, mock(LivingEntity.class));
        ShieldBlockEvent ordinary = new ShieldBlockEvent(player, source, 10.0F);
        DREvents.ForgeEvents.onShieldBlock(ordinary);
        assertTrue(ordinary.isCanceled());
        when(offhand.getItem()).thenReturn(mock(GuardianShield.class));
        ShieldBlockEvent guardian = new ShieldBlockEvent(player, source, 10.0F);
        DREvents.ForgeEvents.onShieldBlock(guardian);
        assertFalse(guardian.isCanceled());
    }
}
