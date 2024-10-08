package com.deriys.divinerelics.entities.client.model;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.HelWalkerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HelWalkerModel extends AnimatedGeoModel<HelWalkerEntity> {
	@Override
	public ResourceLocation getModelResource(HelWalkerEntity helWalker) {
		return new ResourceLocation(DivineRelics.MODID, "geo/hel_walker.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(HelWalkerEntity helWalker) {
		return new ResourceLocation(DivineRelics.MODID, "textures/entity/hel_walker.png");
	}

	@Override
	public ResourceLocation getAnimationResource(HelWalkerEntity helWalker) {
		return new ResourceLocation(DivineRelics.MODID, "animations/hel_walker.animation.json");
	}
}