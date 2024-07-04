package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DRSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DivineRelics.MODID);

    public static final RegistryObject<SoundEvent> MOTOSIGNIR_SOUND_WAVE =
            registerSoundEvent("motosignir_sound_wave");

    public static final RegistryObject<SoundEvent> DRAUPNIR_SPEAR_THROWING =
            registerSoundEvent("draupnir_spear_throwing");
    public static final RegistryObject<SoundEvent> DRAUPNIR_SPEAR_LANDING =
            registerSoundEvent("draupnir_spear_landing");
    public static final RegistryObject<SoundEvent> DRAUPNIR_SPEAR_HIT =
            registerSoundEvent("draupnir_spear_hit");
    public static final RegistryObject<SoundEvent> DRAUPNIR_SPEAR_EXPLOSION_HIT =
            registerSoundEvent("draupnir_spear_explosion_hit");
    public static final RegistryObject<SoundEvent> DRAUPNIR_SPEAR_EXPLOSION =
            registerSoundEvent("draupnir_spear_explosion");

    public static final RegistryObject<SoundEvent> MJOLNIR_THROWING =
            registerSoundEvent("mjolnir_throw");
    public static final RegistryObject<SoundEvent> MJOLNIR_IMPACT =
            registerSoundEvent("mjolnir_ground_impact");
    public static final RegistryObject<SoundEvent> MJOLNIR_RETURN =
            registerSoundEvent("mjolnir_return");
    public static final RegistryObject<SoundEvent> MJOLNIR_RIPTIDE =
            registerSoundEvent("mjolnir_riptide");
    public static final RegistryObject<SoundEvent> MJOLNIR_PIERCE =
            registerSoundEvent("mjolnir_pierce");
    public static final RegistryObject<SoundEvent> MJOLNIR_THUNDER =
            registerSoundEvent("mjolnir_thunder");

    public static final RegistryObject<SoundEvent> GUARDIAN_SHIELD_OPEN =
            registerSoundEvent("guardian_shield_open");
    public static final RegistryObject<SoundEvent> GUARDIAN_SHIELD_PARRY =
            registerSoundEvent("guardian_shield_parry");

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(DivineRelics.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
