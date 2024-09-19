package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.items.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DRItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DivineRelics.MODID);

    public static final RegistryObject<Item> MOTOSIGNIR = ITEMS.register("motosignir",
            () -> new Motosignir(new Item.Properties().fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> HEIMDALL_GAUNTLET = ITEMS.register("heimdall_gauntlet",
            () -> new HeimdallGauntlet(new Item.Properties().fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> DRAUPNIR_RING = ITEMS.register("draupnir_ring",
            () -> new Item(new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> DRAUPNIR_SPEAR_BASE = ITEMS.register("draupnir_spear_base",
            () -> new Item(new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> DRAUPNIR_SPEAR = ITEMS.register("draupnir_spear",
            () -> new DraupnirSpear(DRTiers.DRAUPNIR, 0, 0, new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> THROWN_DRAUPNIR_SPEAR = ITEMS.register("thrown_draupnir_spear_item",
            () -> new Item(new Item.Properties())); // for stuck spears rendering

    public static final RegistryObject<Item> MJOLNIR_HANDLE = ITEMS.register("mjolnir_handle",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).stacksTo(1)));
    public static final RegistryObject<Item> MJOLNIR_HEAD = ITEMS.register("mjolnir_head",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).stacksTo(1)));
    public static final RegistryObject<Item> MJOLNIR = ITEMS.register("mjolnir",
            () -> new Mjolnir(DRTiers.MJOLNIR, 0, 0, new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> GUARDIAN_SHIELD = ITEMS.register("guardian_shield",
            () -> new GuardianShield(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> YGGDRASILS_TWIG = ITEMS.register("yggdrasils_twig",
            () -> new YggdrasilsTwig(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> LEVIATHAN_HANDLE = ITEMS.register("leviathan_handle",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).stacksTo(1)));
    public static final RegistryObject<Item> LEVIATHAN_HEAD = ITEMS.register("leviathan_head",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).stacksTo(1)));
    public static final RegistryObject<Item> FROZEN_FLAME = ITEMS.register("frozen_flame",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).fireResistant().stacksTo(1)));
    public static final RegistryObject<Item> LEVIATHAN_AXE = ITEMS.register("leviathan_axe",
            () -> new LeviathanAxe(DRTiers.LEVIATHAN, 0, 0, new Item.Properties().stacksTo(1).fireResistant().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> THOR_FIGHT_MUSIC_DISC = ITEMS.register("thor_fight_music_disc",
            () -> new RecordItem(6, DRSounds.THOR_FIGHT_MUSIC, new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).rarity(Rarity.RARE), 4013));

    public static final RegistryObject<Item> DIVINE_MEAD = ITEMS.register("divine_mead",
            () -> new DivineMead(new Item.Properties().tab(DRCreativeTab.MAINTAB).stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DRAUGR_SPAWN_EGG = ITEMS.register("draugr_spawn_egg",
            () -> new ForgeSpawnEggItem(DREntitiyTypes.DRAUGR, 0x7e9680, 0xc5d1c5, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> HACKSILVER = ITEMS.register("hacksilver",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> COMPRESSED_HACKSILVER = ITEMS.register("compressed_hacksilver",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> HACKSILVER_INGOT = ITEMS.register("hacksilver_ingot",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_NUGGET = ITEMS.register("svartalfheim_steel_nugget",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> RAW_SVARTALFHEIM_STEEL = ITEMS.register("raw_svartalfheim_steel",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_INGOT = ITEMS.register("svartalfheim_steel_ingot",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_SWORD = ITEMS.register("svartalfheim_steel_sword",
            () -> new SwordItem(DRTiers.SVARTALFHEIM_STEEL_TIER, 2, -2.3f, new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_SHOVEL = ITEMS.register("svartalfheim_steel_shovel",
            () -> new ShovelItem(DRTiers.SVARTALFHEIM_STEEL_TIER, 1.5F, -3.0f, new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_PICKAXE = ITEMS.register("svartalfheim_steel_pickaxe",
            () -> new PickaxeItem(DRTiers.SVARTALFHEIM_STEEL_TIER, 1, -2.8f, new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_AXE = ITEMS.register("svartalfheim_steel_axe",
            () -> new AxeItem(DRTiers.SVARTALFHEIM_STEEL_TIER, 4.0F, -3.0f, new Item.Properties().tab(DRCreativeTab.MAINTAB)));
    public static final RegistryObject<Item> SVARTALFHEIM_STEEL_HOE = ITEMS.register("svartalfheim_steel_hoe",
            () -> new AxeItem(DRTiers.SVARTALFHEIM_STEEL_TIER, -4.0F, 0.0f, new Item.Properties().tab(DRCreativeTab.MAINTAB)));

    public static final RegistryObject<Item> ASGARDIAN_STEEL_NUGGET = ITEMS.register("asgardian_steel_nugget",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> RAW_ASGARDIAN_STEEL = ITEMS.register("raw_asgardian_steel",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ASGARDIAN_STEEL_INGOT = ITEMS.register("asgardian_steel_ingot",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ASGARDIAN_STEEL_SWORD = ITEMS.register("asgardian_steel_sword",
            () -> new SwordItem(DRTiers.ASGARDIAN_STEEL_TIER, 3, -2.4f, new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ASGARDIAN_STEEL_SHOVEL = ITEMS.register("asgardian_steel_shovel",
            () -> new ShovelItem(DRTiers.ASGARDIAN_STEEL_TIER, 2.5F, -3.0f, new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ASGARDIAN_STEEL_PICKAXE = ITEMS.register("asgardian_steel_pickaxe",
            () -> new PickaxeItem(DRTiers.ASGARDIAN_STEEL_TIER, 2, -2.3f, new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ASGARDIAN_STEEL_AXE = ITEMS.register("asgardian_steel_axe",
            () -> new AxeItem(DRTiers.ASGARDIAN_STEEL_TIER, 5.0F, -3.0f, new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ASGARDIAN_STEEL_HOE = ITEMS.register("asgardian_steel_hoe",
            () -> new AxeItem(DRTiers.ASGARDIAN_STEEL_TIER, -4.0F, 0.0f, new Item.Properties().tab(DRCreativeTab.MAINTAB).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> PERFECT_ASGARDIAN_STEEL_INGOT = ITEMS.register("perfect_asgardian_steel_ingot",
            () -> new Item(new Item.Properties().tab(DRCreativeTab.MAINTAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
