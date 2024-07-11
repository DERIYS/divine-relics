package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import com.deriys.divinerelics.entities.entity.ThrownLeviathanAxe;
import com.deriys.divinerelics.entities.entity.ThrownMjolnir;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DREntitiyTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DivineRelics.MODID);

    public static final RegistryObject<EntityType<ThrownDraupnirSpear>> THROWN_DRAUPNIR_SPEAR =
            ENTITY_TYPES.register("thrown_draupnir_spear",
                    () -> EntityType.Builder.of(ThrownDraupnirSpear::create, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(DivineRelics.MODID, "thrown_draupnir_spear").toString()));

    public static final RegistryObject<EntityType<ThrownMjolnir>> THROWN_MJOLNIR =
            ENTITY_TYPES.register("thrown_mjolnir",
                    () -> EntityType.Builder.of(ThrownMjolnir::create, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(DivineRelics.MODID, "thrown_mjolnir").toString()));

    public static final RegistryObject<EntityType<ThrownLeviathanAxe>> THROWN_LEVIATHAN =
            ENTITY_TYPES.register("thrown_leviathan",
                    () -> EntityType.Builder.of(ThrownLeviathanAxe::create, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(DivineRelics.MODID, "thrown_mjolnir").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
