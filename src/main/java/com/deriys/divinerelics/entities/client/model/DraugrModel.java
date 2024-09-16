package com.deriys.divinerelics.entities.client.model;

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