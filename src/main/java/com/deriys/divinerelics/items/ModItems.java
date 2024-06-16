package com.deriys.divinerelics.items;

import com.deriys.divinerelics.DivineRelics;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DivineRelics.MODID);

    public static final RegistryObject<Item> MOTOSIGNIR = ITEMS.register("motosignir",
            () -> new Motosignir(ModTiers.MOTOSIGNIR, -3, -1.5F, new Item.Properties().fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> HEIMDALL_GAUNTLET = ITEMS.register("heimdall_gauntlet",
            () -> new HeimdallGauntlet(ModTiers.HEIMDALL_GAUNTLET, -3, -3F, new Item.Properties().fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> DRAUPNIR_SPEAR = ITEMS.register("draupnir_spear",
            () -> new DraupnirSpear(new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> ORACLE_COMPASS = ITEMS.register("oracle_compass",
            () -> new OracleCompass(new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
