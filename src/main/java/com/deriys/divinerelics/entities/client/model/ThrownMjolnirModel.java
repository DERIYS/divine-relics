package com.deriys.divinerelics.entities.client.model;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.deriys.divinerelics.entities.ThrownMjolnir;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ThrownMjolnirModel extends EntityModel<ThrownMjolnir> {
	private final ModelPart body;

	public ThrownMjolnirModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(35, 44).addBox(-7.25F, -13.0F, -2.252F, 14.5F, 8.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(35, 35).addBox(-7.25F, -13.0F, 2.252F, 14.5F, 8.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(35, 17).addBox(-7.25F, -13.0F, 2.251F, 14.5F, 8.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(35, 26).addBox(-7.25F, -13.0F, -2.251F, 14.5F, 8.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(33, 61).addBox(-7.75F, -7.75F, -2.3125F, 15.5F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(33, 53).addBox(-7.75F, -7.75F, 2.3125F, 15.5F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(10, 17).addBox(-2.0F, -3.159F, -0.7215F, 4.0F, 0.0F, 1.4453F, new CubeDeformation(0.0F))
				.texOffs(22, 20).addBox(-0.75F, -13.0F, -2.25F, 1.5F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(22, 20).addBox(-0.75F, -5.0F, -2.25F, 1.5F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(14, 37).addBox(-7.25F, -9.75F, -2.25F, 2.0F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(1, 53).addBox(-8.125F, -10.1875F, -2.25F, 0.875F, 5.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(1, 53).addBox(7.25F, -10.1875F, -2.25F, 0.875F, 5.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(18, 48).addBox(3.75F, -6.25F, -2.25F, 1.0F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(18, 48).addBox(-4.75F, -6.25F, -2.25F, 1.0F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(14, 37).addBox(5.25F, -9.75F, -2.25F, 2.0F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(6, 31).addBox(-0.4142F, -5.0F, -1.0F, 0.8284F, 12.0F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(0, 31).addBox(-0.4142F, -5.0F, 0.875F, 0.8284F, 12.0F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 17).addBox(0.875F, -5.0F, -0.4142F, 0.125F, 12.0F, 0.8284F, new CubeDeformation(0.0F))
				.texOffs(9, 17).addBox(-1.0F, -5.0F, -0.4142F, 0.125F, 12.0F, 0.8284F, new CubeDeformation(0.0F))
				.texOffs(12, 33).addBox(-0.466F, 7.0F, -1.125F, 0.932F, 1.0F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 31).addBox(-0.466F, 7.0F, 1.0F, 0.932F, 1.0F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 25).addBox(1.0F, 7.0F, -0.466F, 0.125F, 1.0F, 0.932F, new CubeDeformation(0.0F))
				.texOffs(12, 36).addBox(-1.125F, 7.0F, -0.466F, 0.125F, 1.0F, 0.932F, new CubeDeformation(0.0F))
				.texOffs(24, 17).addBox(-1.0625F, 8.001F, -1.0625F, 2.125F, 0.0F, 2.125F, new CubeDeformation(0.0F))
				.texOffs(52, 13).addBox(-1.875F, 7.0F, 0.0F, 3.75F, 3.5F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(52, 7).addBox(0.0F, 7.0F, -1.875F, 0.0F, 2.0F, 3.75F, new CubeDeformation(0.0F))
				.texOffs(18, 48).addBox(3.75F, -6.25F, -2.25F, 1.0F, 0.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(23, 18).addBox(-1.125F, -0.25F, -0.466F, 0.125F, 1.0F, 0.932F, new CubeDeformation(0.0F))
				.texOffs(0, 44).addBox(1.0F, -0.25F, -0.466F, 0.125F, 1.0F, 0.932F, new CubeDeformation(0.0F))
				.texOffs(12, 20).mirror().addBox(-0.466F, -0.25F, 1.0F, 0.932F, 1.0F, 0.125F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(3, 45).addBox(-0.466F, -0.25F, -1.125F, 0.932F, 1.0F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(6, 17).addBox(-1.0F, -12.25F, -0.4142F, 0.125F, 12.0F, 0.8284F, new CubeDeformation(0.0F))
				.texOffs(0, 17).addBox(0.875F, -12.25F, -0.4142F, 0.125F, 12.0F, 0.8284F, new CubeDeformation(0.0F))
				.texOffs(9, 31).addBox(-0.4142F, -12.25F, 0.875F, 0.8284F, 12.0F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 31).addBox(-0.4142F, -12.25F, -1.0F, 0.8284F, 12.0F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.25F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 31).addBox(-1.75F, -0.125F, -2.25F, 3.375F, 0.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.319F, -5.5352F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 31).addBox(-1.625F, -0.125F, -2.25F, 3.375F, 0.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3189F, -5.5352F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(11, 19).addBox(-1.75F, -0.125F, -2.25F, 2.875F, 0.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2585F, -5.4835F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(11, 19).addBox(-1.125F, -0.125F, -2.25F, 2.875F, 0.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2585F, -5.4835F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(2, 44).addBox(1.6017F, -0.017F, -2.25F, 0.25F, 3.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5473F, -11.6786F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(12, 25).addBox(-1.625F, -0.875F, -2.25F, 2.75F, 0.25F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(42, 0).mirror().addBox(-4.375F, -1.875F, -1.75F, 7.25F, 1.0F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.1334F, -9.3258F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r8 = body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(42, 5).addBox(-2.875F, -1.875F, -1.75F, 7.25F, 1.0F, 3.5F, new CubeDeformation(0.0F))
				.texOffs(12, 25).addBox(-1.125F, -0.875F, -2.25F, 2.75F, 0.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1334F, -9.3258F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r9 = body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(2, 44).addBox(-1.8517F, -0.017F, -2.25F, 0.25F, 3.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5473F, -11.6786F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r10 = body.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(42, 9).addBox(0.0F, -1.125F, -2.25F, 0.0F, 2.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4296F, -4.196F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r11 = body.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(42, 6).addBox(0.0F, -1.125F, -2.25F, 0.0F, 2.25F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4296F, -4.196F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r12 = body.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(25, 32).addBox(10.0F, -0.5F, 0.0F, 4.0F, 2.25F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -4.3971F, 1.9592F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r13 = body.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(25, 37).addBox(4.25F, -0.5F, 0.0F, 4.0F, 2.25F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.25F, -4.3964F, -1.9589F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(ThrownMjolnir thrownMjolnir, float v, float v1, float v2, float v3, float v4) {

	}
}