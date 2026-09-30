package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.client.render.StuckSpearRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DRForgeEventBusClientEvents {
    /** Vanilla-style renderers; GeckoLib ones draw their spears themselves (they never fire this event). */
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        StuckSpearRenderer.render(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(),
                event.getPackedLight(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            StuckSpearRenderer.tickParticles();
        }
    }
}
