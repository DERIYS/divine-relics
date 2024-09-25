package com.deriys.divinerelics.init;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DRCreativeTab {
    public static final CreativeModeTab MAINTAB = new CreativeModeTab("drmaintab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(DRItems.MOTOSIGNIR.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            super.fillItemList(items);

            items.clear();

            items.add(new ItemStack(DRItems.MOTOSIGNIR.get()));

            items.add(new ItemStack(DRItems.HEIMDALL_GAUNTLET.get()));

            items.add(new ItemStack(DRItems.GUARDIAN_SHIELD.get()));

            items.add(new ItemStack(DRItems.YGGDRASIL_BRANCH.get()));

            items.add(new ItemStack(DRItems.DRAUPNIR_RING.get()));
            items.add(new ItemStack(DRItems.DRAUPNIR_SPEAR_BASE.get()));
            items.add(new ItemStack(DRItems.DRAUPNIR_SPEAR.get()));

            items.add(new ItemStack(DRItems.LEVIATHAN_HANDLE.get()));
            items.add(new ItemStack(DRItems.LEVIATHAN_HEAD.get()));
            items.add(new ItemStack(DRItems.FROZEN_FLAME.get()));
            items.add(new ItemStack(DRItems.LEVIATHAN_AXE.get()));

            items.add(new ItemStack(DRItems.MJOLNIR_HANDLE.get()));
            items.add(new ItemStack(DRItems.MJOLNIR_HEAD.get()));
            items.add(new ItemStack(DRItems.MJOLNIR.get()));

            items.add(new ItemStack(DRItems.DIVINE_MEAD.get()));

            items.add(new ItemStack(DRItems.HACKSILVER.get()));
            items.add(new ItemStack(DRItems.COMPRESSED_HACKSILVER.get()));
            items.add(new ItemStack(DRItems.HACKSILVER_INGOT.get()));
            items.add(new ItemStack(DRBlocks.HACKSILVER_BLOCK.get()));

            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get()));
            items.add(new ItemStack(DRItems.RAW_SVARTALFHEIM_STEEL.get()));
            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get()));
            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_SWORD.get()));
            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_SHOVEL.get()));
            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_PICKAXE.get()));
            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_AXE.get()));
            items.add(new ItemStack(DRItems.SVARTALFHEIM_STEEL_HOE.get()));
            items.add(new ItemStack(DRItems.DWARVEN_COMPASS.get()));

            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get()));
            items.add(new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get()));
            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get()));
            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_SWORD.get()));
            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_SHOVEL.get()));
            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_PICKAXE.get()));
            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_AXE.get()));
            items.add(new ItemStack(DRItems.ASGARDIAN_STEEL_HOE.get()));

            items.add(new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get()));

            items.add(new ItemStack(DRBlocks.HACKSILVER_ORE.get()));
            items.add(new ItemStack(DRBlocks.DEEPSLATE_HACKSILVER_ORE.get()));

            items.add(new ItemStack(DRBlocks.SVARTALFHEIM_STEEL_ORE.get()));
            items.add(new ItemStack(DRBlocks.DEEPSLATE_SVARTALFHEIM_STEEL_ORE.get()));

            items.add(new ItemStack(DRBlocks.ASGARDIAN_STEEL_ORE.get()));
            items.add(new ItemStack(DRBlocks.DEEPSLATE_ASGARDIAN_STEEL_ORE.get()));
        }
    };
}
