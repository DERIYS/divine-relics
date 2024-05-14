package com.deriys.tutorialmod.entities.client.model;


import com.deriys.tutorialmod.entities.ThrownDraupnirSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;


public class ThrownDraupnirSpearModel extends EntityModel<ThrownDraupnirSpear> {
	private final ModelPart spear;

	public ThrownDraupnirSpearModel(ModelPart root) {
		this.spear = root.getChild("spear");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition spear = partdefinition.addOrReplaceChild("spear", CubeListBuilder.create().texOffs(0, 0).addBox(3.9F, -9.657F, 0.1236F, 0.2071F, 0.5F, 0.25F, new CubeDeformation(0.0F))
				.texOffs(12, 12).addBox(3.3161F, -11.0F, 0.1875F, 1.375F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.3161F, -10.5F, 0.1875F, 1.375F, 0.25F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 10).addBox(3.5661F, -10.25F, 0.1875F, 0.875F, 1.75F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.3965F, -8.7153F, 0.1875F, 0.5F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.5215F, -8.7778F, 0.1875F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.5215F, -8.5903F, 0.1875F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.6465F, -8.5278F, 0.1875F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.1106F, -8.7153F, 0.1875F, 0.5F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.4231F, -8.7778F, 0.1875F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.4231F, -8.5903F, 0.1875F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.2356F, -8.5278F, 0.1875F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(8, 13).addBox(3.32F, -11.5F, 0.1875F, 1.3672F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(10, 13).addBox(3.3278F, -12.0F, 0.1875F, 1.3516F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 13).addBox(3.3356F, -12.5F, 0.1875F, 1.3359F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(4, 14).addBox(3.3395F, -13.0F, 0.1875F, 1.3281F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(6, 14).addBox(3.3434F, -13.5F, 0.1875F, 1.3203F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 10).addBox(3.3512F, -14.0F, 0.1875F, 1.3047F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 11).addBox(3.3551F, -14.5F, 0.1875F, 1.2969F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 14).addBox(3.3668F, -15.0F, 0.1875F, 1.2734F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 12).addBox(3.3707F, -15.5F, 0.1875F, 1.2656F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 13).addBox(3.3786F, -16.0F, 0.1875F, 1.25F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 14).addBox(3.3864F, -16.5F, 0.1875F, 1.2344F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(4, 15).addBox(3.3981F, -17.0F, 0.1875F, 1.2109F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(10, 15).addBox(3.4137F, -17.5F, 0.1875F, 1.1797F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 15).addBox(3.4332F, -18.0F, 0.1875F, 1.1406F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.9411F, -19.125F, 0.1875F, 0.125F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.5661F, -18.1875F, 0.1875F, 0.875F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.6286F, -18.375F, 0.1875F, 0.75F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.6911F, -18.5625F, 0.1875F, 0.625F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.7536F, -18.75F, 0.1875F, 0.5F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.8786F, -18.9375F, 0.1875F, 0.25F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(6, 12).addBox(3.3786F, -1.425F, 0.25F, 1.25F, 2.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(4, 9).addBox(4.0036F, -1.425F, -0.375F, 0.0F, 2.25F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(8, 9).addBox(4.0036F, -2.1625F, -0.375F, 0.0F, 1.125F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.3786F, -2.1625F, 0.25F, 1.25F, 1.125F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(10, 9).addBox(4.0036F, -6.25F, -0.375F, 0.0F, 1.0F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(8, 14).addBox(3.3786F, -6.25F, 0.25F, 1.25F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.9F, -9.0F, 0.0F, 0.2071F, 13.0F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.7536F, -9.0F, 0.1464F, 0.5F, 13.0F, 0.2071F, new CubeDeformation(0.0F)), PartPose.offset(-4.0036F, 12.25F, -0.25F));

		PartDefinition longhilt_r1 = spear.addOrReplaceChild("longhilt_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, -20.75F, -0.1036F, 0.5F, 13.0F, 0.2071F, new CubeDeformation(0.0F))
				.texOffs(2, 0).addBox(-0.1036F, -20.75F, -0.25F, 0.2071F, 13.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0036F, 11.75F, 0.25F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hilt_detail_r1 = spear.addOrReplaceChild("hilt_detail_r1", CubeListBuilder.create().texOffs(10, 10).addBox(0.0F, -0.625F, -0.625F, 0.0F, 1.0F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(10, 14).addBox(-0.625F, -0.625F, 0.0F, 1.25F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(8, 10).addBox(0.0F, 3.4625F, -0.625F, 0.0F, 1.125F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(2, 0).addBox(-0.625F, 3.4625F, 0.0F, 1.25F, 1.125F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(6, 9).addBox(0.0F, 4.2F, -0.625F, 0.0F, 2.25F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(4, 12).addBox(-0.625F, 4.2F, 0.0F, 1.25F, 2.25F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0036F, -5.625F, 0.25F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r1 = spear.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.0625F, -0.5703F, -0.0625F, 0.125F, 1.3281F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2261F, -18.6762F, 0.25F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r2 = spear.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, -0.7578F, -0.0625F, 0.125F, 1.3281F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8825F, -18.4312F, 0.25F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r3 = spear.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0336F, 0.1586F, -0.0625F, 0.0875F, 0.0875F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0921F, -19.4248F, 0.25F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r4 = spear.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.0313F, -0.0625F, -0.0625F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3504F, -8.8733F, 0.25F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r5 = spear.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(0.125F, 0.0625F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1213F, -8.4749F, 0.25F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r6 = spear.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.125F, -0.0625F, 0.4375F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.478F, -8.8112F, 0.25F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r7 = spear.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-0.0625F, 0.0F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3896F, -8.8478F, 0.25F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r8 = spear.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-0.125F, -0.25F, -0.0625F, 0.25F, 0.75F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4799F, -10.0669F, 0.25F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r9 = spear.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, 0.0625F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8859F, -8.4749F, 0.25F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r10 = spear.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(0.0625F, 0.125F, -0.0625F, 0.4375F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5291F, -8.8112F, 0.25F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r11 = spear.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-0.0937F, -0.0625F, -0.0625F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6567F, -8.8733F, 0.25F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r12 = spear.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-0.0625F, 0.0F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6175F, -8.8478F, 0.25F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r13 = spear.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-0.125F, -0.25F, -0.0625F, 0.25F, 0.75F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5272F, -10.0669F, 0.25F, 0.0F, 0.0F, -0.3927F));

		PartDefinition bladestart_r1 = spear.addOrReplaceChild("bladestart_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1036F, 0.5625F, 0.0313F, 0.2071F, 0.1875F, 0.0625F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0036F, -10.3141F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bladestart_r2 = spear.addOrReplaceChild("bladestart_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1036F, 0.5625F, -0.0938F, 0.2071F, 0.1875F, 0.0625F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0036F, -10.3141F, 0.4972F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bladestart_r3 = spear.addOrReplaceChild("bladestart_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1036F, 0.25F, -0.0938F, 0.2071F, 0.5F, 0.1875F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0036F, -9.657F, 0.1264F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bladestart_r4 = spear.addOrReplaceChild("bladestart_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1036F, 0.25F, -0.0938F, 0.2071F, 0.5F, 0.1875F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0036F, -9.657F, 0.3736F, -0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		spear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(ThrownDraupnirSpear thrownDraupnirSpear, float v, float v1, float v2, float v3, float v4) {

	}
}