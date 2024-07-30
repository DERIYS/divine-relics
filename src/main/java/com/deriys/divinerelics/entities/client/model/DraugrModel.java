package com.deriys.divinerelics.entities.client.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.DraugrEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DraugrModel extends AnimatedGeoModel<DraugrEntity> {
	@Override
	public ResourceLocation getModelResource(DraugrEntity draugrEntity) {
		return new ResourceLocation(DivineRelics.MODID, "geo/draugr.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DraugrEntity draugrEntity) {
		return new ResourceLocation(DivineRelics.MODID, "textures/entity/draugr.png");
	}

	@Override
	public ResourceLocation getAnimationResource(DraugrEntity draugrEntity) {
		return new ResourceLocation(DivineRelics.MODID, "animations/draugr.animation.json");
	}
}





//public class DraugrModel<T extends DraugrEntity> extends HierarchicalModel<T> {
//	private final ModelPart draugr;
//	private final ModelPart head;
//	private final ModelPart ey;
//	private final ModelPart body;
//	private final ModelPart bodytiles;
//	private final ModelPart bodytile2;
//	private final ModelPart hand;
//	private final ModelPart handtile;
//	private final ModelPart handtile2;
//	private final ModelPart hand2;
//	private final ModelPart handtile3;
//	private final ModelPart handtile4;
//	private final ModelPart bodytile;
//	private final ModelPart leg;
//	private final ModelPart legtile2;
//	private final ModelPart leg2;
//	private final ModelPart legtile;
//
//	public DraugrModel(ModelPart root) {
//		this.draugr = root.getChild("draugr");
//		this.head = draugr.getChild("head");
//		this.ey = head.getChild("ey");
//		this.body = draugr.getChild("body");
//		this.bodytiles = body.getChild("bodytiles");
//		this.bodytile2 = bodytiles.getChild("bodytile2");
//		this.hand = bodytile2.getChild("hand");
//		this.handtile = hand.getChild("handtile");
//		this.handtile2 = handtile.getChild("handtile2");
//		this.hand2 = bodytile2.getChild("hand2");
//		this.handtile3 = hand2.getChild("handtile3");
//		this.handtile4 = handtile3.getChild("handtile4");
//		this.bodytile = bodytiles.getChild("bodytile");
//		this.leg = bodytile.getChild("leg");
//		this.legtile2 = leg.getChild("legtile2");
//		this.leg2 = bodytile.getChild("leg2");
//		this.legtile = leg2.getChild("legtile");
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition draugr = partdefinition.addOrReplaceChild("draugr", CubeListBuilder.create(), PartPose.offset(3.0F, 45.0F, 1.0F));
//
//		PartDefinition head = draugr.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
//				.texOffs(45, 47).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -12.0F, -2.0F, 0.1309F, 0.0F, -0.2182F));
//
//		PartDefinition ey = head.addOrReplaceChild("ey", CubeListBuilder.create().texOffs(56, 7).addBox(0.1914F, -0.6483F, -3.03F, 1.1856F, 1.0989F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(56, 7).addBox(2.4715F, -0.6483F, -3.03F, 1.3397F, 1.0989F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0368F, -1.9242F, 0.99F));
//
//		PartDefinition body = draugr.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-3.0F, 12.0F, -1.0F));
//
//		PartDefinition bodytiles = body.addOrReplaceChild("bodytiles", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));
//
//		PartDefinition bodytile2 = bodytiles.addOrReplaceChild("bodytile2", CubeListBuilder.create().texOffs(51, 23).addBox(-3.0F, -1.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(38, 17).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
//				.texOffs(19, 13).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
//				.texOffs(32, 22).addBox(-1.0F, -5.0F, 2.9627F, 2.0F, 2.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(23, 2).addBox(-1.0F, -3.0F, 1.9627F, 2.0F, 2.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(0, 25).addBox(-1.0F, -7.0F, 2.9627F, 2.0F, 2.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(39, 28).addBox(-4.364F, -6.0F, 2.9627F, 3.84F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(9, 38).addBox(-4.364F, -8.0F, 2.9627F, 3.84F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(39, 6).addBox(-4.364F, -4.0F, 2.9627F, 3.84F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(32, 24).addBox(-3.364F, -2.0F, 1.9627F, 2.84F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(11, 26).addBox(0.636F, -2.0F, 1.9627F, 2.84F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(30, 12).addBox(0.9F, -4.0F, 2.9627F, 3.6F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(27, 35).addBox(0.9F, -6.0F, 2.9627F, 3.6F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(36, 16).addBox(0.9F, -8.0F, 2.9627F, 3.6F, 1.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(11, 24).addBox(-1.0F, -9.0F, 2.9627F, 2.0F, 2.0F, 0.6693F, new CubeDeformation(0.0F))
//				.texOffs(1, 1).addBox(-4.0F, -9.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
//				.texOffs(1, 14).addBox(-3.0F, -10.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
//				.texOffs(46, 28).addBox(4.0F, -8.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
//				.texOffs(46, 22).addBox(-5.0F, -8.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 31).addBox(4.0F, -9.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
//				.texOffs(27, 30).addBox(4.0F, -6.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
//				.texOffs(30, 22).addBox(4.0F, -4.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
//				.texOffs(9, 24).addBox(-5.0F, -9.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
//				.texOffs(18, 29).addBox(-5.0F, -6.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
//				.texOffs(30, 0).addBox(-5.0F, -4.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
//				.texOffs(39, 22).addBox(-4.0F, -2.0F, -3.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
//				.texOffs(39, 0).addBox(3.0F, -2.0F, -3.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
//				.texOffs(51, 27).addBox(1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(46, 6).addBox(1.0F, -3.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(44, 43).addBox(-4.0F, -3.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(30, 10).addBox(1.0F, -5.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(15, 32).addBox(-4.0F, -5.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(23, 0).addBox(-2.0F, -2.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(19, 16).addBox(1.0F, -2.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 56).addBox(-4.0F, -8.0F, 3.7616F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, -0.0472F, -0.3923F, 0.0181F));
//
//		PartDefinition hand = bodytile2.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(36, 38).addBox(-1.0F, 6.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(18, 44).addBox(-1.0F, 1.5F, -1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(35, 49).addBox(-1.0F, 2.5F, -2.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(25, 50).addBox(-1.0F, 1.5F, 0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(27, 30).addBox(1.0F, -1.5F, -1.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(36, 30).addBox(-2.0F, -1.5F, -2.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
//				.texOffs(48, 8).addBox(-1.0F, -2.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -7.5F, -0.5F, 0.48F, 0.0F, 0.0F));
//
//		PartDefinition handtile = hand.addOrReplaceChild("handtile", CubeListBuilder.create().texOffs(32, 20).addBox(-0.5567F, -1.4F, -0.9127F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(46, 0).mirror().addBox(-0.8767F, -0.4F, -1.2327F, 2.64F, 3.0F, 2.64F, new CubeDeformation(0.0F)).mirror(false)
//				.texOffs(6, 23).addBox(0.3747F, -0.56F, -1.402F, 0.2873F, 3.16F, 0.1811F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(1.789F, -0.56F, -0.8722F, 0.2675F, 3.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(1.789F, -1.56F, -0.3238F, 0.2675F, 4.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(1.789F, -0.56F, 0.6762F, 0.2675F, 3.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(0.3747F, -0.56F, 1.4061F, 0.2873F, 3.16F, 0.2909F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(0.3747F, 2.44F, -0.8722F, 0.2873F, 2.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(1.1368F, 2.44F, -0.8722F, 0.2787F, 2.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(1.1368F, 2.44F, -0.3238F, 0.2787F, 2.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(0, 22).mirror().addBox(-0.3167F, 2.6F, -0.6727F, 1.52F, 2.0F, 1.52F, new CubeDeformation(0.0F)).mirror(false)
//				.texOffs(6, 24).addBox(1.1368F, 2.44F, 0.6762F, 0.2787F, 2.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(0.3747F, 2.44F, 0.6762F, 0.2873F, 2.16F, 0.3505F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4433F, 7.9F, -0.5873F, -1.2654F, 0.0F, 0.0F));
//
//		PartDefinition handtile2 = handtile.addOrReplaceChild("handtile2", CubeListBuilder.create().texOffs(5, 5).addBox(-0.72F, 0.3024F, -2.9747F, 0.54F, 1.1844F, 0.6321F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-0.6012F, 1.4868F, -2.9661F, 0.3024F, 1.1844F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(-1, -1).addBox(-0.72F, 0.1435F, -2.3855F, 0.54F, 0.5621F, 1.4538F, new CubeDeformation(0.0F))
//				.texOffs(17, 36).addBox(-0.3507F, -0.025F, -1.555F, 0.49F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.0186F, -0.185F, -1.5145F, 0.2873F, 1.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.0186F, -0.185F, -0.9661F, 0.2873F, 1.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.0186F, -0.185F, 0.0339F, 0.2873F, 1.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(18, 38).addBox(-0.3585F, 0.685F, -1.9747F, 0.567F, 4.26F, 0.6321F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(-0.2338F, 3.945F, -1.9661F, 0.3175F, 4.26F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-0.2338F, 2.225F, -0.9661F, 0.3175F, 1.5F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-0.3585F, 0.725F, -0.9747F, 0.567F, 1.5F, 0.6321F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-0.2232F, 0.875F, -0.2338F, 0.4465F, 0.8F, 0.4675F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-0.1603F, 1.575F, -0.1678F, 0.3205F, 1.05F, 0.3357F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3933F, 4.625F, 0.6423F, 0.2618F, 0.0F, 0.0F));
//
//		PartDefinition hand2 = bodytile2.addOrReplaceChild("hand2", CubeListBuilder.create().texOffs(18, 29).addBox(-2.0F, 6.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(10, 44).addBox(-2.0F, 1.5F, -1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(49, 16).addBox(-2.0F, 2.5F, -2.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 31).addBox(-2.0F, 1.5F, 0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 13).addBox(-3.0F, -1.5F, -1.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(23, 0).addBox(-2.0F, -1.5F, -2.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
//				.texOffs(47, 37).addBox(-2.0F, -2.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -7.5F, -0.5F, -0.1309F, 0.3927F, 0.0F));
//
//		PartDefinition handtile3 = hand2.addOrReplaceChild("handtile3", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5567F, -1.4F, -0.9127F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(46, 0).addBox(-0.8767F, -0.4F, -1.2327F, 2.64F, 3.0F, 2.64F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(0.3747F, -0.56F, -1.402F, 0.2873F, 3.16F, 0.1811F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(-1.2054F, -0.56F, -0.8722F, 0.3879F, 3.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(-1.2054F, -1.56F, -0.3238F, 0.3879F, 4.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(-1.2054F, -0.56F, 0.6762F, 0.3879F, 3.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(0.3747F, -0.56F, 1.4061F, 0.2873F, 3.16F, 0.2909F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(0.3747F, 2.44F, -0.8722F, 0.2873F, 2.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.5438F, 2.44F, -0.8722F, 0.3428F, 2.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(-0.5438F, 2.44F, -0.3238F, 0.3428F, 2.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(0, 22).addBox(-0.3167F, 2.6F, -0.6727F, 1.52F, 2.0F, 1.52F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.5438F, 2.44F, 0.6762F, 0.3428F, 2.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(0.3747F, 2.44F, 0.6762F, 0.2873F, 2.16F, 0.3505F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4433F, 7.9F, -0.5873F, -1.5631F, -0.1744F, 1.5265F));
//
//		PartDefinition handtile4 = handtile3.addOrReplaceChild("handtile4", CubeListBuilder.create().texOffs(0, 0).addBox(0.2919F, 0.3024F, -2.9747F, 0.5022F, 1.1844F, 0.6321F, new CubeDeformation(0.0F))
//				.texOffs(6, 25).addBox(0.3504F, 1.4868F, -2.9661F, 0.2691F, 1.1844F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(4, 4).addBox(0.7556F, 0.1435F, -2.3855F, -0.5076F, 0.5621F, 1.4538F, new CubeDeformation(0.0F))
//				.texOffs(4, 4).addBox(0.6293F, -0.025F, -1.555F, -0.49F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(-0.0186F, -0.185F, -1.5145F, 0.2873F, 1.16F, 0.2383F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.0186F, -0.185F, -0.9661F, 0.2873F, 1.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 24).addBox(-0.0186F, -0.185F, 0.0339F, 0.2873F, 1.16F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(15, 40).addBox(-0.0222F, 0.685F, -1.9747F, 0.5443F, 4.26F, 0.6321F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(0.0148F, 4.945F, -1.9661F, 0.308F, 2.26F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(6, 23).addBox(0.0148F, 2.225F, -0.9661F, 0.308F, 4.5F, 0.3505F, new CubeDeformation(0.0F))
//				.texOffs(5, 4).addBox(-0.0222F, 0.725F, -0.9747F, 0.5443F, 2.5F, 0.6321F, new CubeDeformation(0.0F))
//				.texOffs(0, 0).addBox(-0.2232F, 0.875F, -0.2338F, 0.4465F, 0.8F, 0.4675F, new CubeDeformation(0.0F))
//				.texOffs(6, 25).addBox(-0.1603F, 1.575F, -0.1678F, 0.3205F, 1.05F, 0.3357F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3933F, 4.625F, 0.6423F, 0.0F, 0.0F, -0.4363F));
//
//		PartDefinition bodytile = bodytiles.addOrReplaceChild("bodytile", CubeListBuilder.create().texOffs(0, 39).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
//
//		PartDefinition leg = bodytile.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(21, 20).mirror().addBox(-2.0F, -2.0F, -3.0F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
//				.texOffs(37, 43).mirror().addBox(-1.0F, 2.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 0.0F, 1.0F));
//
//		PartDefinition legtile2 = leg.addOrReplaceChild("legtile2", CubeListBuilder.create().texOffs(17, 34).addBox(1.0F, 5.0F, 1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(52, 11).addBox(3.0F, 5.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(52, 0).addBox(2.0F, 5.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(22, 37).addBox(1.0F, 5.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
//				.texOffs(50, 40).addBox(2.0F, 5.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(47, 51).addBox(-2.0F, 5.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(36, 32).addBox(-2.0F, 5.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(36, 30).addBox(-1.0F, 5.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(51, 47).addBox(-2.0F, 5.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(33, 0).addBox(-3.0F, 5.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(33, 2).addBox(-3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(12, 37).addBox(-2.0F, 3.0F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 46).addBox(-1.55F, 1.0F, -1.55F, 2.1F, 2.0F, 3.1F, new CubeDeformation(0.0F))
//				.texOffs(50, 33).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(39, 2).addBox(-1.0F, 5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 39).addBox(3.0F, 5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 35).addBox(2.0F, 5.0F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(19, 22).addBox(1.0F, 5.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(39, 0).addBox(-2.0F, 5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(51, 29).addBox(-2.0F, 5.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.0F, -1.0F));
//
//		PartDefinition leg2 = bodytile.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(37, 43).addBox(-0.8F, 2.0F, -3.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
//				.texOffs(21, 20).addBox(-0.8F, -2.0F, -3.0F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.2F, 0.0F, 1.0F));
//
//		PartDefinition legtile = leg2.addOrReplaceChild("legtile", CubeListBuilder.create().texOffs(50, 33).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 46).addBox(-0.55F, 1.0F, -1.55F, 2.1F, 2.0F, 3.1F, new CubeDeformation(0.0F))
//				.texOffs(12, 37).addBox(-1.0F, 3.0F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
//				.texOffs(6, 51).addBox(1.0F, 5.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(12, 51).addBox(2.0F, 5.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(52, 13).addBox(-2.0F, 5.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 51).addBox(-2.0F, 5.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(52, 50).addBox(-2.0F, 5.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(36, 40).addBox(-1.0F, 5.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(53, 4).addBox(-4.0F, 5.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(52, 52).addBox(-5.0F, 5.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(8, 39).addBox(-4.0F, 5.0F, 1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(0, 41).addBox(-4.0F, 5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(30, 53).addBox(-2.0F, 5.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(16, 44).addBox(-1.0F, 5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(33, 45).addBox(-2.0F, 5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(18, 51).addBox(1.0F, 5.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(51, 20).addBox(2.0F, 5.0F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
//				.texOffs(39, 22).addBox(2.0F, 5.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
//				.texOffs(39, 24).addBox(2.0F, 5.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.8F, 6.0F, -1.0F));
//
//		return LayerDefinition.create(meshdefinition, 64, 64);
//	}
//
//	@Override
//	public void setupAnim(DraugrEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//	}
//
//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//
//	@Override
//	public ModelPart root() {
//		return draugr;
//	}
//}