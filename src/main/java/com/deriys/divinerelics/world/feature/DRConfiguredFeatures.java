package com.deriys.divinerelics.world.feature;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.init.DRBlocks;
import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class DRConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, DivineRelics.MODID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_HACKSILVER_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, DRBlocks.HACKSILVER_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, DRBlocks.DEEPSLATE_HACKSILVER_ORE.get().defaultBlockState())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> HACKSILVER_ORE = CONFIGURED_FEATURES.register("hacksilver_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_HACKSILVER_ORES.get(),7)));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_SVARTALFHEIM_STEEL_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, DRBlocks.SVARTALFHEIM_STEEL_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, DRBlocks.DEEPSLATE_SVARTALFHEIM_STEEL_ORE.get().defaultBlockState())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SVARTALFHEIM_STEEL_ORE = CONFIGURED_FEATURES.register("svartalfheim_steel_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_SVARTALFHEIM_STEEL_ORES.get(),7)));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_ASGARDIAN_STEEL_ORES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, DRBlocks.ASGARDIAN_STEEL_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, DRBlocks.DEEPSLATE_ASGARDIAN_STEEL_ORE.get().defaultBlockState())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> ASGARDIAN_STEEL_ORE = CONFIGURED_FEATURES.register("asgardian_steel_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_ASGARDIAN_STEEL_ORES.get(),7)));

    public static void register(IEventBus bus) {
        CONFIGURED_FEATURES.register(bus);
    }
}
