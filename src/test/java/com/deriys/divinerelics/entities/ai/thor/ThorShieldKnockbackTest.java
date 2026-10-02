package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.items.GuardianShield;
import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.ThorKnockbackS2CPacket;
import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyInt;

class ThorShieldKnockbackTest {
    private Player target;
    private Entity source;
    private AtomicReference<Vec3> motion;

    @BeforeAll
    static void bootstrapMinecraft() {
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
        source = mock(Entity.class);
        when(source.position()).thenReturn(Vec3.ZERO);
        when(source.getBoundingBox()).thenReturn(new AABB(-0.5D, 0, -0.5D, 0.5D, 2.8D, 0.5D));
        setupTarget(mock(Player.class));
    }

    private void setupTarget(Player player) {
        target = player;
        Level level = mock(Level.class);
        target.level = level;
        when(target.getUseItem()).thenReturn(new ItemStack(Items.SHIELD));
        when(target.getCooldowns()).thenReturn(mock(ItemCooldowns.class));
        when(level.getBlockState(any(BlockPos.class))).thenReturn(Blocks.STONE.defaultBlockState());
        when(target.getX()).thenReturn(0.8D);
        when(target.getZ()).thenReturn(0.0D);
        when(target.getBoundingBox()).thenReturn(new AABB(0.5D, 0, -0.3D, 1.1D, 1.8D, 0.3D));
        motion = new AtomicReference<>(Vec3.ZERO);
        when(target.getDeltaMovement()).thenAnswer(invocation -> motion.get());
        doAnswer(invocation -> {
            motion.set(invocation.getArgument(0));
            return null;
        }).when(target).setDeltaMovement(any(Vec3.class));
        doAnswer(invocation -> {
            motion.set(new Vec3(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2)));
            return null;
        }).when(target).setDeltaMovement(anyDouble(), anyDouble(), anyDouble());
    }

    @ParameterizedTest
    @CsvSource({"MELEE_ATTACK,3,1.5,0.2", "LEG_ATTACK,5.5,5,0.2", "CLAP_ATTACK,17,17,1",
            "GROUND_ATTACK,28.9,25.5,2.5", "MJOLNIR_THROW,6,5,1"})
    void configuredProfilesApplyToUnblockedAndBlockedHits(ThorAttackState attack, double unblocked,
                                                        double blocked, double height) {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            assertTrue(ThorShieldKnockback.hurtWithKnockback(target, source, attack, 6.0D, () -> {
                target.setDeltaMovement(new Vec3(0.4D, 0.4D, 0.0D));
                return true;
            }));
            assertLaunch(unblocked, height);
            assertFalse(ThorShieldKnockback.hurtWithKnockback(target, source, attack, 6.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                target.setDeltaMovement(new Vec3(0.4D, 0.4D, 0.0D));
                return false;
            }));
            assertLaunch(blocked, height);
        }
    }

    @Test
    void areaEdgeUsesMinimumKnockbackAndDamageFactors() {
        when(target.getX()).thenReturn(6.8D);
        when(target.getBoundingBox()).thenReturn(new AABB(6.5D, 0, -0.3D, 7.1D, 1.8D, 0.3D));
        assertEquals(0.7D, ThorShieldKnockback.damageFactor(source, target, 6.0D), 1.0E-12D);
        ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.CLAP_ATTACK, 6.0D, () -> true);
        assertLaunch(8.5D, 1.0D);
    }

    @Test
    void directHitsDoNotFallOffWithSourceDistance() {
        when(target.getX()).thenReturn(10.8D);
        when(target.getBoundingBox()).thenReturn(new AABB(10.5D, 0, -0.3D, 11.1D, 1.8D, 0.3D));
        ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.MJOLNIR_THROW, 1.0D, () -> true);
        assertLaunch(6.0D, 1.0D);
    }

    @Test
    void unsuccessfulUnblockedHitDoesNotLaunch() {
        Vec3 before = new Vec3(0.1D, -0.1D, 0.2D);
        motion.set(before);
        assertFalse(ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.MELEE_ATTACK, 0.0D, () -> false));
        assertEquals(before, motion.get());
        assertFalse(target.hasImpulse);
    }

    @ParameterizedTest
    @CsvSource({"MELEE_ATTACK,1.5,0.2", "LEG_ATTACK,5,0.2", "CLAP_ATTACK,17,1",
            "GROUND_ATTACK,25.5,2.5", "MJOLNIR_THROW,5,1"})
    void guardianParryScalesBothLaunchSpeedsAndLowersShieldWithoutCooldown(ThorAttackState attack,
                                                                         double blockedDistance, double height) {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            shield.when(() -> GuardianShield.isParrying(target)).thenReturn(true);
            ThorShieldKnockback.hurtWithKnockback(target, source, attack, 8.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                shield.when(() -> GuardianShield.isParrying(target)).thenReturn(false);
                return false;
            });
        }
        double lift = ThorKnockbackPhysics.launchSpeed(height);
        assertEquals(ThorKnockbackPhysics.horizontalSpeed(blockedDistance, 0.6F, lift) * 0.5D, motion.get().x, 1.0E-8D);
        assertEquals(lift * 0.5D, motion.get().y, 1.0E-8D);
        verify(target).stopUsingItem();
        verify(target.getCooldowns(), never()).addCooldown(any(), anyInt());
        verify(target.level, never()).broadcastEntityEvent(target, (byte) 30);
    }

    @ParameterizedTest
    @CsvSource({"MELEE_ATTACK,true,false", "LEG_ATTACK,true,false", "CLAP_ATTACK,true,false",
            "GROUND_ATTACK,true,true", "MJOLNIR_THROW,false,false"})
    void ordinaryBlockAppliesAttackSpecificShieldReaction(ThorAttackState attack, boolean stopsUse, boolean disables) {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            assertFalse(ThorShieldKnockback.hurtWithKnockback(target, source, attack, 8.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                return false;
            }));
        }
        if (stopsUse) {
            verify(target).stopUsingItem();
        } else {
            verify(target, never()).stopUsingItem();
        }
        if (disables) {
            verify(target.getCooldowns()).addCooldown(Items.SHIELD, 100);
            verify(target.level).broadcastEntityEvent(target, (byte) 30);
        } else {
            verify(target.getCooldowns(), never()).addCooldown(any(), anyInt());
            verify(target.level, never()).broadcastEntityEvent(target, (byte) 30);
        }
    }

    @Test
    void groundDisableUsesShieldThatBlockedEvenWhenUseItemChangesDuringHurt() {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.GROUND_ATTACK, 8.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                when(target.getUseItem()).thenReturn(ItemStack.EMPTY);
                return false;
            });
        }
        verify(target.getCooldowns()).addCooldown(Items.SHIELD, 100);
        verify(target.getCooldowns(), never()).addCooldown(Items.AIR, 100);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0D, 0.25D, 1.0D})
    void configurableParryMultiplierScalesVelocityRatherThanDistance(double multiplier) {
        DivineRelicsCommonConfig.THOR_PARRY_KNOCKBACK_MULTIPLIER.set(multiplier);
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            shield.when(() -> GuardianShield.isParrying(target)).thenReturn(true);
            ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.MELEE_ATTACK, 0.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                return false;
            });
            double lift = ThorKnockbackPhysics.launchSpeed(0.2D);
            assertEquals(ThorKnockbackPhysics.horizontalSpeed(1.5D, 0.6F, lift) * multiplier, motion.get().x, 1.0E-8D);
            assertEquals(lift * multiplier, motion.get().y, 1.0E-8D);
        } finally {
            DivineRelicsCommonConfig.THOR_PARRY_KNOCKBACK_MULTIPLIER.set(0.5D);
        }
    }

    @Test
    void nestedHurtDoesNotLoseOuterShieldBlock() {
        LivingEntity other = mock(LivingEntity.class);
        when(other.getDeltaMovement()).thenReturn(Vec3.ZERO);
        when(other.getUseItem()).thenReturn(ItemStack.EMPTY);
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.MELEE_ATTACK, 0.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                ThorShieldKnockback.hurtWithKnockback(other, source, ThorAttackState.MELEE_ATTACK, 0.0D, () -> false);
                return false;
            });
        }
        assertLaunch(1.5D, 0.2D);
    }

    @Test
    void serverPlayerGetsFullMotionWithoutLaterClampedPacket() {
        ServerPlayer player = mock(ServerPlayer.class);
        setupTarget(player);
        ArgumentCaptor<ThorKnockbackS2CPacket> packet = ArgumentCaptor.forClass(ThorKnockbackS2CPacket.class);
        try (MockedStatic<DRMessages> messages = mockStatic(DRMessages.class)) {
            ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.GROUND_ATTACK, 8.0D, () -> {
                target.hurtMarked = true;
                return true;
            });
            messages.verify(() -> DRMessages.sendToPlayer(packet.capture(), eq(player)));
        }
        assertFalse(target.hurtMarked);
        assertTrue(motion.get().x > 3.9D);
        assertLaunch(28.9D, 2.5D);
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            packet.getValue().toBytes(buffer);
            assertEquals(motion.get().x, buffer.readDouble());
            assertEquals(motion.get().y, buffer.readDouble());
            assertEquals(motion.get().z, buffer.readDouble());
        } finally {
            buffer.release();
        }
    }

    @Test
    void updatedConfigValuesAreUsedWithoutRestartingHelper() {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            DivineRelicsCommonConfig.THOR_MELEE_KNOCKBACK_DISTANCE.set(4.0D);
            DivineRelicsCommonConfig.THOR_MELEE_SHIELD_KNOCKBACK_DISTANCE.set(0.75D);
            DivineRelicsCommonConfig.THOR_MELEE_KNOCKBACK_HEIGHT.set(0.3D);
            ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.MELEE_ATTACK, 0.0D, () -> true);
            assertLaunch(4.0D, 0.3D);
            ThorShieldKnockback.hurtWithKnockback(target, source, ThorAttackState.MELEE_ATTACK, 0.0D, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                return false;
            });
            assertLaunch(0.75D, 0.3D);
        } finally {
            DivineRelicsCommonConfig.THOR_MELEE_KNOCKBACK_DISTANCE.set(3.0D);
            DivineRelicsCommonConfig.THOR_MELEE_SHIELD_KNOCKBACK_DISTANCE.set(1.5D);
            DivineRelicsCommonConfig.THOR_MELEE_KNOCKBACK_HEIGHT.set(0.2D);
        }
    }

    @Test
    void legacyPlayerHammerShieldLaunchKeepsGlobalDefaults() {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            ThorShieldKnockback.knockBackAndHurt(target, Vec3.ZERO, 0.0D, force -> {
            }, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                return false;
            });
        }
        assertLaunch(15.0D, 1.0D);
    }

    @Test
    void playerOwnedHammerParryHasHalfSpeedsAndNoShieldCooldown() {
        try (MockedStatic<GuardianShield> shield = mockStatic(GuardianShield.class)) {
            shield.when(() -> GuardianShield.isParrying(target)).thenReturn(true);
            ThorShieldKnockback.knockBackAndHurt(target, Vec3.ZERO, 0.0D, force -> {
            }, () -> {
                ThorShieldKnockback.onShieldBlock(target);
                return false;
            });
        }
        double lift = ThorKnockbackPhysics.launchSpeed(1.0D);
        double normalSpeed = 15.0D / ThorShieldKnockback.distancePerSpeed(0.6F, lift);
        assertEquals(normalSpeed * 0.5D, motion.get().x, 1.0E-8D);
        assertEquals(lift * 0.5D, motion.get().y, 1.0E-8D);
        verify(target).stopUsingItem();
        verify(target.getCooldowns(), never()).addCooldown(any(), anyInt());
    }

    private void assertLaunch(double distance, double height) {
        Vec3 actual = motion.get();
        assertEquals(distance, ThorKnockbackPhysics.travelDistance(actual.horizontalDistance(), 0.6F, actual.y), 0.03D);
        assertEquals(height, ThorKnockbackPhysics.apex(actual.y), 1.0E-8D);
        assertTrue(actual.x > 0.0D);
        assertEquals(0.0D, actual.z);
        assertTrue(target.hasImpulse);
    }
}
