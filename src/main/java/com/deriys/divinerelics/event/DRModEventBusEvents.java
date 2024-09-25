package com.deriys.divinerelics.event;


import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.BrokEntity;
import com.deriys.divinerelics.entities.entity.DraugrEntity;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.deriys.divinerelics.init.DREntitiyTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DRModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(DREntitiyTypes.DRAUGR.get(), DraugrEntity.createAttributes().build());
        event.put(DREntitiyTypes.BROK.get(), BrokEntity.createAttributes().build());
        event.put(DREntitiyTypes.SINDRI.get(), BrokEntity.createAttributes().build());
        event.put(DREntitiyTypes.THOR.get(), ThorEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        SpawnPlacements.register(DREntitiyTypes.DRAUGR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, DraugrEntity::canSpawn);
    }
}
