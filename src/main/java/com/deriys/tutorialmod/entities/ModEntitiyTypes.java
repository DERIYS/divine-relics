package com.deriys.tutorialmod.entities;

import com.deriys.tutorialmod.TutorialMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntitiyTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TutorialMod.MODID);

    public static final RegistryObject<EntityType<ThrownDraupnirSpear>> THROWN_DRAUPNIR_SPEAR =
        ENTITY_TYPES.register("thrown_draupnir_spear",
                () -> EntityType.Builder.of(ThrownDraupnirSpear::create, MobCategory.MISC)
                        .sized(0.5F, 0.5F)
                        .build(new ResourceLocation(TutorialMod.MODID, "thrown_draupnir_spear").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
