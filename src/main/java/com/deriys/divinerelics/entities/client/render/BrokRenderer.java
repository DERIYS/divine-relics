package com.deriys.divinerelics.entities.client.render;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.client.model.BrokModel;
import com.deriys.divinerelics.entities.client.model.DraugrModel;
import com.deriys.divinerelics.entities.entity.BrokEntity;
import com.deriys.divinerelics.entities.entity.DraugrEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class BrokRenderer extends GeoEntityRenderer<BrokEntity> {

    public BrokRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BrokModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BrokEntity animatable) {
        return new ResourceLocation(DivineRelics.MODID, "textures/entity/brok.png");
    }

    @Override
    public RenderType getRenderType(BrokEntity animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource,
                                    @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {

        poseStack.scale(0.9f, 0.9f, 0.9f);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}