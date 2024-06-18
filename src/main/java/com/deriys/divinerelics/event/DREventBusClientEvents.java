package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.client.DRModelLayers;
import com.deriys.divinerelics.entities.client.model.ThrownDraupnirSpearModel;
import com.deriys.divinerelics.entities.client.model.ThrownMjolnirModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DREventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DRModelLayers.DRAUPNIR_SPEAR_LAYER, ThrownDraupnirSpearModel::createBodyLayer);
        event.registerLayerDefinition(DRModelLayers.MJOLNIR_LAYER, ThrownMjolnirModel::createBodyLayer);
    }
}
