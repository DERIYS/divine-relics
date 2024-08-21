package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.items.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DRItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DivineRelics.MODID);

    public static final RegistryObject<Item> MOTOSIGNIR = ITEMS.register("motosignir",
            () -> new Motosignir(DRTiers.MOTOSIGNIR, -3, -1.5F, new Item.Properties().fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> HEIMDALL_GAUNTLET = ITEMS.register("heimdall_gauntlet",
            () -> new HeimdallGauntlet(DRTiers.HEIMDALL_GAUNTLET, -3, -3F, new Item.Properties().fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> DRAUPNIR_SPEAR = ITEMS.register("draupnir_spear",
            () -> new DraupnirSpear(new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> THROWN_DRAUPNIR_SPEAR = ITEMS.register("thrown_draupnir_spear_item",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORACLE_COMPASS = ITEMS.register("oracle_compass",
            () -> new OracleCompass(new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> MJOLNIR = ITEMS.register("mjolnir",
            () -> new Mjolnir(DRTiers.MJOLNIR, 0, 0, new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> GUARDIAN_SHIELD = ITEMS.register("guardian_shield",
            () -> new GuardianShield(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> YGGDRASILS_TWIG = ITEMS.register("yggdrasils_twig",
            () -> new YggdrasilsTwig(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> LEVIATHAN_AXE = ITEMS.register("leviathan_axe",
            () -> new LeviathanAxe(DRTiers.LEVIATHAN, 0, 0, new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));


    public static final RegistryObject<Item> DRAUGR_SPAWN_EGG = ITEMS.register("draugr_spawn_egg",
            () -> new ForgeSpawnEggItem(DREntitiyTypes.DRAUGR, 0x7e9680, 0xc5d1c5, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}