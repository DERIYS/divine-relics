package com.deriys.divinerelics.util;

import com.deriys.divinerelics.init.DRItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class DRItemProperties {
    public static void addCustomItemProperties() {
        makeShield(DRItems.GUARDIAN_SHIELD.get());
        makeUsing(DRItems.DRAUPNIR_SPEAR.get());
        makeUsing(DRItems.MJOLNIR.get());
        makeUsing(DRItems.LEVIATHAN_AXE.get());
    }

    private static void makeShield(Item item) {
        ItemProperties.register(item, new ResourceLocation("blocking"), (p_174590_, p_174591_, p_174592_, p_174593_) -> {
            return p_174592_ != null && p_174592_.isUsingItem() && p_174592_.getUseItem() == p_174590_ ? 1.0F : 0.0F;
        });
    }

    private static void makeUsing(Item item) {
        ItemProperties.register(item, new ResourceLocation("using"), (stack, level, player, number) -> {
            return player != null && player.isUsingItem() && player.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }
}
