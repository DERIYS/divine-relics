package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.init.DRSounds;
import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ThorReturningMjolnirTauntTest {
    @Mock
    private IForgeRegistry<SoundEvent> soundRegistry;

    @BeforeAll
    static void setupMinecraftAndSounds() throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        CommentedConfig config = CommentedConfig.inMemory();
        DivineRelicsCommonConfig.SPEC.correct(config);
        DivineRelicsCommonConfig.SPEC.setConfig(config);

        ThorReturningMjolnirTauntTest fixture = new ThorReturningMjolnirTauntTest();
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
        }
    }

    @AfterAll
    static void unloadConfig() {
        DivineRelicsCommonConfig.SPEC.setConfig(null);
    }

    @Test
    void onlyFirstHitPlaysTauntForEachThor() {
        ThorEntity thor = thor(false);
        thor.onReturningMjolnirHitPlayer();
        thor.onReturningMjolnirHitPlayer();
        thor.onReturningMjolnirHitPlayer();
        verify(thor, times(1)).playSound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()), anyFloat(), anyFloat());

        ThorEntity anotherThor = thor(false);
        anotherThor.onReturningMjolnirHitPlayer();
        verify(anotherThor).playSound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()), anyFloat(), anyFloat());
    }

    @Test
    void clientDoesNotPlayOrConsumeTaunt() {
        ThorEntity thor = thor(true);
        thor.onReturningMjolnirHitPlayer();
        verify(thor, never()).playSound(any(SoundEvent.class), anyFloat(), anyFloat());
        thor.level = mock(Level.class);
        thor.onReturningMjolnirHitPlayer();
        verify(thor).playSound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()), anyFloat(), anyFloat());
    }

    @Test
    void savedFlagPreventsRepeatingAfterLoad() {
        ThorEntity thor = thor(false);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("HasTauntedReturningMjolnirHit", true);
        thor.readAdditionalSaveData(tag);
        thor.onReturningMjolnirHitPlayer();
        verify(thor, never()).playSound(any(SoundEvent.class), anyFloat(), anyFloat());
    }

    @Test
    void olderSavesWithoutFlagCanStillPlayOnce() {
        ThorEntity thor = thor(false);
        thor.readAdditionalSaveData(new CompoundTag());
        thor.onReturningMjolnirHitPlayer();
        thor.onReturningMjolnirHitPlayer();
        verify(thor, times(1)).playSound(eq(DRSounds.THOR_AMBIENT_DUMBASS.get()), anyFloat(), anyFloat());
    }

    private static ThorEntity thor(boolean clientSide) {
        ThorEntity thor = mock(ThorEntity.class);
        thor.level = mock(Level.class);
        when(thor.level.isClientSide()).thenReturn(clientSide);
        doCallRealMethod().when(thor).onReturningMjolnirHitPlayer();
        doCallRealMethod().when(thor).readAdditionalSaveData(any(CompoundTag.class));
        return thor;
    }
}
