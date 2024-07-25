package com.deriys.divinerelics.entities.client.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class DRAnimationDefinitions {
//	public static final AnimationDefinition LEVIATHAN_THROWING = AnimationDefinition.Builder.withLength(0.3333F).looping()
//		.addAnimation("axe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
//			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
//			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -360.0F), AnimationChannel.Interpolations.LINEAR)
//		))
//		.build();

	public static final AnimationDefinition LEVIATHAN_THROWING = AnimationDefinition.Builder.withLength(1F).looping()
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -360.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();
}