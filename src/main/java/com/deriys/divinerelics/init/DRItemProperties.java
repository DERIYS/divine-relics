package com.deriys.divinerelics.init;

import com.deriys.divinerelics.items.DwarvenCompass;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class DRItemProperties {
    public static void addCustomItemProperties() {
        makeShield(DRItems.GUARDIAN_SHIELD.get());
        makeUsing(DRItems.DRAUPNIR_SPEAR.get());
        makeUsing(DRItems.MJOLNIR.get());
        makeUsing(DRItems.LEVIATHAN_AXE.get());
        makeDwarvenCompassAngle((DwarvenCompass) DRItems.DWARVEN_COMPASS.get());
    }

    private static void makeShield(Item item) {
        ItemProperties.register(item, new ResourceLocation("blocking"), (itemStack, level, player, number) -> {
            return player != null && player.isUsingItem() && player.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    private static void makeUsing(Item item) {
        ItemProperties.register(item, new ResourceLocation("using"), (stack, level, player, number) -> {
            return player != null && player.isUsingItem() && player.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }

    private static void makeDwarvenCompassAngle(DwarvenCompass compass) {
        // null (no target yet / search running) makes the needle spin instead of pointing at (0, 0); targets from
        // another dimension also spin, like a lodestone compass.
        ItemProperties.register(compass, new ResourceLocation("angle"), new CompassItemPropertyFunction((level, itemStack, entity) -> DwarvenCompass.getTarget(itemStack)));
    }
}
