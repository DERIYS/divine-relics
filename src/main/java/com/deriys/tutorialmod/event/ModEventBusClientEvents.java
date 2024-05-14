package com.deriys.tutorialmod.event;

import com.deriys.tutorialmod.TutorialMod;
import com.deriys.tutorialmod.entities.client.ModModelLayers;
import com.deriys.tutorialmod.entities.client.model.ThrownDraupnirSpearModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TutorialMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.DRAUPNIR_SPEAR_LAYER, ThrownDraupnirSpearModel::createBodyLayer);
    }
}
