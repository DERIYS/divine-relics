package com.deriys.divinerelics.entities.client.render;

import com.deriys.divinerelics.entities.client.DRModelLayers;
import com.deriys.divinerelics.entities.client.model.ThrownLeviathanModel;
import com.deriys.divinerelics.entities.entity.ThrownLeviathanAxe;
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
public class ThrownLeviathanRenderer extends EntityRenderer<ThrownLeviathanAxe> {
    public static final ResourceLocation LEVIATHAN_LOCATION = new ResourceLocation("divinerelics:textures/entity/thrown_leviathan.png");
    private final ThrownLeviathanModel model;

    public ThrownLeviathanRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ThrownLeviathanModel(context.bakeLayer(DRModelLayers.LEVIATHAN_AXE_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownLeviathanAxe thrownLeviathanAxe) {
        return LEVIATHAN_LOCATION;
    }

    public void render(ThrownLeviathanAxe entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        float rotationAngle = entityIn.tickCount + partialTicks;
        if (!entityIn.isOnGround()) {
            float rotationFactor = (entityIn.dealtDamage) ? 30.0F: -75.0F;
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle * rotationFactor));
        } else if (entityIn.isReturning()) {
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(rotationAngle  * 60.0F));
        } else {
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) + 90.0F));
        }
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect(bufferIn, this.model.renderType(this.getTextureLocation(entityIn)), false, entityIn.isFoil());
        matrixStackIn.scale(0.95f, 0.95f,0.95f);
        this.model.renderToBuffer(matrixStackIn, $$6, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}