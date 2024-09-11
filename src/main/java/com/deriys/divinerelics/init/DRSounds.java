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

    public static final RegistryObject<SoundEvent> DRAUGR_ATTACK =
            registerSoundEvent("draugr_attack");
    public static final RegistryObject<SoundEvent> DRAUGR_AMBIENT_1 =
            registerSoundEvent("draugr_ambient_1");
    public static final RegistryObject<SoundEvent> DRAUGR_AMBIENT_2 =
            registerSoundEvent("draugr_ambient_2");
    public static final RegistryObject<SoundEvent> DRAUGR_AMBIENT_3 =
            registerSoundEvent("draugr_ambient_3");

    public static final RegistryObject<SoundEvent> THOR_FIGHT_MUSIC =
            registerSoundEvent("thor_fight_music");
    public static final RegistryObject<SoundEvent> THOR_MELEE_ATTACK =
            registerSoundEvent("thor_melee_attack");
    public static final RegistryObject<SoundEvent> THOR_CLAP_ATTACK =
            registerSoundEvent("thor_clap_attack");
    public static final RegistryObject<SoundEvent> THOR_GROUND_ATTACK =
            registerSoundEvent("thor_ground_attack");
    public static final RegistryObject<SoundEvent> THOR_MJOLNIR_THROW_ATTACK =
            registerSoundEvent("thor_mjolnir_throw_attack");

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

    public static final RegistryObject<SoundEvent> LEVIATHAN_AXE_IMPACT =
            registerSoundEvent("leviathan_ground_impact");
    public static final RegistryObject<SoundEvent> LEVIATHAN_AXE_PIERCE =
            registerSoundEvent("leviathan_pierce");
    public static final RegistryObject<SoundEvent> LEVIATHAN_AXE_RETURN =
            registerSoundEvent("leviathan_return");
    public static final RegistryObject<SoundEvent> LEVIATHAN_AXE_THROW =
            registerSoundEvent("leviathan_throw");

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(DivineRelics.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
