package com.deriys.divinerelics.entities.client.model;
import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ThorModel extends AnimatedGeoModel<ThorEntity> {
    @Override
    public ResourceLocation getModelResource(ThorEntity thorEntity) {
        if (thorEntity.hasMjolnirInHands()) {
            return new ResourceLocation(DivineRelics.MODID, "geo/thor.geo.json");
        }
        if (thorEntity.waitsForMjolnir()) {
            return new ResourceLocation(DivineRelics.MODID, "geo/thor_no_mjolnir.geo.json");
        }
        return new ResourceLocation(DivineRelics.MODID, "geo/thor_w_mjolnir.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThorEntity thorEntity) {
        return new ResourceLocation(DivineRelics.MODID, "textures/entity/thor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThorEntity thorEntity) {
        return new ResourceLocation(DivineRelics.MODID, "animations/thor.animation.json");
    }
}
