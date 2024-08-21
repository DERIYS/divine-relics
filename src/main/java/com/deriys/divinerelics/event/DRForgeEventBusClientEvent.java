package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpear;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.init.DRItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DRForgeEventBusClientEvent {

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity entity = event.getEntity();
        if (entity.isAlive()) {
            entity.getCapability(StuckSpearsProvider.STUCK_SPEARS).ifPresent(cap -> {
                for (StuckSpear stuckSpear : cap.getSpears()) {
                    PoseStack matrixStack = event.getPoseStack();
                    MultiBufferSource buffer = event.getMultiBufferSource();
                    int packedLight = event.getPackedLight();
                    matrixStack.pushPose();

                    // Apply the entity's body rotation
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(-entity.yBodyRot));

                    float z = 0.5f;
                    ItemTransforms.TransformType transformType = ItemTransforms.TransformType.GROUND;
                    if (!stuckSpear.isFront) {
                        z = -0.5f;
                        transformType = ItemTransforms.TransformType.HEAD;
                    }
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(stuckSpear.rotation));
                    matrixStack.translate(stuckSpear.x, stuckSpear.y, stuckSpear.z + z);

                    Minecraft.getInstance().getItemRenderer().renderStatic(entity, DRItems.THROWN_DRAUPNIR_SPEAR.get().getDefaultInstance(), transformType, false, matrixStack, buffer, entity.level, packedLight, OverlayTexture.NO_OVERLAY, entity.getId());
                    matrixStack.popPose();
                }
            });
        }
    }
}
