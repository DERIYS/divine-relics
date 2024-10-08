package com.deriys.divinerelics.entities.client.render;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.client.model.HelWalkerModel;
import com.deriys.divinerelics.entities.entity.HelWalkerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class HelWalkerRenderer extends GeoEntityRenderer<HelWalkerEntity> {

    public HelWalkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HelWalkerModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(HelWalkerEntity animatable) {
        return new ResourceLocation(DivineRelics.MODID, "textures/entity/hel_walker.png");
    }

    @Override
    public RenderType getRenderType(HelWalkerEntity animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource,
                                    @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {

//        poseStack.scale(1.2f, 1.2f, 1.2f);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}
