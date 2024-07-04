package com.deriys.divinerelics.entities.client.model;


import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
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

		PartDefinition spear = partdefinition.addOrReplaceChild("spear", CubeListBuilder.create().texOffs(0, 0).addBox(-8.15F, -21.952F, 7.85F, 0.5F, 10.952F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(11, 6).addBox(-8.025F, -31.8875F, 8.0375F, 0.25F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 6).addBox(-8.15F, -31.7F, 8.0375F, 0.5F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(9, 7).addBox(-8.2125F, -31.5125F, 8.0375F, 0.625F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(9, 6).addBox(-8.275F, -31.325F, 8.0375F, 0.75F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(10, 3).addBox(-8.3375F, -31.1375F, 8.0375F, 0.875F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(13, 7).addBox(-7.9625F, -32.075F, 8.0375F, 0.125F, 0.1875F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(13, 0).addBox(-8.4703F, -30.95F, 8.0375F, 1.1406F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(7, 5).addBox(-8.4898F, -30.45F, 8.0375F, 1.1797F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 11).addBox(-8.5055F, -29.95F, 8.0375F, 1.2109F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 8).addBox(-8.5172F, -29.45F, 8.0375F, 1.2344F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 10).addBox(-8.525F, -28.95F, 8.0375F, 1.25F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(9, 4).addBox(-8.5328F, -28.45F, 8.0375F, 1.2656F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 7).addBox(-8.5367F, -27.95F, 8.0375F, 1.2734F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(10, 0).addBox(-8.5484F, -27.45F, 8.0375F, 1.2969F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(6, 6).addBox(-8.5523F, -26.95F, 8.0375F, 1.3047F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 9).addBox(-8.5602F, -26.45F, 8.0375F, 1.3203F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 6).addBox(-8.5641F, -25.95F, 8.0375F, 1.3281F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(10, 5).addBox(-8.568F, -25.45F, 8.0375F, 1.3359F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 0).addBox(-8.5758F, -24.95F, 8.0375F, 1.3516F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(11, 1).addBox(-8.5836F, -24.45F, 8.0375F, 1.3672F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 8).addBox(-7.668F, -21.4778F, 8.0375F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(7, 8).addBox(-7.4805F, -21.5403F, 8.0375F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 7).addBox(-7.4805F, -21.7278F, 8.0375F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(3, 12).addBox(-7.793F, -21.6653F, 8.0375F, 0.5F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(15, 8).addBox(-8.257F, -21.4778F, 8.0375F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(8, 8).addBox(-8.382F, -21.5403F, 8.0375F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(8, 9).addBox(-8.382F, -21.7278F, 8.0375F, 0.0625F, 0.0625F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(14, 6).addBox(-8.507F, -21.6653F, 8.0375F, 0.5F, 0.125F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(6, 1).addBox(-8.3375F, -23.2F, 8.0375F, 0.875F, 1.75F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(12, 4).addBox(-8.5875F, -23.45F, 8.0375F, 1.375F, 0.25F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(6, 0).addBox(-8.5875F, -23.95F, 8.0375F, 1.375F, 0.5F, 0.125F, new CubeDeformation(0.0F))
				.texOffs(15, 5).addBox(-8.0036F, -22.607F, 7.9736F, 0.2071F, 0.5F, 0.25F, new CubeDeformation(0.0F))
				.texOffs(3, 0).addBox(-7.9F, -18.9125F, 7.475F, 0.0F, 0.75F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(6, 4).addBox(-8.525F, -18.9125F, 8.1F, 1.25F, 0.75F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(11, 2).addBox(-8.525F, -13.4125F, 8.1F, 1.25F, 0.75F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(3, 3).addBox(-7.9F, -13.4125F, 7.475F, 0.0F, 0.75F, 1.25F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 25.2F, -8.0F));

		PartDefinition hilt_detail_r1 = spear.addOrReplaceChild("hilt_detail_r1", CubeListBuilder.create().texOffs(6, 7).addBox(-0.625F, -0.375F, 0.0F, 1.25F, 0.75F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(3, 1).addBox(0.0F, -5.875F, -0.625F, 0.0F, 0.75F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(6, 3).addBox(-0.625F, -5.875F, 0.0F, 1.25F, 0.75F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(3, 2).addBox(0.0F, -0.375F, -0.625F, 0.0F, 0.75F, 1.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9F, -13.0375F, 8.1F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r1 = spear.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 1).addBox(-0.0625F, -0.5703F, -0.0625F, 0.125F, 1.3281F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6774F, -31.6262F, 8.1F, 0.0F, 0.0F, -0.3927F));

		PartDefinition bladestart_r1 = spear.addOrReplaceChild("bladestart_r1", CubeListBuilder.create().texOffs(6, 9).addBox(-0.1036F, 0.25F, -0.0938F, 0.2071F, 0.5F, 0.1875F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9F, -22.607F, 8.2236F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bladestart_r2 = spear.addOrReplaceChild("bladestart_r2", CubeListBuilder.create().texOffs(11, 7).addBox(-0.1036F, 0.25F, -0.0938F, 0.2071F, 0.5F, 0.1875F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9F, -22.607F, 7.9764F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bladestart_r3 = spear.addOrReplaceChild("bladestart_r3", CubeListBuilder.create().texOffs(14, 2).addBox(-0.1036F, 0.5625F, -0.0938F, 0.2071F, 0.1875F, 0.0625F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9F, -23.2641F, 8.3472F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bladestart_r4 = spear.addOrReplaceChild("bladestart_r4", CubeListBuilder.create().texOffs(9, 9).addBox(-0.1036F, 0.5625F, 0.0313F, 0.2071F, 0.1875F, 0.0625F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9F, -23.2641F, 7.85F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r2 = spear.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 5).addBox(-0.125F, -0.25F, -0.0625F, 0.25F, 0.75F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3763F, -23.0169F, 8.1F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r3 = spear.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 7).addBox(-0.0625F, 0.0F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.286F, -21.7978F, 8.1F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r4 = spear.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(11, 9).addBox(0.0625F, 0.125F, -0.0625F, 0.4375F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3744F, -21.7612F, 8.1F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r5 = spear.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(14, 8).addBox(-0.25F, 0.0625F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0177F, -21.4249F, 8.1F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r6 = spear.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(13, 8).addBox(-0.0938F, -0.0625F, -0.0625F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.2468F, -21.8233F, 8.1F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r7 = spear.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(13, 5).addBox(-0.125F, -0.25F, -0.0625F, 0.25F, 0.75F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4237F, -23.0169F, 8.1F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r8 = spear.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(10, 9).addBox(-0.0625F, 0.0F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.514F, -21.7978F, 8.1F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r9 = spear.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(9, 8).addBox(-0.5F, 0.125F, -0.0625F, 0.4375F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4256F, -21.7612F, 8.1F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r10 = spear.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(15, 7).addBox(0.125F, 0.0625F, -0.0625F, 0.125F, 0.25F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.7823F, -21.4249F, 8.1F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r11 = spear.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(11, 8).addBox(-0.0313F, -0.0625F, -0.0625F, 0.125F, 0.125F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5532F, -21.8233F, 8.1F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r12 = spear.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(9, 1).addBox(-0.25F, -0.7578F, -0.0625F, 0.125F, 1.3281F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0211F, -31.3812F, 8.1F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r13 = spear.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(15, 2).addBox(0.0336F, 0.1586F, -0.0625F, 0.0875F, 0.0875F, 0.125F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8114F, -32.3748F, 8.1F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		spear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(ThrownDraupnirSpear thrownDraupnirSpear, float v, float v1, float v2, float v3, float v4) {

	}
}