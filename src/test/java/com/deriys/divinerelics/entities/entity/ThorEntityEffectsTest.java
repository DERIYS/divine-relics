package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.init.DRSounds;
import com.deriys.divinerelics.capabilities.teammates.TeammatesProvider;
import com.deriys.divinerelics.capabilities.teammates.Teammates;
import com.deriys.divinerelics.items.Motosignir;
import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.MockedConstruction;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mockConstruction;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ThorEntityEffectsTest {
    @Mock
    private IForgeRegistry<SoundEvent> soundRegistry;
    @Mock
    private Capability<Teammates> teammatesCapability;

    @BeforeAll
    static void setupMinecraftAndSounds() throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        CommentedConfig config = CommentedConfig.inMemory();
        DivineRelicsCommonConfig.SPEC.correct(config);
        DivineRelicsCommonConfig.SPEC.setConfig(config);

        ThorEntityEffectsTest fixture = new ThorEntityEffectsTest();
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(fixture)) {
            Map<ResourceLocation, SoundEvent> sounds = new HashMap<>();
            when(fixture.soundRegistry.containsKey(any(ResourceLocation.class)))
                    .thenAnswer(invocation -> sounds.containsKey(invocation.getArgument(0)));
            when(fixture.soundRegistry.getValue(any(ResourceLocation.class)))
                    .thenAnswer(invocation -> sounds.get(invocation.getArgument(0)));
            when(fixture.soundRegistry.getHolder(any(ResourceLocation.class))).thenReturn(Optional.empty());
            RegisterEvent event = mock(RegisterEvent.class);
            doReturn(ForgeRegistries.Keys.SOUND_EVENTS).when(event).getRegistryKey();
            when(event.<SoundEvent>getForgeRegistry()).thenReturn(fixture.soundRegistry);
            doAnswer(invocation -> {
                ResourceLocation name = invocation.getArgument(1);
                Supplier<SoundEvent> supplier = invocation.getArgument(2);
                sounds.put(name, supplier.get());
                return null;
            }).when(event).register(eq(ForgeRegistries.Keys.SOUND_EVENTS), any(ResourceLocation.class), any());
            new DeferredRegister.EventDispatcher(DRSounds.SOUND_EVENTS).handleEvent(event);
            // JUnit has no Forge transformer to fill CapabilityToken.getType().
            try (MockedStatic<CapabilityManager> capabilities = mockStatic(CapabilityManager.class)) {
                capabilities.when(() -> CapabilityManager.get(any())).thenAnswer(invocation -> fixture.teammatesCapability);
                assertEquals(fixture.teammatesCapability, TeammatesProvider.TEAMMATES);
            }
        }
    }

    @AfterAll
    static void unloadConfig() {
        DivineRelicsCommonConfig.SPEC.setConfig(null);
    }

    @Test
    void onlyFirstHitPlaysTauntForEachThor() {
        ThorEntity thor = thor(false);
        ServerPlayer player = mock(ServerPlayer.class);
        thor.onReturningMjolnirHitPlayer(player);
        thor.onReturningMjolnirHitPlayer(player);
        thor.onReturningMjolnirHitPlayer(mock(ServerPlayer.class));
        verify(player, times(1)).playNotifySound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()),
                eq(SoundSource.HOSTILE), eq(1.0F), eq(1.0F));
        verify(thor.level, times(1)).playSound(eq(player), anyDouble(), anyDouble(), anyDouble(),
                eq(DRSounds.THOR_AMBIENT_DUMBASS.get()), eq(SoundSource.HOSTILE), eq(1.0F), eq(1.0F));

        ThorEntity anotherThor = thor(false);
        ServerPlayer anotherPlayer = mock(ServerPlayer.class);
        anotherThor.onReturningMjolnirHitPlayer(anotherPlayer);
        verify(anotherPlayer).playNotifySound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()),
                eq(SoundSource.HOSTILE), anyFloat(), anyFloat());
    }

    @Test
    void clientDoesNotPlayOrConsumeTaunt() {
        ThorEntity thor = thor(true);
        ServerPlayer player = mock(ServerPlayer.class);
        thor.onReturningMjolnirHitPlayer(player);
        verify(player, never()).playNotifySound(any(SoundEvent.class), any(SoundSource.class), anyFloat(), anyFloat());
        thor.level = mock(Level.class);
        thor.onReturningMjolnirHitPlayer(player);
        verify(player).playNotifySound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()),
                eq(SoundSource.HOSTILE), anyFloat(), anyFloat());
    }

    @Test
    void savedFlagPreventsRepeatingAfterLoad() {
        ThorEntity thor = thor(false);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("HasTauntedReturningMjolnirHit", true);
        thor.readAdditionalSaveData(tag);
        ServerPlayer player = mock(ServerPlayer.class);
        thor.onReturningMjolnirHitPlayer(player);
        verify(player, never()).playNotifySound(any(SoundEvent.class), any(SoundSource.class), anyFloat(), anyFloat());
    }

    @Test
    void olderSavesWithoutFlagCanStillPlayOnce() {
        ThorEntity thor = thor(false);
        thor.readAdditionalSaveData(new CompoundTag());
        ServerPlayer player = mock(ServerPlayer.class);
        thor.onReturningMjolnirHitPlayer(player);
        thor.onReturningMjolnirHitPlayer(player);
        verify(player, times(1)).playNotifySound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()),
                eq(SoundSource.HOSTILE), anyFloat(), anyFloat());
    }

    @Test
    void distantVictimGetsLocalTauntAndIsExcludedFromPositionalBroadcast() {
        ThorEntity thor = thor(false);
        when(thor.getX()).thenReturn(10.0D);
        when(thor.getY()).thenReturn(64.0D);
        when(thor.getZ()).thenReturn(20.0D);
        ServerPlayer player = mock(ServerPlayer.class);
        when(player.getX()).thenReturn(74.0D);
        when(player.getY()).thenReturn(64.0D);
        when(player.getZ()).thenReturn(20.0D);

        thor.onReturningMjolnirHitPlayer(player);

        verify(player).playNotifySound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()),
                eq(SoundSource.HOSTILE), eq(1.0F), eq(1.0F));
        verify(thor.level).playSound(eq(player), eq(10.0D), eq(64.0D), eq(20.0D),
                eq(DRSounds.THOR_AMBIENT_DUMBASS.get()), eq(SoundSource.HOSTILE), eq(1.0F), eq(1.0F));
        verify(thor, never()).playSound(any(SoundEvent.class), anyFloat(), anyFloat());
    }

    @Test
    void silentThorDoesNotPlayTaunt() {
        ThorEntity thor = thor(false);
        when(thor.isSilent()).thenReturn(true);
        ServerPlayer player = mock(ServerPlayer.class);
        thor.onReturningMjolnirHitPlayer(player);
        verify(player, never()).playNotifySound(any(SoundEvent.class), any(SoundSource.class), anyFloat(), anyFloat());
        verify(thor.level, never()).playSound(any(), anyDouble(), anyDouble(), anyDouble(),
                any(SoundEvent.class), any(SoundSource.class), anyFloat(), anyFloat());
    }

    @Test
    void introLightningUsesTerrainAtItsRandomStrikeLocation() {
        ThorEntity thor = thor(false);
        thor.tickCount = ThorEntity.LIGHTNING_TICKS[0];
        BlockPos elevatedPos = new BlockPos(10, 74, 20);
        BlockPos randomPos = new BlockPos(80, 74, 90);
        BlockPos groundPos = new BlockPos(80, 50, 90);
        when(thor.blockPosition()).thenReturn(elevatedPos);
        when(thor.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, randomPos)).thenReturn(groundPos);
        try (MockedStatic<ThrownMjolnir> lightning = mockStatic(ThrownMjolnir.class)) {
            lightning.when(() -> ThrownMjolnir.getRandBlockPos(elevatedPos, 100, 100, 50)).thenReturn(randomPos);
            thor.tickSummoning();
            lightning.verify(() -> ThrownMjolnir.spawnLightning(thor.level, groundPos, thor));
        }
        verify(thor, never()).teleportTo(anyDouble(), anyDouble(), anyDouble());
        verify(thor, never()).setSummoningComplete(true);
    }

    @Test
    void arrivalUsesPostDescentPositionAndGroundsItsShockwaveBolts() {
        ThorEntity thor = thor(false);
        thor.tickCount = ThorEntity.INVULNERABLE_TICKS;
        AtomicReference<Vec3> position = new AtomicReference<>(new Vec3(10, 74.5D, 20));
        when(thor.getX()).thenAnswer(invocation -> position.get().x);
        when(thor.getY()).thenAnswer(invocation -> position.get().y);
        when(thor.getZ()).thenAnswer(invocation -> position.get().z);
        when(thor.blockPosition()).thenAnswer(invocation -> new BlockPos(position.get()));
        doAnswer(invocation -> {
            position.set(new Vec3(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2)));
            return null;
        }).when(thor).teleportTo(anyDouble(), anyDouble(), anyDouble());
        BlockPos landedPos = new BlockPos(10, 64, 20);
        when(thor.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, landedPos)).thenReturn(landedPos);
        try (MockedStatic<ThrownMjolnir> lightning = mockStatic(ThrownMjolnir.class)) {
            thor.tickSummoning();
            lightning.verify(() -> ThrownMjolnir.spawnLightning(thor.level, landedPos, thor));
            lightning.verify(() -> ThrownMjolnir.onHitLighnting(thor.level, thor, landedPos, thor,
                    thor.thorDamageSource, 10f, 2f, 12, null, null, true));
        }
        verify(thor).teleportTo(10.0D, 64.5D, 20.0D);
        verify(thor.level).getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, landedPos);
        verify(thor).setSummoningComplete(true);
        verify(thor).setNoAi(false);
    }

    @Test
    void unscheduledTicksAndCompletedSummoningProduceNoIntroLightning() {
        ThorEntity thor = thor(false);
        thor.tickCount = 1;
        try (MockedStatic<ThrownMjolnir> lightning = mockStatic(ThrownMjolnir.class)) {
            thor.tickSummoning();
            thor.tickCount = ThorEntity.INVULNERABLE_TICKS;
            when(thor.isSummoningComplete()).thenReturn(true);
            thor.tickSummoning();
            lightning.verifyNoInteractions();
        }
    }

    @Test
    void groundedStrikeProjectsBothTargetAndRandomBoltsOntoTerrain() {
        assertLightningPlacement(true);
    }

    @Test
    void ordinaryCombatStrikeKeepsExistingLightningPositions() {
        assertLightningPlacement(false);
    }

    private static void assertLightningPlacement(boolean grounded) {
        Level level = mock(Level.class);
        Entity hammer = mock(Entity.class);
        LivingEntity owner = mock(LivingEntity.class);
        LivingEntity target = mock(LivingEntity.class);
        when(owner.getCapability(TeammatesProvider.TEAMMATES)).thenReturn(LazyOptional.empty());
        when(target.getOnPos()).thenReturn(new BlockPos(2, 120, 3));
        when(level.getHeightmapPos(eq(Heightmap.Types.MOTION_BLOCKING), any(BlockPos.class)))
                .thenAnswer(invocation -> {
                    BlockPos pos = invocation.getArgument(1);
                    return new BlockPos(pos.getX(), 64 + Math.floorMod(pos.getX(), 5), pos.getZ());
                });
        try (MockedStatic<Motosignir> area = mockStatic(Motosignir.class);
             MockedConstruction<LightningBolt> bolts = mockConstruction(LightningBolt.class)) {
            area.when(() -> Motosignir.getEntitiesInArea(eq(level), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                    .thenReturn(List.of(target));
            if (grounded) {
                ThrownMjolnir.onHitLighnting(level, hammer, new BlockPos(0, 80, 0), owner, DamageSource.LIGHTNING_BOLT,
                        10.0D, 2.0D, 12.0D, null, null, true);
            } else {
                ThrownMjolnir.onHitLighnting(level, hammer, new BlockPos(0, 80, 0), owner, DamageSource.LIGHTNING_BOLT,
                        10.0D, 2.0D, 12.0D);
            }
            assertEquals(DivineRelicsCommonConfig.THROWN_MJOLNIR_LIGHNTING_COUNT.get(), bolts.constructed().size());
            for (int i = 0; i < bolts.constructed().size(); i++) {
                ArgumentCaptor<Vec3> position = ArgumentCaptor.forClass(Vec3.class);
                verify(bolts.constructed().get(i)).moveTo(position.capture());
                Vec3 actual = position.getValue();
                double expectedY = grounded ? 64 + Math.floorMod((int) Math.floor(actual.x), 5) : i == 0 ? 120 : 80;
                assertEquals(expectedY, actual.y);
                verify(bolts.constructed().get(i)).setVisualOnly(false);
            }
        }
        if (!grounded) {
            verify(level, never()).getHeightmapPos(any(Heightmap.Types.class), any(BlockPos.class));
        }
    }

    private static ThorEntity thor(boolean clientSide) {
        TestThor thor = mock(TestThor.class);
        thor.level = mock(Level.class);
        when(thor.level.isClientSide()).thenReturn(clientSide);
        doCallRealMethod().when(thor).onReturningMjolnirHitPlayer(any(ServerPlayer.class));
        doReturn(1.0F).when(thor).getSoundVolume();
        when(thor.getVoicePitch()).thenReturn(1.0F);
        doCallRealMethod().when(thor).readAdditionalSaveData(any(CompoundTag.class));
        doCallRealMethod().when(thor).tickSummoning();
        return thor;
    }

    private static class TestThor extends ThorEntity {
        TestThor(EntityType<? extends Monster> type, Level level) {
            super(type, level);
        }

        @Override
        public float getSoundVolume() {
            return super.getSoundVolume();
        }
    }
}
