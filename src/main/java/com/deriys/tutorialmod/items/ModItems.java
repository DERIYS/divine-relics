package com.deriys.tutorialmod.items;

import com.deriys.tutorialmod.TutorialMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MODID);

    public static final RegistryObject<Item> MOTOSIGNIR = ITEMS.register("motosignir",
            () -> new Motosignir(new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> HEIMDALL_GAUNTLET = ITEMS.register("heimdall_gauntlet",
            () -> new HeimdallGauntlet(new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
