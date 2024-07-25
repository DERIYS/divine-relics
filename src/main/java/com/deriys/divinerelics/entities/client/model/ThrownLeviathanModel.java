package com.deriys.divinerelics.entities.client.model;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.deriys.divinerelics.entities.client.animations.DRAnimationDefinitions;
import com.deriys.divinerelics.entities.entity.ThrownLeviathanAxe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ThrownLeviathanModel extends HierarchicalModel<ThrownLeviathanAxe> {
	private final ModelPart body;
	private final ModelPart runes;
	private final ModelPart axe;
	private final ModelPart plains;
	private final ModelPart axebladeoutline;
	private final ModelPart center;
	private final ModelPart handle;
	private final ModelPart plains2;
	private final ModelPart handleoutline;
	private final ModelPart main;

	public ThrownLeviathanModel(ModelPart root) {
		this.body = root.getChild("body");
		this.runes = body.getChild("runes");
		this.axe = body.getChild("axe");
		this.plains = axe.getChild("plains");
		this.axebladeoutline = axe.getChild("axebladeoutline");
		this.center = axe.getChild("center");
		this.handle = body.getChild("handle");
		this.plains2 = handle.getChild("plains2");
		this.handleoutline = handle.getChild("handleoutline");
		this.main = handle.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(6.0F, 12.0F, 0.0F));

		PartDefinition runes = body.addOrReplaceChild("runes", CubeListBuilder.create().texOffs(34, 24).addBox(-16.625F, -18.625F, 7.748F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(34, 36).addBox(-16.625F, -18.625F, 8.252F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 6.0F, -8.0F));

		PartDefinition axe = body.addOrReplaceChild("axe", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition plains = axe.addOrReplaceChild("plains", CubeListBuilder.create().texOffs(13, 4).addBox(-7.25F, -19.25F, 7.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(16, 23).addBox(-8.25F, -19.25F, 8.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(34, 12).addBox(-16.625F, -18.625F, 7.749F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(34, 0).addBox(-16.625F, -18.625F, 8.251F, 15.0F, 11.25F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 8.0F, -8.0F));

		PartDefinition axebladeoutline = axe.addOrReplaceChild("axebladeoutline", CubeListBuilder.create().texOffs(21, 0).addBox(4.3125F, -0.75F, 0.0625F, 0.7383F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(11, 32).addBox(5.9341F, 0.1174F, 0.0625F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(15, 33).addBox(8.5394F, -2.3055F, 0.0625F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(4.25F, -2.125F, 0.0625F, 3.5F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(8, 17).addBox(-2.375F, -2.125F, 0.0625F, 4.5625F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(7, 0).addBox(-5.8231F, -3.0987F, 0.0625F, 1.1484F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(18, 0).addBox(-6.4203F, -2.8307F, 0.0625F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 1).addBox(-5.3213F, -2.0963F, 0.0625F, 0.25F, 0.625F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(7, 30).addBox(-6.1132F, -0.3094F, 0.0625F, 0.25F, 1.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(18, 3).addBox(-1.4001F, 1.8529F, 0.0625F, 0.25F, 3.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(0, 3).addBox(1.5F, -0.75F, 0.0625F, 0.625F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -7.5F, -0.3125F));

		PartDefinition cube_r1 = axebladeoutline.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(9, 27).addBox(0.0F, -0.125F, -0.25F, 1.625F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.091F, -0.0161F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r2 = axebladeoutline.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(9, 32).addBox(-0.125F, -2.0F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2826F, 1.371F, 0.3125F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r3 = axebladeoutline.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(7, 33).addBox(-0.125F, -0.5F, -0.25F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0933F, 1.4388F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r4 = axebladeoutline.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(5, 30).addBox(-0.125F, -0.625F, -0.25F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0455F, 5.6325F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r5 = axebladeoutline.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(3, 30).addBox(-0.125F, -1.625F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1411F, 8.3085F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r6 = axebladeoutline.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(2, 4).addBox(-0.375F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5074F, 7.8862F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r7 = axebladeoutline.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(14, 12).addBox(-2.875F, -0.125F, -0.25F, 3.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0785F, 7.5046F, 0.3125F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r8 = axebladeoutline.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(14, 0).addBox(-0.125F, -2.375F, -0.25F, 0.25F, 5.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0889F, 3.087F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r9 = axebladeoutline.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(3, 33).addBox(-1.75F, 0.625F, -0.25F, 0.25F, 0.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9035F, -0.0442F, 0.3125F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r10 = axebladeoutline.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(1, 34).addBox(-0.125F, -0.5F, -0.25F, 0.25F, 0.875F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3781F, -1.0572F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r11 = axebladeoutline.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(15, 31).addBox(-0.125F, -0.125F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3302F, -2.3949F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r12 = axebladeoutline.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(11, 31).addBox(-0.375F, -0.125F, -0.25F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.2626F, -2.3815F, 0.3125F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r13 = axebladeoutline.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(13, 33).addBox(-0.375F, -0.125F, -0.25F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3717F, -2.5717F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r14 = axebladeoutline.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(18, 1).addBox(-0.875F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.6598F, -3.031F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r15 = axebladeoutline.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 28).addBox(-2.2227F, -0.125F, -0.25F, 2.5977F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6736F, -2.134F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r16 = axebladeoutline.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(7, 1).addBox(-0.625F, -0.125F, -0.25F, 1.125F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.2796F, -2.2297F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r17 = axebladeoutline.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(5, 33).addBox(0.125F, 0.125F, -0.25F, 0.25F, 1.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.4907F, -2.3145F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r18 = axebladeoutline.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 30).addBox(0.125F, 0.125F, -0.25F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.2299F, -1.4852F, 0.3125F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r19 = axebladeoutline.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(11, 0).addBox(-1.0F, 0.125F, -0.25F, 0.875F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9644F, -0.1117F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r20 = axebladeoutline.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(14, 32).addBox(-0.375F, -0.375F, -0.25F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.3474F, 0.1851F, 0.3125F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r21 = axebladeoutline.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(13, 31).addBox(-0.375F, -0.875F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.442F, 0.0632F, 0.3125F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r22 = axebladeoutline.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(18, 2).addBox(-0.4922F, -0.125F, -0.25F, 0.7422F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.456F, -0.4473F, 0.3125F, 0.0F, 0.0F, 0.3927F));

		PartDefinition center = axe.addOrReplaceChild("center", CubeListBuilder.create().texOffs(6, 2).addBox(-8.375F, -17.625F, 7.375F, 2.25F, 1.625F, 1.25F, new CubeDeformation(0.0F))
				.texOffs(12, 25).addBox(-7.75F, -18.25F, 7.5F, 1.0F, 0.75F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 8.0F, -8.0F));

		PartDefinition handle = body.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition plains2 = handle.addOrReplaceChild("plains2", CubeListBuilder.create().texOffs(0, 10).addBox(-2.625F, 14.5F, -0.251F, 3.5F, 6.5F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 17).addBox(-2.625F, 14.5F, 0.251F, 3.5F, 6.5F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition handleoutline = handle.addOrReplaceChild("handleoutline", CubeListBuilder.create().texOffs(12, 30).addBox(-8.625F, 6.5F, 7.75F, 0.25F, 0.25F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(16, 0).addBox(-7.375F, 6.5F, 7.75F, 0.25F, 3.5F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(20, 7).addBox(-9.2817F, 9.236F, 7.75F, 0.25F, 2.0F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(11, 1).addBox(-10.2983F, 12.7145F, 7.75F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 8.0F, -8.0F));

		PartDefinition cube_r23 = handleoutline.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(4, 0).addBox(-0.125F, -0.125F, -0.25F, 0.5F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5012F, 12.6098F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r24 = handleoutline.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(4, 1).addBox(-0.125F, -0.125F, -0.25F, 0.5F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5969F, 12.7055F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r25 = handleoutline.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(4, 2).addBox(1.0F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.52F, 13.2317F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r26 = handleoutline.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(14, 27).addBox(-0.125F, -0.125F, -0.25F, 1.25F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1069F, 12.405F, 8.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r27 = handleoutline.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(21, 1).addBox(-0.125F, -0.375F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.2907F, 11.5346F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r28 = handleoutline.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(20, 3).addBox(-0.125F, -1.75F, -0.25F, 0.25F, 1.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1205F, 12.7658F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r29 = handleoutline.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(22, 3).addBox(-0.125F, -1.75F, -0.25F, 0.25F, 1.75F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9102F, 11.5689F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r30 = handleoutline.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(2, 0).addBox(-0.125F, 0.25F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4342F, 8.5909F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r31 = handleoutline.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 4).addBox(0.375F, -0.125F, -0.25F, 0.375F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.8418F, 8.3321F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r32 = handleoutline.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(14, 30).addBox(0.375F, -0.125F, -0.25F, 0.375F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2289F, 8.3373F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r33 = handleoutline.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(3, 3).addBox(0.375F, -0.125F, -0.25F, 0.75F, 0.25F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.3246F, 8.6243F, 8.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r34 = handleoutline.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(2, 2).addBox(-0.125F, -0.875F, -0.25F, 0.25F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.591F, 8.4861F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r35 = handleoutline.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(9, 30).addBox(-0.125F, -1.75F, -0.25F, 0.25F, 1.375F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1429F, 8.2544F, 8.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition main = handle.addOrReplaceChild("main", CubeListBuilder.create().texOffs(1, 5).addBox(-8.25F, -16.0F, 7.5F, 1.875F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 24).addBox(-8.25F, -13.0F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-8.375F, -12.0F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(21, 14).addBox(-8.5F, -11.0F, 7.5F, 1.75F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 15).addBox(-8.625F, -10.0F, 7.5F, 1.75F, 0.5F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 6).addBox(-8.75F, -9.0F, 7.5F, 1.625F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 13).addBox(-8.625F, -9.5F, 7.5F, 1.625F, 0.5F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 9).addBox(-8.875F, -7.0F, 7.5F, 1.625F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 8).addBox(-8.75F, -1.0F, 7.5F, 1.625F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 19).addBox(-8.625F, 1.0F, 7.5F, 1.625F, 2.75F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 19).addBox(-8.625F, 3.75F, 7.625F, 1.625F, 2.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 8.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.body;
	}

	@Override
	public void setupAnim(ThrownLeviathanAxe thrownLeviathanAxe, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animate(thrownLeviathanAxe.throwingAnimationState, DRAnimationDefinitions.LEVIATHAN_THROWING, ageInTicks);
	}
}