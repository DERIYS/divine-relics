package com.deriys.divinerelics;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.init.DRDwarfs;
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
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import software.bernie.geckolib3.GeckoLib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

@Mod(DivineRelics.MODID)
public class DivineRelics
{
    public static final String MODID = "divinerelics";

    public DivineRelics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DivineRelicsCommonConfig.SPEC);

        // TurtyWurty's code for initializing config before other registries
        ModConfig commonConfig = ConfigTracker.INSTANCE.configSets().get(ModConfig.Type.COMMON)
                .stream()
                .filter(modConfig -> modConfig.getModId().equals(MODID))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        Method openConfig;
        try {
            openConfig = ConfigTracker.INSTANCE.getClass().getDeclaredMethod("openConfig", ModConfig.class, Path.class);
            openConfig.setAccessible(true);
            openConfig.invoke(ConfigTracker.INSTANCE, commonConfig, FMLPaths.CONFIGDIR.get());
            openConfig.setAccessible(false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }

        DRItems.register(modEventBus);
        DRBlocks.register(modEventBus);
        DRSounds.register(modEventBus);
        DREffects.register(modEventBus);
        DREntitiyTypes.register(modEventBus);
        DRConfiguredFeatures.register(modEventBus);
        DRPlacedFeatures.register(modEventBus);
        DRDwarfs.register(modEventBus);
        DRStructures.register(modEventBus);

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
            EntityRenderers.register(DREntitiyTypes.HEL_WALKER.get(), HelWalkerRenderer::new);
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
