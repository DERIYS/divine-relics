package com.deriys.divinerelics.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DRCreativeTab {
    public static final CreativeModeTab MAINTAB = new CreativeModeTab("drmaintab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(DRItems.MOTOSIGNIR.get());
        }
    };
}
