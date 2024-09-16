package com.deriys.divinerelics.entities.client.model;


import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.BrokEntity;
import com.deriys.divinerelics.entities.entity.DraugrEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BrokModel extends AnimatedGeoModel<BrokEntity> {
	@Override
	public ResourceLocation getModelResource(BrokEntity sindriEntity) {
		return new ResourceLocation(DivineRelics.MODID, "geo/brok.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BrokEntity sindriEntity) {
		return new ResourceLocation(DivineRelics.MODID, "textures/entity/brok.png");
	}

	@Override
	public ResourceLocation getAnimationResource(BrokEntity sindriEntity) {
		return new ResourceLocation(DivineRelics.MODID, "animations/brok.animation.json");
	}
}