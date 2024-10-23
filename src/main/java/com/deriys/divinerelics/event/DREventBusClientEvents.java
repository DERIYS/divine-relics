package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.client.DRModelLayers;
import com.deriys.divinerelics.entities.client.model.ThrownDraupnirSpearModel;
import com.deriys.divinerelics.entities.client.model.ThrownLeviathanModel;
import com.deriys.divinerelics.entities.client.model.ThrownMjolnirModel;
import com.deriys.divinerelics.init.DRKeyBindings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DREventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DRModelLayers.DRAUPNIR_SPEAR_LAYER, ThrownDraupnirSpearModel::createBodyLayer);
        event.registerLayerDefinition(DRModelLayers.MJOLNIR_LAYER, ThrownMjolnirModel::createBodyLayer);
        event.registerLayerDefinition(DRModelLayers.LEVIATHAN_AXE_LAYER, ThrownLeviathanModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event){
        event.register(DRKeyBindings.GUARDIAN_SHIELD_KEY);
    }
}
