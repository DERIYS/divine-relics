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

    public static final RegistryObject<SoundEvent> HEL_WALKER_ATTACK_1 =
            registerSoundEvent("hel_walker_attack_1");
    public static final RegistryObject<SoundEvent> HEL_WALKER_ATTACK_2 =
            registerSoundEvent("hel_walker_attack_2");
    public static final RegistryObject<SoundEvent> HEL_WALKER_ATTACK_3 =
            registerSoundEvent("hel_walker_attack_3");

    public static final RegistryObject<SoundEvent> HEL_WALKER_AMBIENT_1 =
            registerSoundEvent("hel_walker_ambient_1");
    public static final RegistryObject<SoundEvent> HEL_WALKER_AMBIENT_2 =
            registerSoundEvent("hel_walker_ambient_2");
    public static final RegistryObject<SoundEvent> HEL_WALKER_AMBIENT_3 =
            registerSoundEvent("hel_walker_ambient_3");
    public static final RegistryObject<SoundEvent> HEL_WALKER_AMBIENT_4 =
            registerSoundEvent("hel_walker_ambient_4");
    public static final RegistryObject<SoundEvent> HEL_WALKER_AMBIENT_5 =
            registerSoundEvent("hel_walker_ambient_5");
    public static final RegistryObject<SoundEvent> HEL_WALKER_AMBIENT_6 =
            registerSoundEvent("hel_walker_ambient_6");

    public static final RegistryObject<SoundEvent> HEL_WALKER_HIT_1 =
            registerSoundEvent("hel_walker_hit_1");
    public static final RegistryObject<SoundEvent> HEL_WALKER_HIT_2 =
            registerSoundEvent("hel_walker_hit_2");
    public static final RegistryObject<SoundEvent> HEL_WALKER_HIT_3 =
            registerSoundEvent("hel_walker_hit_3");
    public static final RegistryObject<SoundEvent> HEL_WALKER_HIT_4 =
            registerSoundEvent("hel_walker_hit_4");
    public static final RegistryObject<SoundEvent> HEL_WALKER_HIT_5 =
            registerSoundEvent("hel_walker_hit_5");
    public static final RegistryObject<SoundEvent> HEL_WALKER_HIT_6 =
            registerSoundEvent("hel_walker_hit_6");

    public static final RegistryObject<SoundEvent> THOR_FIGHT_MUSIC =
            registerSoundEvent("thor_fight_music");
    public static final RegistryObject<SoundEvent> THOR_FIGHT_MUSIC_LOOP =
            registerSoundEvent("thor_fight_music_loop");
    public static final RegistryObject<SoundEvent> THOR_MELEE_ATTACK =
            registerSoundEvent("thor_melee_attack");
    public static final RegistryObject<SoundEvent> THOR_LEG_ATTACK =
            registerSoundEvent("thor_leg_attack");
    public static final RegistryObject<SoundEvent> THOR_CLAP_ATTACK =
            registerSoundEvent("thor_clap_attack");
    public static final RegistryObject<SoundEvent> THOR_GROUND_ATTACK =
            registerSoundEvent("thor_ground_attack");
    public static final RegistryObject<SoundEvent> THOR_MJOLNIR_THROW_ATTACK =
            registerSoundEvent("thor_mjolnir_throw_attack");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_1 =
            registerSoundEvent("thor_ambient_1");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_2 =
            registerSoundEvent("thor_ambient_2");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_3 =
            registerSoundEvent("thor_ambient_3");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_4 =
            registerSoundEvent("thor_ambient_4");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_5 =
            registerSoundEvent("thor_ambient_5");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_6 =
            registerSoundEvent("thor_ambient_6");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_7 =
            registerSoundEvent("thor_ambient_7");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_8 =
            registerSoundEvent("thor_ambient_8");
    public static final RegistryObject<SoundEvent> THOR_AMBIENT_9 =
            registerSoundEvent("thor_ambient_9");
    public static final RegistryObject<SoundEvent> THOR_DEATH_SOUND =
            registerSoundEvent("thor_death_sound");

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

    public static final RegistryObject<SoundEvent> HARMONY =
            registerSoundEvent("harmony");

    public static final RegistryObject<SoundEvent> SCAPE_THEME =
            registerSoundEvent("scape_theme");

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(DivineRelics.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
