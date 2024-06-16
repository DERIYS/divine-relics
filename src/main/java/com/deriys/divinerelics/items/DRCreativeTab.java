package com.deriys.divinerelics.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DRCreativeTab {
    public static final CreativeModeTab MAINTAB = new CreativeModeTab("drmaintab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MOTOSIGNIR.get());
        }
    };
}