package com.deriys.divinerelics.dwarfs;

import com.deriys.divinerelics.DivineRelics;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DRDwarfs {
    public static final DeferredRegister<VillagerProfession> DWARVEN_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, DivineRelics.MODID);

    public static final RegistryObject<VillagerProfession> BROK = DWARVEN_PROFESSIONS.register("brok_master",
            () -> new VillagerProfession("brok_master", x -> true,
                    x -> true, ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_ARMORER));

    public static final RegistryObject<VillagerProfession> SINDRI = DWARVEN_PROFESSIONS.register("sindri_master",
            () -> new VillagerProfession("brok_master", x -> true,
                    x -> true, ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_ARMORER));

    public static void register(IEventBus eventBus) {
        DWARVEN_PROFESSIONS.register(eventBus);
    }
}
