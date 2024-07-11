package com.deriys.divinerelics.entities.client.model;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.deriys.divinerelics.entities.entity.ThrownLeviathanAxe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ThrownLeviathanModel extends EntityModel<ThrownLeviathanAxe> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart axe;

	public ThrownLeviathanModel(ModelPart root) {
		this.axe = root.getChild("axe");
	}

	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition axe = partdefinition.addOrReplaceChild("axe", CubeListBuilder.create().texOffs(43, 20).addBox(-14.375F, -18.375F, 7.748F, 10.5F, 8.0F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(43, 30).addBox(-14.375F, -18.375F, 8.252F, 10.5F, 8.0F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(20, 18).addBox(-8.4375F, -17.625F, 7.374F, 2.25F, 1.625F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(21, 27).addBox(-8.4375F, -17.625F, 8.626F, 2.25F, 1.625F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(6, 10).addBox(-6.1865F, -17.625F, 7.375F, 0.0F, 1.625F, 1.25F, new CubeDeformation(0.0F))
//		.texOffs(6, 12).addBox(-8.4385F, -17.625F, 7.375F, 0.0F, 1.625F, 1.25F, new CubeDeformation(0.0F))
//		.texOffs(43, 10).addBox(-14.375F, -18.375F, 7.749F, 10.5F, 8.0F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(43, 0).addBox(-14.375F, -18.375F, 8.251F, 10.5F, 8.0F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 14).addBox(-8.4375F, -17.625F, 7.375F, 2.25F, 1.625F, 1.25F, new CubeDeformation(0.0F))
//		.texOffs(19, 0).addBox(-11.875F, -17.625F, 7.75F, 3.5F, 0.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(11, 26).addBox(-6.25F, -17.625F, 7.75F, 1.25F, 0.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(22, 24).addBox(-6.2543F, -16.3855F, 7.75F, 0.375F, 0.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(20, 24).addBox(-5.3803F, -15.5012F, 7.75F, 0.25F, 0.5625F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(8, 15).addBox(-5.1303F, -15.1887F, 7.75F, 0.5F, 0.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(0, 2).addBox(-4.2106F, -17.8055F, 7.75F, 0.25F, 1.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(9, 6).addBox(-14.3166F, -17.0333F, 7.75F, 0.25F, 2.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(10, 0).addBox(-9.0F, -16.25F, 7.75F, 0.625F, 0.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(8, 0).addBox(-11.4382F, -13.8384F, 7.75F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(0, 19).addBox(-9.5625F, 7.75F, 8.0F, 2.875F, 3.5F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(28, 0).addBox(-8.3125F, -18.25F, 8.0F, 2.0F, 0.625F, 0.0F, new CubeDeformation(0.0F))
//		.texOffs(4, -1).addBox(-7.3125F, -18.25F, 7.4375F, 0.0F, 0.625F, 1.125F, new CubeDeformation(0.0F))
//		.texOffs(27, 18).addBox(-8.3125F, -1.5F, 7.5F, 1.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(7, 18).addBox(-8.25F, -1.0F, 7.5F, 1.25F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(13, 21).addBox(-8.1875F, 0.0F, 7.5F, 1.375F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(13, 18).addBox(-8.0625F, 1.0F, 7.5F, 1.375F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(12, 27).addBox(-8.0F, 2.0F, 7.5F, 1.375F, 3.25F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(26, 26).addBox(-8.0625F, 5.25F, 7.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(26, 28).addBox(-8.1875F, 5.75F, 7.5F, 1.625F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(26, 30).addBox(-8.3125F, 6.25F, 7.5F, 1.75F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 5).addBox(-8.4375F, 6.75F, 7.5F, 1.75F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 9).addBox(-8.5625F, 7.25F, 7.5F, 1.875F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(18, 30).addBox(-6.9375F, 7.75F, 7.5F, 0.25F, 0.625F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(8, 4).addBox(-7.1875F, 7.75F, 7.5F, 0.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(6, 1).addBox(-7.4375F, 7.75F, 7.5F, 0.25F, 0.375F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(8, 2).addBox(-7.6875F, 7.75F, 7.5F, 0.25F, 0.25F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(3, 1).addBox(-7.9375F, 7.75F, 7.5F, 0.25F, 0.125F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(19, 20).addBox(-8.375F, -2.0F, 7.5F, 1.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(27, 20).addBox(-8.5F, -3.0F, 7.5F, 1.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(27, 22).addBox(-8.4375F, -2.5F, 7.5F, 1.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(7, 21).addBox(-8.5625F, -9.0F, 7.5F, 1.375F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(27, 24).addBox(-8.5625F, -9.5F, 7.5F, 1.5F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(18, 22).addBox(-8.4375F, -10.5F, 7.5F, 1.625F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(14, 24).addBox(-8.5625F, -10.0F, 7.5F, 1.625F, 0.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 11).addBox(-8.4375F, -11.5F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(5, 25).addBox(-8.3125F, -12.5F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 23).addBox(-8.3125F, -13.5F, 7.5F, 1.875F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(5, 28).addBox(-8.3125F, -16.0F, 7.5F, 2.0F, 2.5F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(10, 24).addBox(-7.8125F, -18.25F, 7.6875F, 1.0F, 0.625F, 0.625F, new CubeDeformation(0.0F))
//		.texOffs(0, 26).addBox(-8.5625F, -8.0F, 7.5F, 1.25F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.1F));
//
//		PartDefinition cube_r1 = axe.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-0.9375F, 0.0F, -0.5F, 1.875F, 0.25F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6007F, 7.8778F, 8.0F, 0.0F, 0.0F, 0.3927F));
//
//		PartDefinition cube_r2 = axe.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(7, 16).addBox(-0.125F, -0.125F, -0.25F, 0.5F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8116F, -16.2222F, 8.0F, 0.0F, 0.0F, 0.3927F));
//
//		PartDefinition cube_r3 = axe.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.125F, -0.125F, -0.25F, 0.25F, 0.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.485F, -16.0308F, 8.0F, 0.0F, 0.0F, -0.3927F));
//
//		PartDefinition cube_r4 = axe.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(2, 3).addBox(-0.625F, -0.125F, -0.25F, 1.125F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4704F, -17.7297F, 8.0F, 0.0F, 0.0F, -0.3927F));
//
//		PartDefinition cube_r5 = axe.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(7, 8).addBox(0.125F, 0.125F, -0.25F, 0.25F, 1.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2592F, -16.8145F, 8.0F, 0.0F, 0.0F, 0.3927F));
//
//		PartDefinition cube_r6 = axe.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(13, 0).addBox(-1.75F, -0.125F, -0.25F, 2.125F, 0.25F, 0.5F, new CubeDeformation(0.0F))
//		.texOffs(9, 10).addBox(-1.75F, 0.125F, -0.25F, 0.25F, 1.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.1736F, -17.634F, 8.0F, 0.0F, 0.0F, 0.3927F));
//
//		PartDefinition cube_r7 = axe.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(25, 19).addBox(-0.125F, -1.125F, -0.25F, 0.25F, 3.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.7706F, -13.7918F, 8.0F, 0.0F, 0.0F, -0.3927F));
//
//		PartDefinition cube_r8 = axe.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(6, 3).addBox(-0.125F, -1.125F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.9977F, -10.6117F, 8.0F, 0.0F, 0.0F, -0.7854F));
//
//		PartDefinition cube_r9 = axe.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(24, 24).addBox(-0.125F, -1.625F, -0.25F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5619F, -10.5568F, 8.0F, 0.0F, 0.0F, 0.3927F));
//
//		PartDefinition cube_r10 = axe.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(22, 25).addBox(-0.125F, -0.625F, -0.25F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0835F, -12.3088F, 8.0F, 0.0F, 0.0F, -0.3927F));
//
//		PartDefinition cube_r11 = axe.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(7, 6).addBox(-0.125F, -0.5F, -0.25F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.1314F, -14.2525F, 8.0F, 0.0F, 0.0F, 0.3927F));
//
//		PartDefinition cube_r12 = axe.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(9, 12).addBox(-0.125F, -2.0F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.3207F, -14.3204F, 8.0F, 0.0F, 0.0F, 0.7854F));
//
//		PartDefinition cube_r13 = axe.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(2, 4).addBox(0.5F, -0.125F, -0.25F, 1.125F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.409F, -15.5161F, 8.0F, 0.0F, 0.0F, -0.3927F));
//
//		return LayerDefinition.create(meshdefinition, 64, 64);
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition axe = partdefinition.addOrReplaceChild("axe", CubeListBuilder.create().texOffs(34, 24).addBox(-16.625F, -18.625F, 7.748F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(34, 36).addBox(-16.625F, -18.625F, 8.252F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(13, 4).addBox(-7.25F, -19.25F, 7.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(16, 23).addBox(-8.25F, -19.25F, 8.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(34, 12).addBox(-16.625F, -18.625F, 7.749F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(34, 0).addBox(-16.625F, -18.625F, 8.251F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(21, 0).addBox(-6.1875F, -16.25F, 7.75F, 0.7383F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(11, 32).addBox(-4.5659F, -15.3826F, 7.75F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(15, 33).addBox(-1.9606F, -17.8055F, 7.75F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(-6.25F, -17.625F, 7.75F, 3.5F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(8, 17).addBox(-12.875F, -17.625F, 7.75F, 4.5625F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(7, 0).addBox(-16.3231F, -18.5987F, 7.75F, 1.1484F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(18, 0).addBox(-16.9203F, -18.3307F, 7.75F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 1).addBox(-15.8213F, -17.5963F, 7.75F, 0.25F, 0.625F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(7, 30).addBox(-16.6132F, -15.8094F, 7.75F, 0.25F, 1.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(18, 3).addBox(-11.9001F, -13.6471F, 7.75F, 0.25F, 3.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 3).addBox(-9.0F, -16.25F, 7.75F, 0.625F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(6, 2).addBox(-8.375F, -17.625F, 7.375F, 2.25F, 1.625F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(12, 25).addBox(-7.75F, -18.25F, 7.5F, 1.0F, 0.75F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-10.625F, 6.5F, 7.749F, 3.5F, 6.5F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 17).addBox(-10.625F, 6.5F, 8.251F, 3.5F, 6.5F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(12, 30).addBox(-8.625F, 6.5F, 7.75F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(16, 0).addBox(-7.375F, 6.5F, 7.75F, 0.25F, 3.5F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(20, 7).addBox(-9.2817F, 9.236F, 7.75F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(11, 1).addBox(-10.2983F, 12.7145F, 7.75F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(1, 5).addBox(-8.25F, -16.0F, 7.5F, 1.875F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 24).addBox(-8.25F, -13.0F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-8.375F, -12.0F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(21, 14).addBox(-8.5F, -11.0F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 15).addBox(-8.625F, -10.0F, 7.5F, 1.75F, 0.5F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 6).addBox(-8.75F, -9.0F, 7.5F, 1.625F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 13).addBox(-8.625F, -9.5F, 7.5F, 1.625F, 0.5F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 9).addBox(-8.875F, -7.0F, 7.5F, 1.625F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 8).addBox(-8.75F, -1.0F, 7.5F, 1.625F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 19).addBox(-8.625F, 1.0F, 7.5F, 1.625F, 2.75F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 19).addBox(-8.625F, 3.75F, 7.625F, 1.625F, 2.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, 15.0F, -8.0F));

		PartDefinition cube_r1 = axe.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 1).addBox(-0.125F, -0.125F, -0.25F, 0.5F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5969F, 12.7055F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r2 = axe.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 2).addBox(1.0F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.52F, 13.2317F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r3 = axe.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 27).addBox(-0.125F, -0.125F, -0.25F, 1.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1069F, 12.405F, 8.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r4 = axe.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(21, 1).addBox(-0.125F, -0.375F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.2907F, 11.5346F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r5 = axe.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(20, 3).addBox(-0.125F, -1.75F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1205F, 12.7658F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r6 = axe.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(22, 3).addBox(-0.125F, -1.75F, -0.25F, 0.25F, 1.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9102F, 11.5689F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r7 = axe.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(2, 0).addBox(-0.125F, 0.25F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4342F, 8.5909F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r8 = axe.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 4).addBox(0.375F, -0.125F, -0.25F, 0.375F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.8418F, 8.3321F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r9 = axe.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 30).addBox(0.375F, -0.125F, -0.25F, 0.375F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2289F, 8.3373F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r10 = axe.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(3, 3).addBox(0.375F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.3246F, 8.6243F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r11 = axe.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(2, 2).addBox(-0.125F, -0.875F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.591F, 8.4861F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r12 = axe.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(9, 30).addBox(-0.125F, -1.75F, -0.25F, 0.25F, 1.375F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1429F, 8.2544F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r13 = axe.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(9, 27).addBox(0.0F, -0.125F, -0.25F, 1.625F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.409F, -15.5161F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r14 = axe.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(9, 32).addBox(-0.125F, -2.0F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.7826F, -14.129F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r15 = axe.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(7, 33).addBox(-0.125F, -0.5F, -0.25F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5933F, -14.0612F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r16 = axe.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(5, 30).addBox(-0.125F, -0.625F, -0.25F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5455F, -9.8675F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r17 = axe.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(3, 30).addBox(-0.125F, -1.625F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.6411F, -7.1915F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r18 = axe.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(2, 4).addBox(-0.375F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0074F, -7.6138F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r19 = axe.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(14, 12).addBox(-2.875F, -0.125F, -0.25F, 3.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.5785F, -7.9954F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r20 = axe.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(14, 0).addBox(-0.125F, -2.375F, -0.25F, 0.25F, 5.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5889F, -12.413F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r21 = axe.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(3, 33).addBox(-1.75F, 0.625F, -0.25F, 0.25F, 0.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.4035F, -15.5442F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r22 = axe.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(1, 34).addBox(-0.125F, -0.5F, -0.25F, 0.25F, 0.875F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.8781F, -16.5572F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r23 = axe.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(15, 31).addBox(-0.125F, -0.125F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.8302F, -17.8949F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r24 = axe.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(11, 31).addBox(-0.375F, -0.125F, -0.25F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7626F, -17.8815F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r25 = axe.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(13, 33).addBox(-0.375F, -0.125F, -0.25F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.8717F, -18.0717F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r26 = axe.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(18, 1).addBox(-0.875F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.1598F, -18.531F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r27 = axe.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 28).addBox(-2.2227F, -0.125F, -0.25F, 2.5977F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.1736F, -17.634F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r28 = axe.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(7, 1).addBox(-0.625F, -0.125F, -0.25F, 1.125F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2204F, -17.7297F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r29 = axe.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(5, 33).addBox(0.125F, 0.125F, -0.25F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0093F, -17.8145F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r30 = axe.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 30).addBox(0.125F, 0.125F, -0.25F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2701F, -16.9852F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r31 = axe.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(11, 0).addBox(-1.0F, 0.125F, -0.25F, 0.875F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5356F, -15.6117F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r32 = axe.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(14, 32).addBox(-0.375F, -0.375F, -0.25F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1526F, -15.3149F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r33 = axe.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(13, 31).addBox(-0.375F, -0.875F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.058F, -15.4368F, 8.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r34 = axe.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(18, 2).addBox(-0.4922F, -0.125F, -0.25F, 0.7422F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.044F, -15.9473F, 8.0F, 0.0F, 0.0F, 0.3927F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		axe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(ThrownLeviathanAxe thrownLeviathanAxe, float v, float v1, float v2, float v3, float v4) {

	}
}