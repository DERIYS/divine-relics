package com.deriys.divinerelics.event;


import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.DraugrEntity;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.deriys.divinerelics.init.DREntitiyTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DRModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(DREntitiyTypes.DRAUGR.get(), DraugrEntity.createAttributes().build());
        event.put(DREntitiyTypes.THOR.get(), ThorEntity.createAttributes().build());
    }
}
