package com.deriys.divinerelics.entities.client.model;


import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.BrokEntity;
import com.deriys.divinerelics.entities.entity.SindriEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SindriModel extends AnimatedGeoModel<SindriEntity> {
	@Override
	public ResourceLocation getModelResource(SindriEntity sindriEntity) {
		return new ResourceLocation(DivineRelics.MODID, "geo/sindri.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(SindriEntity sindriEntity) {
		return new ResourceLocation(DivineRelics.MODID, "textures/entity/sindri.png");
	}

	@Override
	public ResourceLocation getAnimationResource(SindriEntity sindriEntity) {
		return new ResourceLocation(DivineRelics.MODID, "animations/sindri.animation.json");
	}
}