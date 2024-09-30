package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class DRBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DivineRelics.MODID);

    public static final RegistryObject<Block> HACKSILVER_ORE = registerBlock("hacksilver_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4f).requiresCorrectToolForDrops()), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> DEEPSLATE_HACKSILVER_ORE = registerBlock("deepslate_hacksilver_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> HACKSILVER_BLOCK = registerBlock("hacksilver_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> SVARTALFHEIM_STEEL_ORE = registerBlock("svartalfheim_steel_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> DEEPSLATE_SVARTALFHEIM_STEEL_ORE = registerBlock("deepslate_svartalfheim_steel_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> ASGARDIAN_STEEL_ORE = registerBlock("asgardian_steel_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> DEEPSLATE_ASGARDIAN_STEEL_ORE = registerBlock("deepslate_asgardian_steel_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)), DRCreativeTab.MAINTAB);

    public static final RegistryObject<Block> OBSIDIAN_ASGARDIAN_STEEL_ORE = registerBlock("obsidian_asgardian_steel_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(8f).requiresCorrectToolForDrops()), DRCreativeTab.MAINTAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return DRItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
