package com.deriys.divinerelics.util;

import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.items.DwarvenCompass;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
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
        ItemProperties.register(compass, new ResourceLocation("angle"), new CompassItemPropertyFunction((level, itemStack, entity) -> {
            return GlobalPos.of(level.dimension(), new BlockPos(compass.getShopX(itemStack), 0, compass.getShopZ(itemStack)));
        }));
    }
}
