package com.deriys.divinerelics;

import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.effects.DREffects;
import com.deriys.divinerelics.entities.DREntitiyTypes;
import com.deriys.divinerelics.entities.client.render.ThrownDraupnirSpearRenderer;
import com.deriys.divinerelics.items.ModItems;
import com.deriys.divinerelics.sound.DRSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DivineRelics.MODID)
public class DivineRelics
{
    public static final String MODID = "divinerelics";

    public DivineRelics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        ModItems.register(modEventBus);
        DRSounds.register(modEventBus);
        DREffects.register(modEventBus);
        DREntitiyTypes.register(modEventBus);

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
            EntityRenderers.register(DREntitiyTypes.THROWN_DRAUPNIR_SPEAR.get(), ThrownDraupnirSpearRenderer::new);
        }
    }
}