package com.deriys.tutorialmod.items;

import com.deriys.tutorialmod.TutorialMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MODID);

    public static final RegistryObject<Item> MOTOSIGNIR = ITEMS.register("motosignir",
            () -> new Motosignir(ModTiers.MOTOSIGNIR, -3, -1.5F, new Item.Properties().fireResistant().tab(ModCreativeTab.TUTORIAL_TAB)));

    public static final RegistryObject<Item> HEIMDALL_GAUNTLET = ITEMS.register("heimdall_gauntlet",
            () -> new HeimdallGauntlet(ModTiers.HEIMDALL_GAUNTLET, -3, -3F, new Item.Properties().fireResistant().tab(ModCreativeTab.TUTORIAL_TAB)));

    public static final RegistryObject<Item> SPEAR = ITEMS.register("spear",
            () -> new Spear(new Item.Properties().fireResistant().tab(ModCreativeTab.TUTORIAL_TAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
