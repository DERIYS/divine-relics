package com.deriys.divinerelics;

import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.dwarfs.DRDwarfs;
import com.deriys.divinerelics.entities.client.render.*;
import com.deriys.divinerelics.init.*;
import com.deriys.divinerelics.util.DRItemProperties;
import com.deriys.divinerelics.world.feature.DRConfiguredFeatures;
import com.deriys.divinerelics.world.feature.DRPlacedFeatures;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(DivineRelics.MODID)
public class DivineRelics
{
    public static final String MODID = "divinerelics";

    public DivineRelics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        DRBlocks.register(modEventBus);
        DRItems.register(modEventBus);
        DRSounds.register(modEventBus);
        DREffects.register(modEventBus);
        DREntitiyTypes.register(modEventBus);
        DRConfiguredFeatures.register(modEventBus);
        DRPlacedFeatures.register(modEventBus);
        DRDwarfs.register(modEventBus);
        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DRMessages.register();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(DREntitiyTypes.DRAUGR.get(), DraugrRenderer::new);
            EntityRenderers.register(DREntitiyTypes.BROK.get(), BrokRenderer::new);
            EntityRenderers.register(DREntitiyTypes.SINDRI.get(), SindriRenderer::new);
            EntityRenderers.register(DREntitiyTypes.THOR.get(), ThorRenderer::new);
            EntityRenderers.register(DREntitiyTypes.THROWN_DRAUPNIR_SPEAR.get(), ThrownDraupnirSpearRenderer::new);
            EntityRenderers.register(DREntitiyTypes.THROWN_MJOLNIR.get(), ThrownMjolnirRenderer::new);
            EntityRenderers.register(DREntitiyTypes.THROWN_LEVIATHAN.get(), ThrownLeviathanRenderer::new);
            DRItemProperties.addCustomItemProperties();
        }
    }
}
