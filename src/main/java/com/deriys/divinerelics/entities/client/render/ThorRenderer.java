package com.deriys.divinerelics.entities.client.render;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.client.model.ThorModel;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ThorRenderer extends GeoEntityRenderer<ThorEntity> {
    public ThorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ThorModel());
        this.shadowRadius = 1f;
    }

    @Override
    public ResourceLocation getTextureLocation(ThorEntity animatable) {
        return new ResourceLocation(DivineRelics.MODID, "textures/entity/thor.png");
    }

    @Override
    public RenderType getRenderType(ThorEntity animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource,
                                    @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {

        poseStack.scale(0.7f, 0.7f, 0.7f);

        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}
