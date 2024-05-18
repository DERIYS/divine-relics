package com.deriys.tutorialmod.entities.client.render;

import com.deriys.tutorialmod.entities.ThrownDraupnirSpear;
import com.deriys.tutorialmod.entities.client.ModModelLayers;
import com.deriys.tutorialmod.entities.client.model.ThrownDraupnirSpearModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownDraupnirSpearRenderer extends EntityRenderer<ThrownDraupnirSpear> {
    public static final ResourceLocation SPEAR_LOCATION = new ResourceLocation("tutorialmod:textures/entity/draupnir_spear.png");
    private final ThrownDraupnirSpearModel model;

    public ThrownDraupnirSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ThrownDraupnirSpearModel(context.bakeLayer(ModModelLayers.DRAUPNIR_SPEAR_LAYER));
    }

    public void render(ThrownDraupnirSpear entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) + 90.0F));
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect(bufferIn, this.model.renderType(this.getTextureLocation(entityIn)), false, entityIn.isFoil());
        this.model.renderToBuffer(matrixStackIn, $$6, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownDraupnirSpear thrownDraupnirSpear) {
        return SPEAR_LOCATION;
    }
}