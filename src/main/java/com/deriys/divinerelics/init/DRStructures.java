package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.structures.FluidSealProcessor;
import com.deriys.divinerelics.structures.NoWaterNearbyStructure;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DRStructures {
    public static final DeferredRegister<StructureType<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, DivineRelics.MODID);

    public static final DeferredRegister<StructureProcessorType<?>> DEFERRED_REGISTRY_PROCESSOR = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, DivineRelics.MODID);

    public static final RegistryObject<StructureType<NoWaterNearbyStructure>> NO_WATER_NEARBY = DEFERRED_REGISTRY_STRUCTURE.register("no_water_nearby", () -> explicitStructureTypeTyping(NoWaterNearbyStructure.CODEC));

    /** Used by the Dwarven Mines template pools (processor list divinerelics:dwarven_mines_fluid_seal). */
    public static final RegistryObject<StructureProcessorType<FluidSealProcessor>> FLUID_SEAL = DEFERRED_REGISTRY_PROCESSOR.register("fluid_seal", () -> () -> FluidSealProcessor.CODEC);

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus eventBus) {
        DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
        DEFERRED_REGISTRY_PROCESSOR.register(eventBus);
    }
}
